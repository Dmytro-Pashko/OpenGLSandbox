package com.dpashko.sandbox.scene.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class CameraController extends InputAdapter {

    /**
     * The button for rotating the camera.
     */
    public int rotateButton = Input.Buttons.RIGHT;

    /**
     * The angle to rotate when moved the full width or height of the screen.
     */
    public float rotateAngle = 90f;

    /**
     * The button for translating the camera along the up/right plane
     */
    public int translateButton = Input.Buttons.LEFT;
    /**
     * The units to translate the camera when moved the full width or height of the screen.
     */
    public float translateUnits = 10f; // FIXME auto calculate this based on the target

    /**
     * The key which must be pressed to activate rotate, translate and forward or 0 to always activate.
     */
    public int activateKey = 0;
    /**
     * Indicates if the activateKey is currently being pressed.
     */
    protected boolean activatePressed;
    /**
     * Whether scrolling requires the activeKey to be pressed (false) or always allow scrolling (true).
     */
    public boolean alwaysScroll = true;
    /**
     * The weight for each scrolled amount.
     */
    public float scrollFactor = -0.1f;

    /**
     * Minimal camera position by Z axis.
     */
    public float minZoom = 2.0f;

    /**
     * Maximal camera position by Z axis.
     */
    public float maxZoom = 10.0f;

    /**
     * The target to rotate around.
     */
    public Vector3 target = new Vector3();

    public int forwardKey = Input.Keys.W;
    protected boolean forwardPressed;
    public int backwardKey = Input.Keys.S;
    protected boolean backwardPressed;
    public int forwardRightKey = Input.Keys.D;
    protected boolean forwardRight;
    public int forwardLeftKey = Input.Keys.A;
    protected boolean forwardLeft;
    /**
     * The camera.
     */
    public Camera camera;
    /**
     * The current (first) button being pressed.
     */
    protected int button = -1;

    private float startX, startY;
    private final Vector3 tmpV1 = new Vector3();
    private final Vector3 tmpV2 = new Vector3();

    public CameraController(final Camera camera) {
        super();
        this.camera = camera;
    }

    public void update() {
        if (forwardRight || forwardLeft || forwardPressed || backwardPressed) {
            final float delta = Gdx.graphics.getDeltaTime();

            tmpV1.set(camera.direction).scl(translateUnits).z = 0;

            if (forwardRight) {
                tmpV2.set(tmpV1).scl(-delta).rotate(Vector3.Z, 90);
                camera.translate(tmpV2);
                target.add(tmpV2);
            }
            if (forwardLeft) {
                tmpV2.set(tmpV1).scl(delta).rotate(Vector3.Z, 90);
                camera.translate(tmpV2);
                target.add(tmpV2);
            }

            if (forwardPressed) {
                tmpV1.scl(delta);
                camera.translate(tmpV1);
                target.add(tmpV1);
            }
            if (backwardPressed) {
                tmpV1.scl(-delta);
                camera.translate(tmpV1);
                target.add(tmpV1);
            }
            camera.update();
        }
    }

    private int touched;
    private boolean multiTouch;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touched |= (1 << pointer);
        multiTouch = !MathUtils.isPowerOfTwo(touched);
        if (multiTouch)
            this.button = -1;
        else if (this.button < 0 && (activateKey == 0 || activatePressed)) {
            startX = screenX;
            startY = screenY;
            this.button = button;
        }
        return super.touchDown(screenX, screenY, pointer, button) || (activateKey == 0 || activatePressed);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touched &= -1 ^ (1 << pointer);
        multiTouch = !MathUtils.isPowerOfTwo(touched);
        if (button == this.button) this.button = -1;
        return super.touchUp(screenX, screenY, pointer, button) || activatePressed;
    }

    protected boolean process(float deltaX, float deltaY, int button) {
        if (button == translateButton) {
            tmpV1.set(camera.direction).crs(camera.up).nor().scl(-deltaX * translateUnits).z = 0;
            camera.translate(tmpV1);
            tmpV2.set(camera.up).scl(-deltaY * translateUnits).z = 0;
            camera.translate(tmpV2);
            target.add(tmpV1).add(tmpV2);
        }
        if (button == rotateButton) {
            camera.rotateAround(target, Vector3.Z, -deltaX * rotateAngle);
        }
        camera.update();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean result = super.touchDragged(screenX, screenY, pointer);
        if (result || this.button < 0) return result;
        final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
        final float deltaY = (startY - screenY) / Gdx.graphics.getHeight();
        startX = screenX;
        startY = screenY;
        return process(deltaX, deltaY, button);
    }

    @Override
    public boolean scrolled(int amount) {
        return zoom(amount * scrollFactor * translateUnits);
    }

    public boolean zoom(float amount) {
        if (isCanZoomIn(amount) || isCanZoomOut(amount)) {
            if (!alwaysScroll && activateKey != 0 && !activatePressed) return false;
            camera.translate(tmpV1.set(camera.direction).scl(amount));

            camera.update();
            return true;
        } else {
            return false;
        }
    }

    public boolean isCanZoomIn(float amount) {
        return camera.position.z > minZoom && amount > 0;
    }

    public boolean isCanZoomOut(float amount) {
        return camera.position.z < maxZoom && amount < 0;
    }

    @Override
    public boolean keyDown(final int keycode) {
        if (keycode == activateKey) activatePressed = true;
        if (keycode == forwardKey)
            forwardPressed = true;
        else if (keycode == backwardKey)
            backwardPressed = true;
        else if (keycode == forwardRightKey)
            forwardRight = true;
        else if (keycode == forwardLeftKey) forwardLeft = true;
        return false;
    }

    @Override
    public boolean keyUp(final int keycode) {
        if (keycode == activateKey) {
            activatePressed = false;
            button = -1;
        }
        if (keycode == forwardKey)
            forwardPressed = false;
        else if (keycode == backwardKey)
            backwardPressed = false;
        else if (keycode == forwardRightKey)
            forwardRight = false;
        else if (keycode == forwardLeftKey) forwardLeft = false;
        return false;
    }
}