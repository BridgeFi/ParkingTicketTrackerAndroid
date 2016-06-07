package com.example.futin.parkingtickettracker.RESTService.loader;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Futin on 6/7/16.
 */
public class LoadFiles {
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
        return listOfFiles;
    }

    public File getImagesFolder(){
        return imagesFolder;
    }

    public File getFile(String name){
        return new File(imagesFolder,name);
    }



}
