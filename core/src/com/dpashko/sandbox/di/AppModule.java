package com.dpashko.sandbox.di;

import com.dpashko.sandbox.scene.editor.EditorScene;
import com.dpashko.sandbox.skin.SkinProvider;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    public EditorScene providesEditorScene() {
        return new EditorScene(SkinProvider.getDefaultSkin());
    }
}
