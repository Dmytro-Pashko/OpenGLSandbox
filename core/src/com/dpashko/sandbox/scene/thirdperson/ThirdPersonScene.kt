package com.dpashko.sandbox.scene.thirdperson

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.PointLight
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.material.MaterialProvider
import com.dpashko.sandbox.model.ModelProvider
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.shader.BoundingBoxShader
import com.dpashko.sandbox.shader.GridShader
import javax.inject.Inject

class ThirdPersonScene @Inject internal constructor() : Scene {

    private val batch = ModelBatch()
    private val gridShader = GridShader(32)
    private lateinit var boundingBoxShader: BoundingBoxShader

    private lateinit var camera: Camera
    private lateinit var actor: Actor
    private lateinit var cameraController: ThirdPersonCameraController
    private lateinit var environment: Environment
    private lateinit var gui: ThirdPersonSceneGui

    override fun init() {
        actor = Actor(
            Vector3.Zero,
            Vector3.Y,
            ModelInstance(ModelProvider.createSuzanne(material = MaterialProvider.diffuse(Color.GREEN)))
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
        environment = Environment().apply {
            set(ColorAttribute.createDiffuse(Color.GREEN))
            add(PointLight().apply {
                position.set(0f, 0f, 10f)
                intensity = 50f
            })

        }
        gui = ThirdPersonSceneGui().apply {
            init()
        }
    }

    override fun draw() {
        cameraController.update()

        Gdx.gl.glEnable(GL30.GL_DEPTH_TEST)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT)

        // Draw debug objects
        gridShader.draw(camera)
        boundingBoxShader.render(camera)
        // Draw models.
        batch.begin(camera)
        batch.render(actor.model, environment)
        batch.end()
        // Draw GUI.
        gui.update(camera.position, camera.direction)
        gui.draw()
    }

    override fun dispose() {
        batch.dispose()
        gridShader.dispose()
        boundingBoxShader.dispose()
        actor.model?.model?.dispose()
    }
}
