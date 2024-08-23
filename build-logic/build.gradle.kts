plugins {
  `kotlin-dsl`
}

group = "com.example.project.template.buildlogic"

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
  implementation(libs.androidGradlePlugin)
  implementation(libs.kotlinGradlePlugin)
  implementation(libs.spotlessGradlePlugin)
}

gradlePlugin {
  plugins {
    register("androidApplicationCompose") {
      id = "example.android.application"
      implementationClass = "AndroidApplicationPlugin"
    }
  }
}
