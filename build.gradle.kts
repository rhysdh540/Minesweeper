plugins {
    java
}

group = "dev.rdh"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    val lombok = "org.projectlombok:lombok:1.18.20"
    implementation(lombok)
    annotationProcessor(lombok)
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "dev.rdh.minesweeper.Minesweeper"
    }
}