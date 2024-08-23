#!/bin/bash
echo "Add the build-logic to project"
# Root project directory
PROJECT_DIR=$(pwd)

# 1. Create the build-logic directory structure
mkdir -p "$PROJECT_DIR/build-logic/src/main/kotlin"
touch "$PROJECT_DIR/build-logic/build.gradle.kts"
touch "$PROJECT_DIR/build-logic/settings.gradle"
touch "$PROJECT_DIR/build-logic/.gitignore"

# 2. Add build.gradle.kts for the build-logic module
cat <<EOL > "$PROJECT_DIR/build-logic/build.gradle.kts"
plugins {
  \`kotlin-dsl\`
}

group = "com.example.project.template.buildlogic"

java {
  // please refer to current project version
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
EOL

# 3. Add settings.gradle for the build-logic module
cat <<EOL > "$PROJECT_DIR/build-logic/settings.gradle"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
  versionCatalogs {
    create("libs") {
      from(files("../gradle/libs.versions.toml"))
    }
  }
}
EOL

# 4. Add .gitignore for the build-logic module
cat <<EOL > "$PROJECT_DIR/build-logic/.gitignore"
# Ignore build output
/build/
EOL

# 5. Create a custom plugin for Application module Kotlin file
cat <<EOL > $PROJECT_DIR/build-logic/src/main/kotlin/AndroidApplicationPlugin.kt
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class AndroidApplicationPlugin : Plugin<Project> {
  override fun apply(target: Project) {
  // todo: add later
  }
}
EOL

# 6. Insert includeBuild and enableFeaturePreview into settings.gradle.kts
# Insert includeBuild("build-logic") if not present
if ! grep -q "includeBuild(\"build-logic\")" "$PROJECT_DIR/settings.gradle.kts"; then
  sed -i -e 's/\(pluginManagement {\)/\1\n\tincludeBuild("build-logic")/g' "$PROJECT_DIR/settings.gradle.kts"
fi
# Insert enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS") if not present
if ! grep -q 'enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")' "$PROJECT_DIR/settings.gradle.kts"; then
    echo '' >> "$PROJECT_DIR/settings.gradle.kts"
    echo 'enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")' >> "$PROJECT_DIR/settings.gradle.kts"
fi
# 7. Output message
echo "build-logic module and related files have been created successfully."
echo "You can now use the custom plugin in your modules by adding the following to your build.gradle.kts:"
echo 'plugins { id("com.example.custom-android-plugin") }'
