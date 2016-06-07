package com.example.futin.parkingtickettracker.RESTService.loader;

import android.widget.ImageView;

/**
 * Created by Futin on 6/7/16.
 */
public class PhotoToLoad {
    public String fileName;
    public ImageView imageView;

    public PhotoToLoad(String fileName, ImageView imageView) {
        this.fileName = fileName;
        this.imageView = imageView;
    }

}
