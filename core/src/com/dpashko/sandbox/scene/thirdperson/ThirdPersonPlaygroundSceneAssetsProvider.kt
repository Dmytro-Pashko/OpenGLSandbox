package com.dpashko.sandbox.scene.thirdperson

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.graphics.g3d.Model
import javax.inject.Inject

/**
 * Provider of Third Person Playground scene assets.
 */
class ThirdPersonPlaygroundSceneAssetsProvider : AssetManager {

    companion object {
        private const val MODELS_DIR = "core/assets/models/playground/"
        private const val TEXTURES_DIR = "core/assets/textures/playground/"

        private const val ACTOR_MODEL = "actor.obj"
        private const val PLAYGROUND_MODEL = "playground.obj"
    }

    @Inject
    constructor() : super(FileHandleResolver {
        val fileName = it.substringAfterLast("/", it)
        if (fileName.contains(".obj", true)) {
            Gdx.files.local(MODELS_DIR + fileName)
        } else if (fileName.contains(".mtl", true)) {
            Gdx.files.local(MODELS_DIR + fileName)
        } else if (fileName.contains(".png", true) || fileName.contains(".jpg")) {
            Gdx.files.local(TEXTURES_DIR + fileName)
        } else {
            throw java.lang.IllegalArgumentException("Unsupported asset: $fileName")
        }
    }, true)

    fun getPlaygroundModel(): Model {
        return get(PLAYGROUND_MODEL)
    }

    fun getActorModel(): Model {
        return get(ACTOR_MODEL)
    }

    fun loadAssets() {
        load(PLAYGROUND_MODEL, Model::class.java)
        load(ACTOR_MODEL, Model::class.java)
        update()
    }

}
