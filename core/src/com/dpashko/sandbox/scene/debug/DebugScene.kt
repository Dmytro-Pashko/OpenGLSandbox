package com.dpashko.sandbox.scene.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.dpashko.sandbox.font.FontsProvider
import com.dpashko.sandbox.scene.Scene
import javax.inject.Inject

class DebugScene @Inject protected constructor() : Scene {

    companion object {
        const val fpsLabel = "FPS: %d"
        const val heapSizeLabel = "Heap: %d kb"
        private val batch: SpriteBatch = SpriteBatch()
        private val fps: Int
            get() {
                return Gdx.graphics.framesPerSecond
            }
        private val heapSize: Long
            get() {
                return Gdx.app.javaHeap / 1024L
            }
    }

    override fun init() {

    }

    override fun render() {
        batch.begin()
        FontsProvider.defaultFont.draw(batch, fpsLabel.format(fps), 5f, Gdx.graphics.height - 10.toFloat())
        FontsProvider.defaultFont.draw(batch, heapSizeLabel.format(heapSize), 75f, Gdx.graphics.height - 10.toFloat())
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
    }
}