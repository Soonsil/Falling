package com.example.jongmin.falling.Util;

import android.opengl.Matrix;

import java.text.DecimalFormat;


/**
 * Created by 84395 on 10/29/2016.
 */

public class MatOperator {
    public static DecimalFormat printForm = new DecimalFormat("0.3f");

    public static float[] matLinear(float[] mat) {
        float[] translation = new float[16];
        System.arraycopy(mat, 0, translation, 0, 16);
        translation[12] = 0;
        translation[13] = 0;
        translation[14] = 0;
        return translation;
    }

    public static float[] matTranslation(float[] mat) {
        float[] transformation = new float[16];
        Matrix.setIdentityM(transformation, 0);
        transformation[12] = mat[12];
        transformation[13] = mat[13];
        transformation[14] = mat[14];
        return transformation;
    }

    public static void print(float[] mat) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(printForm.format(mat[i * 4 + j]) + " ");
            }
            System.out.println();
        }
    }

    public static void print3(float[] mat) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(printForm.format(mat[i * 4 + j]) + " ");
            }
            System.out.println();
        }
    }
}
