package com.dpashko.sandbox.scene.editor

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.dpashko.sandbox.model.ModelProvider
import com.dpashko.sandbox.objects.WorldObject

class EditorWorldController(private val state: EditorSceneState) {

    private val light = DirectionalLight().apply {
        setColor(Color.WHITE)
    }

    fun init() {
        state.worldObjects.add(WorldObject(ModelProvider.createSuzanne()))
        state.environment.add(light)
    }

    fun update() {
        light.direction.set(state.camera.direction)
    }

    fun dispose() {
    }
}
