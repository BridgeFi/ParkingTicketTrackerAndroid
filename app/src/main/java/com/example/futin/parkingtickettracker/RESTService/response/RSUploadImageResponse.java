package com.example.futin.parkingtickettracker.RESTService.response;

import org.springframework.http.HttpStatus;

/**
 * Created by Futin on 6/2/16.
 */
public class RSUploadImageResponse extends BaseApiResponse{

    String fileName="";
    public RSUploadImageResponse(HttpStatus status, String statusName) {
        super(status, statusName);
    }

    public RSUploadImageResponse(HttpStatus status, String statusName, String fileName) {
        super(status, statusName);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
