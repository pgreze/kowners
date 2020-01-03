import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    application
    id("io.gitlab.arturbosch.detekt").version("1.3.1")
    id("com.palantir.graal").version("0.6.0")
}

group = "com.github.pgreze"
version = "1.0"

val applicationClass = "com.github.pgreze.kowners.KownersKt"

// ./gradlew run --args "..." to run locally
// :distZip or :distTar to generate build/distributions/*.zip/tar
// :installDist to unzip. Use: ./build/install/kowners/bin/kowners ...
application {
    mainClassName = applicationClass
}

// https://github.com/palantir/gradle-graal
// :nativeImage is building a bigger but standalone executable. Use: ./build/graal/kowners ...
configure<com.palantir.gradle.graal.GraalExtension> {
    mainClass(applicationClass)
    outputName("kowners")
}

repositories {
    jcenter()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines("spek2")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.ajalt:clikt:2.3.0")

    testImplementation(kotlin("test"))
    val junit5 = "5.3.1"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit5")
    val spek = "2.0.7"
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spek")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spek")
    testImplementation(kotlin("reflect"))
}
