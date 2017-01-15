package com.example.jongmin.falling.Util;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by avantgarde on 2017-01-02.
 */

public class BackPressHandler {
    private long pressTime = 0;
    private long currTime = 0;
    private long diffTime = 2000;
    private Toast mToast;
    private Activity mActivity;

    public BackPressHandler(Activity context) {
        this.mActivity = context;
    }

    public void onBackPressed() {
        currTime = getTime();

        if (currTime > pressTime + diffTime) {
            pressTime = getTime();
            mToast = Toast.makeText(mActivity, "Press the key again to quit.", Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mActivity.finish();
            mToast.cancel();
        }
    }

    private long getTime() {
        return System.currentTimeMillis();
    }
}
