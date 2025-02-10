// Build

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

sourceSets {
    main {
        // Use `src/**/*` instead of `src/main/java/domain/name/notation/**/*.java`
        java {
            srcDirs("src")
        }
    }
}

// Dependencies

plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.mkpaz:atlantafx-base:2.0.0")
}

// Targets

application {
    mainClass = "App"
}

javafx {
    version = "23.0.2"

    modules("javafx.controls", "javafx.base", "javafx.graphics")
}
