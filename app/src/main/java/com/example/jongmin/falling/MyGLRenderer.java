package com.example.jongmin.falling;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.jongmin.falling.Activities.MainActivity;
import com.example.jongmin.falling.Environment.GameEnv;
import com.example.jongmin.falling.Model.DirectionalPoint;
import com.example.jongmin.falling.Model.DirectionalPoint.Direction;
import com.example.jongmin.falling.Model.Model;
import com.example.jongmin.falling.Model.Point;
import com.example.jongmin.falling.Model.Square;
import com.example.jongmin.falling.Model.TextureModel;
import com.example.jongmin.falling.Util.ArrayOperator;
import com.example.jongmin.falling.Util.Debug;
import com.example.jongmin.falling.Util.VecOperator;

import org.apache.commons.io.IOUtils;

import java.io.File;
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

    private int mWidth;
    private int mHeight;

    private MainActivity activity;

    private ArrayList<Model> mModels;
    private Model mSquare;
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

//        if( file.isFile() == false ) {
//            String strBuf = ReadTextAssets("level1/map.txt");
//            String[] array;
//            array =  strBuf.split("\n");
//            for(int i=1; i<array.length; i++){
//                String[] arr = array[i].split(" ");
//                Model square = new Square(this);
//                Matrix.setIdentityM(mTempMatrix, 0);
//                Matrix.translateM(mTempMatrix, 0 , Float.parseFloat(arr[0]), Float.parseFloat(arr[1]),0.0f);
//                Matrix.scaleM(mTempMatrix, 0, Float.parseFloat(arr[2]), Float.parseFloat(arr[3]), 0.0f);
//                square.setMatrix(mTempMatrix);
//                mModels.add(square);
//            }
//        }







        // INITIALIZE LIGHTS
        mLight = new float[]{2.0f, 3.0f, 14.0f};
        mLight2 = new float[]{-2.0f, -3.0f, -5.0f};

        // INIT VIEW MATRIX
        Matrix.setIdentityM(mViewRotationMatrix, 0);
        Matrix.setIdentityM(mViewTranslationMatrix, 0);
        Matrix.translateM(mViewTranslationMatrix, 0, 0, 0, -1.001f);
        GameEnv.starttime = System.currentTimeMillis();

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glLineWidth(10.0f);

        if(GameEnv.initflag==1){
            init();
        }
        if(GameEnv.newflag == 1){
            makeNewModelByTouch();
            GameEnv.newflag = 0;
        }

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
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
//        activity.update();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mWidth = width;
        mHeight = height;

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -1;
        final float right = 1;
        final float bottom = -1.0f/ratio;
        final float top = 1.0f/ratio;
        final float near = 1.0f;
        final float far = 400.0f;

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

    public int loadShaderFromFile(int type, String fileName) {
        try {
            return loadShader(type, activity.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Bitmap loadImage(String fileName) {
        try {
            Bitmap tmp = BitmapFactory.decodeStream(activity.getAssets().open(fileName));
            android.graphics.Matrix matrix = new android.graphics.Matrix();
            matrix.preScale(1.0f, -1.0f);
            Bitmap image = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);

//            System.out.println(image.getWidth() + " " + image.getHeight());
//            System.out.println(tmp.getWidth() + " " + tmp.getHeight());

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

    public void makeNewModelByTouch(){

        Model m = new Model(this);
        m.makeShader();
        float[] vertices = new float[GameEnv.points.size() * 3];
        float[] normals = new float[GameEnv.points.size() * 3];

        int idx = 0;
        float ratio = (float) mWidth/mHeight;
        while (idx != GameEnv.points.size()){
            vertices[idx * 3] = ((GameEnv.points.get(idx).x/mWidth) * 2.0f - 1.0f);
            vertices[idx * 3 + 1] = -((GameEnv.points.get(idx).y/mHeight) * 2.0f - 1.0f)/ratio;
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
        m.makeBuffer();

        mModels.add(m);


    }
    public String ReadTextAssets(String fileName) {
        String text = null;
        try {
            InputStream is = activity.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            text = new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    public void makeModelByImageFile(TextureModel m, String filename){
        Bitmap bitmap = loadImage(filename);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int []colors = new int[width * height];
        int []below = new int[width];
        int []above = new int[width];
        int transparent = 0xFF0000FF;

        int minWidth = -1, maxWidth = -1;
        int minHeight = height, maxHeight = -1;
        bitmap.getPixels(colors, 0, width, 0, 0 , width, height);
        for (int i=0; i<width; i++){
            below[i] = -1;
            above[i] = -1;
            for(int j=0; j<height; j++){
                int color = colors[i + j * width];
                if(color != transparent){
                    if(minWidth == -1){
                        minWidth = i;
                    }
                    maxWidth = i;

                    if(below[i] == -1){
                        below[i] = j;
                    }
                    above[i] = j;

                    if(minHeight > j){
                        minHeight = j;
                    }
                    if(maxHeight < j){
                        maxHeight = j;
                    }
                }
            }
        }

        ArrayList<Point> aboves = new ArrayList<Point>();
        ArrayList<Point> belows = new ArrayList<Point>();
        ArrayList<DirectionalPoint> points = new ArrayList<DirectionalPoint>();

        aboves.add(new Point(minWidth, above[minWidth]));
        belows.add(new Point(minWidth, below[minWidth]));
        points.add(new DirectionalPoint(new Point(minWidth, above[minWidth]), Direction.above));
        points.add(new DirectionalPoint(new Point(minWidth, below[minWidth]), Direction.below));
        for(int i=minWidth + 2; i<maxWidth; i++){
            if((above[i] - above[i-1]) != (above[i-1] - above[i-2])){
                aboves.add(new Point(i-1, above[i-1]));
                points.add(new DirectionalPoint(new Point(i-1, above[i-1]), Direction.above));
            }
            if((below[i] - below[i-1]) != (below[i-1] - below[i-2])){
                belows.add(new Point(i-1, below[i-1]));
                points.add(new DirectionalPoint(new Point(i-1, below[i-1]), Direction.below));
            }
        }



        aboves.add(new Point(maxWidth - 1, above[maxWidth - 1]));
        belows.add(new Point(maxWidth - 1, below[maxWidth - 1]));
        points.add(new DirectionalPoint(new Point(maxWidth - 1, above[maxWidth - 1]), Direction.above));
        points.add(new DirectionalPoint(new Point(maxWidth - 1, below[maxWidth - 1]), Direction.below));

        System.out.println("pointsize " + points.size());

//        float[] vertices = new float[points.size() * 3];
//        float[] textures = new float[points.size() * 3];
//        for(int i=0; i<points.size(); i++){
//            Point p = new Point(points.get(i).p.x/width, points.get(i).p.y/height);
//            ArrayOperator.insertPoint(vertices, 3*i, p);
//            ArrayOperator.insertPoint(textures, 3*i, p);
//        }

        float[] vertices = new float[(points.size() - 2) * 9];
        float[] textures = new float[(points.size() - 2) * 9];
        for(int i=0; i<vertices.length; i++){
            vertices[i] = -1;
        }
        int [] indexs = new int[]{0,1,2};
        int offset = 0;
        while(indexs[2] < points.size()){
//            System.out.println("hi" + " " +  indexs[2] + " " +  points.size());
            offset = makeTriangles(vertices, offset, indexs, points);
        }
        ArrayOperator.scaleArray(vertices, (float)1/width, 0, 3);
        ArrayOperator.scaleArray(vertices, (float)1/height, 1, 3);
        System.arraycopy(vertices, 0, textures, 0 ,vertices.length);

        float[][] borders = new float[aboves.size() + belows.size()][2];
        for(int i=0; i<aboves.size(); i++){
            Point p = aboves.get(i);
            borders[i][0] = p.x;
            borders[i][1] = p.y;
        }
        for(int i=0; i<belows.size(); i++){
            Point p = belows.get(i);
            borders[aboves.size() + belows.size() - i - 1][0] = p.x;
            borders[aboves.size() + belows.size() - i - 1][1] = p.y;
        }

        int vwidth = maxWidth - minWidth;
        int vheight = maxHeight - minHeight;
        System.out.println(minWidth + " " + maxWidth + " " + width + " " + height);


        ArrayOperator.addArray(vertices, (float)-minWidth/width, 0, 3);
        ArrayOperator.addArray(vertices, (float)-minHeight/height, 1, 3);;
        ArrayOperator.scaleArray(vertices, (float)width / vwidth * 2.0f, 0, 3);
        ArrayOperator.scaleArray(vertices, (float)height / vheight * 2.0f, 1, 3);
        ArrayOperator.addArray(vertices, -1.0f);
        ArrayOperator.scaleArray(vertices, (float)vheight / vwidth, 1, 3);

        ArrayOperator.addArray(vertices, 1.0f, 2 , 3);
        System.out.println("---texture---");
        Debug.printvert(textures);
        System.out.println("---vertice---");

        Debug.printvert(vertices);

        m.setVertices(vertices);
        m.setTextureCoords(textures);
//        m.setDrawType(GLES20.GL_LINE_STRIP);
        m.makeBuffer();
    }

    public int makeTriangles(float[] vertices, int offset, int[] indexs, ArrayList<DirectionalPoint> points){
//        System.out.println(offset + " " + indexs[0] + " " + indexs[1] + " " + indexs[2] + " " + points.size() + " " +  vertices.length);
        DirectionalPoint a = points.get(indexs[0]);
        DirectionalPoint b = points.get(indexs[1]);
        DirectionalPoint c = points.get(indexs[2]);
//        System.out.println("(" + a.p.x + " " + a.p.y + ") (" + b.p.x + " " + b.p.y + ") (" + c.p.x + " " + c.p.y + ")");


        Point ba = Point.sub(b.p, a.p);
        Point cb = Point.sub(c.p, b.p);
        float[] normal = new float[3];
        VecOperator.cross(ba.pointToVector(), cb.pointToVector(), normal);


        if(b.d == c.d){
            if(normal[2] < 0){
                int offset2 = offset + 9;
                int newc = indexs[2];
                int [] newindexs = new int[]{indexs[1], indexs[2], indexs[2]+1};
                while((normal[2] < 0 && a.d == Direction.above) || (normal[2] > 0 && a.d == Direction.below)) {
                    offset2 = makeTriangles(vertices, offset2, newindexs, points);
                    newc = newindexs[1];
                    c = points.get(newc);
                    cb = Point.sub(c.p, b.p);
                    VecOperator.cross(ba.pointToVector(), cb.pointToVector(), normal);
//                    System.out.println();
                    //조치가 필요
//                    System.out.println(indexs[0] + " " + indexs[1] + " " + newc + " " + normal[2]);
                }
                makeTriangleThreePoints(a.p, b.p, c.p, vertices, offset);
                offset = offset2;

                indexs[1] = newindexs[1];
                indexs[2] = newindexs[2];

                if(b.d != c.d){
                    indexs[0] = newindexs[0];
                }
                return offset;
            }
            else{
                if(vertices[offset + 2] == -1) {
                    makeTriangleThreePoints(a.p, b.p, c.p, vertices, offset);
                }
                int ret = indexs[2];
                indexs[1] = indexs[2];
                indexs[2] = indexs[2] + 1;
                offset = offset + 9;

                return offset;
            }
        }
        else{
            if(vertices[offset + 2] == -1) {
                makeTriangleThreePoints(a.p, b.p, c.p, vertices, offset);
            }
            int ret = indexs[2];
            indexs[0] = indexs[1];
            indexs[1] = indexs[2];
            indexs[2] = indexs[2]+1;
            offset = offset + 9;

            return offset;
        }
    }
    public int makeTriangles(float[] vertices, int offset, int aindex, int bindex, int cindex, ArrayList<DirectionalPoint> points){
        System.out.println(aindex + " " + bindex + " " + cindex + " " + offset + " " + points.size() + " " + vertices.length);

        if(cindex >= points.size()){
            return 0;
        }
        DirectionalPoint a = points.get(aindex);
        DirectionalPoint b = points.get(bindex);
        DirectionalPoint c = points.get(cindex);


        Point ba = Point.sub(b.p, a.p);
        Point cb = Point.sub(c.p, b.p);
        float[] normal = new float[3];
        VecOperator.cross(ba.pointToVector(), cb.pointToVector(), normal);


        if(b.d == c.d){
            if(normal[2] < 0){
                int offset2 = offset;
                int newc = cindex;
                while(normal[2] < 0) {
                    offset2 = offset2 + 9;
                    newc = makeTriangles(vertices, offset2, bindex, cindex, cindex + 1, points);
                    c = points.get(newc);
                    cb = Point.sub(c.p, b.p);
                    VecOperator.cross(ba.pointToVector(), cb.pointToVector(), normal);
                    //조치가 필요
                }
                makeTriangleThreePoints(a.p, b.p, c.p, vertices, offset);
                return newc;

            }
            else{
                if(vertices[offset + 2] != -1) {
                    makeTriangleThreePoints(a.p, b.p, c.p, vertices, offset);
                }
//                System.out.println(aindex + " " + cindex + " " + (cindex + 1) + " " + (offset+9) + " " + points.size() + " " + vertices.length);

                makeTriangles(vertices, offset + 9, aindex, cindex, cindex + 1, points);
                return cindex + 1;
            }
        }
        else{
            if(vertices[offset + 2] != -1) {
                makeTriangleThreePoints(a.p, b.p, c.p, vertices, offset);
            }

            makeTriangles(vertices, offset + 9, bindex, cindex, cindex + 1, points);
            return cindex + 1;
        }
    }

    public void makeTriangleThreePoints(Point a, Point b, Point c, float[] vertices, int offset){
        Point ba = Point.sub(b, a);
        Point cb = Point.sub(c, b);
        float[] normal = new float[3];
        VecOperator.cross(ba.pointToVector(), cb.pointToVector(), normal);
        if(normal[2] > 0){
            ArrayOperator.insertPoint(vertices, offset + 0, a);
            ArrayOperator.insertPoint(vertices, offset + 3, b);
            ArrayOperator.insertPoint(vertices, offset + 6, c);
        }
        else{
            ArrayOperator.insertPoint(vertices, offset + 0, c);
            ArrayOperator.insertPoint(vertices, offset + 3, b);
            ArrayOperator.insertPoint(vertices, offset + 6, a);

        }
    }
    public void init(){
        System.out.println("init!!!");
        mModels.clear();
        String levelfile = "level" +  GameEnv.level + "/map.txt";
//            File file = activity.getFileStreamPath(levelfile);
//
//            if( file.isFile() == false ) {
        String strBuf = ReadTextAssets(levelfile);
        String[] array;
        array =  strBuf.split("\n");
        for(int i=1; i<array.length; i++){
            String[] arr = array[i].split(" ");
            TextureModel square = new TextureModel(this, i);
            String folder = "level" + GameEnv.level;
//            System.out.println("folder: " + folder);
            String imagefilename = folder + "/brick" + i + ".png";
            square.setTextureFileName(imagefilename);
            square.makeShader();
            makeModelByImageFile(square, imagefilename);
//            System.out.println(imagefilename);
            float ratio = (float)mWidth/mHeight;
            Matrix.setIdentityM(mTempMatrix, 0);
            float[] transMatrix = new float[16];
            float[] scaleMatrix = new float[16];

            Matrix.translateM(mTempMatrix, 0 , Float.parseFloat(arr[0]), Float.parseFloat(arr[1])/ratio,0.0f);
            System.arraycopy(mTempMatrix, 0, transMatrix, 0 , 16);
            Matrix.setIdentityM(mTempMatrix, 0);

            Matrix.scaleM(mTempMatrix, 0, Float.parseFloat(arr[2]), Float.parseFloat(arr[3]), 1.0f);
            System.arraycopy(mTempMatrix, 0 , scaleMatrix, 0 , 16);
            Matrix.multiplyMM(mTempMatrix, 0, transMatrix, 0 , scaleMatrix, 0);
//            Matrix.multiplyMM(mTempMatrix, 0, scaleMatrix, 0 , transMatrix, 0);
            square.setMatrix(mTempMatrix);
            mModels.add(square);
        }
//            }
        GameEnv.initflag = 0;
    }
}