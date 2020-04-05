package com.dpashko.sandbox.scene.test

import com.badlogic.gdx.graphics.VertexAttributes
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
    private var ground: ModelInstance? = null

    override fun init() {
        ground = ModelsFactory.loadGround().apply {
            transform.set(Vector3.X, Vector3.Y, Vector3.Z, WORLD_ORIGIN)
            objects.add(WorldObject(this))

            val debugGrid = false
            if (debugGrid) {
                for (mesh in model.meshes) {
                    val vertices = FloatArray(mesh.numVertices * mesh.vertexSize / 4)
                    mesh.getVertices(vertices)

                    val positionOffset = mesh.vertexAttributes.getOffset(VertexAttributes.Usage.Position)
                    for (i in positionOffset until vertices.size step mesh.vertexSize / 4) {
                        val x = vertices[i]
                        val y = vertices[i + 1]
                        val z = vertices[i + 2]

                        objects.add(WorldObject(ModelsFactory.createBox().also {
                            it.transform.set(Vector3.X, Vector3.Y, Vector3.Z, Vector3(x, y, z))
                        }))
                    }

                }
            }
        }

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
