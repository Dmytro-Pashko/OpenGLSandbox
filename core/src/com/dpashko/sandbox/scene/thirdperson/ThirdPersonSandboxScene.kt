package com.dpashko.sandbox.scene.thirdperson

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.scene.SandboxScene
import com.dpashko.sandbox.shader.BoundingBoxShader
import com.dpashko.sandbox.shader.GridShader
import net.mgsx.gltf.scene3d.lights.PointLightEx
import net.mgsx.gltf.scene3d.scene.Scene
import net.mgsx.gltf.scene3d.scene.SceneManager
import javax.inject.Inject

class ThirdPersonSandboxScene @Inject internal constructor(
    private val assetManager: ThirdPersonSceneAssetsProvider
) : SandboxScene {

    private val sceneManager = SceneManager()
    private val gridShader = GridShader(32)
    private lateinit var boundingBoxShader: BoundingBoxShader

    private lateinit var camera: Camera
    private lateinit var cameraController: ThirdPersonCameraController

    private lateinit var playground: Scene
    private lateinit var actor: Actor

    private lateinit var sceneGui: ThirdPersonSceneGui
    private lateinit var loadingGui: ThirdPersonSceneLoadingGui

    private var isSceneLoaded = false

    override fun init() {
        isSceneLoaded = false
        loadingGui = ThirdPersonSceneLoadingGui().apply {
            init()
        }
        // Starts loading of scene assets asynchronously.
        assetManager.loadAssets()
    }

    // Initialize scene state after loading assets.
    private fun loadSceneState() {
        playground = Scene(assetManager.getPlaygroundModel().scene)
        actor = Actor(
            Vector3.Zero,
            Vector3.Y,
            ModelInstance(assetManager.getActorModel().scene?.model)
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
        sceneGui = ThirdPersonSceneGui().apply {
            init()
        }
        sceneManager.addScene(playground, false)
        sceneManager.addScene(Scene(actor.model), false)

        sceneManager.setAmbientLight(0f)

        val sunLight = PointLightEx().apply {
            val direction = Vector3(Vector3.Z).scl(-1f)
            position.set(Vector3(0f, 0f, 32f))
            color.set(Color.WHITE)
            intensity = 3000f
            range = 64f
        }
        sceneManager.environment.add(sunLight)

        sceneManager.camera = camera
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
                sceneManager.update(Gdx.graphics.deltaTime)
                // Draw debug objects
                gridShader.draw(camera)
                boundingBoxShader.render(camera)
                // Draw scene.
                sceneManager.render()
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
        sceneManager.dispose()
        gridShader.dispose()
        assetManager.clear()
    }
}
