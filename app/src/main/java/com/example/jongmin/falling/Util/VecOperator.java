package com.example.jongmin.falling.Util;

/**
 * Created by 84395 on 10/29/2016.
 */

public class VecOperator {

    public static float getMag(float[] vector) {
        float result;
        result = (float) Math.sqrt(
                (vector[0] * vector[0]) + (vector[1] * vector[1]) + (vector[2] * vector[2]));
        return result;
    }

    public static float[] normalize(float[] vector) {
        float result[] = new float[3];
        float mag = getMag(vector);

        result[0] = vector[0];
        result[1] = vector[1];
        result[2] = vector[2];
        if (mag > 0 || mag < 0) {
            result = scale(result, 1 / mag);
        } else if (mag == 0) {
            result[0] = 0f;
            result[1] = 0f;
            result[2] = 0f;
        }

        return result;
    }

    public static float[] scale(float[] vector, float scale) {
        float result[] = new float[3];
        result[0] = vector[0] * scale;
        result[1] = vector[1] * scale;
        result[2] = vector[2] * scale;
        return result;
    }

    public static float[] add(float[] vectorA, float[] vectorB) {
        float result[] = new float[3];
        result[0] = vectorA[0] + vectorB[0];
        result[1] = vectorA[1] + vectorB[1];
        result[2] = vectorA[2] + vectorB[2];
        return result;
    }

    public static float[] sub(float[] vectorA, float[] vectorB) {
        float result[] = new float[3];
        result[0] = vectorA[0] - vectorB[0];
        result[1] = vectorA[1] - vectorB[1];
        result[2] = vectorA[2] - vectorB[2];
        return result;
    }

    public static float getDistance(float[] pointA, float[] pointB) {
        float dist = 0;

        float distXa = pointA[0];
        float distXb = pointB[0];
        float sqDistX = (distXa - distXb) * (distXa - distXb);

        float distYa = pointA[1];
        float distYb = pointB[1];
        float sqDistY = (distYa - distYb) * (distYa - distYb);

        float distZa = pointA[2];
        float distZb = pointB[2];
        float sqDistZ = (distZa - distZb) * (distZa - distZb);

        dist = (float) Math.sqrt(sqDistX + sqDistY + sqDistZ);
        return dist;
    }

    // angle between two vectors
    public static float angle(float[] vec_1, float[] vec_2) {
        float mag_1 = getMag(vec_1);
        float mag_2 = getMag(vec_2);

        if ((mag_1 == 0) || (mag_2 == 0)) {
            return 0;
        } else {
            return (float) Math.acos((double) dot(vec_1, vec_2) / (mag_1 * mag_2));
        }
    }

    public static float dot(float[] p1, float[] p2) {
        return p1[0] * p2[0] + p1[1] * p2[1] + p1[2] * p2[2];
    }

    // cross(a, b, c) -> in-place cross product
    // cross(a, b) -> return the RESULT of cross product
    public static void cross(float[] p1, float[] p2, float[] result) {
        result[0] = p1[1] * p2[2] - p2[1] * p1[2];
        result[1] = p1[2] * p2[0] - p2[2] * p1[0];
        result[2] = p1[0] * p2[1] - p2[0] * p1[1];
    }

    public static float[] cross(float[] vec_1, float[] vec_2) {
        float[] result = new float[3];
        cross(vec_1, vec_2, result);
        return result;
    }

}
