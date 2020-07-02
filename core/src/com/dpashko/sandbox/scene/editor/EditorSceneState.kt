package com.dpashko.sandbox.scene.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.objects.WorldObject

data class EditorSceneState(
        var camera: PerspectiveCamera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
                .apply {
                    near = 1f
                    far = 300f
                    position.set(Vector3(-1f, -2f, 5f))
                    rotate(Vector3.X, 45f)
                    rotate(Vector3.Z, -15f)
                    update()
                },
        var worldSize: WorldSize = WorldSize.S_32,
        var isDrawAxis: Boolean = true,
        val isDrawSkyBox: Boolean = true,
        var isDrawGrid: Boolean = true,
        val worldObjects: MutableList<WorldObject> = mutableListOf(),
        val environment: Environment = Environment(),
        var isWireframeMode: Boolean = false)

enum class WorldSize(val size: Int) {
    S_16(16),
    S_32(32),
    S_64(64);

    override fun toString() = "Size=$size"
}
