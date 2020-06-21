package com.dpashko.sandbox.scene.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.shader.AxisShader
import com.dpashko.sandbox.shader.GridShader

class EditorSceneController(val state: EditorSceneState = EditorSceneState()) {

    private val camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private var cameraController = EditorCameraController(camera)
    private var axisShader = AxisShader(axisLength = state.worldSize.size.toFloat())
    private var gridShader = GridShader(gridSize = state.worldSize.size)

    fun init() {
        camera.apply {
            near = 1f
            far = 300f
            position.set(Vector3(-1f, -2f, 5f))
            rotate(Vector3.X, 45f)
            rotate(Vector3.Z, -15f)
            update()
        }
        if (Gdx.input.inputProcessor == null) {
            Gdx.input.inputProcessor = cameraController
        } else {
            val mixer = InputMultiplexer().also {
                it.addProcessor(Gdx.input.inputProcessor)
                it.addProcessor(cameraController)
            }
            Gdx.input.inputProcessor = mixer
        }
    }

    fun draw() {
        Gdx.gl.glEnable(GL30.GL_DEPTH_TEST)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT)
        cameraController.update()
        if (state.isDrawAxis) {
            axisShader.draw(camera)
        }
        if (state.isDrawGrid) {
            gridShader.draw(camera)
        }
    }

    fun onDrawGridChanged(isDrawGrid: Boolean) {
        state.isDrawGrid = isDrawGrid
    }

    fun onDrawAxisChanged(isDrawAxis: Boolean) {
        state.isDrawAxis = isDrawAxis
    }

    fun onWorldSizeChanged(worldSize: WorldSize) {
        state.worldSize = worldSize
        gridShader = GridShader(worldSize.size)
        axisShader = AxisShader(worldSize.size.toFloat())
    }

    fun dispose() {
        axisShader.dispose()
        gridShader.dispose()
    }
}
