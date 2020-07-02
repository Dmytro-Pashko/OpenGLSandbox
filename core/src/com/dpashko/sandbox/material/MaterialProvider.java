package com.dpashko.sandbox.material;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class MaterialProvider {

    public static Material diffuse(final Color color) {
        return new Material(ColorAttribute.createDiffuse(color));
    }
}
