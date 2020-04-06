package com.dpashko.sandbox.scene.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.utils.Disposable
import com.dpashko.sandbox.font.FontsProvider
import com.dpashko.sandbox.model.ModelsProvider
import javax.inject.Inject

class DebugRender @Inject protected constructor() : Disposable {

    private val debugObjects = mutableListOf<ModelInstance>()

    companion object {
        const val cameraPositionLabel = "Camera Position: %s"
        const val fpsLabel = "FPS: %d"
        const val heapSizeLabel = "Heap: %d kb"
        private val spriteBatch = SpriteBatch()
        private val modelBatch = ModelBatch()
        private val fps: Int
            get() {
                return Gdx.graphics.framesPerSecond
            }
        private val heapSize: Long
            get() {
                return Gdx.app.javaHeap / 1024L
            }
    }

    public fun init() {
        debugObjects.addAll(initAxises())
    }

    private fun initAxises() = listOf(
            ModelsProvider.createXAxisModel(64f),
            ModelsProvider.createYAxisModel(64f),
            ModelsProvider.createZAxisModel(64f))

    public fun render(camera: Camera) {
        modelBatch.begin(camera)
        for (debugObject in debugObjects) {
            modelBatch.render(debugObject)
        }
        modelBatch.end()
        render()
        drawCameraPos(camera)
    }

    private fun drawCameraPos(camera: Camera) {
        spriteBatch.begin()
        FontsProvider.defaultFont.draw(spriteBatch, cameraPositionLabel.format(camera.position),
                250f, Gdx.graphics.height - 10.toFloat())
        spriteBatch.end()
    }

    public fun render() {
        spriteBatch.begin()
        FontsProvider.defaultFont.draw(spriteBatch, fpsLabel.format(fps), 5f, Gdx.graphics.height - 10.toFloat())
        FontsProvider.defaultFont.draw(spriteBatch, heapSizeLabel.format(heapSize), 75f, Gdx.graphics.height - 10.toFloat())
        spriteBatch.end()
    }

    override fun dispose() {
        spriteBatch.dispose()
        modelBatch.dispose()
    }
}