plugins {
    kotlin("jvm") version "1.6.10"
    id("java-library")
}

group = "com.rxcountdowntimer"
version = "0.1.0"

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("io.reactivex.rxjava3:rxjava:3.1.3")
}

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
            "Implementation-Version" to project.version))
    }
}
