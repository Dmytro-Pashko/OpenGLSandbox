package com.dpashko.sandbox.scene.cube3d

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

class Cubes3dScene @Inject internal constructor() : Scene {

    private val viewMatrix = Matrix4().apply {
        val position = Vector3(0f, -8f, 0f)
        val target = Vector3(0f, 0f, 0f)
        val direction = Vector3(position).sub(target).nor()
        val right = Vector3.X
        val up = Vector3(direction).crs(right).nor()
        setToLookAt(position, target, up)
    }
    private val projectionMatrix = Matrix4().apply {
        val near = 0.1f
        val far = 32f
        val fov = 67f
        val aspectRatio = Gdx.graphics.width / Gdx.graphics.height
        setToProjection(near, far, fov, aspectRatio.toFloat())
    }
    private val combinedCameraMatrix = Matrix4().apply {
        set(projectionMatrix).mul(viewMatrix)
    }
    private lateinit var shader: ShaderProgram
    private lateinit var cubes: Map<Cube, Vector3>

    override fun init() {
        shader = ShaderProvider.simple3dShader().apply {
            if (!isCompiled) {
                throw IllegalStateException("Shader is not compiled: $log")
            }
        }
        cubes = mapOf(
                Cube(position = Vector3(2f, 1f, -2f), texture = Texture(FileProvider.cube1)) to getRandomDirection(),
                Cube(position = Vector3(-2f, 1f, -2f), texture = Texture(FileProvider.cube2)) to getRandomDirection(),
                Cube(position = Vector3(0f, -2f, -2f), texture = Texture(FileProvider.cube3)) to getRandomDirection(),
                Cube(position = Vector3.Zero, texture = Texture(FileProvider.cube4), size = 2f) to Vector3.Zero,
                Cube(position = Vector3(-2f, -1f, 2f), texture = Texture(FileProvider.cube5)) to getRandomDirection(),
                Cube(position = Vector3(2f, -1f, 2f), texture = Texture(FileProvider.cube6)) to getRandomDirection(),
                Cube(position = Vector3(0f, 2f, 2f), texture = Texture(FileProvider.cube7)) to getRandomDirection()
        )
    }

    private fun getRandomDirection() = Vector3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())

    override fun draw() {
        Gdx.gl.glEnable(GL_DEPTH_TEST)
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        viewMatrix.rotate(Vector3.Z, 0.25f)
        combinedCameraMatrix.set(projectionMatrix).mul(viewMatrix)
        shader.begin()
        for ((cube, rotationAxis) in cubes) {
            cube.rotate(rotationAxis, 0.25f)
            cube.draw(shader, combinedCameraMatrix)
        }
        shader.end()
    }

    override fun dispose() {
        for (cube in cubes.keys) {
            cube.dispose()
        }
        shader.dispose()
    }

    data class Cube(val position: Vector3, val size: Float = 1f, val texture: Texture) {
        private val vertices = createVertexData()
        private val indices = createIndices()
        private val modelMatrix = Matrix4().translate(position).scale(size, size, size)
        private var tmp = Matrix4()

        fun draw(shader: ShaderProgram, combinedCameraMatrix: Matrix4) {
            shader.setUniformMatrix("combined", tmp.set(combinedCameraMatrix).mul(modelMatrix))
            Gdx.gl.glBindTexture(GL_TEXTURE_2D, texture.textureObjectHandle)
            vertices.bind(shader)
            Gdx.gl.glDrawElements(GL_TRIANGLES, indices.numIndices, GL_UNSIGNED_SHORT, indices.buffer)
            vertices.unbind(shader)
        }

        fun rotate(axis: Vector3, degree: Float) {
            modelMatrix.rotate(axis, degree)
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
                    // Bottom face                                 ___________
                    -.5f, -.5f, -.5f, 0f, 0f,   //0                |16     17|
                    .5f, -.5f, -.5f, 1f, 0f,    //1                |  BACK   |
                    .5f, .5f, -.5f, 1f, 1f,     //2                |         |
                    -.5f, .5f, -.5f, 0f, 1f,    //3                |19_____18|
                    // Top face                         _________________________________
                    -.5f, -.5f, .5f, 0f, 0f,    //4     |9       8||7       6||12     13|
                    .5f, -.5f, .5f, 1f, 0f,     //5     |   LEFT  ||   TOP   ||  RIGHT  |
                    .5f, .5f, .5f, 1f, 1f,      //6     |         ||         ||         |
                    -.5f, .5f, .5f, 0f, 1f,     //7     |10_____11||4_______5||15_____14|
                    // Left face                                   ___________
                    -.5f, .5f, .5f, 1f, 0f,     //8                |23     22|
                    -.5f, .5f, -.5f, 1f, 1f,    //9                |  FRONT  |
                    -.5f, -.5f, -.5f, 0f, 1f,   //10               |         |
                    -.5f, -.5f, .5f, 0f, 0f,    //11               |20_____21|
                    // Right face                                  ___________
                    .5f, .5f, .5f, 1f, 0f,      //12               |0       1|
                    .5f, .5f, -.5f, 1f, 1f,     //13               |  BOTTOM |
                    .5f, -.5f, -.5f, 0f, 1f,    //14               |         |
                    .5f, -.5f, .5f, 0f, 0f,     //15               |3_______2|
                    // Back face
                    -.5f, -.5f, -.5f, 0f, 1f,   //16
                    .5f, -.5f, -.5f, 1f, 1f,    //17
                    .5f, -.5f, .5f, 1f, 0f,     //18
                    -.5f, -.5f, .5f, 0f, 0f,    //19
                    // Front Face
                    -.5f, .5f, -.5f, 0f, 1f,    //20
                    .5f, .5f, -.5f, 1f, 1f,     //21
                    .5f, .5f, .5f, 1f, 0f,      //22
                    -.5f, .5f, .5f, 0f, 0f      //23
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
