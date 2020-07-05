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
import com.dpashko.sandbox.material.MaterialProvider
import com.dpashko.sandbox.model.ModelProvider
import com.dpashko.sandbox.scene.Scene
import javax.inject.Inject

open class Model3dScene @Inject protected constructor() : Scene {

    private val camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private val batch = ModelBatch()
    private val model = ModelInstance(ModelProvider.loadModel(
            FileProvider.suzanneModel, MaterialProvider.diffuse(Color.GOLD)))

    private val environment = Environment().apply {
        add(PointLight().set(Color.WHITE, Vector3(8f, -8f, 8f), 100f))
    }

    override fun init() {
        camera.position.set(0f, -5f, 2f)
        camera.lookAt(Vector3.Zero)
        camera.update()
    }

    override fun draw() {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        camera.rotateAround(Vector3.Zero, Vector3.Z, 1f)
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
