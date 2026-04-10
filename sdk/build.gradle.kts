plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.sqldelight)
  id("maven-publish")
  id("signing")
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

android {
  namespace = providers.gradleProperty("ANDROID_NAMESPACE").get()
  compileSdk = providers.gradleProperty("ANDROID_COMPILE_SDK").get().toInt()

  defaultConfig {
    minSdk = providers.gradleProperty("ANDROID_MIN_SDK").get().toInt()
    consumerProguardFiles("consumer-rules.pro")
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
  }

  publishing {
    singleVariant("release") {
      withSourcesJar()
      withJavadocJar()
    }
  }
}

dependencies {
  implementation(libs.androidx.security.crypto)
  implementation(libs.androidx.webkit)
  implementation(libs.coroutines.android)
  implementation(libs.coroutines.core)
  implementation(libs.ktor.client.android)
  implementation(libs.ktor.client.content.negotiation)
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.serialization.kotlinx.json)
  implementation(libs.serialization.json)
  implementation(libs.sqldelight.android.driver)
  implementation(libs.sqldelight.runtime)

  testImplementation(kotlin("test"))
}

sqldelight {
  databases {
    create("AudienceDatabase") {
      packageName.set("com.pillow.mobile.audience.persistence")
    }
  }
}

publishing {
  publications {
    create<MavenPublication>("release") {
      groupId = providers.gradleProperty("GROUP").get()
      artifactId = providers.gradleProperty("POM_ARTIFACT_ID").get()
      version = providers.gradleProperty("VERSION_NAME").get()

      afterEvaluate {
        from(components["release"])
      }

      pom {
        name.set(providers.gradleProperty("POM_NAME"))
        description.set(providers.gradleProperty("POM_DESCRIPTION"))
        url.set(providers.gradleProperty("POM_URL"))

        licenses {
          license {
            name.set(providers.gradleProperty("POM_LICENSE_NAME"))
            url.set(providers.gradleProperty("POM_LICENSE_URL"))
          }
        }

        developers {
          developer {
            id.set(providers.gradleProperty("POM_DEVELOPER_ID"))
            name.set(providers.gradleProperty("POM_DEVELOPER_NAME"))
            email.set(providers.gradleProperty("POM_DEVELOPER_EMAIL"))
          }
        }

        scm {
          url.set(providers.gradleProperty("POM_SCM_URL"))
          connection.set(providers.gradleProperty("POM_SCM_CONNECTION"))
          developerConnection.set(providers.gradleProperty("POM_SCM_DEV_CONNECTION"))
        }
      }
    }
  }

  repositories {
    mavenLocal()
    maven {
      name = "ossrhStagingApi"
      url = uri(
        providers.gradleProperty("mavenCentralReleaseUrl")
          .orElse("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
          .get(),
      )
      credentials {
        username = providers.gradleProperty("mavenCentralUsername").orNull
          ?: System.getenv("MAVEN_CENTRAL_USERNAME")
        password = providers.gradleProperty("mavenCentralPassword").orNull
          ?: System.getenv("MAVEN_CENTRAL_PASSWORD")
      }
    }
  }
}

signing {
  val signingKey = providers.gradleProperty("signingInMemoryKey").orNull
    ?: System.getenv("MAVEN_CENTRAL_SIGNING_KEY")
  val signingPassword = providers.gradleProperty("signingInMemoryKeyPassword").orNull
    ?: System.getenv("MAVEN_CENTRAL_SIGNING_PASSWORD")

  if (!signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
  }
}
