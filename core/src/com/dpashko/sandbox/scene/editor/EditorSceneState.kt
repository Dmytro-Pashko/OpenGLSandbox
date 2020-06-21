package com.dpashko.sandbox.scene.editor

data class EditorSceneState(
        var worldSize: WorldSize = WorldSize.S_32,
        var isDrawAxis: Boolean = true,
        val isDrawSkyBox: Boolean = true,
        var isDrawGrid: Boolean = true)

enum class WorldSize(val size: Int) {
    S_16(16),
    S_32(32),
    S_64(64);

    override fun toString() = "Size=$size"
}
