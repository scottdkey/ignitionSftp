

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
  implementation (project(":common"))
  implementation (project(":client"))
  implementation ("com.inductiveautomation.ignition:ia-gson:2.8.5")
  implementation ("org.slf4j:slf4j-api:1.7.32")
  toModl ("com.jcraft:jsch:0.1.55")
  compileOnly ("com.inductiveautomation.ignitionsdk:ignition-common:${sdkVersion}")
  compileOnly ("com.inductiveautomation.ignitionsdk:gateway-api:${sdkVersion}")
  compileOnly ("com.inductiveautomation.ignitionsdk:reporting-common:${sdkVersion}")
  compileOnly ("com.inductiveautomation.ignitionsdk:perspective-gateway:${sdkVersion}")

}
