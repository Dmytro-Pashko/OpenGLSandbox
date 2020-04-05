package com.dpashko.sandbox.scene.test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.PointLight
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.font.FontsProvider
import com.dpashko.sandbox.models.ModelsFactory
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.scene.debug.DebugScene
import java.util.*
import javax.inject.Inject

class TestScene @Inject protected constructor(
        private val debugScene: DebugScene,
        private val camera: PerspectiveCamera,
        private val controller: TestSceneController
) : Scene {

    private val cameraPositionLabel = "Camera Position: %s"
    private val batch = ModelBatch()
    private val spriteBatch = SpriteBatch();
    private var inputController = CameraController(camera)
    private val debugObjects = LinkedList<ModelInstance>()

    private val environment = Environment().apply {
        set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        add(PointLight().set(Color.WHITE, Vector3(5f, 0f, 5f), 10f))
    }

    override fun init() {
        debugScene.init()
        Gdx.input.inputProcessor = inputController
        camera.apply {
            near = 1f
            far = 300f
            position.set(Vector3(-1f, -2f, 5f))
            rotate(Vector3.X, 45f)
            rotate(Vector3.Z, -15f)
            update()
        }
        initAxises()
        initWorldObjects()
    }

    private fun initAxises() {
        debugObjects.add(ModelsFactory.createXAxisModel(controller.getWorldSize()))
        debugObjects.add(ModelsFactory.createYAxisModel(controller.getWorldSize()))
        debugObjects.add(ModelsFactory.createZAxisModel(controller.getWorldSize()))
    }

    private fun initWorldObjects() {
        controller.init()
    }

    override fun render() {
        inputController.update()
        batch.begin(camera)
        for (obj in debugObjects) {
            batch.render(obj, environment)
        }
        for (obj in controller.objects) {
            batch.render(obj.model, environment)
        }
        batch.end()
        drawCameraPos()
        debugScene.render()
    }

    private fun drawCameraPos() {
        spriteBatch.begin()
        FontsProvider.defaultFont.draw(spriteBatch, cameraPositionLabel.format(camera.position),
                250f, Gdx.graphics.height - 10.toFloat())
        spriteBatch.end()
    }

    override fun dispose() {
        for (obj in debugObjects) {
            obj.model?.dispose()
        }
        controller.dispose()
        debugScene.dispose()
    }
}