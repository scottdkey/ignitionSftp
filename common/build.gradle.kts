plugins {
  kotlin("jvm")
  "java-library"
}


dependencies {
  val sdkVersion = "8.1.0-SNAPSHOT"
  compileOnly("com.inductiveautomation.ignitionsdk:ignition-common:${sdkVersion}")
  compileOnly("com.inductiveautomation.ignitionsdk:perspective-common:${sdkVersion}")
  implementation("com.inductiveautomation.ignitionsdk:reporting-common:${sdkVersion}")
  implementation("org.apache.poi:poi:4.1.1")
  implementation("org.slf4j:slf4j-api:1.7.21")
  compileOnly("com.google.guava:guava:23.3-jre")
  compileOnly("com.inductiveautomation.ignition:ia-gson:2.8.5")
  implementation("com.hierynomus:sshj:0.27.0")
}