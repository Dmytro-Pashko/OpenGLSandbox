package com.dpashko.sandbox

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.dpashko.sandbox.di.AppComponent
import com.dpashko.sandbox.di.DaggerAppComponent
import com.dpashko.sandbox.scene.SandboxScene

class Sandbox : ApplicationAdapter() {

    private lateinit var component: AppComponent

    internal companion object {
        @JvmStatic
        private var activeSandboxScene: SandboxScene? = null
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        component = DaggerAppComponent.create().also {
            activeSandboxScene = it.thirdPersonScene
        }
        activeSandboxScene?.init()
    }

    override fun render() {
        activeSandboxScene?.draw()
    }

    override fun dispose() {
        activeSandboxScene?.dispose()
    }
}
