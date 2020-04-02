package com.dpashko.sandbox.di;

import com.dpashko.sandbox.scene.test.TestScene;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    TestScene getTestScene();

    @Component.Builder
    interface Builder {

        AppComponent build();
    }
}
