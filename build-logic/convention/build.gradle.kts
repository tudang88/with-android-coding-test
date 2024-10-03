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
    register("androidApplication") {
      id = "myprj.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }
  }
}
version = "1.0.0"
