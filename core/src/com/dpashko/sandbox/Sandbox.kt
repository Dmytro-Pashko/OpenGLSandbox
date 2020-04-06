package com.dpashko.sandbox

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
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
            activeScene = it.testScene
        }
        activeScene?.init()
    }

    override fun render() {
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT)
        activeScene?.render()
    }

    override fun dispose() {
        activeScene?.dispose()
    }
}