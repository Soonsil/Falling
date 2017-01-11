package com.example.jongmin.falling.Acitivities;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.example.jongmin.falling.R;
import com.example.jongmin.falling.View.MyGLSurfaceView;


public class MainActivity extends Activity {
    public static Context context;
    public static MyGLSurfaceView mGLView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        // Prevent android being dark
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Make it as full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);

        final ToggleButton myButton1 = new ToggleButton(this);
        myButton1.setBackgroundColor(0);
        final ToggleButton myButton2 = new ToggleButton(this);
        myButton2.setBackgroundColor(0);

        setButtonText(myButton1, "안녕하세요");
        setButtonText(myButton2, "감사해요");
        buttonLayout.addView(myButton1);
        buttonLayout.addView(myButton2);

        LinearLayout.LayoutParams glParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        glParams.weight = 1;
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layout.addView(buttonLayout, buttonParams);
        layout.addView(mGLView, glParams);
        setContentView(layout);

    }

    private void setButtonText(ToggleButton button, String text) {
        button.setText(text);
        button.setTextSize(17f);
        button.setTextOn(text);
        button.setTextOff(text);
    }

    // reference : https://developer.android.com/guide/topics/sensors/sensors_motion.html


    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}


/*
public class MainActivity extends AppCompatActivity {
    public static Context context;
    private MyGLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        mGLView = new MyGLSurfaceView(this);

        setContentView(R.layout.activity_main);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);

        ToggleButton worldButton = new ToggleButton(this);
        ToggleButton cube1Button = new ToggleButton(this);

        setButtonText(worldButton, "World");
        setButtonText(cube1Button, "Picking");

        buttonLayout.addView(worldButton);
        buttonLayout.addView(cube1Button);

        final ToggleButton[] buttons = {worldButton, cube1Button};

        for (int i = 0; i < buttons.length; i++) {
            final ToggleButton button = buttons[i];

            final int finalI = i;
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (ToggleButton toggleButton : buttons) {
                            if (toggleButton != buttonView) {
                                toggleButton.setChecked(false);
                            }
                            //mGLView.recalculateArcballTranslation();
                            mGLView.requestRender();
                        }
                    }
                }
            });
        }
        worldButton.setChecked(true);

        LinearLayout.LayoutParams glParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        glParams.weight = 1;

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        layout.addView(buttonLayout, buttonParams);
        layout.addView(mGLView, glParams);

        setContentView(layout);

    }
    private void setButtonText(ToggleButton button, String text) {
        button.setText(text);
        button.setTextOn(text);
        button.setTextOff(text);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.

        mGLView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}

*/
/*
package com.example.jongmin.falling.Acitivities;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

@SuppressLint("DrawAllocation")
public class MainActivity extends Activity {
    class Point {
        float x;
        float y;
        boolean isDraw;
        public Point(float x, float y, boolean isDraw) {
            this.x = x;
            this.y = y;
            this.isDraw = isDraw;
        }
    }
    class MyPaint extends View {
        public MyPaint(Context context) {
            super(context);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            this.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch( event.getAction() ) {
                        case MotionEvent.ACTION_MOVE:
                            points.add(new Point(event.getX(), event.getY(), true));
                            System.out.println(event.getX() + " " + event.getY());
                            invalidate();
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_DOWN:
                            points.add(new Point(event.getX(), event.getY(), false));
                    }
                    return true;
                }
            });
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint p = new Paint();
            p.setColor(Color.MAGENTA);
            p.setStrokeWidth(3);
            for(int i=1; i<points.size(); i++) {
                if(!points.get(i).isDraw) continue;
                canvas.drawLine(points.get(i-1).x, points.get(i-1).y, points.get(i).x, points.get(i).y, p);
            }
        }
    }
    ArrayList<Point> points = new ArrayList<Point>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyPaint mp = new MyPaint(this);
        setContentView(mp);
    }
}
*/