import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    id("java-library")
    id("kotlin")
    id("kotlin-kapt")
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

dependencies {
    api("com.badlogicgames.gdx:gdx:1.10.0")
    api("com.badlogicgames.gdx:gdx-freetype:1.10.0")
    api("com.github.mgsx-dev.gdx-gltf:core:2.0.0-rc.1")
    api("com.google.dagger:dagger:2.41")
    api("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
    kapt("com.google.dagger:dagger-compiler:2.41")
}
