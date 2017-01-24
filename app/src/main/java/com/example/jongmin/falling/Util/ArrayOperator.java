package com.example.jongmin.falling.Util;

import com.example.jongmin.falling.Model.Point;

/**
 * Created by Jongmin on 2017-01-20.
 */

public class ArrayOperator {
    public static void scaleArray(float[] array, float scale){
        for(int i=0; i<array.length; i++){
            array[i] = array[i]*scale;
        }
    }

    public static void scaleArray(float[] array, float scale, int offset, int interval){
        for(int i=offset; i<array.length; i = i + interval){
            array[i] = array[i]*scale;
        }
    }
    public static void addArray(float[] array, float num){
        for(int i=0; i<array.length; i++){
            array[i] = array[i] + num;
        }
    }
    public static void insertPoint(float[] array, int offset, Point p){
        array[offset] = p.x;
        array[offset+1] = p.y;
        array[offset+2] = 0.0f;
    }
}
