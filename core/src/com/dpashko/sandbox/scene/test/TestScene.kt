package com.dpashko.sandbox.scene.test

import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.models.ModelsProvider
import com.dpashko.sandbox.objects.WorldObject
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.scene.debug.DebugScene
import java.util.*
import javax.inject.Inject

class TestScene @Inject protected constructor(
        private val debugScene: DebugScene,
        private val camera: PerspectiveCamera,
        private val controller: TestSceneController
) : Scene {

    private val batch: ModelBatch = ModelBatch()
    private val cameraPosition: Vector3 = Vector3(10f, 10f, 10f)
    private val cameraDirection: Vector3 = Vector3(0f, 0f, 0f)
    private val objects: MutableList<WorldObject> = LinkedList()

    override fun init() {
        debugScene.init()
        camera.position.set(cameraPosition)
        camera.direction.set(cameraDirection)
        camera.near = 1f
        camera.far = 300f

        objects.add(WorldObject(Vector3(0f,0f,0f), ModelInstance(ModelsProvider.ground)))
    }

    override fun render() {
        debugScene.render()
        camera.update()
        batch.begin(camera)
        for (obj in objects) {
            batch.render(obj.model)
        }
        batch.end()
    }

    override fun dispose() {
        controller.dispose()
        debugScene.dispose()
    }
}