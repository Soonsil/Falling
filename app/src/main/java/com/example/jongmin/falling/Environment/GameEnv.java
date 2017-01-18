package com.example.jongmin.falling.Environment;

import com.example.jongmin.falling.Model.Point;

import java.util.ArrayList;

/**
 * Created by Jongmin on 2017-01-11.
 */
public class GameEnv {
    private static GameEnv ourInstance = new GameEnv();

    public static int newflag;
    public static int initflag;
    public static int level;
    public static ArrayList<Point> points;
    public static long starttime;
    public static int dialogflag;

    public static GameEnv getInstance() {
        return ourInstance;
    }

    private GameEnv() {
        newflag = 0;
        initflag = 1;
        level = 1;
        points = new ArrayList<Point>();
        starttime = 0;
        dialogflag = 0;
    }
}
