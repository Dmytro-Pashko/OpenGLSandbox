package com.dpashko.sandbox.scene.test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3

open class CameraController(private var camera: Camera) : InputAdapter() {
    /**
     * The button for rotating the camera.
     */
    private var rotateButton = Input.Buttons.RIGHT
    /**
     * The angle to rotate when moved the full width or height of the screen.
     */
    private var rotateAngle = 90f
    /**
     * The button for translating the camera along the up/right plane
     */
    private var translateButton = Input.Buttons.LEFT
    /**
     * The units to translate the camera when moved the full width or height of the screen.
     */
    private var translateUnits = 15f
    /**
     * The key which must be pressed to activate rotate, translate and forward or 0 to always activate.
     */
    private var activateKey = 0
    /**
     * Indicates if the activateKey is currently being pressed.
     */
    private var activatePressed = false
    /**
     * Whether scrolling requires the activeKey to be pressed (false) or always allow scrolling (true).
     */
    var alwaysScroll = true
    /**
     * The weight for each scrolled amount.
     */
    var scrollFactor = -0.1f
    /**
     * Minimal camera position by Z axis.
     */
    var minZoom = 2.0f
    /**
     * Maximal camera position by Z axis.
     */
    var maxZoom = 50.0f
    /**
     * The target to rotate around.
     */
    var target = Vector3()
    var forwardKey = Input.Keys.W
    private var forwardPressed = false
    var backwardKey = Input.Keys.S
    private var backwardPressed = false
    var forwardRightKey = Input.Keys.D
    private var forwardRight = false
    var forwardLeftKey = Input.Keys.A
    private var forwardLeft = false

    /**
     * The current (first) button being pressed.
     */
    private var button = -1
    private var startX = 0f
    private var startY = 0f
    private val tmpV1 = Vector3()
    private val tmpV2 = Vector3()
    fun update() {
        if (forwardRight || forwardLeft || forwardPressed || backwardPressed) {
            val delta = Gdx.graphics.deltaTime
            tmpV1.set(camera.direction).scl(translateUnits).z = 0f
            if (forwardRight) {
                tmpV2.set(tmpV1).scl(-delta).rotate(Vector3.Z, 90f)
                camera.translate(tmpV2)
                target.add(tmpV2)
            }
            if (forwardLeft) {
                tmpV2.set(tmpV1).scl(delta).rotate(Vector3.Z, 90f)
                camera.translate(tmpV2)
                target.add(tmpV2)
            }
            if (forwardPressed) {
                tmpV1.scl(delta)
                camera.translate(tmpV1)
                target.add(tmpV1)
            }
            if (backwardPressed) {
                tmpV1.scl(-delta)
                camera.translate(tmpV1)
                target.add(tmpV1)
            }
            camera.update()
        }
    }

    private var touched = 0
    private var multiTouch = false
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        touched = touched or (1 shl pointer)
        multiTouch = !MathUtils.isPowerOfTwo(touched)
        if (multiTouch) this.button = -1 else if (this.button < 0 && (activateKey == 0 || activatePressed)) {
            startX = screenX.toFloat()
            startY = screenY.toFloat()
            this.button = button
        }
        return super.touchDown(screenX, screenY, pointer, button) || activateKey == 0 || activatePressed
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        touched = touched and (1 shl pointer).inv()
        multiTouch = !MathUtils.isPowerOfTwo(touched)
        if (button == this.button) this.button = -1
        return super.touchUp(screenX, screenY, pointer, button) || activatePressed
    }

    private fun process(deltaX: Float, deltaY: Float, button: Int): Boolean {
        if (button == translateButton) {
            tmpV1.set(camera.direction).crs(camera.up).nor().scl(-deltaX * translateUnits).z = 0f
            camera.translate(tmpV1)
            tmpV2.set(camera.up).scl(-deltaY * translateUnits).z = 0f
            camera.translate(tmpV2)
            target.add(tmpV1).add(tmpV2)
        }
        if (button == rotateButton) {
            camera.rotateAround(target, Vector3.Z, -deltaX * rotateAngle)
        }
        camera.update()
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val result = super.touchDragged(screenX, screenY, pointer)
        if (result || button < 0) return result
        val deltaX = (screenX - startX) / Gdx.graphics.width
        val deltaY = (startY - screenY) / Gdx.graphics.height
        startX = screenX.toFloat()
        startY = screenY.toFloat()
        return process(deltaX, deltaY, button)
    }

    override fun scrolled(amount: Int) = zoom(amount * scrollFactor * translateUnits)

    private fun zoom(amount: Float): Boolean {
        return if (isCanZoomIn(amount) || isCanZoomOut(amount)) {
            if (!alwaysScroll && activateKey != 0 && !activatePressed) return false
            camera.translate(tmpV1.set(camera.direction).scl(amount))
            camera.update()
            true
        } else {
            false
        }
    }

    private fun isCanZoomIn(amount: Float) = camera.position.z > minZoom && amount > 0

    private fun isCanZoomOut(amount: Float) = camera.position.z < maxZoom && amount < 0


    override fun keyDown(keycode: Int): Boolean {
        if (keycode == activateKey) activatePressed = true
        when (keycode) {
            forwardKey -> forwardPressed = true
            backwardKey -> backwardPressed = true
            forwardRightKey -> forwardRight = true
            forwardLeftKey -> forwardLeft = true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == activateKey) {
            activatePressed = false
            button = -1
        }
        when (keycode) {
            forwardKey -> forwardPressed = false
            backwardKey -> backwardPressed = false
            forwardRightKey -> forwardRight = false
            forwardLeftKey -> forwardLeft = false
        }
        return false
    }
}