package com.dpashko.sandbox.scene.thirdperson

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3
import kotlin.math.cos
import kotlin.math.sin

class ThirdPersonCameraController(
    private val actor: Actor, private var camera: Camera
) : InputAdapter() {

    /**
     * Distance to Player.
     */
    private var distanceToPlayer = 3f

    /**
     * Angle around the player in degrees.
     */
    private var angleAroundPlayer = 20f

    /**
     * Angle between horizontal axis and camera.
     */
    private var pithAngle = 30f


    private var startX: Int = 0
    private var startY: Int = 0
    private var screenX: Int = 0
    private var screenY: Int = 0
    private var button: Int = -1
    private var isTouchedDown = false

    private var cameraDirection = Vector3(0f, 0f, 0f)

    init {
        cameraDirection = cameraDirection
            .set(actor.direction)
        camera.direction.set(cameraDirection)
    }

    fun update() {
        calculateCameraPosition()
        calculateCameraDirection()
        camera.update()
    }

    private fun calculateCameraPosition() {
        val offsetX = distanceToPlayer * sin(Math.toRadians(angleAroundPlayer.toDouble()))
        val offsetY = distanceToPlayer * cos(Math.toRadians(angleAroundPlayer.toDouble()))
        val offsetZ = distanceToPlayer * cos(Math.toRadians(pithAngle.toDouble()))

        if (actor.position.x >= 0) {
            camera.position.x = (actor.position.x - offsetX).toFloat()
        } else {
            camera.position.x = (actor.position.x + offsetX).toFloat()
        }

        if (actor.position.y >= 0) {
            camera.position.y = (actor.position.y - offsetY).toFloat()
        } else {
            camera.position.y = (actor.position.y + offsetY).toFloat()
        }

        camera.position.z = (actor.position.z + offsetZ).toFloat()
    }

    private fun calculateCameraDirection() {
    }


    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (this.screenX != screenX) {
            // Change value only if mouse was moved.
            if (button == Input.Buttons.LEFT) {
                angleAroundPlayer += ((startX - screenX).toFloat() * 0.2f) % 360f
            }
            startX = screenX
        }
        if (this.screenY != screenY) {
            // Change value only if mouse was moved.
            if (button == Input.Buttons.LEFT) {
                pithAngle += ((startY - screenY).toFloat() * 0.2f) % 360f
            }
            startX = screenX
        }
        return super.touchDragged(screenX, screenY, pointer)
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        distanceToPlayer += amountY * 0.1f
        return super.scrolled(amountX, amountY)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        isTouchedDown = true
        startX = screenX
        startY = screenY
        this.button = button
        return super.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        isTouchedDown = false
        startX = 0
        startY = 0
        this.button = -1
        return super.touchUp(screenX, screenY, pointer, button)
    }
}
