package com.dpashko.sandbox.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FileProvider {

    public static  FileHandle TEXTURE_SKY = Gdx.files.internal("textures/sky.png");

    public static FileHandle BASIC_VERTEX_SHADER = Gdx.files.internal("shaders/simple_3d_v.glsl");
    public static FileHandle BASIC_FRAGMENT_SHADER = Gdx.files.internal("shaders/simple_3d_f.glsl");

}
