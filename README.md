# PawKnits 🐾 — Sweater Shop for Dogs (Android demo)

Android (Kotlin + Jetpack Compose) port of the iOS `ApplePayDemo` shop, built as a demo app
for running on Sauce Labs real devices / emulators.

**Flow:** Login → Shop (8 sweaters) → Cart → Checkout (demo payment) → Order confirmation

## Open & build
- Open this folder in Android Studio and hit **Run**, or:
  ```bash
  ./gradlew assembleDebug      # app/build/outputs/apk/debug/PawKnits.apk
  ./gradlew assembleRelease    # app/build/outputs/apk/release/PawKnits.apk (debug-signed)
  ```
- Package / app id: `com.pawknits.demo` · Activity: `com.pawknits.demo.MainActivity`
- minSdk 26 (Android 8.0), targetSdk 35

## Demo data
| What | Value |
|---|---|
| Valid user | `doglover` / `woof1234` |
| Locked-out user | `locked` / `woof1234` (shows lockout error) |
| Approved card | `4242 4242 4242 4242` (prefilled) |
| Declined card | `4000 0000 0000 0002` |

No backend: nothing leaves the device and no real payment is made. The Pay button waits
about 1.5 seconds and then approves or declines.

## Network traffic
Everything runs locally, but every login attempt also sends a small request so network
capture has something to show:

`POST https://httpbin.org/anything/pawknits/login` with body
`{"event":"login","username":"doglover","success":true}` and an `X-App: PawKnits` header
(the password is never sent).

The app never waits for or depends on the request: if the device is offline it's logged
(`adb logcat -s PawKnitsApi`) and ignored. The app trusts user-installed CA certificates
(`res/xml/network_security_config.xml`), so HTTPS traffic can be decrypted by capture proxies.
To capture it on Sauce Labs, add `"networkCapture": true` to `sauce:options`.

## Running on Sauce Labs
Upload the APK to Sauce storage:
```bash
curl -u "$SAUCE_USERNAME:$SAUCE_ACCESS_KEY" -X POST \
  "https://api.us-west-1.saucelabs.com/v1/storage/upload" \
  -F "payload=@app/build/outputs/apk/debug/PawKnits.apk" -F "name=PawKnits.apk"
```
Appium capabilities:
```json
{
  "platformName": "Android",
  "appium:automationName": "UiAutomator2",
  "appium:app": "storage:filename=PawKnits.apk",
  "appium:deviceName": "Google.*",
  "appium:appPackage": "com.pawknits.demo",
  "appium:appActivity": "com.pawknits.demo.MainActivity",
  "sauce:options": { "name": "PawKnits demo", "appiumVersion": "latest", "networkCapture": true }
}
```

## Locators
Compose `testTag`s are exposed as Android **resource-ids** (`testTagsAsResourceId`), so use
`By.id("login_username")` / `$('id=login_username')` or `android=new UiSelector().resourceId("login_button")`.

| Screen | resource-ids |
|---|---|
| Login | `login_username`, `login_password`, `login_toggle_password`, `login_button`, `login_error`, `demo_user_valid`, `demo_user_locked` |
| Shop | `shop_screen`, `product_grid`, `product_card_{1..8}`, `product_name_{n}`, `product_price_{n}`, `add_to_cart_{n}`, `cart_button`, `cart_badge`, `logout_button` |
| Cart | `cart_screen`, `cart_item_{n}`, `cart_quantity_{n}`, `cart_increment_{n}`, `cart_decrement_{n}`, `cart_remove_{n}`, `cart_total`, `checkout_button`, `cart_empty`, `back_button` |
| Checkout | `checkout_screen`, `demo_payment_banner`, `checkout_total`, `checkout_name`, `checkout_address`, `checkout_card_number`, `checkout_expiry`, `checkout_cvv`, `*_error`, `pay_button`, `payment_progress`, `payment_error` |
| Confirmation | `order_complete_screen`, `order_complete_title`, `order_number`, `order_amount`, `continue_shopping_button` |

Products (ids 1–8): Orange sweater $1, Grey blanket $89, US scarf $79, Grey sweater $94,
Yellow sweater $99, Green sweater $65, Xmas sweater $54, Grey-Red sweater $83.
