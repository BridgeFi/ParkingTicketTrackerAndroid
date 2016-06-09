package com.example.futin.parkingtickettracker.userInterface.gallery.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.futin.parkingtickettracker.R;
import com.example.futin.parkingtickettracker.RESTService.loader.DisplayFiles;
import com.example.futin.parkingtickettracker.RESTService.loader.LoadFiles;
import com.example.futin.parkingtickettracker.userInterface.gallery.ImagePager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Futin on 6/7/16.
 */
public class GridViewAdapter extends BaseAdapter {

    DisplayFiles displayFiles;
    LoadFiles loadFiles;

    Context context;
    LayoutInflater inflater;
    ViewHolder viewHolder;

    final String TAG="GridViewAdapter";
    ArrayList<String>listOfFiles;

    public GridViewAdapter( Context context) {
        this.context = context;
        loadFiles=new LoadFiles();
        displayFiles=new DisplayFiles(loadFiles);
        listOfFiles=loadFiles.getListOfFiles();
    }

    @Override
    public int getCount() {
        if(listOfFiles.size() == 0){
            Toast.makeText(context, "No files to display",Toast.LENGTH_LONG).show();
            Log.i(TAG,"No files to display");
        }
            return listOfFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "get view method");
        if(convertView == null){

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_image, parent, false);
            viewHolder=new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.singleImageView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
            loadFromDisc(viewHolder.imageView, position);

        viewHolder.imageView.setOnClickListener(new OnImageClickListener(position,loadFiles));
        return convertView;
    }

    void loadFromDisc(ImageView image, int position){
        String file;
        Log.i(TAG, "Load from disc");

        if(!listOfFiles.isEmpty()){
            file=listOfFiles.get(position);
            displayFiles.displayImage(file,image);
        }
    }
    /*
    * Private image click listener that is used for sending data to ImagePager, along with current
    * position and loadFiles class(by implementing Serialization)
    * */
    private class OnImageClickListener implements View.OnClickListener {

        int position;
        LoadFiles loadFiles;

        // constructor
        public OnImageClickListener(int position, LoadFiles loadFiles) {
            this.position = position;
            this.loadFiles=loadFiles;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, ImagePager.class);
            i.putExtra("position", position);
            i.putExtra("loadFiles", loadFiles);

            context.startActivity(i);
        }
    }
    /*
    * View Holder class as part of loading images pattern
    * */
    private class ViewHolder{
        ImageView imageView;
    }
}
