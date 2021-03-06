plugins {
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("com.palantir.graal") version "0.7.2"
}

// ./gradlew run --args "..." to run locally
// :distZip or :distTar to generate build/distributions/*.zip/tar
// :installDist to unzip. Use: build/install/kowners/bin/kowners ...
application {
    mainClassName = "com.github.pgreze.kowners.KownersKt"
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveBaseName.set("kowners")
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

val installShadowDist = tasks.named("installShadowDist", Sync::class.java)
val dropAllSuffix = tasks.register("dropAllSuffix", Copy::class.java) {
    val libFolder = installShadowDist.get().destinationDir.resolve("lib")
    val artifactName = "kowners-$version-all.jar"
    from(libFolder)
    include(artifactName)
    destinationDir = file(libFolder.parentFile.parentFile)
    rename("-$version-all".toPattern(), "")
}
installShadowDist.configure { finalizedBy(dropAllSuffix) }

dependencies {
    implementation(project(":core"))

    implementation("com.github.ajalt.clikt:clikt:3.1.0")
}
