package com.dpashko.sandbox.scene.test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.PointLight
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.files.FilesProvider
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.scene.debug.DebugRender
import com.dpashko.sandbox.shader.SkyBoxShader
import java.util.*
import javax.inject.Inject

class TestScene @Inject protected constructor(
        private val debugRender: DebugRender,
        private val camera: PerspectiveCamera,
        private val controller: TestSceneController
) : Scene {

    private val batch = ModelBatch()
    private var inputController = CameraController(camera)
    private val debugObjects = LinkedList<ModelInstance>()
    private val skyBox = SkyBoxShader(FilesProvider.skybox)

    private val environment = Environment().apply {
        set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        add(PointLight().set(Color.WHITE, Vector3(32f, 0f, 32f), 500f))
    }

    override fun init() {
        debugRender.init()
        Gdx.input.inputProcessor = inputController
        camera.apply {
            near = 1f
            far = 300f
            position.set(Vector3(-1f, -2f, 5f))
            rotate(Vector3.X, 45f)
            rotate(Vector3.Z, -15f)
            update()
        }
        initWorldObjects()
    }

    private fun initWorldObjects() {
        controller.init()
    }

    override fun render() {
        inputController.update()
        skyBox.render(camera)
        batch.begin(camera)
        for (obj in controller.objects) {
            batch.render(obj.model, environment)
        }
        batch.end()
        debugRender.render(camera)
    }

    override fun dispose() {
        for (obj in debugObjects) {
            obj.model?.dispose()
        }
        controller.dispose()
        debugRender.dispose()
    }
}