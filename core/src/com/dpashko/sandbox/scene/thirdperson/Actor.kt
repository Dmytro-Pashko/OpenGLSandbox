package com.dpashko.sandbox.scene.thirdperson

import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox

class Actor(
    val position: Vector3,
    val direction: Vector3,
    val model: ModelInstance?
) : Renderable() {

    val boundingBox = model?.calculateBoundingBox(BoundingBox())

    init {
        model?.transform?.setTranslation(position)
    }


}