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
    public static Model groundModel = objLoader.loadModel(FilesProvider.ground);

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
