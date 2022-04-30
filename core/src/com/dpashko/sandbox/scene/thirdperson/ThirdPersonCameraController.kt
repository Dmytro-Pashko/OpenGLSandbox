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
     *
     */
    private val zoomVelocity = 0.2f

    /**
     *
     */
    private val rotateVelocity = 0.2f

    /**
     * Distance to Player.
     */
    private var distanceToPlayer = 7f

    /**
     * Angle around the player in degrees.
     */
    private var angleAroundPlayer = 0f

    /**
     * Angle between horizontal axis and camera.
     */
    private var pitchAngle = 60f

    private val minPitchAngle = 10f

    private val maxPitchAngle = 70f

    private var startX: Int = 0
    private var startY: Int = 0
    private var screenX: Int = 0
    private var screenY: Int = 0
    private var button: Int = -1
    private var isTouchedDown = false

    fun update() {
        calculateCameraPosition()
        calculateCameraDirection()
        camera.update()
    }

    private fun calculateCameraPosition() {
        val offsetX = distanceToPlayer * sin(Math.toRadians(angleAroundPlayer.toDouble()))
        val offsetY = distanceToPlayer * cos(Math.toRadians(angleAroundPlayer.toDouble()))
        val offsetZ = distanceToPlayer * sin(Math.toRadians(pitchAngle.toDouble()))

        camera.position.x = (actor.position.x + offsetX).toFloat()
        camera.position.y = (actor.position.y + offsetY).toFloat()
        camera.position.z = (actor.position.z + offsetZ).toFloat()
    }

    val tmp: Vector3 = Vector3(0f, 0f, 0f)
    private fun calculateCameraDirection() {
        camera.up.set(Vector3.Z)
        camera.lookAt(actor.boundingBox?.getCenter(tmp))
    }


    var deltaX = 0F
    var deltaY = 0F
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (button == Input.Buttons.LEFT) {
            // Change value only if mouse was moved.
            if (this.screenX != screenX) {
                deltaX = (startX - screenX).toFloat() * rotateVelocity
                angleAroundPlayer -= deltaX % 360f
                startX = screenX
            }
            // Change value only if mouse was moved.
            if (this.screenY != screenY) {
                deltaY = ((startY - screenY).toFloat() * rotateVelocity)
                // Do not to rotate lower that minPitchAngle.
                if ((deltaY < 0 && pitchAngle < 0)) {
                    pitchAngle = minPitchAngle
                    // Do not to rotate upper that maxPitchAngle.
                } else if (deltaY > 0 && pitchAngle > maxPitchAngle) {
                    pitchAngle = maxPitchAngle
                } else {
                    pitchAngle -= deltaY
                }
                startY = screenY
            }
            return true
        } else {
            return super.touchDragged(screenX, screenY, pointer)
        }
    }


    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        distanceToPlayer += amountY * zoomVelocity
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        isTouchedDown = true
        startX = screenX
        startY = screenY
        this.button = button
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        isTouchedDown = false
        startX = 0
        startY = 0
        this.button = -1
        return true
    }
}
