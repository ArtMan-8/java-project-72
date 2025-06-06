group = "hexlet.code"
version = "HexletJavalin-1.0"

application {
    mainClass = "hexlet.code.App"
}

repositories {
    mavenCentral()
}

plugins {
    application
    checkstyle
    jacoco
    id("io.freefair.lombok") version "8.13.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.github.ben-manes.versions") version "0.52.0"
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("gg.jte:jte:3.2.0")
    implementation("gg.jte:jte-watcher:3.2.0")
    implementation("io.javalin:javalin:6.5.0")
    implementation("io.javalin:javalin-rendering:6.5.0")
    implementation("com.zaxxer:HikariCP:6.3.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.12.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
