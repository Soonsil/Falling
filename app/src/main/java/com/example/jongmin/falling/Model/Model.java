package com.example.jongmin.falling.Model;

import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.example.jongmin.falling.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Jongmin on 2017-01-02.
 */

public class Model {

    private int mProgram;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mNormalBuffer;
    private FloatBuffer mTextureCoorBuffer;

    // attribute handles
    private int mPositionHandle;
    private int mNormalHandle;
    private int mTextureHandle;
    private int mTextureCoorHandle;

    // uniform handles
    private int mProjMatrixHandle;
    private int mModelViewMatrixHandle;
    private int mNormalMatrixHandle;

    private int mLightHandle;
    private int mColorHandle;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    protected float color[] = new float[]{0.0f, 1.0f, 0.0f};
    protected float vertices[];
    protected float normals[];
    protected float textureCoords[];

    private float modelMatrix[] = new float[16];
    private float normalMatrix[] = new float[16];

    private String vshader = "basic-gl2-vshader.glsl";
    private String fshader = "basic-gl2-fshader.glsl";
    private MyGLRenderer mRenderer;

    private int drawtype = GLES20.GL_TRIANGLES;

    private  int useNormal = 0;
    private  int useTexture = 0;

    public Model(MyGLRenderer mRenderer){
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.setIdentityM(normalMatrix, 0);
        this.mRenderer = mRenderer;
    }

    public void setUseNormal(int useNormal){
        this.useNormal = useNormal;
    }
    public void setUseTexture(int useTexture){
        this.useTexture = useTexture;
    }

    public void setVertices(float[] vertices){
        this.vertices = new float[vertices.length];
        System.arraycopy(vertices, 0, this.vertices, 0, vertices.length);
    }

    public void setNormals(float[] normals){
        if(useNormal == 1) {
            this.normals = new float[normals.length];
            System.arraycopy(normals, 0, this.normals, 0, normals.length);
        }
    }

    public void setTextureCoords(float[] textureCoords){
        if(useTexture == 1) {
            this.textureCoords = new float[textureCoords.length];
            System.arraycopy(textureCoords, 0, this.textureCoords, 0, textureCoords.length);
        }
    }
    public void setMatrix(float[] mat){
        System.arraycopy(mat, 0, modelMatrix, 0, 16);
    }

    public void setShader(String vshader, String fshader){
        this.vshader = vshader;
        this.fshader = fshader;
    }

    public void setDrawType(int drawtype){
        this.drawtype = drawtype;
    }

    public void setColor(float[] color){
        this.color[0] = color[0];
        this.color[1] = color[1];
        this.color[2] = color[2];
    }

    public void make(){
        makeBuffer();
        makeShader();
    }

    public void makeBuffer(){
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        if(useNormal == 1) {
            byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
            byteBuf.order(ByteOrder.nativeOrder());
            mNormalBuffer = byteBuf.asFloatBuffer();
            mNormalBuffer.put(normals);
            mNormalBuffer.position(0);
        }
        if(useTexture == 1){
            byteBuf = ByteBuffer.allocateDirect(textureCoords.length * 4);
            byteBuf.order(ByteOrder.nativeOrder());
            mTextureCoorBuffer = byteBuf.asFloatBuffer();
            mTextureCoorBuffer.put(textureCoords);
            mTextureCoorBuffer.position(0);
        }
    }

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


        if(useTexture == 1) {
            int[] textureHandles = new int[1];
            GLES20.glGenTextures(1, textureHandles, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0]);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mRenderer.loadImage("brick.png"), 0);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        }
    }


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

        if(useNormal == 1) {
            mNormalMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uNormalMatrix");
            GLES20.glUniformMatrix4fv(mNormalMatrixHandle, 1, false, normalMatrix, 0);
            GLES20.glEnableVertexAttribArray(mNormalHandle);
            GLES20.glVertexAttribPointer(
                    mNormalHandle, COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT, false,
                    VERTEX_STRIDE, mNormalBuffer);
        }
        GLES20.glUniform3fv(mColorHandle, 1, color, 0);
//        GLES20.glUniform3fv(mLightHandle, 1, light, 0);

        // attributes
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");

        if(useTexture == 1) {
            mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTextureUnit");
            mTextureCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
            GLES20.glEnableVertexAttribArray(mTextureCoorHandle);
            GLES20.glVertexAttribPointer(
                    mTextureCoorHandle, COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT, false,
                    VERTEX_STRIDE, mTextureCoorBuffer
            );
        }

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
        if(useTexture == 1) {
            GLES20.glDisableVertexAttribArray(mTextureHandle);
        }
    }
}
