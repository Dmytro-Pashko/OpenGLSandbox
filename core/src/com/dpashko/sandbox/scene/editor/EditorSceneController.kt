package com.dpashko.sandbox.scene.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.dpashko.sandbox.shader.AxisShader
import com.dpashko.sandbox.shader.GridShader
import com.dpashko.sandbox.shader.WireframeShader

class EditorSceneController(val state: EditorSceneState = EditorSceneState()) {


    private var cameraController = EditorCameraController(state.camera)
    private var worldController = EditorWorldController(state)
    private var modelBatch = ModelBatch()
    private var wireframeShader = WireframeShader()
    private var axisShader = AxisShader(axisLength = state.worldSize.size.toFloat())
    private var gridShader = GridShader(gridSize = state.worldSize.size)

    fun init() {
        if (Gdx.input.inputProcessor == null) {
            Gdx.input.inputProcessor = cameraController
        } else {
            val mixer = InputMultiplexer().also {
                it.addProcessor(Gdx.input.inputProcessor)
                it.addProcessor(cameraController)
            }
            Gdx.input.inputProcessor = mixer
        }
        worldController.init()
    }

    fun draw() {
        updateScene()
        drawAxis()
        drawGrid()
        drawWorldObjects()
    }

    private fun updateScene() {
        Gdx.gl.glEnable(GL30.GL_DEPTH_TEST)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT)
        cameraController.update()
        worldController.update()
    }

    private fun drawAxis() {
        if (state.isDrawAxis) {
            axisShader.draw(state.camera)
        }
    }

    private fun drawGrid() {
        if (state.isDrawGrid) {
            gridShader.draw(state.camera)
        }
    }

    private fun drawWorldObjects() {
        for (obj in state.worldObjects) {
            if (obj.model != null) {
                when (state.isWireframeMode) {
                    true -> wireframeShader.render(obj.model, state.camera)
                    false -> {
                        modelBatch.begin(state.camera)
                        modelBatch.render(obj.model, state.environment)
                        modelBatch.end()
                    }
                }
            }
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
        worldController.dispose()
    }

    fun onWireframeModeChanged(isWireframeMode: Boolean) {
        state.isWireframeMode = isWireframeMode
    }
}
