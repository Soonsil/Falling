package com.example.jongmin.falling.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.jongmin.falling.Activities.MainActivity;
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
    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
                else if (count == 2 && GameEnv.dialogflag == 0){
//                    float[] rot = new float[16];
//
                    GameEnv.dialogflag = 1;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

                    // 제목셋팅
                    alertDialogBuilder.setTitle("프로그램 종료");

                    // AlertDialog 셋팅
                    alertDialogBuilder
                            .setMessage("다음 레벨로?")
                            .setCancelable(false)
                            .setPositiveButton("다음레벨",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // 프로그램을 종료한다
                                            GameEnv.level = GameEnv.level % 2 + 1;
                                            System.out.println("level : " + GameEnv.level);
                                            GameEnv.initflag = 1;
                                            GameEnv.dialogflag = 0;
                                            requestRender();
                                        }
                                    })
                            .setNeutralButton("재시작",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // 다이얼로그를 취소한다
                                            GameEnv.initflag = 1;
                                            GameEnv.dialogflag = 0;
                                            requestRender();
                                        }
                                    })
                            .setNegativeButton("취소",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {

                                            GameEnv.dialogflag = 0;
                                        }
                                    });

                    // 다이얼로그 생성
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // 다이얼로그 보여주기
                    alertDialog.show();
//                    Matrix.setIdentityM(rot, 0);
//                    Matrix.rotateM(rot, 0, dx, 0, 1, 0);
//                    Matrix.rotateM(rot, 0, dy, 1, 0, 0);
//                    Matrix.multiplyMM(temp2, 0, rot, 0, mRenderer.mViewRotationMatrix, 0);
//                    System.arraycopy(temp2, 0, mRenderer.mViewRotationMatrix, 0, 16);
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