plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "so.pillow.sample"
  compileSdk = providers.gradleProperty("ANDROID_COMPILE_SDK").get().toInt()

  defaultConfig {
    applicationId = "so.pillow.sample"
    minSdk = providers.gradleProperty("ANDROID_MIN_SDK").get().toInt()
    targetSdk = providers.gradleProperty("ANDROID_COMPILE_SDK").get().toInt()
    versionCode = 1
    versionName = providers.gradleProperty("VERSION_NAME").get()
  }

  buildTypes {
    debug {
      isMinifyEnabled = false
    }
    release {
      isMinifyEnabled = false
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
  }
}

val useMavenLocalSdk = providers.gradleProperty("useMavenLocalSdk")
  .map { it.equals("true", ignoreCase = true) }
  .orElse(false)

dependencies {
  if (useMavenLocalSdk.get()) {
    implementation(
      "${providers.gradleProperty("GROUP").get()}:${providers.gradleProperty("POM_ARTIFACT_ID").get()}:${providers.gradleProperty("VERSION_NAME").get()}",
    )
  } else {
    implementation(project(":sdk"))
  }
}
