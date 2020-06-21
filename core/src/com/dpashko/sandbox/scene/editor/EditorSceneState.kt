package com.dpashko.sandbox.scene.editor

data class EditorSceneState(
        val worldSize: Float = 64f,
        val isDrawAxis: Boolean = true,
        val isDrawSkyBox: Boolean = true,
        val isDrawGrid: Boolean = true)