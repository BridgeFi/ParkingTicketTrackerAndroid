package com.example.futin.parkingtickettracker.RESTService.response;

import org.springframework.http.HttpStatus;

/**
 * Created by Futin on 6/2/16.
 */
public class RSUploadImageResponse extends BaseApiResponse{

    public RSUploadImageResponse(HttpStatus status, String statusName) {
        super(status, statusName);
    }

}
