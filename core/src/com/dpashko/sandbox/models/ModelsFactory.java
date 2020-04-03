package com.dpashko.sandbox.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.dpashko.sandbox.files.FilesProvider;
import org.jetbrains.annotations.NotNull;

public class ModelsFactory {

    private final static ModelBuilder builder = new ModelBuilder();
    private static final ObjLoader objLoader = new ObjLoader();

    public static Model sphereModel = objLoader.loadModel(FilesProvider.sphereModel);
    public static Model cylinderModel = objLoader.loadModel(FilesProvider.cylinderModel);
    public static Model wallModel = objLoader.loadModel(FilesProvider.wallModel);

    @NotNull
    public static ModelInstance createXAxisModel() {
        final Model model = builder.createBox(50f, .02f, .02f, MaterialFactory.diffuse(Color.RED), VertexAttributes.Usage.Position);
        final ModelInstance instance = new ModelInstance(model);
        return instance;
    }

    @NotNull
    public static ModelInstance createYAxisModel() {
        final Model model = builder.createBox(.02f, 50f, .02f, MaterialFactory.diffuse(Color.GREEN), VertexAttributes.Usage.Position);
        final ModelInstance instance = new ModelInstance(model);
        return instance;
    }

    @NotNull
    public static ModelInstance createZAxisModel() {
        final Model model = builder.createBox(.02f, .02f, 50f, MaterialFactory.diffuse(Color.BLUE), VertexAttributes.Usage.Position);
        final ModelInstance instance = new ModelInstance(model);
        return instance;
    }
}
