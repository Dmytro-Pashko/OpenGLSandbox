package com.dpashko.sandbox.scene.test

import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.models.ModelsFactory
import com.dpashko.sandbox.objects.WorldObject
import com.dpashko.sandbox.scene.Controller
import java.util.*
import javax.inject.Inject

class TestSceneController @Inject constructor() : Controller {

    companion object {
        private const val WORLD_SIZE = 64f
        val WORLD_ORIGIN: Vector3 = Vector3.Zero
    }

    val objects: MutableList<WorldObject> = LinkedList()

    override fun init() {
        objects.add(WorldObject(ModelInstance(ModelsFactory.groundModel).also {
            it.transform.set(Vector3.X, Vector3.Y, Vector3.Z, WORLD_ORIGIN)
        }))
    }

    override fun tick() {

    }

    override fun dispose() {
        for (obj in objects) {
            obj.model?.model?.dispose()
        }
    }

    fun getWorldSize() = WORLD_SIZE
}
