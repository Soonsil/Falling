package com.example.jongmin.falling.Util;

/**
 * Created by avantgarde on 2017-01-15.
 */

public class Vector2D {
    public float x;
    public float y;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // -------------------------------------------------------------
    // (vector).(func)(...)

    public void neg() {
        x = -x;
        y = -y;
    }

    public void scale(float s) {
        x *= s;
        y *= s;
    }

    public void add(Vector2D vec) {
        x += vec.x;
        y += vec.y;
    }

    // -------------------------------------------------------------
    // (func)((vector), (vector))

    public static Vector2D neg(Vector2D vec) {
        return new Vector2D(-vec.x, -vec.y);
    }

    public static Vector2D scale(Vector2D vec, float s) {
        return new Vector2D(vec.x * s, vec.y * s);
    }

    public static Vector2D add(Vector2D vec1, Vector2D vec2) {
        return new Vector2D(vec1.x + vec2.x, vec1.y + vec2.y);
    }

    public static float inner(Vector2D vec1, Vector2D vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y;
    }

    public static float cross(Vector2D vec1, Vector2D vec2) {
        return vec1.x * vec2.y - vec1.y * vec2.x;
    }

    public static Vector2D cross(Vector2D vec, float s) {
        return new Vector2D(s * vec.y, -s * vec.x);
    }

    public static Vector2D cross(float s, Vector2D vec) {
        return new Vector2D(-s * vec.y, s * vec.x);
    }
}
