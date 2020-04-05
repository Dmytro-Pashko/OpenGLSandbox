package com.dpashko.sandbox.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.dpashko.sandbox.files.FilesProvider;

public class ShaderProvider {

    public static ShaderProgram getSkyBoxShader() {
        final String vertexShader = FilesProvider.skybox_vertex_shader.readString();
        final String fragmentShader = FilesProvider.skybox_fragment_shader.readString();
        return new ShaderProgram(vertexShader, fragmentShader);
    }
}
