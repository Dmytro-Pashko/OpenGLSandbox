package com.dpashko.sandbox.skin;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.dpashko.sandbox.files.FileProvider;

public class SkinProvider {

    public static Skin getDefaultSkin() {
        return new Skin(FileProvider.ui_default_skin_json);
    }

}
