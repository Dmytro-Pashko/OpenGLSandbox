package com.dpashko.sandbox.scene.test

import com.dpashko.sandbox.scene.Controller
import javax.inject.Inject

class TestSceneController @Inject constructor() : Controller {

    override fun tick() {}

    override fun dispose() {}
}
