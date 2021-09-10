plugins {
  kotlin("jvm")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = "11"
  }
}
val sdkVersion: String by project

dependencies {
  implementation(project(":common"))
  implementation(project(":client"))
  implementation("com.inductiveautomation.ignitionsdk:client-api:${sdkVersion}")
  implementation("com.inductiveautomation.ignitionsdk:designer-api:${sdkVersion}")
  implementation("com.inductiveautomation.ignitionsdk:vision-designer-api:${sdkVersion}")
  compileOnly("com.inductiveautomation.ignitionsdk:perspective-common:${sdkVersion}")
  compileOnly("com.inductiveautomation.ignitionsdk:perspective-designer:${sdkVersion}")
  compileOnly("com.inductiveautomation.ignitionsdk:ignition-common:${sdkVersion}")
}