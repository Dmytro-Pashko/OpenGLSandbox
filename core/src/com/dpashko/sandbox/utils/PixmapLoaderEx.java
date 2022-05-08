package com.dpashko.sandbox.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;

/**
 * Loader of pixmaps that fixes issue with incorrect file resolving in original one.
 */
public class PixmapLoaderEx extends AsynchronousAssetLoader<Pixmap, PixmapLoaderEx.PixmapParameter> {

    private FileHandleResolver resolver;

    public PixmapLoaderEx(FileHandleResolver resolver) {
        super(resolver);
        this.resolver = resolver;
    }

    Pixmap pixmap;

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, PixmapLoaderEx.PixmapParameter parameter) {
        pixmap = null;
        // Default Pixmap loaded doesn't use resolver.
        pixmap = new Pixmap(resolver.resolve(fileName));
    }

    @Override
    public Pixmap loadSync(AssetManager manager, String fileName, FileHandle file, PixmapLoaderEx.PixmapParameter parameter) {
        Pixmap pixmap = this.pixmap;
        this.pixmap = null;
        return pixmap;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, PixmapLoaderEx.PixmapParameter parameter) {
        return null;
    }

    static public class PixmapParameter extends AssetLoaderParameters<Pixmap> {
    }
}
