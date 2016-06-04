package com.example.futin.parkingtickettracker.RESTService.request;

/**
 * Created by Futin on 6/2/16.
 */
public class RSUploadImageRequest {
    String fileName="";
    String filePath="";

    public RSUploadImageRequest(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath=filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        return "name="+fileName;
    }
}
