package com.dpashko.sandbox.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FilesProvider {

    //Fonts
    public static FileHandle mainFont = Gdx.files.internal("fonts/main.fnt");

    //Textures
    public static FileHandle skybox = Gdx.files.internal("textures/skybox.jpg");

    //Models
    public static FileHandle cylinderModel = Gdx.files.internal("models/cylinder.obj");
    public static FileHandle sphereModel = Gdx.files.internal("models/sphere.obj");
    public static FileHandle boxModel = Gdx.files.internal("models/box.obj");

    public static FileHandle ground = Gdx.files.internal("models/ground.obj");

}
