// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://plugins.gradle.org/m2/")
        flatDir {
            dirs("libs")
        }
    }

    dependencies {
        classpath("io.realm:realm-gradle-plugin:10.19.0")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    //// không cần plugin ở đây nếu chỉ apply trong :app
}
