package com.example.jongmin.falling.Util;

/**
 * Created by Jongmin on 2017-01-10.
 */

public class Debug {
    public static void printArr(float[] vertices){
        int i;
        for(i=0; i<vertices.length; i++){
            if(i%3 == 0){
                System.out.print((i/3) + " : ");
            }
            System.out.print(vertices[i] + " ");
            if(i%3 == 2){
                System.out.println();
            }
        }
    }
}
