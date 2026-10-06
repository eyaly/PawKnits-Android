package com.pawknits.demo.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Fire-and-forget HTTP calls whose only purpose is to produce real network traffic
 * (e.g. for Sauce Labs network capture / HAR files). The app never depends on the
 * response: failures, timeouts and offline devices are logged and ignored.
 *
 * httpbin.org/anything echoes back whatever it receives. Never send passwords here.
 */
object DemoApi {
    private const val TAG = "PawKnitsApi"
    private const val BASE_URL = "https://httpbin.org/anything/pawknits"
    private const val TIMEOUT_MS = 5_000

    suspend fun reportLogin(username: String, success: Boolean) {
        post(
            "/login",
            JSONObject()
                .put("event", "login")
                .put("username", username)
                .put("success", success),
        )
    }

    private suspend fun post(path: String, body: JSONObject) = withContext(Dispatchers.IO) {
        try {
            val connection = URL(BASE_URL + path).openConnection() as HttpURLConnection
            try {
                connection.requestMethod = "POST"
                connection.connectTimeout = TIMEOUT_MS
                connection.readTimeout = TIMEOUT_MS
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("X-App", "PawKnits")
                connection.outputStream.use { it.write(body.toString().toByteArray()) }
                val code = connection.responseCode
                connection.inputStream.use { it.readBytes() }
                Log.d(TAG, "POST $path -> $code")
            } finally {
                connection.disconnect()
            }
        } catch (e: Exception) {
            Log.w(TAG, "POST $path failed (ignored): ${e.message}")
        }
    }
}
