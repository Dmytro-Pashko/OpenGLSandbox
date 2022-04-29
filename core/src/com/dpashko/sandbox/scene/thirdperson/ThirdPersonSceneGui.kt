package com.dpashko.sandbox.scene.thirdperson

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.dpashko.sandbox.skin.SkinProvider

class ThirdPersonSceneGui {

    private val stage = Stage()
    private var cameraInformationLabel = Label(null, SkinProvider.getDefaultSkin())

    fun init() {
        stage.addActor(
            Table().apply {
                left()
                add(Table().apply {
                    add(cameraInformationLabel).padLeft(10f)
                }).align(Align.bottom).expandY()
                setFillParent(true)
            }
        )
    }

    fun update(cameraPosition: Vector3, cameraDirection: Vector3) {
        cameraInformationLabel.setText(
            "Camera:\n" +
                    "position:  [${cameraPosition.x}, ${cameraPosition.y}, ${cameraPosition.z}]\n" +
                    "direction: [${cameraDirection.x}, ${cameraDirection.y}, ${cameraDirection.z}]"
        )
    }

    fun draw() {
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }
}