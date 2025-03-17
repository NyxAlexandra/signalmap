group = "signalmap"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

sourceSets {
    main {
        java {
            srcDirs("src")
        }
        resources {
            srcDirs("res")
        }
    }
    test {
        java {
            srcDirs("test")
        }
    }
}

plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.mkpaz:atlantafx-base:2.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "App"
}

javafx {
    version = "23.0.2"

    modules("javafx.controls", "javafx.base", "javafx.graphics")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
