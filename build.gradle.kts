plugins {
    id("java")
    application
}

group = "ru.tigerbank"
version = "1.0-SNAPSHOT"

application {
    mainClass = "ru.tigerbank.view.ConsoleApplication"
}

repositories {
    mavenCentral()
}

dependencies {
    // JSON - Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")

    // CSV
    implementation("com.opencsv:opencsv:5.7.1")

    // YAML
    implementation("org.yaml:snakeyaml:2.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.7.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}