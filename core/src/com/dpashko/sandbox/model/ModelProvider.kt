package com.dpashko.sandbox.model;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.dpashko.sandbox.files.FileProvider;
import com.dpashko.sandbox.material.MaterialProvider;
import org.jetbrains.annotations.NotNull;

public class ModelProvider {

    private static final ObjLoader objLoader = new ObjLoader();
    private static ModelInstance cachedBoxModel = null;
    private static ModelInstance cachedCylinderModel = null;
    private static ModelInstance cachedSphereModel = null;

    @NotNull
    public static ModelInstance createSphere() {
        if (cachedSphereModel == null) {
            cachedSphereModel = loadModel(FileProvider.sphereModel, null);
        }
        return cachedSphereModel.copy();
    }

    @NotNull
    public static ModelInstance createBox() {
        return createBox(1f);
    }

    @NotNull
    public static ModelInstance createBox(float size) {
        if (cachedBoxModel == null) {
            cachedBoxModel = loadModel(FileProvider.boxModel, MaterialProvider.diffuse(Color.GRAY));
        }
        ModelInstance modelInstance = cachedBoxModel.copy();
        modelInstance.transform.scl(size);
        return modelInstance;
    }

    @NotNull
    public static ModelInstance createCylinder() {
        if (cachedCylinderModel == null) {
            cachedCylinderModel = loadModel(FileProvider.cylinderModel, null);
        }
        return cachedCylinderModel.copy();
    }


    @NotNull
    public static ModelInstance loadModel(final FileHandle file, final Material material) {
        final Model model = objLoader.loadModel(file, true);
        model.materials.clear();
        if (material != null)
            model.materials.add(material);
        return new ModelInstance(model);
    }
}
