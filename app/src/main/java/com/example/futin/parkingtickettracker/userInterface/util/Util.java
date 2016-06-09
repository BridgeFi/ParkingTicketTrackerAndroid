package com.example.futin.parkingtickettracker.userInterface.util;

import android.media.ExifInterface;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by Futin on 6/7/16.
 */
public class Util {

    private static Util utilInstance;

    public static Util getOInstance(){
        if(utilInstance == null){
            utilInstance = new Util();
        }
        return utilInstance;
    }
    /*
    * Change rotation of image depending on SDK version, so we can display every image in
    * portrait
    * */
    public void checkOrientation(String filePath, ImageView imageView){
        ExifInterface ei;
        try {
            ei = new ExifInterface(filePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            switch (orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    imageView.setRotation(90f);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    imageView.setRotation(180f);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    imageView.setRotation(270f);
                    break;
            }
        } catch (IOException e) {
           // e.printStackTrace();
        }
    }
}
