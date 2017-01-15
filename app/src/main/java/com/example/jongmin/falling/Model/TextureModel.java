package com.example.jongmin.falling.Model;

import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.example.jongmin.falling.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Jongmin on 2017-01-15.
 */

public class TextureModel extends Model {
    private int mTextureHandle;
    private int mTextureCoorHandle;

    protected float textureCoords[];
    public TextureModel(MyGLRenderer mRenderer){
        super(mRenderer);
        setShader("texture-gl2-vshader.glsl", "texture-gl2-fshader.glsl");
    }
    public void setTextureCoords(float[] textureCoords){
        this.textureCoords = new float[textureCoords.length];
        System.arraycopy(textureCoords, 0, this.textureCoords, 0, textureCoords.length);
    }
    @Override
    public void makeBuffer(){
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(textureCoords.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mTextureCoorBuffer = byteBuf.asFloatBuffer();
        mTextureCoorBuffer.put(textureCoords);
        mTextureCoorBuffer.position(0);
    }

    @Override
    public void makeShader(){
        // prepare shaders and OpenGL program
        int vertexShader = mRenderer.loadShaderFromFile(
                GLES20.GL_VERTEX_SHADER, vshader);
        int fragmentShader = mRenderer.loadShaderFromFile(
                GLES20.GL_FRAGMENT_SHADER, fshader);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        System.out.println("program " + mProgram +" " +vertexShader +" " + fragmentShader);
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables


        int[] textureHandles = new int[1];
        GLES20.glGenTextures(1, textureHandles, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mRenderer.loadImage("brick.png"), 0);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
    }

    @Override
    public void draw(float[] projMatrix,
                     float[] viewMatrix) {

        GLES20.glUseProgram(mProgram);
        float[] modelViewMatrix = new float[16];
        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
//        MatOperator.print(modelViewMatrix);
//        MatOperator.print(projMatrix);
        // uniforms
        mProjMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uProjMatrix");
        mModelViewMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uModelViewMatrix");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");

        //mLightHandle = GLES20.glGetUniformLocation(mProgram, "uLight");

        GLES20.glUniformMatrix4fv(mProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniformMatrix4fv(mModelViewMatrixHandle, 1, false, modelViewMatrix, 0);

        GLES20.glUniform3fv(mColorHandle, 1, color, 0);
//        GLES20.glUniform3fv(mLightHandle, 1, light, 0);

        // attributes
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");


        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTextureUnit");
        mTextureCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        GLES20.glEnableVertexAttribArray(mTextureCoorHandle);
        GLES20.glVertexAttribPointer(
                mTextureCoorHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mTextureCoorBuffer
        );

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mVertexBuffer);



        System.out.println(drawtype + " " + vertices.length);
        // Draw the cube
        GLES20.glDrawArrays(drawtype, 0, vertices.length / 3);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        if(useNormal == 1) {
            GLES20.glDisableVertexAttribArray(mNormalHandle);
        }

        GLES20.glDisableVertexAttribArray(mTextureHandle);

    }
}
