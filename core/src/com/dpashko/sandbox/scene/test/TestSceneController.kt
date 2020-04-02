package com.dpashko.sandbox.scene.test

import com.dpashko.sandbox.scene.BaseSceneController
import javax.inject.Inject

class TestSceneController @Inject constructor() : BaseSceneController() {

    override fun tick() {}

    override fun dispose() {}
}
