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
    implementation(project(":common"))
    implementation("com.inductiveautomation.ignitionsdk:client-api:$sdkVersion")
    implementation("com.inductiveautomation.ignitionsdk:designer-api:$sdkVersion")
    implementation("com.inductiveautomation.ignitionsdk:vision-designer-api:$sdkVersion")
    implementation("com.inductiveautomation.ignitionsdk:vision-client-api:$sdkVersion")
}