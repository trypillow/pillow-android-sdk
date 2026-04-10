package so.pillow.sample

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import so.pillow.sdk.PillowSDK
import so.pillow.sdk.PillowStudy
import so.pillow.sdk.PillowStudyListener

class MainActivity : Activity() {
  private val studyListener = object : PillowStudyListener {
    override fun studyDidFailToLoad(study: PillowStudy, error: Throwable) {
      Log.e("PillowSample", "Study ${study.id} failed to load: ${error.message}", error)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    PillowSDK.initialize(
      context = applicationContext,
      publishableKey = "pk_live_demo",
    )

    val study = PillowStudy(id = "demo")

    setContentView(
      TextView(this).apply {
        text = "Pillow Android SDK sample is configured.\nTap to present study ID: ${study.id}"
        setOnClickListener { presentStudy(study) }
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
  private fun presentStudy(study: PillowStudy) {
    PillowSDK.presentStudy(
      activity = this,
      study = study,
      listener = studyListener,
    )
  }
}
