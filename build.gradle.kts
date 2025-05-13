// File: build.gradle.kts (Project-level)

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        // Downgrade AGP to a version that works with Kotlin 1.8 (e.g., 7.4.2)
        classpath("com.android.tools.build:gradle:7.4.2")
        // Force Kotlin to 1.8.21
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
        classpath("com.google.gms:google-services:4.4.2")
        // No KSP classpath dependency needed since you're switching to KAPT.
    }
}
