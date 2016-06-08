package com.example.futin.parkingtickettracker.RESTService.data;

/**
 * Created by Futin on 6/2/16.
 */
public class RSServerUrl {
    //localhost //dev
    private final static String API_BASE_URL="http://192.168.1.182:3000";

    //production
    //private final static String API_BASE_URL="https://ara-fe-object-001.appspot.com";
    private final static String API_UPLOAD_IMG="/upload";
    private final static String API_SEND_SMS="/sms";


    public String getBaseUrl(){
        return API_BASE_URL;
    }

    public String getUploadImageUrl(){
        return getBaseUrl()+API_UPLOAD_IMG;
    }
    public String getApiSendSms(){
        return getBaseUrl()+API_SEND_SMS;
    }


}
