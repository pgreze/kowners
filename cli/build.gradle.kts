plugins {
    application
    id("com.palantir.graal").version("0.6.0")
}

// ./gradlew run --args "..." to run locally
// :distZip or :distTar to generate build/distributions/*.zip/tar
// :installDist to unzip. Use: build/install/kowners/bin/kowners ...
application {
    mainClassName = "com.github.pgreze.kowners.KownersKt"
}

// https://github.com/palantir/gradle-graal
// :nativeImage is building a bigger but standalone executable. Use: ./build/graal/kowners ...
configure<com.palantir.gradle.graal.GraalExtension> {
    mainClass(application.mainClassName)
    outputName("kowners")
    option("--report-unsupported-elements-at-runtime")
    option("--initialize-at-build-time")
    option("--no-fallback")
    option("--no-server")
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines("spek2")
    }
}

dependencies {
    implementation(project(":core"))

    implementation("com.github.ajalt:clikt:2.3.0")
}
