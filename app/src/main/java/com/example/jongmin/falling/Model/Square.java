package com.example.jongmin.falling.Model;

import android.opengl.GLES20;

import com.example.jongmin.falling.MyGLRenderer;

/**
 * Created by Jongmin on 2017-01-08.
 */

public class Square extends Model {
    public Square(MyGLRenderer mRenderer){
        super(mRenderer);
        setVertices(GeometrySet.squareVertices);
        setNormals(GeometrySet.squareNormals);
        setDrawType(GLES20.GL_TRIANGLES);
        setUseTexture(1);
        setTextureCoords(GeometrySet.squaretextureCoords);
        setShader("texture-gl2-vshader.glsl", "texture-gl2-fshader.glsl");
//        setDrawType(GLES20.GL_LINE_STRIP);
        setColor(new float[]{1.0f, 0.0f, 1.0f});
        make();
    }
}
