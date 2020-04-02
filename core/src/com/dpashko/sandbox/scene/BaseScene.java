package com.dpashko.sandbox.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dpashko.sandbox.font.FontsProvider;

public abstract class BaseScene<T extends BaseSceneController> {

    private final SpriteBatch batch;
    private final T controller;

    protected BaseScene(final T controller) {
        this.controller = controller;
        batch = new SpriteBatch();
    }

    public void render() {
        controller.tick();
        batch.begin();
        FontsProvider.defaultFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, Gdx.graphics.getHeight() - 10);
        batch.end();
    }

    public abstract void dispose();
}
