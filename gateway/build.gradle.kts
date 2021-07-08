plugins {
  kotlin("jvm")
  "java-library"
}

dependencies {
  val sdkVersion= "8.1.0-SNAPSHOT"
  compile(project(":common"))
  compile(project(":client"))
  compileOnly("com.inductiveautomation.ignitionsdk:ignition-common:${sdkVersion}")
  compileOnly("com.inductiveautomation.ignitionsdk:gateway-api:${sdkVersion}")
  implementation("com.inductiveautomation.ignitionsdk:reporting-common:${sdkVersion}")
  compileOnly("com.inductiveautomation.ignitionsdk:perspective-gateway:${sdkVersion}")
  implementation("org.slf4j:slf4j-api:1.7.21")
  implementation("org.apache.poi:poi:3.13")
}