package com.dpashko.sandbox.scene.model3d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.environment.PointLight
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.files.FileProvider
import com.dpashko.sandbox.model.ModelProvider
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.scene.editor.EditorCameraController
import javax.inject.Inject

open class Model3dScene @Inject protected constructor() : Scene {

    private val rotationSpeed = .1f
    private val camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private val cameraController = EditorCameraController(camera)
    private val batch = ModelBatch()
    private val model = ModelInstance(ModelProvider.loadModel(FileProvider.house, null))

    private val environment = Environment().apply {
        add(PointLight().set(Color.WHITE, Vector3(25f, 25f, 40f), 2000f))
        add(PointLight().set(Color.WHITE, Vector3(-25f, -25f, 40f), 2000f))
        add(PointLight().set(Color.WHITE, Vector3(25f, -25f, 40f), 2000f))
        add(PointLight().set(Color.WHITE, Vector3(-25f, 25f, 40f), 2000f))
    }

    override fun init() {
        Gdx.input.inputProcessor = cameraController
        camera.position.set(0f, -15f, 4f)
        camera.lookAt(Vector3(0f, 0f, 1f))
        camera.update()
    }

    override fun draw() {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        camera.rotateAround(Vector3.Zero, Vector3.Z, rotationSpeed)
        camera.update()
        batch.begin(camera)
        batch.render(model, environment)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        model.model.dispose()
    }
}
