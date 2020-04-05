package com.dpashko.sandbox.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class SkyBoxShader implements Disposable {

    protected final Pixmap[] data = new Pixmap[6];
    protected ShaderProgram shader;

    protected int u_worldTrans;
    protected Mesh quad;
    private Matrix4 worldTrans;

    public SkyBoxShader(final FileHandle skyBox) {
        this(new Pixmap(skyBox));
    }

    private SkyBoxShader(final Pixmap skyBox) {
        int w = skyBox.getWidth();
        int h = skyBox.getHeight();
        for (int i = 0; i < 6; i++) data[i] = new Pixmap(w / 4, h / 3, Pixmap.Format.RGB888);
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                //-X
                if (x <= w / 4 && y >= h / 3 && y <= h * 2 / 3)
                    data[1].drawPixel(x, y - h / 3, skyBox.getPixel(x, y));
                //+Y
                if (x >= w / 4 && x <= w / 2 && y <= h / 3)
                    data[2].drawPixel(x - w / 4, y, skyBox.getPixel(x, y));
                //+Z
                if (x >= w / 4 && x <= w / 2 && y >= h / 3 && y <= h * 2 / 3)
                    data[4].drawPixel(x - w / 4, y - h / 3, skyBox.getPixel(x, y));
                //-Y
                if (x >= w / 4 && x <= w / 2 && y >= h * 2 / 3)
                    data[3].drawPixel(x - w / 4, y - h * 2 / 3, skyBox.getPixel(x, y));
                //+X
                if (x >= w / 2 && x <= w * 3 / 4 && y >= h / 3 && y <= h * 2 / 3)
                    data[0].drawPixel(x - w / 2, y - h / 3, skyBox.getPixel(x, y));
                //-Z
                if (x >= w * 3 / 4 && y >= h / 3 && y <= h * 2 / 3)
                    data[5].drawPixel(x - w * 3 / 4, y - h / 3, skyBox.getPixel(x, y));
            }
        skyBox.dispose();
        init();
    }

    private void init() {
        shader = ShaderProvider.getSkyBoxShader();
        if (!shader.isCompiled())
            throw new GdxRuntimeException(shader.getLog());
        u_worldTrans = shader.getUniformLocation("u_worldTrans");
        quad = createQuad();
        worldTrans = new Matrix4();
        initShader();
    }

    private void initShader() {
        Gdx.gl20.glBindTexture(GL20.GL_TEXTURE_CUBE_MAP, 0);
        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL20.GL_RGB, data[0].getWidth(),
                data[0].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[0].getPixels());

        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL20.GL_RGB, data[1].getWidth(),
                data[1].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[1].getPixels());

        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL20.GL_RGB, data[2].getWidth(),
                data[2].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[2].getPixels());

        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL20.GL_RGB, data[3].getWidth(),
                data[3].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[3].getPixels());

        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL20.GL_RGB, data[4].getWidth(),
                data[4].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[4].getPixels());

        Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL20.GL_RGB, data[5].getWidth(),
                data[5].getHeight(), 0, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, data[5].getPixels());

        Gdx.gl20.glTexParameteri(GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR_MIPMAP_LINEAR);
        Gdx.gl20.glTexParameteri(GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_LINEAR);
        Gdx.gl20.glTexParameteri(GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_EDGE);
        Gdx.gl20.glTexParameteri(GL20.GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_EDGE);

        Gdx.gl20.glGenerateMipmap(GL20.GL_TEXTURE_CUBE_MAP);
    }

    public void render(Camera camera) {

        Quaternion q = new Quaternion();
        camera.view.getRotation(q, true);
        q.conjugate();

        worldTrans.idt();
        worldTrans.rotate(Vector3.X, -90);
        worldTrans.rotate(q);

        shader.begin();
        shader.setUniformMatrix(u_worldTrans, worldTrans.translate(0, 0, -1));
        quad.render(shader, GL20.GL_TRIANGLES);
        shader.end();
    }

    public Mesh createQuad() {
        Mesh mesh = new Mesh(true, 4, 6,
                VertexAttribute.Position(),
                VertexAttribute.ColorUnpacked(),
                VertexAttribute.TexCoords(0));

        mesh.setVertices(new float[]
                {
                        -1f, -1f, 0,
                        1, 1, 1,
                        1, 0, 1,
                        1f, -1f, 0,
                        1, 1, 1,
                        1, 1, 1,
                        1f, 1f, 0,
                        1, 1, 1,
                        1, 1, 0,
                        -1f, 1f, 0,
                        1, 1, 1,
                        1, 0, 0});
        mesh.setIndices(new short[]{0, 1, 2, 2, 3, 0});
        return mesh;
    }

    @Override
    public void dispose() {
        shader.dispose();
        quad.dispose();
        for (int i = 0; i < 6; i++)
            data[i].dispose();
    }
}