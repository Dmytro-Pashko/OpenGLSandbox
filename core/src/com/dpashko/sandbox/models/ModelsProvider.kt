package com.dpashko.sandbox.models

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.utils.UBJsonReader
import com.dpashko.sandbox.files.FilesProvider


object ModelsProvider {

    private val jsonReader = UBJsonReader()
    private val modelLoader = G3dModelLoader(jsonReader)

    var ground: Model = modelLoader.loadModel(FilesProvider.groundModel)
}