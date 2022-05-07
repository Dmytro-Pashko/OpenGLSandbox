package com.dpashko.sandbox.scene.thirdperson

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.dpashko.sandbox.skin.SkinProvider

class ThirdPersonPlaygroundSceneGui {

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

    val tmp = Vector3(0f, 0f, 0f)
    fun update(camera: Camera, actor: Actor, deltaX: Float, deltaY: Float) {
        val target = actor.boundingBox?.getCenter(tmp) ?: Vector3(0f, 0f, 0f)
        cameraInformationLabel.setText(
            "Camera:\n" +
                    "position:  [${camera.position.x}, ${camera.position.y}, ${camera.position.z}]\n" +
                    "direction: [${camera.direction.x}, ${camera.direction.y}, ${camera.direction.z}]\n" +
                    "target: [${target.x}, ${target.y}, ${target.z}]\n" +
                    "distance = ${target.dst2(camera.position)}\n" +
                    "DeltaX=$deltaX DeltaY=$deltaY"
        )
    }

    fun draw() {
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }
}