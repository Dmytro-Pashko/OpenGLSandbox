package com.dpashko.sandbox.model

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader
import com.dpashko.sandbox.files.FileProvider
import com.dpashko.sandbox.material.MaterialProvider

object ModelProvider {

    private val objLoader = ObjLoader()

    @JvmOverloads
    fun createSphere(
        material: Material = MaterialProvider.diffuse(Color.GRAY)
    ) = loadModel(FileProvider.sphereModel, material)

    @JvmOverloads
    fun createCylinder(
        material:
        Material = MaterialProvider.diffuse(Color.GRAY)
    ) = loadModel(FileProvider.cylinderModel, material)

    @JvmOverloads
    fun createBox(
        material: Material = MaterialProvider.diffuse(Color.GRAY)
    ) = loadModel(FileProvider.boxModel, material)

    @JvmOverloads
    fun createSuzanne(
        material: Material = MaterialProvider.diffuse(Color.GRAY)
    ) = loadModel(FileProvider.suzanneModel, material)

    @JvmOverloads
    fun createActor(
        material: Material = MaterialProvider.diffuse(Color.GRAY)
    ) = loadModel(FileProvider.actor, material)

    fun createPlayground() = loadModel(FileProvider.playground, null)

    public fun loadModel(file: FileHandle?, material: Material?): ModelInstance {
        val model = objLoader.loadModel(file, true)
        if (material != null) {
            model.materials.clear()
            model.materials.add(material)
        }
        return ModelInstance(model)
    }
}
