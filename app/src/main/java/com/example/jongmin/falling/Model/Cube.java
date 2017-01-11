package com.example.jongmin.falling.Model;

import android.opengl.GLES20;

/**
 * Created by Jongmin on 2017-01-08.
 */

public class Cube extends Model {
    public Cube(){
        super();
        setVertices(GeometrySet.cubeVertices);
        setNormals(GeometrySet.cubeNormals);
        setDrawType(GLES20.GL_TRIANGLES);
        setColor(new float[]{0.0f, 0.0f, 1.0f});
        make();
    }
}
