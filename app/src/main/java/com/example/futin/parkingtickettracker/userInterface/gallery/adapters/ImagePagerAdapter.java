package com.example.futin.parkingtickettracker.userInterface.gallery.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.futin.parkingtickettracker.R;
import com.example.futin.parkingtickettracker.RESTService.listeners.FileChangeListener;
import com.example.futin.parkingtickettracker.RESTService.loader.LoadFiles;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Futin on 6/7/16.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private final String TAG="ImagePagerAdapter";
    Context context;
    LayoutInflater inflater;
    ViewPager viewPager;
    FileChangeListener listener;
    LoadFiles loadFiles;
    int size=0;

    public ImagePagerAdapter(Context context, ViewPager viewPager, LoadFiles loadFiles) {
        this.context = context;
        this.viewPager = viewPager;
        this.loadFiles=loadFiles;
        listener= (FileChangeListener) context;
        size=getFileSize();

    }

    private int getFileSize(){
            return loadFiles.getListSize();
    }

    @Override
    public int getCount() {
        Log.i(TAG,"size: "+size);
        return size;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView;

        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.single_image_pager, container, false);

        imageView= (ImageView) viewLayout.findViewById(R.id.singleImagePagerView);
        Bitmap bitmap = BitmapFactory.decodeFile(loadFiles.getFileFromList(position).getPath());
        imageView.setImageBitmap(bitmap);

        container.addView(viewLayout);

        return viewLayout;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    void makeToast(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


}
