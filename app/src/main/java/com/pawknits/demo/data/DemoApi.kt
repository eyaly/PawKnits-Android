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
 * httpbin.org/anything echoes back whatever it receives (always 200); httpbin.org/status/<code>
 * replies with that status code, which lets failed logins show up as 4xx. Never send passwords here.
 */
object DemoApi {
    private const val TAG = "PawKnitsApi"
    private const val BASE_URL = "https://httpbin.org"
    private const val TIMEOUT_MS = 5_000

    /** [status] is what a real API would answer: 200 OK, 400 missing fields, 401 bad credentials, 403 locked. */
    suspend fun reportLogin(username: String, status: Int) {
        post(
            if (status == 200) "/anything/pawknits/login" else "/status/$status",
            JSONObject()
                .put("event", "login")
                .put("username", username)
                .put("success", status == 200),
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
                // 4xx/5xx responses are read from errorStream; inputStream would throw.
                (if (code >= 400) connection.errorStream else connection.inputStream)?.use { it.readBytes() }
                Log.d(TAG, "POST $path -> $code")
            } finally {
                connection.disconnect()
            }
        } catch (e: Exception) {
            Log.w(TAG, "POST $path failed (ignored): ${e.message}")
        }
    }
}
