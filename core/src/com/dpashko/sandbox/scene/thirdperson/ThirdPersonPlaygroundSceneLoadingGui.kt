package com.dpashko.sandbox.scene.thirdperson

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.dpashko.sandbox.skin.SkinProvider

class ThirdPersonPlaygroundSceneLoadingGui {

    private val stage = Stage()
    private var loadingProgressLabel = Label(null, SkinProvider.getDefaultSkin())

    fun init() {
        stage.addActor(
            Table().apply {
                add(loadingProgressLabel).center()
                setFillParent(true)
            }
        )
    }

    fun update(progress: Float) {
        loadingProgressLabel.setText("Loading: {$progress} %")
    }

    fun draw() {
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }
}
