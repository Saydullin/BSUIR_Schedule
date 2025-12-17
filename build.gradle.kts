// Top-level build file where you can add configuration options common to all subprojects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    id("com.google.devtools.ksp") version "2.2.20-2.0.4" apply false
    id("com.google.dagger.hilt.android") version "2.57.2" apply false
    id("androidx.room") version "2.8.4" apply false
}


