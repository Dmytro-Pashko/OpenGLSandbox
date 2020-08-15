package com.dpashko.sandbox.scene.plane3d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20.*
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.glutils.*
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.files.FileProvider
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.shader.ShaderProvider
import javax.inject.Inject

class Plane3dScene @Inject internal constructor() : Scene {

    private lateinit var shader: ShaderProgram
    private lateinit var plane: Plane

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

    // Combined camera matrix matrix.
    private val cameraMatrix = Matrix4().set(projectionMatrix).mul(viewMatrix)

    override fun init() {
        shader = ShaderProvider.getBasicShader().apply {
            if (!isCompiled) {
                throw IllegalStateException("Shader is not compiled: $log")
            }
        }
        plane = Plane(Texture(Pixmap(FileProvider.TEXTURE_SKY)))
    }

    override fun draw() {
        rotatePlane()
        Gdx.gl.glEnable(GL_DEPTH_TEST)
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        shader.begin()
        plane.draw(shader, cameraMatrix)
        shader.end()
    }

    private val rotationSpeed = 100f
    private fun rotatePlane() {
        val angle = Gdx.graphics.deltaTime * rotationSpeed
        plane.rotate(Vector3.Y, angle)
    }

    override fun dispose() {
        plane.dispose()
        shader.dispose()
    }

    data class Plane(val texture: Texture) {
        private val indices = createIndices()
        private val vertices = createVertices()
        private val tmp = Matrix4()
        private val modelMatrix = Matrix4()

        fun draw(shader: ShaderProgram, cameraMatrix: Matrix4) {
            tmp.set(cameraMatrix).mul(modelMatrix)
            shader.setUniformMatrix("combined", tmp)
            texture.bind(GL_TEXTURE_2D)
            vertices.bind(shader)
            Gdx.gl.glDrawElements(GL_TRIANGLES, indices.numIndices, GL_UNSIGNED_SHORT, indices.buffer)
            vertices.unbind(shader)
        }

        fun rotate(axis: Vector3, degree: Float) {
            modelMatrix.rotate(axis, degree)
        }

        private fun createVertices(): VertexData {
            val vertices = floatArrayOf(
                    // Position(x,y,z), Texture coordinates (u,v)
                    -1f, 1f, 0f, 0f, 1f,
                    -1f, -1f, 0f, 0f, 0f,
                    1f, -1f, 0f, 1f, 0f,
                    1f, 1f, 0f, 1f, 1f)
            return VertexBufferObjectWithVAO(true, vertices.size,
                    VertexAttribute.Position(), VertexAttribute.TexCoords(0)).apply {
                setVertices(vertices, 0, vertices.size)
            }
        }

        // Plane mesh separated by two triangles.                   0╔═╗3
        // Each triangle contains 3 vertices (0 1 2) and (2 3 0).   1╚═╝2
        private fun createIndices(): IndexData {
            val indices = shortArrayOf(0, 1, 2, 2, 3, 0)
            return IndexArray(indices.size).apply {
                setIndices(indices, 0, indices.size)
            }
        }

        fun dispose() {
            vertices.dispose()
            indices.dispose()
            texture.dispose()
        }
    }
}
