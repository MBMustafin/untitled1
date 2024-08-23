import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "ru.mustafin.spring_book"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.exposed:exposed-core:0.49.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.49.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.49.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.49.0")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("org.slf4j:slf4j-api:2.0.13")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}