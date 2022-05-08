package com.dpashko.sandbox.di;

import com.dpashko.sandbox.scene.cube3d.Cubes3DSandboxScene;
import com.dpashko.sandbox.scene.editor.EditorSandboxScene;
import com.dpashko.sandbox.scene.light3d.Light3DSandboxScene;
import com.dpashko.sandbox.scene.model3d.Model3DSandboxScene;
import com.dpashko.sandbox.scene.plane3d.Plane3DSandboxScene;
import com.dpashko.sandbox.scene.thirdperson.ThirdPersonSandboxScene;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    EditorSandboxScene getEditorScene();

    Plane3DSandboxScene getPlane3dScene();

    Cubes3DSandboxScene getCube3dScene();

    Light3DSandboxScene getLight3dScene();

    Model3DSandboxScene getModel3dScene();

    ThirdPersonSandboxScene getThirdPersonScene();

    @Component.Builder
    interface Builder {

        AppComponent build();
    }
}
