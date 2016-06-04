package com.example.futin.parkingtickettracker.RESTService;

import com.example.futin.parkingtickettracker.RESTService.Tasks.RSUploadImageTask;
import com.example.futin.parkingtickettracker.RESTService.interfaces.AsyncTaskReturnData;
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
}
