# Pillow Android SDK

Identify users, set properties, and present Pillow studies in your Android app.

Shared Kotlin Multiplatform source and architecture are published separately in `https://github.com/trypillow/pillow-core-sdk`.

## Requirements

- Android minSdk 24+ (Android 7.0)
- Kotlin / Java 17 compatible build environment

## Prerequisites

- A **publishable API key** from the [Developer section](https://app.trypillow.ai) of your dashboard
- A **study** set to live mode — copy its ID from the **Integration** tab

## Installation

Add the dependency from Maven Central:

```kotlin
repositories {
  google()
  mavenCentral()
}

dependencies {
  implementation("so.pillow:pillow-android-sdk:0.1.1")
}
```

## Quick start

### 1. Initialize the SDK

Call `initialize()` once at app startup, typically in your `Application` class or main `Activity`. Do not call it repeatedly from view code.

```kotlin
import so.pillow.sdk.PillowSDK

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PillowSDK.initialize(
            context = this,
            publishableKey = "pk_live_...",
        )
    }
}
```

### 2. Identify the user

Call after login or when you know who the user is. You can also set properties to segment users in the dashboard.

```kotlin
PillowSDK.setExternalId("user_123")
PillowSDK.setUserProperty("plan", "pro")
```

### 3. Present a study

Show a study from any `Activity`:

```kotlin
import so.pillow.sdk.PillowStudy

PillowSDK.presentStudy(
    activity = this,
    study = PillowStudy(id = "your-study-id-here"),
)
```

### 4. Handle microphone permissions

If your study uses voice input, forward the permission result to the SDK in the `Activity` that calls `presentStudy`:

```kotlin
override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray,
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    PillowSDK.onRequestPermissionsResult(requestCode, permissions, grantResults)
}
```

## Full example

```kotlin
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import so.pillow.sdk.PillowSDK
import so.pillow.sdk.PillowStudy
import so.pillow.sdk.PillowStudyPresentationOptions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PillowSDK.initialize(
            context = applicationContext,
            publishableKey = "pk_live_...",
        )

        PillowSDK.setExternalId("user_123")
        PillowSDK.setUserProperty("plan", "pro")

        PillowSDK.presentStudy(
            activity = this,
            study = PillowStudy(id = "your-study-id-here"),
            options = PillowStudyPresentationOptions(skipIfAlreadyExposed = true),
            listener = object : PillowStudyListener {
                override fun studyDidPresent(study: PillowStudy) {
                    // study modal appeared on screen
                }
                override fun studyDidSkip(study: PillowStudy) {
                    // study was intentionally skipped
                }
                override fun studyDidFinish(study: PillowStudy) {
                    // user finished or dismissed the study
                }
                override fun studyDidFailToLoad(study: PillowStudy, error: Throwable) {
                    Log.e("Pillow", "Study failed: ${error.message}")
                }
            },
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PillowSDK.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
```

## API reference

### `initialize(context, publishableKey)`

Starts the SDK. Call once during app startup — subsequent calls are ignored.

```kotlin
PillowSDK.initialize(
  context = applicationContext,
  publishableKey = "pk_live_...",
)
```

### `setExternalId(externalId)`

Identifies the current user. Call after login or when you know who the user is.

```kotlin
PillowSDK.setExternalId("user_123")
```

### `setUserProperty(key, value)`

Sets a property on the current user. Supports `String`, `Boolean`, `Int`, and `Double` values.

```kotlin
PillowSDK.setUserProperty("plan", "pro")
PillowSDK.setUserProperty("email_verified", true)
PillowSDK.setUserProperty("login_count", 7)
PillowSDK.setUserProperty("lifetime_value", 49.5)
```

### `clearUserProperty(key)`

Removes a property from the current user.

```kotlin
PillowSDK.clearUserProperty("plan")
```

### `clearAllProperties()`

Removes all properties from the current user.

```kotlin
PillowSDK.clearAllProperties()
```

### `presentStudy(activity, study, options, listener)`

Presents a Pillow study. Resumes an in-progress session if one exists for the same study ID unless you override that behavior in `options`.

```kotlin
PillowSDK.presentStudy(
  activity = this,
  study = PillowStudy(id = "demo"),
  options = PillowStudyPresentationOptions(
    forceFreshSession = true,
    skipIfAlreadyExposed = true,
  ),
)
```

Use `forceFreshSession = true` to always start a new session.

Use `skipIfAlreadyExposed = true` to only show the study once per user.

### `PillowStudyListener`

An interface with lifecycle methods. Override only the ones you need — all methods have no-op defaults.

| Method | Description |
|--------|-------------|
| `studyDidPresent(study)` | The study modal appeared on screen |
| `studyDidSkip(study)` | The study was intentionally skipped and not presented |
| `studyDidFinish(study)` | The user finished or dismissed the study |
| `studyDidFailToLoad(study, error)` | The study could not be loaded or presented |

All listener methods are invoked on the main thread.

### `onRequestPermissionsResult(...)`

**Required.** Forward permission results to the SDK from any Activity that calls `presentStudy`.

```kotlin
override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray,
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    PillowSDK.onRequestPermissionsResult(requestCode, permissions, grantResults)
}
```

### `reset()`

Clears the user identity and all properties, then starts a fresh anonymous session. Call on logout.

```kotlin
PillowSDK.reset()
```

## Documentation

Full integration guides are available at [docs.trypillow.ai/sdk](https://docs.trypillow.ai/sdk/overview).

## Support

Use GitHub Issues in this repository for SDK bugs, integration questions, and feature requests.
