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
    ArrayList<String>listOfFiles;
    File imagesFolder;

    public LoadFiles() {
        listOfFiles=new ArrayList<>();
    }

    public ArrayList<String> getListOfFiles() {
        imagesFolder = new File(Environment.getExternalStorageDirectory(), "ParkingTicketImages");
        if (imagesFolder.exists()) {
            for(File f : imagesFolder.listFiles()){
                listOfFiles.add(f.getName());
            }
        }
        Log.i("listsize: ",listOfFiles.size()+"");
        return listOfFiles;
    }
    public int getListSize(){
        return listOfFiles.size();
    }

    public File getImagesFolder(){
        return imagesFolder;
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
