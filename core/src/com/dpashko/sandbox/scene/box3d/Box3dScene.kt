package com.dpashko.sandbox.scene.box3d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20.*
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.glutils.IndexBufferObjectSubData
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.VertexBufferObjectWithVAO
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.files.FileProvider
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.shader.ShaderProvider
import javax.inject.Inject
import kotlin.random.Random

class Box3dScene @Inject internal constructor() : Scene {

    private lateinit var shader: ShaderProgram
    private lateinit var boxes: Map<Box, Vector3>

    private val viewMatrix = Matrix4()
            .setToLookAt(Vector3(0f, -8f, 1f), Vector3.Zero, Vector3.Y)
    private val projectionMatrix = Matrix4()
            .setToProjection(0.1f, 64f, 67f, (Gdx.graphics.width / Gdx.graphics.height).toFloat())
    private val combinedCameraMatrix = Matrix4()

    override fun init() {
        shader = ShaderProvider.simple3dShader().apply {
            if (!isCompiled) {
                throw IllegalStateException("Shader is not compiled: $log")
            }
        }
        boxes = mapOf(
                Box(position = Vector3(2f, 1f, -2f), texture = Texture(FileProvider.box)) to getRandomDirection(),
                Box(position = Vector3(-2f, 1f, -2f), texture = Texture(FileProvider.box)) to getRandomDirection(),
                Box(position = Vector3(0f, -2f, -2f), texture = Texture(FileProvider.box)) to getRandomDirection(),
                Box(position = Vector3.Zero, texture = Texture(FileProvider.box)) to Vector3.Zero,
                Box(position = Vector3(-2f, -1f, 2f), texture = Texture(FileProvider.box)) to getRandomDirection(),
                Box(position = Vector3(2f, -1f, 2f), texture = Texture(FileProvider.box)) to getRandomDirection(),
                Box(position = Vector3(0f, 2f, 2f), texture = Texture(FileProvider.box)) to getRandomDirection()
        )
    }


    private fun getRandomDirection() = Vector3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())

    override fun draw() {
        Gdx.gl.glEnable(GL_DEPTH_TEST)
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        // Rotate camera around Z axis by 0.25 degree.
        viewMatrix.rotate(Vector3.Z, 0.25f)
        shader.begin()
        combinedCameraMatrix.set(projectionMatrix).mul(viewMatrix)
        for ((box, rotationAxis) in boxes) {
            box.rotate(rotationAxis, 0.25f)
            box.draw(shader, combinedCameraMatrix)
        }
        shader.end()
    }

    override fun dispose() {
        for (box in boxes.keys) {
            box.dispose()
        }
        shader.dispose()
    }

    data class Box(val position: Vector3, val size: Float = 1f, val texture: Texture) {
        private val vertices = createVertexData()
        private val indices = createIndices()
        private val viewMatrix = Matrix4().translate(position)
        private var tmp = Matrix4()

        fun draw(shader: ShaderProgram, combinedCameraMatrix: Matrix4) {
            shader.setUniformMatrix("combined", tmp.set(combinedCameraMatrix).mul(viewMatrix))
            Gdx.gl.glBindTexture(GL_TEXTURE_2D, texture.textureObjectHandle)
            vertices.bind(shader)
            Gdx.gl.glDrawElements(GL_TRIANGLES, indices.numIndices, GL_UNSIGNED_SHORT, indices.buffer)
            vertices.unbind(shader)
        }

        fun rotate(axis: Vector3, degree: Float) {
            viewMatrix.rotate(axis, degree)
        }

        private fun createIndices() = IndexBufferObjectSubData(true, 36).apply {
            val indices = shortArrayOf(
                    0, 1, 2, 2, 3, 0,       // Bottom face
                    4, 5, 6, 6, 7, 4,       // Top face
                    8, 9, 10, 10, 11, 8,    // Left face
                    12, 13, 14, 14, 15, 12, // Right face
                    16, 17, 18, 18, 19, 16, // Front face
                    20, 21, 22, 22, 23, 20  // Back Face
            )
            setIndices(indices, 0, indices.size)
        }

        private fun createVertexData() =
                VertexBufferObjectWithVAO(true, 24, VertexAttribute.Position(), VertexAttribute.TexCoords(0)).apply {
                    val vertices = floatArrayOf(
                            // Bottom face
                            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
                            0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
                            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                            // Top face
                            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                            0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                            0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                            -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
                            // Left face
                            -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                            -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                            // Right face
                            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                            0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                            0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                            // Front face
                            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                            0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
                            0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                            // Back Face
                            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                            -0.5f, 0.5f, 0.5f, 0.0f, 0.0f
                    )
                    setVertices(vertices, 0, vertices.size)
                }

        fun dispose() {
            vertices.dispose()
            indices.dispose()
            texture.dispose()
        }
    }
}
