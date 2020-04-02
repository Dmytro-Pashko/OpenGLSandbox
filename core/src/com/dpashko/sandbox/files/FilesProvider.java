package com.dpashko.sandbox.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FilesProvider {

    public static FileHandle mainFont = Gdx.files.internal("fonts/main.fnt");
    public static FileHandle groundModel = Gdx.files.internal("models/box.g3db");
}
