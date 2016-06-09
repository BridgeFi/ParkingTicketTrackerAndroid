package com.example.futin.parkingtickettracker.userInterface.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.futin.parkingtickettracker.R;
import com.example.futin.parkingtickettracker.RESTService.listeners.FileChangeListener;
import com.example.futin.parkingtickettracker.RESTService.loader.DisplayFiles;
import com.example.futin.parkingtickettracker.RESTService.loader.LoadFiles;
import com.example.futin.parkingtickettracker.userInterface.gallery.adapters.ImagePagerAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Futin on 6/7/16.
 */
public class ImagePager extends Activity implements FileChangeListener{

    ImagePagerAdapter adapter;
    private ViewPager viewPager;
    private final String TAG="ImagePager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_layout);
        viewPager= (ViewPager) findViewById(R.id.pager);
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        LoadFiles loadFiles= (LoadFiles) i.getSerializableExtra("loadFiles");

        adapter = new ImagePagerAdapter(this,viewPager,loadFiles);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }

    @Override
    public void closeViewPager() {
        finish();
    }

}
