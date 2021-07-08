plugins {
    kotlin("jvm")
}


dependencies {
    val sdkVersion = "8.1.0-SNAPSHOT"
    compileOnly(project(":common"))
    implementation("com.inductiveautomation.ignitionsdk:reporting-common:${sdkVersion}")
    compile("com.inductiveautomation.ignitionsdk:client-api:${sdkVersion}")
    compile("com.inductiveautomation.ignitionsdk:designer-api:${sdkVersion}")
    compile("com.inductiveautomation.ignitionsdk:vision-designer-api:${sdkVersion}")
    compile("com.inductiveautomation.ignitionsdk:vision-client-api:${sdkVersion}")
}