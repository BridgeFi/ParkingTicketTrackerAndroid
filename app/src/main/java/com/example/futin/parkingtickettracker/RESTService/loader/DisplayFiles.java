package com.example.futin.parkingtickettracker.RESTService.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;

import com.example.futin.parkingtickettracker.RESTService.loader.cache.MemoryCache;
import com.example.futin.parkingtickettracker.userInterface.util.Util;

/**
 * Created by Futin on 6/7/16.
 */
public class DisplayFiles{

    LoadFiles loadFiles;
    MemoryCache memoryCache;

    Map<ImageView, String> imageViews;
    ExecutorService executorService;
    Handler handler;

    private static final String TAG = "DisplayFiles";

    public DisplayFiles(LoadFiles loadFiles) {
        this.loadFiles = loadFiles;
        imageViews = Collections
                .synchronizedMap(new WeakHashMap<ImageView, String>());
        handler=new Handler();
        memoryCache = new MemoryCache();
        executorService= Executors.newFixedThreadPool(5);
    }

    public void displayImage(String fileName, ImageView imageView) {
        imageViews.put(imageView, fileName);
        Bitmap bitmap = memoryCache.get(fileName);
        if (bitmap != null){
            Util.getOInstance().checkOrientation(loadFiles.getFile(fileName).getPath(),imageView);
            imageView.setImageBitmap(bitmap);
            // Class with method for setting animation, that takes context where should animation
            // be displayed, imageView that displays that photo and random duration from [0,500)ms
            //new MyAnimation().setAnimationRandom(context,imageView,500,R.anim.fade_in_and_scale);
        }
        else {
            queuePhoto(fileName, imageView);
        }
    }
    /*
    * Send file and imageView where we are want to display photo to PhotoToLoad class,
    * and call our PhotosLoader through executor
    * */
    public void queuePhoto(String file, ImageView imageView) {
        imageViews.put(imageView, file);

        PhotoToLoad p = new PhotoToLoad(file, imageView);
        executorService.submit(new PhotosLoader(p));
    }
    /*
    * Method that is used for reusing imageViews and saving memory
    * */
    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        return tag == null || !tag.equals(photoToLoad.fileName);
    }
    /*
    * Used to display bitmap in the UI thread
    * */
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null) {
                Util.getOInstance().checkOrientation
                        (loadFiles.getFile(photoToLoad.fileName).getPath(),photoToLoad.imageView);

                photoToLoad.imageView.setImageBitmap(bitmap);
                // new MyAnimation().setAnimationRandom(context, photoToLoad.imageView, 250, R.anim.fade_in);
            }
        }
    }
    /*
    * Using file sent from GridViewAdapter class to decode it with imageLoader's method,
    * end return that as Bitmap.
    * */
    public Bitmap getBitmapFromFile(String file){
        File f = loadFiles.getFile(file);

        Bitmap b = decodeFile(f);
        if (b != null)
            return b;
        return null;
    }
    /*
    * The most important method, that is making sure that app does not crash and does not get
    * OutOfMemory exception. Even if that happens, Memory cache will take care of exception
    * */
    public Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 128;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (Throwable e) {
            e.printStackTrace();
            if (e instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }
    /*
    * When executor calls this class, it is checking if imageView has been reused, if not extract
    * Bitmap from file that has been sent before. Then BitmapDisplayer class simply handle
    * that process.
    * */
    class PhotosLoader implements Runnable {

        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                if (imageViewReused(photoToLoad))
                    return;
                Bitmap bmp = getBitmapFromFile(photoToLoad.fileName);
                memoryCache.put(photoToLoad.fileName, bmp);

                if (imageViewReused(photoToLoad)){
                    return;
                }
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
