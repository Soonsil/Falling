package com.example.jongmin.falling.Model;

import android.opengl.GLES20;

/**
 * Created by avantgarde on 2017-01-15.
 */

public class Circle extends Model {
    private float radius;

    public Circle(float radius, int fanCount) {
        super();

        this.radius = radius;

        setVertices(GeometrySet.circleVertices(radius, fanCount));
        setNormals(GeometrySet.circleNormals(radius, fanCount));
        setDrawType(GLES20.GL_TRIANGLES);
        setColor(new float[]{0.0f, 0.0f, 1.0f});

        make();
    }

    public Circle(float radius) {
        this(radius, 100);
    }
}
