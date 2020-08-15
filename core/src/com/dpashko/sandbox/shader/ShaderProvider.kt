package com.dpashko.sandbox.shader

import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.dpashko.sandbox.files.FileProvider

class ShaderProvider {

    companion object {

        fun getBasicShader() =
                ShaderProgram(FileProvider.BASIC_VERTEX_SHADER, FileProvider.BASIC_FRAGMENT_SHADER)
    }
}
