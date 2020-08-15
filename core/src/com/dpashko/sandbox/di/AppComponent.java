package com.dpashko.sandbox.di;

import com.dpashko.sandbox.scene.plane3d.Plane3dScene;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Plane3dScene getPlane3dScene();

    @Component.Builder
    interface Builder {

        AppComponent build();
    }
}
