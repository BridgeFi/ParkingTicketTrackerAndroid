package com.example.futin.parkingtickettracker.RESTService.loader.cache;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Futin on 6/7/16.
 */
public class MemoryCache {

    private static final String TAG = "MemoryCache";

    // Last argument true for LRU ordering
    private Map<String, Bitmap> cache = Collections
            .synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));

    // Current allocated size
    private long size = 0;

    // Max memory in bytes
    private long limit = 1000000;

    public MemoryCache() {
        // Use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long new_limit) {
        limit = new_limit;
        Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
    }
    /*
    * Using given fileName, return Bitmap from stored cache
    * */
    public Bitmap get(String fileName) {
        try {
            if (!cache.containsKey(fileName))
                return null;
            return cache.get(fileName);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    /*
    * Place fileName into cache for later use, and calculate size on every entry
    */
    public void put(String fileName, Bitmap bitmap) {
        try {
            if (cache.containsKey(fileName))
                size -= getSizeInBytes(cache.get(fileName));
            cache.put(fileName, bitmap);
            Log.i(TAG ,cache.toString());

            size += getSizeInBytes(bitmap);
            checkSize();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
    /*
    * If size of cache is greater than its limit, this method is used to clean cache and
    * remove first item in map
    */
    private void checkSize() {
        Log.i(TAG, "cache size=" + size + " length=" + cache.size());
        if (size > limit) {
            // Least recently accessed item will be the first one iterated
            Iterator<Map.Entry<String, Bitmap>> iter = cache.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Bitmap> entry = iter.next();
                size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if (size <= limit)
                    break;
            }
            Log.i(TAG, "Clean cache. New size " + cache.size());
        }
    }
    /*
    * If Memory Overload exception is caught, clear cache memory
    * */
    public void clear() {
        try {
            cache.clear();
            size = 0;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
    /*
    * Simple method for calculation the size in Bytes of every Bitmap
    * */
    long getSizeInBytes(Bitmap bitmap) {
        if (bitmap == null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
