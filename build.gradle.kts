// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath (libs.google.services)
        classpath (libs.gradle)
        classpath (libs.androidx.navigation.safe.args.gradle.plugin)
    }
}

plugins {
    id ("com.android.application") version "8.0.2" apply false
    id ("com.android.library" )version "8.0.2" apply false
    id ("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id ("androidx.navigation.safeargs.kotlin") version "2.8.0" apply false
    id ("com.google.dagger.hilt.android") version "2.48" apply false
}


