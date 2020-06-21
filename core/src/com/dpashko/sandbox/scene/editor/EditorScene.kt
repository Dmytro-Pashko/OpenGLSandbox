package com.dpashko.sandbox.scene.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.shader.AxisShader
import com.dpashko.sandbox.shader.GridShader
import javax.inject.Inject

open class EditorScene @Inject protected constructor() : Scene {

    private val state: EditorSceneState = EditorSceneState()
    private val camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private var inputController = EditorCameraController(camera)
    private var axisShader = AxisShader(axisLength = state.worldSize)
    private var gridShader = GridShader(gridSize = state.worldSize.toInt())
    private val view = EditorSceneView()

    override fun init() {
        Gdx.input.inputProcessor = inputController
        camera.apply {
            near = 1f
            far = 300f
            position.set(Vector3(-1f, -2f, 5f))
            rotate(Vector3.X, 45f)
            rotate(Vector3.Z, -15f)
            update()
        }
        view.init()
    }

    override fun render() {
        Gdx.gl.glEnable(GL30.GL_DEPTH_TEST)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT)
        inputController.update()
        if (state.isDrawAxis) {
            axisShader.draw(camera)
        }
        if (state.isDrawGrid) {
            gridShader.draw(camera)
        }
        view.draw()
    }

    override fun dispose() {
        axisShader.dispose()
        gridShader.dispose()
        view.dispose()
    }
}
