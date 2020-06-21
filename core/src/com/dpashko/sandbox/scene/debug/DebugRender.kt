package com.dpashko.sandbox.scene.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Disposable
import com.dpashko.sandbox.font.FontsProvider
import javax.inject.Inject

class DebugRender @Inject protected constructor() : Disposable {

    companion object {
        const val fpsText = "FPS: %d"
        const val heapSizeText = "Heap: %d kb"
        const val cameraPositionText = "Camera Position: %s"

        val fpsLabel = Label("", Label.LabelStyle(FontsProvider.defaultFont, Color.WHITE)).apply {
            setPosition(5f, 10f)
        }

        val heapSizeLabel = Label("", Label.LabelStyle(FontsProvider.defaultFont, Color.WHITE)).apply {
            setPosition(5f, 25f)
        }

        val cameraPositionLabel = Label("", Label.LabelStyle(FontsProvider.defaultFont, Color.WHITE)).apply {
            setPosition(5f, 40f)
        }

        private val spriteBatch = SpriteBatch()
        private val fps: Int
            get() {
                return Gdx.graphics.framesPerSecond
            }
        private val heapSize: Long
            get() {
                return Gdx.app.javaHeap / 1024L
            }
    }

    fun render(camera: Camera) {
        render()
        drawCameraPos(camera)
    }

    private fun drawCameraPos(camera: Camera) {
        spriteBatch.begin()
        cameraPositionLabel.setText(cameraPositionText.format(camera.position))
        cameraPositionLabel.draw(spriteBatch, 1f)
        spriteBatch.end()
    }

    fun render() {
        spriteBatch.begin()
        fpsLabel.setText(fpsText.format(fps))
        heapSizeLabel.setText(heapSizeText.format(heapSize))
        fpsLabel.draw(spriteBatch, 1f)
        heapSizeLabel.draw(spriteBatch, 1f)
        spriteBatch.end()
    }

    override fun dispose() {
        spriteBatch.dispose()
    }
}