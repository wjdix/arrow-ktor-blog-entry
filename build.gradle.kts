import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.41"
    kotlin("kapt") version "1.3.41"
    application
}

group = "com.siili"
version = "1.0"

application {
    mainClassName = "com.siili.sample.AppKt"
}

repositories {
    mavenCentral()
    jcenter()
}

val ktorVersion  = "1.2.3"
val arrowVersion = "0.9.0"
val requeryVersion = "1.6.0"

val logbackVersion = "1.2.3"
val h2DatabaseVersion = "1.4.199"

val junitVersion = "5.5.1"
val spekVersion  = "2.0.5"

dependencies {
    // Base
    implementation(kotlin("stdlib-jdk8"))
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Ktor
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")

    // Arrow
    implementation("io.arrow-kt:arrow-core-data:$arrowVersion")
    implementation("io.arrow-kt:arrow-core-extensions:$arrowVersion")
    implementation("io.arrow-kt:arrow-effects-data:$arrowVersion")
    implementation("io.arrow-kt:arrow-effects-extensions:$arrowVersion")
    implementation("io.arrow-kt:arrow-effects-rx2-data:$arrowVersion")
    implementation("io.arrow-kt:arrow-effects-rx2-extensions:$arrowVersion")
    implementation("io.arrow-kt:arrow-typeclasses:$arrowVersion")

    // Requery
    implementation( "io.requery:requery:$requeryVersion")
    implementation( "io.requery:requery-kotlin:$requeryVersion")
    kapt("io.requery:requery-processor:$requeryVersion")

    // Database
    implementation("com.h2database:h2:$h2DatabaseVersion")

    // Unit tests
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines("spek2")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
