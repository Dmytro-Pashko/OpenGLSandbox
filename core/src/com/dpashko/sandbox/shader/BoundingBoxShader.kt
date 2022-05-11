package com.dpashko.sandbox.shader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.VertexBufferObjectWithVAO
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.utils.Disposable
import java.util.*

class BoundingBoxShader(
    private val shader: ShaderProgram,
    bounding: BoundingBox,
    private val lineWidth: Float = 1f,
    lineColor: Color = Color.RED
) : Disposable {

    private val colorVector = floatArrayOf(lineColor.r, lineColor.g, lineColor.b, 0f)
    private val vertices = createVertices(bounding)

    fun render(camera: Camera) {
        shader.bind()
        shader.setUniformMatrix("cameraCombinedMatrix", camera.combined)
        shader.setUniform4fv("gridColor", colorVector, 0, colorVector.size)
        vertices.bind(shader)
        Gdx.gl.glLineWidth(lineWidth)
        Gdx.gl.glDrawArrays(GL20.GL_LINES, 0, vertices.numVertices)
        vertices.unbind(shader)
    }

    private fun createVertices(bounding: BoundingBox): VertexBufferObjectWithVAO {
        return VertexBufferObjectWithVAO(true, 72, VertexAttribute.Position()).apply {
            val vertices = LinkedList<Float>()
            val tmp = Vector3(0f, 0f, 0f)
            val tmp2 = Vector3(0f, 0f, 0f)

            bounding.apply {
                vertices.addAll(getLinePoints(getCorner000(tmp), getCorner001(tmp2)))
                vertices.addAll(getLinePoints(getCorner010(tmp), getCorner011(tmp2)))
                vertices.addAll(getLinePoints(getCorner000(tmp), getCorner010(tmp2)))
                vertices.addAll(getLinePoints(getCorner001(tmp), getCorner011(tmp2)))

                vertices.addAll(getLinePoints(getCorner100(tmp), getCorner101(tmp2)))
                vertices.addAll(getLinePoints(getCorner110(tmp), getCorner111(tmp2)))
                vertices.addAll(getLinePoints(getCorner100(tmp), getCorner110(tmp2)))
                vertices.addAll(getLinePoints(getCorner101(tmp), getCorner111(tmp2)))

                vertices.addAll(getLinePoints(getCorner000(tmp), getCorner100(tmp2)))
                vertices.addAll(getLinePoints(getCorner001(tmp), getCorner101(tmp2)))
                vertices.addAll(getLinePoints(getCorner010(tmp), getCorner110(tmp2)))
                vertices.addAll(getLinePoints(getCorner011(tmp), getCorner111(tmp2)))
            }
            setVertices(vertices.toFloatArray(), 0, vertices.size)
        }
    }

    private fun getLinePoints(point1: Vector3, point2: Vector3): Array<Float> {
        return arrayOf(point1.x, point1.y, point1.z, point2.x, point2.y, point2.z)
    }

    override fun dispose() {
        shader.dispose()
    }
}
