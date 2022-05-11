import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    // Plugin for Jar files creation.
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
    id("kotlin")
}

kotlin{
    java{
        sourceCompatibility = VERSION_1_8
        targetCompatibility = VERSION_1_8
    }
}

java.sourceSets["main"].java {
    srcDir("src/")
}

tasks.withType<ShadowJar> {
    configurations.add(project.configurations.getAt("compileClasspath"))

    archiveBaseName.set(rootProject.name)
    archiveClassifier.set("debug")
    archiveVersion.set(rootProject.version.toString())

    manifest {
        attributes.put("Main-Class", "com.dpashko.sandbox.desktop.DesktopLauncher")
    }
}

dependencies {
    api(project(":core"))
    api("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.10.0")
    api("com.badlogicgames.gdx:gdx-platform:1.10.0:natives-desktop")
    api("com.badlogicgames.gdx:gdx-freetype-platform:1.10.0:natives-desktop")
}