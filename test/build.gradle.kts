plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("kotlin-gradle-plugin-example") version "1.0"
}
group = "net.matsudamper"

repositories {
    mavenCentral()
}

dependencies {
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

tasks.test {
    useJUnitPlatform()
}
