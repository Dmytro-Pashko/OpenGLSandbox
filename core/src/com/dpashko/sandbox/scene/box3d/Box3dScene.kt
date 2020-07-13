package com.dpashko.sandbox.scene.box3d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20.*
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.glutils.*
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.dpashko.sandbox.files.FileProvider
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.shader.ShaderProvider
import javax.inject.Inject
import kotlin.random.Random

class Box3dScene @Inject internal constructor() : Scene {

    private val viewMatrix = Matrix4().setToLookAt(Vector3(0f, -8f, 1f), Vector3.Zero, Vector3.Y)
    private val projectionMatrix = Matrix4().setToProjection(0.1f, 64f, 67f, 1.33f)
    private val combinedCameraMatrix = Matrix4()
    private lateinit var shader: ShaderProgram
    private lateinit var boxes: Map<Box, Vector3>

    override fun init() {
        shader = ShaderProvider.simple3dShader().apply {
            if (!isCompiled) {
                throw IllegalStateException("Shader is not compiled: $log")
            }
        }
        boxes = mapOf(
                Box(position = Vector3(2f, 1f, -2f), texture = Texture(FileProvider.box1)) to getRandomDirection(),
                Box(position = Vector3(-2f, 1f, -2f), texture = Texture(FileProvider.box2)) to getRandomDirection(),
                Box(position = Vector3(0f, -2f, -2f), texture = Texture(FileProvider.box3)) to getRandomDirection(),
                Box(position = Vector3.Zero, texture = Texture(FileProvider.box4), size = 2f) to Vector3.Zero,
                Box(position = Vector3(-2f, -1f, 2f), texture = Texture(FileProvider.box5)) to getRandomDirection(),
                Box(position = Vector3(2f, -1f, 2f), texture = Texture(FileProvider.box6)) to getRandomDirection(),
                Box(position = Vector3(0f, 2f, 2f), texture = Texture(FileProvider.box7)) to getRandomDirection()
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
        private val viewMatrix = Matrix4().translate(position).scale(size, size, size)
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

        private fun createIndices(): IndexData {
            val indices = shortArrayOf(
                    0, 1, 2, 2, 3, 0,       // Bottom
                    4, 5, 6, 6, 7, 4,       // Top
                    8, 9, 10, 10, 11, 8,    // Left
                    12, 13, 14, 14, 15, 12, // Right
                    16, 17, 18, 18, 19, 16, // Back
                    20, 21, 22, 22, 23, 20  // Front
            )
            return IndexArray(indices.size).apply {
                setIndices(indices, 0, indices.size)
            }
        }

        private fun createVertexData(): VertexData {
            val vertices = floatArrayOf(
                    // Bottom face                                         ___________
                    -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,    //0                |16     17|
                    0.5f, -0.5f, -0.5f, 1.0f, 0.0f,     //1                |  BACK   |
                    0.5f, 0.5f, -0.5f, 1.0f, 1.0f,      //2                |         |
                    -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,     //3                |19_____18|
                    // Top face                                 _________________________________
                    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,     //4     |9       8||7       6||12     13|
                    0.5f, -0.5f, 0.5f, 1.0f, 0.0f,      //5     |   LEFT  ||   TOP   ||  RIGHT  |
                    0.5f, 0.5f, 0.5f, 1.0f, 1.0f,       //6     |         ||         ||         |
                    -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,      //7     |10_____11||4_______5||15_____14|
                    // Left face                                           ___________
                    -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,      //8                |23     22|
                    -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,     //9                |  FRONT  |
                    -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,    //10               |         |
                    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,     //11               |20_____21|
                    // Right face                                          ___________
                    0.5f, 0.5f, 0.5f, 1.0f, 0.0f,       //12               |0       1|
                    0.5f, 0.5f, -0.5f, 1.0f, 1.0f,      //13               |  BOTTOM |
                    0.5f, -0.5f, -0.5f, 0.0f, 1.0f,     //14               |         |
                    0.5f, -0.5f, 0.5f, 0.0f, 0.0f,      //15               |3_______2|
                    // Back face
                    -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,    //16
                    0.5f, -0.5f, -0.5f, 1.0f, 1.0f,     //17
                    0.5f, -0.5f, 0.5f, 1.0f, 0.0f,      //18
                    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,     //19
                    // Front Face
                    -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,     //20
                    0.5f, 0.5f, -0.5f, 1.0f, 1.0f,      //21
                    0.5f, 0.5f, 0.5f, 1.0f, 0.0f,       //22
                    -0.5f, 0.5f, 0.5f, 0.0f, 0.0f       //23
            )
            return VertexBufferObjectWithVAO(
                    true, vertices.size, VertexAttribute.Position(), VertexAttribute.TexCoords(0)).apply {
                setVertices(vertices, 0, vertices.size)
            }
        }

        fun dispose() {
            vertices.dispose()
            indices.dispose()
            texture.dispose()
        }
    }
}
