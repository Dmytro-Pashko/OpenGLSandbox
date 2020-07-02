package com.dpashko.sandbox.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.dpashko.sandbox.files.FileProvider;

public class ShaderProvider {

    public static ShaderProgram getSkyBoxShader() {
        final String vertexShader = FileProvider.skybox_vertex_shader.readString();
        final String fragmentShader = FileProvider.skybox_fragment_shader.readString();
        return new ShaderProgram(vertexShader, fragmentShader);
    }

    public static ShaderProgram axis3dShader() {
        final String vertexShader = FileProvider.axis3d_vertex_shader.readString();
        final String fragmentShader = FileProvider.axis3d_fragment_shader.readString();
        return new ShaderProgram(vertexShader, fragmentShader);
    }

    public static ShaderProgram grid3dShader() {
        final String vertexShader = FileProvider.grid3d_vertex_shader.readString();
        final String fragmentShader = FileProvider.grid3d_fragment_shader.readString();
        return new ShaderProgram(vertexShader, fragmentShader);
    }
}
