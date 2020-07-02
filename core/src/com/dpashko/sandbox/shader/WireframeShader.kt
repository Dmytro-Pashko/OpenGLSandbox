package com.dpashko.sandbox.shader

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.utils.Disposable

class WireframeShader(private val lineWidth: Float = 1f,
                      lineColor: Color = Color(240f/255f, 149f/255f, 42f/255f, 0f)) : Disposable {

    private val colorVector = floatArrayOf(lineColor.r, lineColor.g, lineColor.b, 0f)
    private val shader = ShaderProvider.grid3dShader()

    fun render(model: ModelInstance, camera: PerspectiveCamera) {
        shader.begin()
        shader.setUniformMatrix("cameraCombinedMatrix", camera.combined)
        shader.setUniform4fv("gridColor", colorVector, 0, colorVector.size)
        for (mesh in model.model.meshes) {
            mesh.render(shader, PRIMITIVE_TYPE)
        }
    }

    override fun dispose() {

    }

    companion object {
        const val PRIMITIVE_TYPE = GL20.GL_LINE_STRIP
    }
}