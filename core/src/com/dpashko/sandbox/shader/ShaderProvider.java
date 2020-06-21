package com.dpashko.sandbox.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.dpashko.sandbox.files.FilesProvider;

public class ShaderProvider {

    public static ShaderProgram getSkyBoxShader() {
        final String vertexShader = FilesProvider.skybox_vertex_shader.readString();
        final String fragmentShader = FilesProvider.skybox_fragment_shader.readString();
        return new ShaderProgram(vertexShader, fragmentShader);
    }

    public static ShaderProgram axis3dShader() {
        final String vertexShader = FilesProvider.test_axis3d_vertex_shader.readString();
        final String fragmentShader = FilesProvider.test_axis3d_fragment_shader.readString();
        return new ShaderProgram(vertexShader, fragmentShader);
    }
}
