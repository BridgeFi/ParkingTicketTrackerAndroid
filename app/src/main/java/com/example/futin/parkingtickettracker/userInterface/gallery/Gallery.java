package com.example.futin.parkingtickettracker.userInterface.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.example.futin.parkingtickettracker.R;
import com.example.futin.parkingtickettracker.RESTService.loader.LoadFiles;
import com.example.futin.parkingtickettracker.userInterface.gallery.adapters.GridViewAdapter;

public class Gallery extends Activity {

    GridView gridView;
    GridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gridView = (GridView) findViewById(R.id.grid_view);
        initAdapter();
    }

    private void initAdapter(){
        gridViewAdapter=new GridViewAdapter(this);
        gridView.setAdapter(gridViewAdapter);
    }
}
