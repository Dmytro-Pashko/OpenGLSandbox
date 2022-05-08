package com.dpashko.sandbox.scene.thirdperson

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.dpashko.sandbox.utils.PixmapLoaderEx
import com.dpashko.sandbox.utils.TextureLoaderEx
import net.mgsx.gltf.loaders.glb.GLBAssetLoader
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader
import net.mgsx.gltf.scene3d.scene.SceneAsset
import javax.inject.Inject

/**
 * Provider of Third Person Playground scene assets. Loads assets asynchronously on background thread.
 */
class ThirdPersonSceneAssetsProvider @Inject constructor() : AssetManager(ASSETS_RESOLVER, false) {

    companion object {
        private const val MODELS_DIR = "core/assets/models/playground/"
        private const val TEXTURES_DIR = "core/assets/textures/playground/"

        private const val ACTOR_MODEL = "actor.glb"
        private const val PLAYGROUND_MODEL = "playground.gltf"

        private val supportedFormats = mapOf(
            ".gltf" to MODELS_DIR,
            ".glb" to MODELS_DIR,

            ".png" to TEXTURES_DIR,
            ".jpg" to TEXTURES_DIR,
        )

        private val ASSETS_RESOLVER = FileHandleResolver { assetFile ->
            val fileName = assetFile.substringAfterLast("/", assetFile)
            var handle: FileHandle? = null
            supportedFormats.forEach { (format, assetsDir) ->
                if (assetFile.contains(format, true)) {
                    handle = Gdx.files.local(assetsDir + fileName)
                }
            }
            handle ?: throw IllegalArgumentException("Unsupported asset: $fileName")
        }
    }

    init {
        setLoader(SceneAsset::class.java, ".gltf", GLTFAssetLoader(ASSETS_RESOLVER))
        setLoader(SceneAsset::class.java, ".glb", GLBAssetLoader(ASSETS_RESOLVER))
        setLoader(Texture::class.java, TextureLoaderEx(ASSETS_RESOLVER))
        setLoader(Pixmap::class.java, PixmapLoaderEx(ASSETS_RESOLVER))
    }

    fun getPlaygroundModel(): SceneAsset {
        return get(PLAYGROUND_MODEL)
    }

    fun getActorModel(): SceneAsset {
        return get(ACTOR_MODEL)
    }

    /**
     * Starts async loading of scene assets.
     */
    fun loadAssets() {
        load(PLAYGROUND_MODEL, SceneAsset::class.java)
        load(ACTOR_MODEL, SceneAsset::class.java)
        update()
    }
}
