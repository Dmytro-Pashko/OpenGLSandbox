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
    private var boxes = mutableListOf<Box>().apply {
        add(Box(Vector3(2.0f, 5.0f, -15.0f), Texture(Pixmap(FileProvider.box))))
        add(Box(Vector3(-1.5f, -2.2f, -2.5f), Texture(Pixmap(FileProvider.box))))
        add(Box(Vector3(-3.8f, -2.0f, -12.3f), Texture(Pixmap(FileProvider.box))))
        add(Box(Vector3(2.4f, -0.4f, -3.5f), Texture(Pixmap(FileProvider.box))))
        add(Box(Vector3(-1.7f, 3.0f, -7.5f), Texture(Pixmap(FileProvider.box))))
        add(Box(Vector3(1.3f, -2.0f, -2.5f), Texture(Pixmap(FileProvider.box))))
        add(Box(Vector3(1.5f, 2.0f, -2.5f), Texture(Pixmap(FileProvider.box))))
        add(Box(Vector3(1.5f, 0.2f, -1.5f), Texture(Pixmap(FileProvider.box))))
        add(Box(Vector3(-1.3f, 1.0f, -1.5f), Texture(Pixmap(FileProvider.box))))
    }

    // Camera position and rotation matrix
    private val viewMatrix = Matrix4()
            .setToLookAt(Vector3(0f, 0f, 3f), Vector3.Zero, Vector3.Y)

    // Camera projection(frustum)
    private val projectionMatrix = Matrix4()
            .setToProjection(0.1f, 64f, 67f, (Gdx.graphics.width / Gdx.graphics.height).toFloat())

    // Combined projection matrix.
    private val combinedCameraMatrix = Matrix4()

    override fun init() {
        shader = ShaderProvider.simple3dShader().apply {
            if (!isCompiled) {
                throw IllegalStateException("Shader is not compiled: $log")
            }
        }
    }

    override fun draw() {
        rotateBoxes()
        Gdx.gl.glEnable(GL_DEPTH_TEST)
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        shader.begin()
        combinedCameraMatrix.set(projectionMatrix).mul(viewMatrix)
        for (box in boxes) {
            box.draw(shader, combinedCameraMatrix)
        }
        shader.end()
    }

    private val rotations = initRotations()
    private fun rotateBoxes() {
        for (i in 0 until boxes.size) {
            val angle = Gdx.graphics.deltaTime * rotations[i].first
            boxes[i].rotate(rotations[i].second, angle)
        }
    }

    //Each box has own rotation direction and rotation speed.
    private fun initRotations() = mutableListOf<Pair<Float, Vector3>>().apply {
        for (box in boxes) {
            add(Random.nextInt(20, 99).toFloat() to Vector3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()))
        }
    }

    override fun dispose() {
        for (box in boxes) {
            box.dispose()
        }
        shader.dispose()
    }

    data class Box(val pos: Vector3 = Vector3.Zero, val texture: Texture) {

        private var vertices = createVertices()
        private var transformation = Matrix4().translate(pos)
        private var tmp = Matrix4()

        fun draw(shader: ShaderProgram, combinedCameraMatrix: Matrix4) {
            Gdx.gl.glBindTexture(GL_TEXTURE_2D, texture.textureObjectHandle)
            shader.setUniformMatrix("combined", tmp.set(combinedCameraMatrix).mul(transformation))
            shader.setUniformi("texture_1", 0)
            vertices.bind(shader)
            Gdx.gl.glDrawArrays(GL_TRIANGLES, 0, vertices.numVertices)
            vertices.unbind(shader)
            Gdx.gl.glBindTexture(GL_TEXTURE_2D, 0)
        }

        fun rotate(axis: Vector3, degree: Float): Matrix4 = transformation.rotate(axis, degree)

        private fun createVertices() =
                VertexBufferObjectWithVAO(true, 36, VertexAttribute.Position(), VertexAttribute.TexCoords(0)).apply {
                    val vertices = floatArrayOf(
                            // Position(x,y,z), Texture coordinates (u,v)
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
