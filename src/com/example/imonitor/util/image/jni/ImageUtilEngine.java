/**
 * Name        : ImageUtilEngine.java
 * Description : TODO
 */

package com.example.imonitor.util.image.jni;

public class ImageUtilEngine {

    static {
        System.loadLibrary("DecodeJni");
    }

    public native int[] decodeYUV420SP(byte[] buf, int width, int heigth);
}