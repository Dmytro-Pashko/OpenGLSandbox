package com.dpashko.sandbox.scene.test

import com.badlogic.gdx.Gdx
import com.dpashko.sandbox.scene.Scene
import com.dpashko.sandbox.scene.debug.DebugScene
import javax.inject.Inject

class TestScene @Inject protected constructor(
        private val debugScene: DebugScene,
        private val controller: TestSceneController
) : Scene {

    private val logger = Gdx.app.applicationLogger

    override fun dispose() {
        controller.dispose()
        debugScene.dispose()
    }

    override fun render() {
        debugScene.render()
    }
}