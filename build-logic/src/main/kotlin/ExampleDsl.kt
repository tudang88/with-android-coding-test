import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

fun Project.example(block: ProjectScope.() -> Unit) {
    block(ProjectScope(this))
}

class ProjectScope(val target: Project) {
    /**
     * define a convenient helper method to config
     * an android app module
     */
    fun androidApplication(block: AndroidScope.() -> Unit) {
        with(target) {
            // apply common plugin for and standard android application module
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-kapt")
            }
            // read version catalog from toml file
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            // configure for android block
            extensions.configure<BaseAppModuleExtension> {
                compileSdk = 34

                defaultConfig {
                    minSdk = libs.findVersion("minSdk").get().toString().toInt()
                    targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                    isCoreLibraryDesugaringEnabled = true
                }

                kotlinOptions {
                    jvmTarget = "17"
                    // treat all kotlin warnings as errors
                    allWarningsAsErrors = properties["warningAsErrors"] as? Boolean ?: false
                    freeCompilerArgs += listOf(
                        //              "-opt-in=kotlin.RequiresOptIn",
                        // Enable experimental coroutines APIs, including Flow
                        //              "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    )
                }

                // dependencies
//                dependencies {
//                    add("coreLibraryDesugaring", libs.findLibrary("androidDesugaring").get())
//                }
                // delegate lambda block to android scope
                block(AndroidScope(target, this))
            }
        }
    }
}

/**
 * define an AndroidScope
 * mainly config dagger-hilt
 */
class AndroidScope(private val target: Project, val androidExtension: TestedExtension) {
    fun hilt() {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("implementation", libs.findLibrary("hilt.android.core").get())
                add("kapt", libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}

/**
 * extension function
 * for add kotlin jvm
 */
private fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}