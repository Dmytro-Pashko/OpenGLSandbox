package com.dpashko.sandbox.scene.model3d

import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.model.ModelsProvider
import com.dpashko.sandbox.objects.WorldObject
import com.dpashko.sandbox.scene.Controller
import java.util.*
import javax.inject.Inject

class Model3dController @Inject constructor() : Controller {

    val objects: MutableList<WorldObject> = LinkedList()
    private var ground: ModelInstance? = null

    override fun init() {
        ground = ModelsProvider.loadGround().apply {
            transform.set(Vector3.X, Vector3.Y, Vector3.Z, Vector3.Zero)
            objects.add(WorldObject(this))
        }
    }

    override fun tick() {

    }

    override fun dispose() {
        for (obj in objects) {
            obj.model?.model?.dispose()
        }
    }
}
