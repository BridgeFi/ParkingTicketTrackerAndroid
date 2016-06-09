package com.example.futin.parkingtickettracker.RESTService.loader;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Futin on 6/7/16.
 */
public class LoadFiles implements Serializable{

    File imagesFolder;
    ArrayList<String>listOfFiles;
    private static final String TAG = "LoadFiles";

    public LoadFiles() {
        listOfFiles=new ArrayList<>();
    }
    /*
    * Making sure that folder for saving images exist and getting list of all names, which will
    * be used for displaying in Gallery
    * */
    public ArrayList<String> getListOfFiles() {
        imagesFolder = new File(Environment.getExternalStorageDirectory(), "ParkingTicketImages");
        if (imagesFolder.exists()) {
            for(File f : imagesFolder.listFiles()){
                listOfFiles.add(f.getName());
            }
        }
        Log.i(TAG ,listOfFiles.size()+"");
        return listOfFiles;
    }
    public int getListSize(){
        return listOfFiles.size();
    }

    public File getFile(String name){
        return new File(imagesFolder,name);
    }

    public File getFileFromList(int position){
        if(listOfFiles != null){
           return getFile(listOfFiles.get(position));
        }else{
            return null;
        }
    }
}
