package com.example.futin.parkingtickettracker.RESTService.request;

/**
 * Created by Futin on 6/8/16.
 */
public class RSSendSmsRequest {
    String message;
    public RSSendSmsRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "message="+message;
    }
}
