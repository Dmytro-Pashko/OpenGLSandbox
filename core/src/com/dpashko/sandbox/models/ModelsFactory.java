package com.dpashko.sandbox.models;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.dpashko.sandbox.files.FilesProvider;
import org.jetbrains.annotations.NotNull;

public class ModelsFactory {

    private final static ModelBuilder builder = new ModelBuilder();
    private static final ObjLoader objLoader = new ObjLoader();
    private static ModelInstance cachedBoxModel = null;
    private static ModelInstance cachedCylinderModel = null;
    private static ModelInstance cachedSphereModel = null;

    @NotNull
    public static ModelInstance createSphere() {
        if (cachedSphereModel == null) {
            cachedSphereModel = loadModel(FilesProvider.sphereModel, null);
        }
        return cachedSphereModel.copy();
    }

    @NotNull
    public static ModelInstance createBox() {
        if (cachedBoxModel == null) {
            cachedBoxModel = loadModel(FilesProvider.boxModel, null);
        }
        return cachedBoxModel.copy();
    }

    @NotNull
    public static ModelInstance createCylinder() {
        if (cachedCylinderModel == null) {
            cachedCylinderModel = loadModel(FilesProvider.cylinderModel, null);
        }
        return cachedCylinderModel.copy();
    }

    @NotNull
    public static ModelInstance loadGround() {
        return loadModel(FilesProvider.ground, null);
    }

    @NotNull
    public static ModelInstance loadModel(final FileHandle file, final Material material) {
        final Model model = objLoader.loadModel(file, true);
        model.materials.clear();
        if (material != null)
            model.materials.add(material);
        return new ModelInstance(model);
    }

    @NotNull
    public static ModelInstance createXAxisModel(final float size) {
        final Model model = builder.createBox(size, .02f, .02f, MaterialFactory.diffuse(Color.RED), VertexAttributes.Usage.Position);
        return new ModelInstance(model);
    }

    @NotNull
    public static ModelInstance createYAxisModel(final float size) {
        final Model model = builder.createBox(.02f, size, .02f, MaterialFactory.diffuse(Color.GREEN), VertexAttributes.Usage.Position);
        return new ModelInstance(model);
    }

    @NotNull
    public static ModelInstance createZAxisModel(final float size) {
        final Model model = builder.createBox(.02f, .02f, size, MaterialFactory.diffuse(Color.BLUE), VertexAttributes.Usage.Position);
        return new ModelInstance(model);
    }
}
