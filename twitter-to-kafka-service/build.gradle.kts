import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "1.6.21"
    java
    //id("org.springframework.boot")
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.spring.study"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.springframework.boot:spring-boot-starter:2.6.7")
    implementation("org.twitter4j:twitter4j-stream:4.0.7")
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.7")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

//tasks.getByName<Test>("test") {
//    useJUnitPlatform()
//}
tasks.test {
    useJUnitPlatform()
}