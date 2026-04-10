package so.pillow.sdk

import android.app.Activity
import android.content.Context
import com.pillow.mobile.android.sdk.PillowSDK as DelegatePillowSDK

public object PillowSDK {
  public fun initialize(
    context: Context,
    publishableKey: String,
  ) {
    DelegatePillowSDK.initialize(
      context = context,
      publishableKey = publishableKey,
    )
  }

  public fun setExternalId(externalId: String) {
    DelegatePillowSDK.setExternalId(externalId)
  }

  public fun setUserProperty(key: String, value: String) {
    DelegatePillowSDK.setUserProperty(key, value)
  }

  public fun setUserProperty(key: String, value: Boolean) {
    DelegatePillowSDK.setUserProperty(key, value)
  }

  public fun setUserProperty(key: String, value: Int) {
    DelegatePillowSDK.setUserProperty(key, value)
  }

  public fun setUserProperty(key: String, value: Double) {
    DelegatePillowSDK.setUserProperty(key, value)
  }

  public fun clearUserProperty(key: String) {
    DelegatePillowSDK.clearUserProperty(key)
  }

  public fun clearAllProperties() {
    DelegatePillowSDK.clearAllProperties()
  }

  public fun reset() {
    DelegatePillowSDK.reset()
  }

  public fun presentStudy(
    activity: Activity,
    study: PillowStudy,
    options: PillowStudyPresentationOptions = PillowStudyPresentationOptions(),
    listener: PillowStudyListener? = null,
  ) {
    DelegatePillowSDK.presentStudy(
      activity = activity,
      study = study,
      options = options,
      delegate = listener,
    )
  }

  public fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray,
  ) {
    DelegatePillowSDK.onRequestPermissionsResult(
      requestCode = requestCode,
      permissions = permissions,
      grantResults = grantResults,
    )
  }
}
