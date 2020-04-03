package com.dpashko.sandbox.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FilesProvider {

    public static FileHandle mainFont = Gdx.files.internal("fonts/main.fnt");
    public static FileHandle cylinderModel = Gdx.files.internal("models/cylinder.obj");
    public static FileHandle sphereModel = Gdx.files.internal("models/sphere.obj");
    public static FileHandle wallModel = Gdx.files.internal("models/wall.obj");
}
