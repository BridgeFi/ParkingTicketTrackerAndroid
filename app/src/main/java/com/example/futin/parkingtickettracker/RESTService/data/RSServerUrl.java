package com.example.futin.parkingtickettracker.RESTService.data;

/**
 * Created by Futin on 6/2/16.
 */
public class RSServerUrl {
    //localhost
    private final static String API_BASE_URL="http://172.31.98.198:3000";

    //dev
   // private final static String API_BASE_URL="https://ara-fe-object-001.appspot.com";
    private final static String API_UPLOAD_IMG="/upload";

    public String getBaseUrl(){
        return API_BASE_URL;
    }

    public String getUploadImageUrl(){
        return getBaseUrl()+API_UPLOAD_IMG;
    }

}
