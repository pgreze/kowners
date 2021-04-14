plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.32" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.16.0" apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    group = "com.github.pgreze"
    version = properties.getOrDefault("VERSION", "WIP").toString()

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config = files(rootDir.resolve("detekt.yml"))
    }
    tasks.withType<Test> {
        useJUnitPlatform {
            includeEngines("spek2")
        }
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        "implementation"(kotlin("stdlib-jdk8"))

        "testImplementation"(kotlin("test"))
        "testImplementation"("com.google.truth:truth:1.0.1")
        val junit5 = "5.3.1"
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:$junit5")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:$junit5")
        val spek = "2.0.7"
        "testImplementation"("org.spekframework.spek2:spek-dsl-jvm:$spek")
        "testRuntimeOnly"("org.spekframework.spek2:spek-runner-junit5:$spek")
        "testImplementation"(kotlin("reflect"))
    }
}
