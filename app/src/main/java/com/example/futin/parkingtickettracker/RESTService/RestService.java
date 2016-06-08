package com.example.futin.parkingtickettracker.RESTService;

import com.example.futin.parkingtickettracker.RESTService.Tasks.RSSendSmsTask;
import com.example.futin.parkingtickettracker.RESTService.Tasks.RSUploadImageTask;
import com.example.futin.parkingtickettracker.RESTService.listeners.AsyncTaskReturnData;
import com.example.futin.parkingtickettracker.RESTService.request.RSSendSmsRequest;
import com.example.futin.parkingtickettracker.RESTService.request.RSUploadImageRequest;

/**
 * Created by Futin on 6/2/16.
 */
public class RestService {
    AsyncTaskReturnData returnData;

    public RestService(AsyncTaskReturnData returnData) {
        this.returnData = returnData;
    }

    public void uploadImage(String fileName, String filePath){
        new RSUploadImageTask(returnData,new RSUploadImageRequest(fileName, filePath)).execute((Void) null);
    }
    public void sendSms(String message){
        new RSSendSmsTask(returnData,new RSSendSmsRequest(message)).execute((Void) null);
    }
}
