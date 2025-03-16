rootProject.name = "signalmap"

include("desktop")

plugins {
    // Automatic JDK downloading
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
