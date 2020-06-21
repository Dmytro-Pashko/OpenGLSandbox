package com.dpashko.sandbox.di;

import com.dpashko.sandbox.scene.model3d.Model3dScene;
import com.dpashko.sandbox.scene.axis3d.Axis3dScene;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Model3dScene getModel3dScene();

    Axis3dScene getAxis3dScene();

    @Component.Builder
    interface Builder {

        AppComponent build();
    }
}
