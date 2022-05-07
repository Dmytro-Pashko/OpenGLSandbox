package com.dpashko.sandbox.scene.thirdperson

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.shader.BoundingBoxShader
import com.dpashko.sandbox.shader.GridShader
import javax.inject.Inject

class ThirdPersonPlaygroundScene @Inject internal constructor(
    private val assetManager: ThirdPersonPlaygroundSceneAssetsProvider
) : Scene {

    private val batch = ModelBatch()
    private val gridShader = GridShader(32)
    private lateinit var boundingBoxShader: BoundingBoxShader

    private lateinit var camera: Camera
    private lateinit var cameraController: ThirdPersonCameraController

    private lateinit var playground: ModelInstance
    private lateinit var actor: Actor

    private lateinit var sceneGui: ThirdPersonPlaygroundSceneGui
    private lateinit var loadingGui: ThirdPersonPlaygroundSceneLoadingGui

    private var isSceneLoaded = false

    override fun init() {
        isSceneLoaded = false
        loadingGui = ThirdPersonPlaygroundSceneLoadingGui().apply {
            init()
        }
        // Starts loading of scene assets asynchronously.
        assetManager.loadAssets()
    }

    private fun loadSceneState() {
        playground = ModelInstance(assetManager.getPlaygroundModel())
        actor = Actor(
            Vector3.Zero,
            Vector3.Y,
            ModelInstance(assetManager.getActorModel())
        ).apply {
            boundingBox?.let {
                boundingBoxShader = BoundingBoxShader(it)
            }
        }
        camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()).apply {
            up.set(Vector3.Z)
            update()
        }
        cameraController = ThirdPersonCameraController(actor, camera).also {
            Gdx.input.inputProcessor = it
        }
        sceneGui = ThirdPersonPlaygroundSceneGui().apply {
            init()
        }
    }

    override fun draw() {
        Gdx.gl.glEnable(GL30.GL_DEPTH_TEST)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT)
        // Draw Loading GUI till asset loaded.
        if (!assetManager.isFinished) {
            assetManager.update()
            loadingGui.update(assetManager.progress)
            loadingGui.draw()
        } else {
            if (isSceneLoaded) {
                cameraController.update()
                // Draw debug objects
                gridShader.draw(camera)
                boundingBoxShader.render(camera)
                // Draw models.
                batch.begin(camera)
                batch.render(playground)
                batch.render(actor.model)
                batch.end()
                // Draw GUI.
                sceneGui.update(camera, actor, cameraController.deltaX, cameraController.deltaY)
                sceneGui.draw()
            } else {
                loadSceneState()
                isSceneLoaded = true
            }
        }
    }

    override fun dispose() {
        batch.dispose()
        gridShader.dispose()
        boundingBoxShader.dispose()
        assetManager.clear()
    }
}
