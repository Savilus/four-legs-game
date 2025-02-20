plugins {
    id("java")
    id ("io.freefair.lombok") version "5.3.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor ("org.projectlombok:lombok:1.18.36")
    compileOnly ("org.projectlombok:lombok:1.18.36")
    implementation ("io.vavr:vavr:0.10.5")
    implementation("org.yaml:snakeyaml:2.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.slf4j:slf4j-api:2.0.9")
}

tasks.test {
    useJUnitPlatform()
}