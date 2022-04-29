package com.dpashko.sandbox.di;

import com.dpashko.sandbox.scene.cube3d.Cubes3dScene;
import com.dpashko.sandbox.scene.editor.EditorScene;
import com.dpashko.sandbox.scene.light3d.Light3dScene;
import com.dpashko.sandbox.scene.model3d.Model3dScene;
import com.dpashko.sandbox.scene.plane3d.Plane3dScene;
import com.dpashko.sandbox.scene.thirdperson.ThirdPersonScene;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    EditorScene getEditorScene();

    Plane3dScene getPlane3dScene();

    Cubes3dScene getCube3dScene();

    Light3dScene getLight3dScene();

    Model3dScene getModel3dScene();

    ThirdPersonScene getThirdPersonScene();

    @Component.Builder
    interface Builder {

        AppComponent build();
    }
}
