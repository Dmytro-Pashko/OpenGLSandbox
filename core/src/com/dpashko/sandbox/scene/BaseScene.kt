package com.dpashko.sandbox.scene

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.dpashko.sandbox.font.FontsProvider

abstract class BaseScene<T : BaseSceneController> protected constructor(protected val controller: T) {

    private val batch: SpriteBatch = SpriteBatch()

    fun render() {
        controller.tick()
        batch.begin()
        FontsProvider.defaultFont.draw(batch, "FPS: ${Gdx.graphics.framesPerSecond}", 5f, Gdx.graphics.height - 10.toFloat())
        FontsProvider.defaultFont.draw(batch, "Heap: ${Gdx.app.javaHeap / 1_048_576L} mb.", 75f, Gdx.graphics.height - 10.toFloat())
        batch.end()
    }

    abstract fun dispose()

}