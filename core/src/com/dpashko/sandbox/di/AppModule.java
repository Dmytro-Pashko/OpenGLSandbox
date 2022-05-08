package com.dpashko.sandbox.di;

import com.dpashko.sandbox.scene.editor.EditorSandboxScene;
import com.dpashko.sandbox.skin.SkinProvider;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    public EditorSandboxScene providesEditorScene() {
        return new EditorSandboxScene(SkinProvider.getDefaultSkin());
    }
}
