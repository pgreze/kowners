plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.50" apply false
    id("io.gitlab.arturbosch.detekt") version "1.3.1" apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    group = "com.github.pgreze"
    version = "1.0"

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    tasks.withType<Test> {
        useJUnitPlatform {
            includeEngines("spek2")
        }
    }

    repositories {
        jcenter()
    }

    dependencies {
        "implementation"(kotlin("stdlib-jdk8"))

        "testImplementation"(kotlin("test"))
        val junit5 = "5.3.1"
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:$junit5")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:$junit5")
        val spek = "2.0.7"
        "testImplementation"("org.spekframework.spek2:spek-dsl-jvm:$spek")
        "testRuntimeOnly"("org.spekframework.spek2:spek-runner-junit5:$spek")
        "testImplementation"(kotlin("reflect"))
    }
}
