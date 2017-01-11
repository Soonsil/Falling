package com.example.jongmin.falling;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.jongmin.falling.Acitivities.MainActivity;
import com.example.jongmin.falling.Environment.GameEnv;
import com.example.jongmin.falling.Model.Cube;
import com.example.jongmin.falling.Model.GeometrySet;
import com.example.jongmin.falling.Model.Model;
import com.example.jongmin.falling.Model.Point;
import com.example.jongmin.falling.Util.Debug;
import com.example.jongmin.falling.Util.MatOperator;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    // TAG
    private static final String TAG = "MyGLRenderer";

    // DECLARE LIGHTS
    private float[] mLight = new float[3];
    private float[] mLight2 = new float[3];

    // DECLARE POSITION OF CAMERA
    float[] mCamera = new float[3];

    // MATRICES FOR VIEW
    private float[] mViewMatrix = new float[16];
    public float[] mViewRotationMatrix = new float[16];
    public float[] mViewTranslationMatrix = new float[16];

    private float[] mProjMatrix = new float[16];
    private float[] mTempMatrix = new float[16];

    private int width;
    private int height;

    private MainActivity activity;

    private ArrayList<Model> mModels;
    private int flag = 0;
    @Override
    // CALLED WHEN SURFACE IS CREATED AT FIRST.
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // SET BACKGROUND COLOR
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        resetViewMatrix();

        // INIT BUBBLE
//        mCube = new Model();
//        mCube.setVertices(GeometrySet.cubeVertices);
//        mCube.setNormals(GeometrySet.cubeNormals);
//        mCube.setDrawType(GLES20.GL_LINE_STRIP);
//        mCube.setColor(new float[]{0.0f, 0.0f, 1.0f});
//        mCube.make();
        mModels = new ArrayList<Model>();

        // INITIALIZE LIGHTS
        mLight = new float[]{2.0f, 3.0f, 14.0f};
        mLight2 = new float[]{-2.0f, -3.0f, -5.0f};

        // INIT VIEW MATRIX
        Matrix.setIdentityM(mViewRotationMatrix, 0);
        Matrix.setIdentityM(mViewTranslationMatrix, 0);
        Matrix.translateM(mViewTranslationMatrix, 0, 0, 0, -1.0001f);

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glLineWidth(10.0f);
        if(GameEnv.newflag == 1){
            Model m = new Model();
            m.makeShader();
            makeNewModelByTouch(m);
            GameEnv.newflag = 0;
        }
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Calculate view matrix
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mViewRotationMatrix, 0, mViewMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mViewMatrix, 0, 16);

        Matrix.multiplyMM(mTempMatrix, 0, mViewTranslationMatrix, 0, mViewMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mViewMatrix, 0, 16);

        // Calculate Cube ModelMatrix
        float scale = 0.4f;

        // Calculate ModelViewMatrix

        // Calculate NormalMatrix

        // Draw
//        mCube.draw(mProjMatrix, mViewMatrix);
//        for (Model m:mModels) {
//            m.draw(mProjMatrix, mViewMatrix);
//        }

        for(Model m : mModels){
            m.draw(mProjMatrix, mViewMatrix);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        this.width = width;
        this.height = height;

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        System.out.println(width + " " + height);
        final float left = -1;
        final float right = 1;
        final float bottom = -1.0f/ratio;
        final float top = 1.0f/ratio;
        final float near = 1.0f;
        final float far = 100.0f;

        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    private void normalMatrix(float[] dst, int dstOffset, float[] src, int srcOffset) {
        Matrix.invertM(dst, dstOffset, src, srcOffset);
        dst[12] = 0;
        dst[13] = 0;
        dst[14] = 0;

        float[] temp = Arrays.copyOf(dst, 16);
        Matrix.transposeM(dst, dstOffset, temp, 0);
    }

    private void resetViewMatrix() {
        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 4f;

        mCamera[0] = eyeX;
        mCamera[1] = eyeY;
        mCamera[2] = eyeZ;
        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -1.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    // Utility method for compiling a OpenGL shader.
    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public static int loadShader(int type, InputStream shaderFile) {
        String shaderCode = null;
        try {
            shaderCode = IOUtils.toString(shaderFile, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadShader(type, shaderCode);
    }

    public static int loadShaderFromFile(int type, String fileName) {
        try {
            return loadShader(type, MainActivity.context.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Bitmap loadImage(String fileName) {
        try {
            Bitmap tmp = BitmapFactory.decodeStream(MainActivity.context.getAssets().open(fileName));
            android.graphics.Matrix matrix = new android.graphics.Matrix();
            matrix.preScale(1.0f, -1.0f);
            Bitmap image = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
            tmp.recycle();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     * <p>
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     * <p>
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
    public void setActivity(MainActivity activity){
        this.activity = activity;
    }

    public void makeNewModelByTouch(Model m){
        float[] vertices = new float[GameEnv.points.size() * 3];
        float[] normals = new float[GameEnv.points.size() * 3];

        int idx = 0;
        float ratio = (float) width/height;
        System.out.println(ratio);
        while (idx != GameEnv.points.size()){
            vertices[idx * 3] = ((GameEnv.points.get(idx).x/width) * 2.0f - 1.0f);
            vertices[idx * 3 + 1] = -((GameEnv.points.get(idx).y/height) * 2.0f - 1.0f)/ratio;
            vertices[idx * 3 + 2] = 0.0f;
            //System.out.println(points.get(idx).x/width + " " + points.get(idx).y/height);
//            System.out.println((idx * 3 + 2)+ " " + vertices.length);
            normals[idx * 3] = 0.0f;
            normals[idx * 3 + 1] = 0.0f;
            normals[idx * 3 + 2] = 0.0f;
            idx++;
        }

        m.setVertices(vertices);
        m.setNormals(normals);
        m.setDrawType(GLES20.GL_LINE_STRIP);
        m.setColor(new float[]{1.0f, 0.0f, 0.0f});
//        Debug.printArr(vertices);
//        Debug.printArr(normals);
        m.makeBuffer();

        mModels.add(m);

        
        
//        line.setVertices(GeometrySet.cubeVertices);
//        line.setNormals(GeometrySet.cubeNormals);
//        line.setDrawType(GLES20.GL_LINE_STRIP);
//        line.setColor(new float[]{1.0f, 0.0f, 0.0f});
////        Debug.printArr(vertices);
////        Debug.printArr(normals);
//        line.make();
//        mModels.add(line);
    }

}