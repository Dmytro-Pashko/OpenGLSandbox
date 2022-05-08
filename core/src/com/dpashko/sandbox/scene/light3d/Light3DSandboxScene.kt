package com.dpashko.sandbox.scene.light3d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.files.FileProvider
import com.dpashko.sandbox.model.ModelProvider
import com.dpashko.sandbox.scene.SandboxScene
import com.dpashko.sandbox.shader.LightShader
import javax.inject.Inject

class Light3DSandboxScene @Inject constructor() : SandboxScene {

    private val lightShader = LightShader()
    private val model = ModelProvider.loadModel(FileProvider.suzanneModelHighPoly, null)
    private val camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

    override fun init() {
        camera.position.set(0f, -3f, 2f)
        camera.lookAt(Vector3.Zero)
        camera.update()
    }

    override fun draw() {
        Gdx.gl.glEnable(GL30.GL_DEPTH_TEST)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT)

        lightShader.render(model, camera)
    }

    override fun dispose() {
        lightShader.dispose()
        model.model.dispose()
    }
}
