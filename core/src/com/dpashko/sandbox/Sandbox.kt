package com.dpashko.sandbox

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.dpashko.sandbox.di.AppComponent
import com.dpashko.sandbox.di.DaggerAppComponent
import com.dpashko.sandbox.scene.Scene

class Sandbox : ApplicationAdapter() {

    private lateinit var component: AppComponent

    internal companion object {
        @JvmStatic
        private var activeScene: Scene? = null
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        component = DaggerAppComponent.create().also {
            activeScene = it.plane3dScene
        }
        activeScene?.init()
    }

    override fun render() {
        activeScene?.draw()
    }

    override fun dispose() {
        activeScene?.dispose()
    }
}
