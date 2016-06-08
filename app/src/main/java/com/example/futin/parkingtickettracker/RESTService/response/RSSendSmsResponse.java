package com.example.futin.parkingtickettracker.RESTService.response;

import org.springframework.http.HttpStatus;

/**
 * Created by Futin on 6/8/16.
 */
public class RSSendSmsResponse extends BaseApiResponse {

    public RSSendSmsResponse(HttpStatus status, String statusName) {
        super(status, statusName);
    }
}
