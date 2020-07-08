package com.dpashko.sandbox.scene.box3d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20.*
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttribute
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
    private val boxes = mutableListOf<Box>()
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
        createBoxes()
    }

    private fun createBoxes() {
        boxes.apply {
            add(Box(Vector3(2f, 1f, -2f), getRandomDirection()))
            add(Box(Vector3(-2f, 1f, -2f), getRandomDirection()))
            add(Box(Vector3(0f, -2f, -2f), getRandomDirection()))
            add(Box())
            add(Box(Vector3(-2f, -1f, 2f), getRandomDirection()))
            add(Box(Vector3(2f, -1f, 2f), getRandomDirection()))
            add(Box(Vector3(0f, 2f, 2f), getRandomDirection()))
        }
    }

    private fun getRandomDirection() = Vector3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())

    override fun draw() {
        Gdx.gl.glEnable(GL_DEPTH_TEST)
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        updateRotations()
        shader.begin()
        combinedCameraMatrix.set(projectionMatrix).mul(viewMatrix)
        for (box in boxes) {
            box.draw(shader, combinedCameraMatrix)
        }
        shader.end()
    }

    private fun updateRotations() {
        viewMatrix.rotate(Vector3.Z, 0.25f)
        for (box in boxes) {
            box.rotate()
        }
    }

    override fun dispose() {
        for (box in boxes) {
            box.dispose()
        }
        shader.dispose()
    }

    data class Box(val pos: Vector3 = Vector3.Zero,
                   val rotationDirection: Vector3 = Vector3.Zero,
                   val texture: Texture = Texture(Pixmap(FileProvider.box))) {
        private val vertices = createVertexData()
        private val viewMatrix = Matrix4().translate(pos)
        private val rotationSpeed = .2f
        private var tmp = Matrix4()

        fun rotate() {
            viewMatrix.rotate(rotationDirection, rotationSpeed)
        }

        fun draw(shader: ShaderProgram, combinedCameraMatrix: Matrix4) {
            Gdx.gl.glBindTexture(GL_TEXTURE_2D, texture.textureObjectHandle)
            shader.setUniformMatrix("combined", tmp.set(combinedCameraMatrix).mul(viewMatrix))
            vertices.bind(shader)
            Gdx.gl.glDrawArrays(GL_TRIANGLES, 0, vertices.numVertices)
            vertices.unbind(shader)
        }

        private fun createVertexData() =
                VertexBufferObjectWithVAO(true, 36, VertexAttribute.Position(), VertexAttribute.TexCoords(0)).apply {
                    val vertices = floatArrayOf(
                            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
                            0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
                            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

                            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                            0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                            0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                            0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                            -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
                            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

                            -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                            -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                            -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                            0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                            0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                            0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                            0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
                            0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                            0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

                            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                            -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
                            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f)
                    setVertices(vertices, 0, vertices.size)
                }

        fun dispose() {
            vertices.dispose()
            texture.dispose()
        }
    }
}
