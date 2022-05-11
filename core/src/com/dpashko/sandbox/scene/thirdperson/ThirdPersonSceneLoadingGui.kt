package com.dpashko.sandbox.scene.thirdperson

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table

class ThirdPersonSceneLoadingGui(skin: Skin) {

    private val stage = Stage()
    private var loadingProgressLabel = Label(null, skin)

    fun init() {
        stage.addActor(
            Table().apply {
                add(loadingProgressLabel).center()
                setFillParent(true)
            }
        )
    }

    fun update(progress: Float) {
        loadingProgressLabel.setText("Loading: ${(100 * progress).toInt()}%")
    }

    fun draw() {
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }
}
