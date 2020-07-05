package com.dpashko.sandbox.shader

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable
import kotlin.math.cos
import kotlin.math.sin

class LightShader : Disposable {

    private val lightStrength = 0f
    private val specularStrength = 1f
    private val lightColor = Color.WHITE.let {
        Vector3(it.r, it.g, it.b)
    }
    private val objectColor = Color.GOLD.let {
        Vector3(it.r, it.g, it.b)
    }
    private val shader = ShaderProvider.lightShader().apply {
        if (!isCompiled) {
            throw IllegalStateException("Shader is not compiled: $log")
        }
    }
    private var lightAngle = 0

    fun render(model: ModelInstance, camera: PerspectiveCamera) {
        if (lightAngle > 360) {
            lightAngle = 0
        }
        shader.begin()
        shader.setUniformMatrix("model", model.transform)
        shader.setUniformMatrix("view", camera.view)
        shader.setUniformMatrix("projection", camera.projection)
        shader.setUniformf("lightPos", calculateLightPosition())
        shader.setUniformf("viewPos", camera.position)
        shader.setUniformf("ambientStrength", lightStrength)
        shader.setUniformf("specularStrength", specularStrength)
        shader.setUniformf("lightColor", lightColor)
        shader.setUniformf("objectColor", objectColor)
        for (mesh in model.model.meshes) {
            mesh.render(shader, GL30.GL_TRIANGLES)
        }
        shader.end()
        lightAngle++
    }

    private var rad = Math.PI.toFloat() / 180.0f
    private fun calculateLightPosition(): Vector3 {
        val x = cos(lightAngle * rad) * 5f
        val y = sin(lightAngle * rad) * 5f
        return Vector3(x, y, 4f)
    }

    override fun dispose() {
        shader.dispose()
    }
}
