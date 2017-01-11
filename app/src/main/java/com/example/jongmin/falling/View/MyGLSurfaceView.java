package com.example.jongmin.falling.View;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.example.jongmin.falling.Acitivities.MainActivity;
import com.example.jongmin.falling.Environment.GameEnv;
import com.example.jongmin.falling.Model.Point;
import com.example.jongmin.falling.MyGLRenderer;

import java.util.ArrayList;


/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    public static MyGLRenderer mRenderer;

    private float mPreviousX;
    private float mPreviousY;

    private float[] temp1 = new float[16];
    private float[] temp2 = new float[16];

    public int mode;
    private ArrayList<Point> points = new ArrayList<Point>();
    private MainActivity activity;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        int count = e.getPointerCount();
        int action = e.getAction();

        float x = e.getX();
        float y = e.getY();

        if (count == 2) {
            x = (x + e.getX(1)) / 2;
            y = (y + e.getY(1)) / 2;
        }

        float dx = Math.max(Math.min(x - mPreviousX, 10f), -10f);
        float dy = Math.max(Math.min(y - mPreviousY, 10f), -10f);


        switch (action) {
            case MotionEvent.ACTION_DOWN: // 누를때
                points.clear();
                break;
            case MotionEvent.ACTION_UP:
                GameEnv.newflag = 1;
                GameEnv.points = points;
                break;
            case MotionEvent.ACTION_MOVE:
                if (count == 1){
                    Point p = new Point(e.getX(), e.getY());
//                    System.out.println(e.getX() + " " + e.getY());
                    points.add(p);
                }
                else if (count == 2){
                    float[] rot = new float[16];

                    Matrix.setIdentityM(rot, 0);
                    Matrix.rotateM(rot, 0, dx, 0, 1, 0);
                    Matrix.rotateM(rot, 0, dy, 1, 0, 0);
                    Matrix.multiplyMM(temp2, 0, rot, 0, mRenderer.mViewRotationMatrix, 0);
                    System.arraycopy(temp2, 0, mRenderer.mViewRotationMatrix, 0, 16);
                }
                break;
        }
        // TOUCH DOWN & UP -> HINT MODE ON / OFF (MAP BLENDING ON / OFF)

        mPreviousX = x;
        mPreviousY = y;

        requestRender();
        return true;
    }
    public void setActivity(MainActivity activity){
        this.activity = activity;
        this.mRenderer.setActivity(activity);
    }

}