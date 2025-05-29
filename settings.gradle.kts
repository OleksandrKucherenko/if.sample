pluginManagement {
  repositories {
    mavenLocal() // for SNAPSHOTS
    
    google()
    gradlePluginPortal()
    mavenCentral()
  }
}

rootProject.name = "if.sample"

// include subprojects
include(":services:vehicle")
include(":services:insurance")