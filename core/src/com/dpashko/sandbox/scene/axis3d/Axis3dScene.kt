package com.dpashko.sandbox.scene.axis3d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.glutils.VertexBufferObjectWithVAO
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.scene.debug.DebugRender
import com.dpashko.sandbox.scene.model3d.Model3dCameraController
import com.dpashko.sandbox.shader.AxisShader
import javax.inject.Inject

open class Axis3dScene @Inject protected constructor(
        private val debugRender: DebugRender
) : Scene {

    private val camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private var inputController = Model3dCameraController(camera)
    private var axisShader = AxisShader(camera)

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
    }

    override fun render() {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        inputController.update()
        axisShader.draw()
        debugRender.render(camera)
    }

    override fun dispose() {
        debugRender.dispose()
        axisShader.dispose()
    }
}
