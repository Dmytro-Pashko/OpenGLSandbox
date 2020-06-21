package com.dpashko.sandbox.shader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.glutils.VertexBufferObjectWithVAO
import com.badlogic.gdx.utils.Disposable
import java.util.*

class GridShader(private val camera: Camera,
                 private val gridSize: Int = 64,
                 private val gridLineWidth: Float = 1f) : Disposable {

    private val color: Color = Color.GRAY
    private var vertices = createVertices()
    private val shader = ShaderProvider.axis3dShader()

    private fun createVertices() =
            VertexBufferObjectWithVAO(true, 4 * (gridSize + 1), VertexAttribute.Position(), VertexAttribute.ColorUnpacked()).apply {

                var vertices = LinkedList<Float>()
                //Fill Y grid lines.
                for (line in -gridSize / 2..gridSize / 2) {
                    //Y lines.
                    vertices.addAll(arrayOf(
                            line.toFloat(), -gridSize / 2f, 0f, color.r, color.g, color.b, 0f,
                            line.toFloat(), gridSize / 2f, 0f, color.r, color.g, color.b, 0f))
                    //X lines.
                    vertices.addAll(arrayOf(
                            -gridSize / 2f, line.toFloat(), 0f, color.r, color.g, color.b, 0f,
                            gridSize / 2f, line.toFloat(), 0f, color.r, color.g, color.b, 0f))
                }
                setVertices(vertices.toFloatArray(), 0, vertices.size)
            }

    fun draw() {
        shader.begin()
        shader.setUniformMatrix("cameraCombinedMatrix", camera.combined)
        vertices.bind(shader)
        Gdx.gl.glLineWidth(gridLineWidth)
        Gdx.gl.glDrawArrays(GL20.GL_LINES, 0, vertices.numVertices)
        vertices.unbind(shader)
        shader.end()
    }

    override fun dispose() {
        vertices.dispose()
        shader.dispose()
    }
}
