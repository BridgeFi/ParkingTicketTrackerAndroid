package com.example.futin.parkingtickettracker.userInterface.gallery.adapters;

import android.content.Context;
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

import java.util.ArrayList;

/**
 * Created by Futin on 6/7/16.
 */
public class GridViewAdapter extends BaseAdapter {

    final String TAG="GridViewAdapter";
    Context context;
    LayoutInflater inflater;
    ArrayList<String>listOfFiles;
    LoadFiles loadFiles;
    DisplayFiles displayFiles;
    ViewHolder viewHolder;

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

       // viewHolder.imageView.setOnClickListener(new OnImageClickListener(position));
        return convertView;
    }

    void loadFromDisc(ImageView image, int position){
        String file=null;
        Log.i(TAG, "Load from disc");

        if(!listOfFiles.isEmpty()){
            file=listOfFiles.get(position);
            Log.i(TAG,"File: "+file+" at position: "+position);
            displayFiles.displayImage(file,image);
        }else{
            Toast.makeText(context, "No files to display",Toast.LENGTH_LONG).show();
            Log.i(TAG,"No files to display");

        }
    }


    private class ViewHolder{
        ImageView imageView;
    }
}
