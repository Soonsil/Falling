package com.example.jongmin.falling.Model;

/**
 * Created by Jongmin on 2017-01-09.
 */

public class Point {
    public float x;
    public float y;
    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public static Point add(Point a, Point b){
        Point p = new Point(a.x + b.x, a.y + b.y);
        return p;
    }

    public static Point sub(Point a, Point b){
        Point p = new Point(a.x - b.x, a.y - b.y);
        return p;
    }

    public float[] pointToVector(){
        float[] vector = new float[3];
        vector[0] = x;
        vector[1] = y;
        vector[2] = 0.0f;
        return vector;
    }

}


