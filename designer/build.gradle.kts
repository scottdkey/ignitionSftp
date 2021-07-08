plugins {
  kotlin("jvm")
  "java-library"
}

dependencies {
  val sdkVersion= "8.1.0-SNAPSHOT"
  compile(project(":common"))
  compile(project(":client"))
  implementation("com.inductiveautomation.ignitionsdk:client-api:${sdkVersion}")
  implementation("com.inductiveautomation.ignitionsdk:designer-api:${sdkVersion}")
  implementation("com.inductiveautomation.ignitionsdk:vision-designer-api:${sdkVersion}")
  compileOnly("com.inductiveautomation.ignitionsdk:perspective-common:${sdkVersion}")
  compileOnly("com.inductiveautomation.ignitionsdk:perspective-designer:${sdkVersion}")
  compileOnly("com.inductiveautomation.ignitionsdk:ignition-common:${sdkVersion}")

}