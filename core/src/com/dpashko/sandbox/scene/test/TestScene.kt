package com.dpashko.sandbox.scene.test

import com.dpashko.sandbox.scene.BaseScene
import javax.inject.Inject

class TestScene @Inject constructor(controller: TestSceneController) :
        BaseScene<TestSceneController>(controller) {


    override fun dispose() {
        controller.dispose()
    }
}