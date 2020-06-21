package com.dpashko.sandbox.scene.model3d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.GL30
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

open class Model3dScene @Inject protected constructor(
        private val debugRender: DebugRender,
        private val controller: Model3dController
) : Scene {

    private val camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private val batch = ModelBatch()
    private var inputController = Model3dCameraController(camera)
    private val debugObjects = LinkedList<ModelInstance>()
    private val skyBox = SkyBoxShader(FilesProvider.skybox)

    private val environment = Environment().apply {
        set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        add(PointLight().set(Color.WHITE, Vector3(32f, 0f, 32f), 500f))
    }

    override fun init() {
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
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

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