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
        if (it.contains(".obj", true)) {
            return@FileHandleResolver Gdx.files.local(MODELS_DIR + it)
        } else if (it.contains("*.mtl", true)) {
            return@FileHandleResolver Gdx.files.local(MODELS_DIR + it)
        } else if (it.contains("*.png", true)) {
            return@FileHandleResolver Gdx.files.local(TEXTURES_DIR + it)
        } else {
            throw java.lang.IllegalArgumentException("Unsupported asset: $it")
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
