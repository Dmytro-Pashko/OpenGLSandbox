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
        final String vertexShader = FilesProvider.axis3d_vertex_shader.readString();
        final String fragmentShader = FilesProvider.axis3d_fragment_shader.readString();
        return new ShaderProgram(vertexShader, fragmentShader);
    }

    public static ShaderProgram grid3dShader() {
        final String vertexShader = FilesProvider.grid3d_vertex_shader.readString();
        final String fragmentShader = FilesProvider.grid3d_fragment_shader.readString();
        return new ShaderProgram(vertexShader, fragmentShader);
    }
}
