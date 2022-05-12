import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    // Plugin for Jar files creation.
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
    id("kotlin")
}

kotlin {
    java {
        sourceCompatibility = VERSION_1_8
        targetCompatibility = VERSION_1_8
    }
}

java.sourceSets["main"].java {
    srcDir("src/")
}

/**
 * Copy resourcrs into build dir for including into final jar file.
 */
val prepareResourceTask = tasks.register("prepareResources", Copy::class.java) {
    from(File(rootProject.projectDir.path + "/resources"))
    into(File(project.buildDir.path + "/tmp/res/resources"))
}.get()

val prepareJarTask = tasks.withType<ShadowJar> {
    configurations.add(project.configurations.getAt("compileClasspath"))
    from(File(project.buildDir.path + "/tmp/res/"))

    archiveBaseName.set(rootProject.name)
    archiveClassifier.set("debug")
    archiveVersion.set(rootProject.version.toString())

    manifest {
        attributes.put("Main-Class", "com.dpashko.sandbox.desktop.DesktopLauncher")
    }
}.first()

prepareJarTask.dependsOn(prepareResourceTask)

dependencies {
    api(project(":core"))
    api("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.10.0")
    api("com.badlogicgames.gdx:gdx-platform:1.10.0:natives-desktop")
    api("com.badlogicgames.gdx:gdx-freetype-platform:1.10.0:natives-desktop")
}