package com.example.futin.parkingtickettracker.RESTService.response;

import org.springframework.http.HttpStatus;

/**
 * Created by Futin on 6/2/16.
 */
public class BaseApiResponse {

    HttpStatus status;
    String statusName;

    public BaseApiResponse(HttpStatus status, String statusName) {
        this.status = status;
        this.statusName = statusName;
    }

    public BaseApiResponse() {
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getStatusName() {
        return statusName;
    }
}
