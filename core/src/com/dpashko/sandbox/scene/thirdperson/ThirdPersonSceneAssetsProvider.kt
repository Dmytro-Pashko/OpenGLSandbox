package com.dpashko.sandbox.scene.thirdperson

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader
import com.badlogic.gdx.assets.loaders.SkinLoader
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.scenes.scene2d.ui.Skin
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

        private const val ACTOR_MODEL = "resources/models/playground/actor.glb"
        private const val PLAYGROUND_MODEL = "resources/models/playground/playground.gltf"
        private const val DEFAULT_UI_SKIN = "resources/ui/default/uiskin.json"

        private val GRID_SHADER = ShaderProgramLoader.ShaderProgramParameter().apply {
            fragmentFile = "resources/shaders/grid_3d_f.glsl"
            vertexFile = "resources/shaders/grid_3d_v.glsl"
        }

        private val ASSETS_RESOLVER = FileHandleResolver { assetName ->
            // Normalize path to asset, some application keeps some related path double dots are used for moving up in
            // the hierarchy.
            val assetFile = Gdx.files.internal(Gdx.files.internal(assetName).file().normalize().path)
            println("Loading of ${assetFile.file().absoluteFile}")
            assetFile
        }
    }

    init {
        setLoader(SceneAsset::class.java, ".gltf", GLTFAssetLoader(ASSETS_RESOLVER))
        setLoader(SceneAsset::class.java, ".glb", GLBAssetLoader(ASSETS_RESOLVER))
        setLoader(ShaderProgram::class.java, ShaderProgramLoader(ASSETS_RESOLVER))
        setLoader(Texture::class.java, TextureLoaderEx(ASSETS_RESOLVER))
        setLoader(Pixmap::class.java, PixmapLoaderEx(ASSETS_RESOLVER))
        setLoader(Skin::class.java, SkinLoader(ASSETS_RESOLVER))
        setLoader(TextureAtlas::class.java, TextureAtlasLoader(ASSETS_RESOLVER))
    }

    /**
     * Starts async loading of scene assets.
     */
    fun loadAssets() {
        load(DEFAULT_UI_SKIN, Skin::class.java)
        load(PLAYGROUND_MODEL, SceneAsset::class.java)
        load(ACTOR_MODEL, SceneAsset::class.java)
        load(AssetDescriptor("", ShaderProgram::class.java, GRID_SHADER))
        update()
    }

    fun getPlaygroundModel(): SceneAsset {
        return get(PLAYGROUND_MODEL)
    }

    fun getActorModel(): SceneAsset {
        return get(ACTOR_MODEL)
    }

    fun getDefaultGuiSkin(): Skin {
        if (!isLoaded(DEFAULT_UI_SKIN)) {
            finishLoadingAsset<Skin>(DEFAULT_UI_SKIN)
        }
        return get(DEFAULT_UI_SKIN)
    }

    fun getGridShader(): ShaderProgram {
        return get(AssetDescriptor("", ShaderProgram::class.java, GRID_SHADER))
    }

    fun getBoundingBoxShader(): ShaderProgram {
        return get(AssetDescriptor("", ShaderProgram::class.java, GRID_SHADER))
    }
}
