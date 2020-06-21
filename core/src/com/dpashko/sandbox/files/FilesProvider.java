package com.dpashko.sandbox.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FilesProvider {

    //Textures
    public static FileHandle skyBox = Gdx.files.internal("textures/skybox_clouds.jpg");

    //Models
    public static FileHandle cylinderModel = Gdx.files.internal("models/cylinder.obj");
    public static FileHandle sphereModel = Gdx.files.internal("models/sphere.obj");
    public static FileHandle boxModel = Gdx.files.internal("models/box.obj");

    //Shaders
    public static FileHandle skybox_vertex_shader = Gdx.files.internal("shaders/skybox_vertex_shader.glsl");
    public static FileHandle skybox_fragment_shader = Gdx.files.internal("shaders/skybox_fragment_shader.glsl");

    public static FileHandle axis3d_vertex_shader = Gdx.files.internal("shaders/axis_3d_v.glsl");
    public static FileHandle axis3d_fragment_shader = Gdx.files.internal("shaders/axis_3d_f.glsl");

    public static FileHandle grid3d_vertex_shader = Gdx.files.internal("shaders/grid_3d_v.glsl");
    public static FileHandle grid3d_fragment_shader = Gdx.files.internal("shaders/grid_3d_f.glsl");

    //Default UI skin.
    public static FileHandle ui_default_skin_fnt = Gdx.files.internal("ui/default/default.fnt");
    public static FileHandle ui_default_skin_atlas = Gdx.files.internal("ui/default/uiskin.atlas");
    public static FileHandle ui_default_skin_json = Gdx.files.internal("ui/default/uiskin.json");
    public static FileHandle ui_default_skin_png = Gdx.files.internal("ui/default/uiskin.png");
}
