package com.dpashko.sandbox.scene.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.dpashko.sandbox.skin.SkinProvider

class EditorSceneView(private val skin: Skin = SkinProvider.getDefaultSkin()) {

    private lateinit var stage: Stage
    private lateinit var table: Table

    fun init() {
        stage = Stage()
        table = Table(skin)
        table.setFillParent(true)
        stage.addActor(table)
        table.debug = true // This is optional, but enables debug lines for tables.
    }

    fun draw() {
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }

    fun dispose() {
        stage.dispose()
    }
}