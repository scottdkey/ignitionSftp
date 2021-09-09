plugins {
  kotlin("jvm")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}


val sdkVersion: String by project

dependencies {
  compileOnly("com.inductiveautomation.ignitionsdk:ignition-common:${sdkVersion}")
  compileOnly("com.inductiveautomation.ignitionsdk:perspective-common:${sdkVersion}")
  implementation("com.inductiveautomation.ignitionsdk:reporting-common:${sdkVersion}")
  implementation("org.slf4j:slf4j-api:1.7.32")
  implementation("com.google.guava:guava:30.1.1-jre")
  compileOnly("com.inductiveautomation.ignition:ia-gson:2.8.5")
}