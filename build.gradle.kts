buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath ("io.realm:realm-gradle-plugin:10.18.0")
        classpath ("com.android.tools.build:gradle:8.2.1-alpha01")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
}