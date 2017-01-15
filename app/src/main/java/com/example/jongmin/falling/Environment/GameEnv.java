package com.example.jongmin.falling.Environment;

import com.example.jongmin.falling.Model.Point;
import com.example.jongmin.falling.Util.Vector2D;

import java.util.ArrayList;

/**
 * Created by Jongmin on 2017-01-11.
 */
public class GameEnv {
    private static GameEnv ourInstance = new GameEnv();

    public static int newflag;
    public static ArrayList<Point> points;

    public static Vector2D gravity;

    public static GameEnv getInstance() {
        return ourInstance;
    }

    private GameEnv() {
        newflag = 0;
        points = new ArrayList<Point>();
        gravity = new Vector2D(0.0f, -0.5f);
    }
}
