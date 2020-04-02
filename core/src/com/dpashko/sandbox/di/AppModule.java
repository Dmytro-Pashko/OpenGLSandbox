package com.dpashko.sandbox.di;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    public static PerspectiveCamera providesPerspectiveCamera() {
        return new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
