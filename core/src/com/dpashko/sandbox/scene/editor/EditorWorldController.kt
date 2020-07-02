package com.dpashko.sandbox.scene.editor

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.PointLight
import com.dpashko.sandbox.model.ModelProvider
import com.dpashko.sandbox.objects.WorldObject

class EditorWorldController(private val state: EditorSceneState) {

    private val light = PointLight().apply {
        intensity = 10f
        setColor(Color.WHITE)
    }

    fun init() {
        state.worldObjects.add(WorldObject(ModelProvider.createBox()))
        state.environment.set(ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f))
        state.environment.add(light)
    }

    fun update() {
        light.setPosition(state.camera.position)
    }

    fun dispose() {
    }
}
