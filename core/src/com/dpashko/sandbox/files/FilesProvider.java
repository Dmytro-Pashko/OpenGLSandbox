package com.dpashko.sandbox.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FilesProvider {

    //Fonts
    public static FileHandle mainFont = Gdx.files.internal("fonts/main.fnt");

    //Textures
    public static FileHandle skybox = Gdx.files.internal("textures/skybox_field.jpg");

    //Models
    public static FileHandle cylinderModel = Gdx.files.internal("models/cylinder.obj");
    public static FileHandle sphereModel = Gdx.files.internal("models/sphere.obj");
    public static FileHandle boxModel = Gdx.files.internal("models/box.obj");

    public static FileHandle ground = Gdx.files.internal("models/ground.obj");

    //Shaders
    public static FileHandle skybox_vertex_shader = Gdx.files.internal("shaders/skybox_vertex_shader.glsl");
    public static FileHandle skybox_fragment_shader = Gdx.files.internal("shaders/skybox_fragment_shader.glsl");

    public static FileHandle test_axis3d_vertex_shader = Gdx.files.internal("shaders/axis_3d_v.glsl");
    public static FileHandle test_axis3d_fragment_shader = Gdx.files.internal("shaders/axis_3d_f.glsl");
}
