package com.dpashko.sandbox.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FileProvider {

    //Textures
    public static FileHandle skyBox = Gdx.files.internal("textures/skybox_clouds.jpg");
    //Cubes
    public static FileHandle cube1 = Gdx.files.internal("textures/cube1.jpg");
    public static FileHandle cube2 = Gdx.files.internal("textures/cube2.jpg");
    public static FileHandle cube3 = Gdx.files.internal("textures/cube3.jpg");
    public static FileHandle cube4 = Gdx.files.internal("textures/cube4.jpg");
    public static FileHandle cube5 = Gdx.files.internal("textures/cube5.jpg");
    public static FileHandle cube6 = Gdx.files.internal("textures/cube6.jpg");
    public static FileHandle cube7 = Gdx.files.internal("textures/cube7.jpg");

    public static FileHandle sky = Gdx.files.internal("textures/sky.png");

    //Models
    public static FileHandle cylinderModel = Gdx.files.internal("models/cylinder.obj");
    public static FileHandle sphereModel = Gdx.files.internal("models/sphere.obj");
    public static FileHandle boxModel = Gdx.files.internal("models/box.obj");
    public static FileHandle suzanneModel = Gdx.files.internal("models/suzanne.obj");
    public static FileHandle house = Gdx.files.internal("models/house/house.obj");
    public static FileHandle suzanneModelHighPoly = Gdx.files.internal("models/high_poly_suzanne.obj");

    //Shaders
    public static FileHandle skybox_vertex_shader = Gdx.files.internal("shaders/skybox_vertex_shader.glsl");
    public static FileHandle skybox_fragment_shader = Gdx.files.internal("shaders/skybox_fragment_shader.glsl");

    public static FileHandle axis3d_vertex_shader = Gdx.files.internal("shaders/axis_3d_v.glsl");
    public static FileHandle axis3d_fragment_shader = Gdx.files.internal("shaders/axis_3d_f.glsl");

    public static FileHandle grid3d_vertex_shader = Gdx.files.internal("shaders/grid_3d_v.glsl");
    public static FileHandle grid3d_fragment_shader = Gdx.files.internal("shaders/grid_3d_f.glsl");

    public static FileHandle simple3d_vertex_shader = Gdx.files.internal("shaders/simple_3d_v.glsl");
    public static FileHandle simple3d_fragment_shader = Gdx.files.internal("shaders/simple_3d_f.glsl");

    public static FileHandle light_3d_vertex_shader = Gdx.files.internal("shaders/light_3d_v.glsl");
    public static FileHandle light_3d_fragment_shader = Gdx.files.internal("shaders/light_3d_f.glsl");

    //Default UI skin.
    public static FileHandle ui_default_skin_fnt = Gdx.files.internal("ui/default/default.fnt");
    public static FileHandle ui_default_skin_atlas = Gdx.files.internal("ui/default/uiskin.atlas");
    public static FileHandle ui_default_skin_json = Gdx.files.internal("ui/default/uiskin.json");
    public static FileHandle ui_default_skin_png = Gdx.files.internal("ui/default/uiskin.png");
}
