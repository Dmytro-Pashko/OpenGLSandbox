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
import com.dpashko.sandbox.objects.WorldObject
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.scene.debug.DebugScene
import java.util.*
import javax.inject.Inject

class TestScene @Inject protected constructor(
        private val debugScene: DebugScene,
        private val camera: PerspectiveCamera
) : Scene {

    private val batch = ModelBatch()
    private val spriteBatch = SpriteBatch();

    private val worldOrigin = Vector3(0f, 0f, 0f)
    private val cameraPosition: Vector3 = Vector3(-2.5f, -2.5f, 5f)
    private val cameraDir: Vector3 = Vector3(2.5f, 2.5f, 0f)

    private val objects: MutableList<WorldObject> = LinkedList()
    private var inputController: CameraController? = null

    private val environment = Environment().apply {
        set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        add(PointLight().set(Color.WHITE, Vector3(5f, 0f, 5f), 10f))
    }

    override fun init() {
        debugScene.init()
        camera.position.set(cameraPosition)
        camera.lookAt(cameraDir)
        camera.near = 1f
        camera.far = 300f
        camera.rotate(Vector3(Vector3.Y), 45f)
        camera.rotate(Vector3(Vector3.Z), -45f)

        inputController = CameraController(camera).apply {
            Gdx.input.inputProcessor = this
        }
        camera.update()
        initAxises()
        objects.add(WorldObject(worldOrigin, ModelInstance(ModelsFactory.groundModel).also {
            it.transform.set(Vector3.X, Vector3.Y, Vector3.Z, Vector3(0f, 0f, 0f))
        }))
    }

    private fun initAxises() {
        objects.add(WorldObject(worldOrigin, ModelsFactory.createXAxisModel()))
        objects.add(WorldObject(worldOrigin, ModelsFactory.createYAxisModel()))
        objects.add(WorldObject(worldOrigin, ModelsFactory.createZAxisModel()))
    }

    override fun render() {

        debugScene.render()
        inputController?.update()

        camera.update()
        batch.begin(camera)
        for (obj in objects) {
            batch.render(obj.model, environment)
        }
        batch.end()
        drawCameraPos()
    }

    private fun drawCameraPos() {
        spriteBatch.begin()
        FontsProvider.defaultFont.draw(spriteBatch, "Cam pos${camera.position}", 250f, Gdx.graphics.height - 10.toFloat())
        FontsProvider.defaultFont.draw(spriteBatch, "Target${inputController?.target}", 250f, Gdx.graphics.height - 35.toFloat())
        spriteBatch.end()
    }

    override fun dispose() {
        debugScene.dispose()
    }
}