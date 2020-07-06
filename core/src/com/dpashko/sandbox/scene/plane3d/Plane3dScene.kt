package com.dpashko.sandbox.scene.plane3d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20.*
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.glutils.IndexArray
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.VertexBufferObjectWithVAO
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.files.FileProvider
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.shader.ShaderProvider
import javax.inject.Inject

class Plane3dScene @Inject internal constructor() : Scene {

    private lateinit var shader: ShaderProgram
    private lateinit var plane: Plane

    // Plane position and rotation matrix
    private val modelMatrix = Matrix4().translate(0f, 0f, 0f)

    // Camera position and rotation matrix
    private val viewMatrix = Matrix4()
            .setToLookAt(Vector3(0f, 0f, 3f), Vector3.Zero, Vector3.Y)

    // Camera projection(frustum)
    private val projectionMatrix = Matrix4().apply {
        val near = 1f
        val far = 5f
        val fov = 67f
        val aspectRation = (Gdx.graphics.width / Gdx.graphics.height).toFloat()
        setToProjection(near, far, fov, aspectRation)
    }

    // Combined projection matrix.
    private val combinedMatrix = Matrix4()

    override fun init() {
        shader = ShaderProvider.simple3dShader().apply {
            if (!isCompiled) {
                throw IllegalStateException("Shader is not compiled: $log")
            }
        }
        plane = Plane(Texture(Pixmap(FileProvider.sky)))
    }

    override fun draw() {
        rotatePlane()
        Gdx.gl.glEnable(GL_DEPTH_TEST)
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        shader.begin()
        combinedMatrix.set(projectionMatrix).mul(viewMatrix).mul(modelMatrix)
        shader.setUniformMatrix("combined", combinedMatrix)
        plane.draw(shader)
        shader.end()
    }

    private val rotationSpeed = 100f
    private fun rotatePlane() {
        val angle = Gdx.graphics.deltaTime * rotationSpeed
        modelMatrix.rotate(Vector3.Y, angle)
    }

    override fun dispose() {
        plane.dispose()
        shader.dispose()
    }

    data class Plane(val texture: Texture) {
        private var indices = createIndices()
        private var vertices = createVertices()

        fun draw(shader: ShaderProgram) {
            Gdx.gl.glBindTexture(GL_TEXTURE_2D, texture.textureObjectHandle)
            vertices.bind(shader)
            Gdx.gl.glDrawElements(GL_TRIANGLES, vertices.numVertices, GL_UNSIGNED_SHORT, indices.buffer)
            vertices.unbind(shader)
        }

        private fun createVertices() =
                VertexBufferObjectWithVAO(true, 4, VertexAttribute.Position(), VertexAttribute.TexCoords(0)).apply {
                    val vertices = floatArrayOf(
                            // Position(x,y,z), Texture coordinates (u,v)
                            -1f, 1f, 0f, 0f, 1f,
                            -1f, -1f, 0f, 0f, 0f,
                            1f, -1f, 0f, 1f, 0f,
                            1f, 1f, 0f, 1f, 1f)
                    setVertices(vertices, 0, vertices.size)
                }

        // Plane mesh separated by two triangles.                   0╔═╗3
        // Each triangle contains 3 vertices (0 1 2) and (2 3 0).   1╚═╝2
        private fun createIndices() = IndexArray(6).apply {
            setIndices(shortArrayOf(0, 1, 2, 2, 3, 0), 0, 6)
        }

        fun dispose() {
            vertices.dispose()
            texture.dispose()
        }
    }
}
