package com.dpashko.sandbox.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class MaterialFactory {

    public static Material diffuse(final Color color) {
        return new Material(ColorAttribute.createDiffuse(color));
    }
}
