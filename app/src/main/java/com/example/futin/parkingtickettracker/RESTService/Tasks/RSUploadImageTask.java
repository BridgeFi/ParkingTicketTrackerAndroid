package com.example.futin.parkingtickettracker.RESTService.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.futin.parkingtickettracker.RESTService.data.RSDataSingleton;
import com.example.futin.parkingtickettracker.RESTService.listeners.AsyncTaskReturnData;
import com.example.futin.parkingtickettracker.RESTService.request.RSUploadImageRequest;
import com.example.futin.parkingtickettracker.RESTService.response.RSUploadImageResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


public class RSUploadImageTask extends AsyncTask<Void, Void, RSUploadImageResponse> {

    RestTemplate restTemplate;
    RSUploadImageRequest request;
    AsyncTaskReturnData returnData;

    final String TAG="imageUploadTask";

    public RSUploadImageTask( AsyncTaskReturnData returnData, RSUploadImageRequest request) {
        this.request=request;
        this.returnData = returnData;
        restTemplate=new RestTemplate();
    }

    @Override
    protected RSUploadImageResponse doInBackground(Void... params) {
        try {
            HttpHeaders header = new HttpHeaders();
            header.set("Connection", "Keep-Alive");
            header.setContentType(MediaType.MULTIPART_FORM_DATA);

            String filePath=request.getFilePath();
            //Using spring MultiValueMap for storing image that we want to upload
            MultiValueMap<String, Object> parts =
                    new LinkedMultiValueMap<>();
            //adding image from local and using as a key same name as on Node.js so Multer module
            //can recognize this image and process it
            parts.add("ticketPhoto", new FileSystemResource(filePath));

            HttpEntity<MultiValueMap<String, Object>> entity =
                    new HttpEntity<>(parts, header);
            String address = RSDataSingleton.getInstance().getServerUrl().getUploadImageUrl();

            Log.i(TAG, "Before response ");
            ResponseEntity response = restTemplate.exchange(address, HttpMethod.POST, entity, String.class);
            Log.i(TAG, "After response ");
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new RSUploadImageResponse(HttpStatus.NOT_FOUND,
                        HttpStatus.NOT_FOUND.name());
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return new RSUploadImageResponse(HttpStatus.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST.name());
            }else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                return new RSUploadImageResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        HttpStatus.INTERNAL_SERVER_ERROR.name());
            } else {
                Log.i(TAG, "Response ok ");
                String jsonBody=response.getBody().toString();
                Log.i(TAG, jsonBody);
                JSONArray array=new JSONArray(jsonBody);
                String fileName="";
                for (int i=0; i<array.length();i++){
                    JSONObject objResponse=array.getJSONObject(i);
                    fileName = objResponse.getString("response");
                    Log.i(TAG,objResponse.toString());
                }
                Log.i(TAG, "fileName:  "+fileName);
                return new RSUploadImageResponse(HttpStatus.OK,
                        HttpStatus.OK.name(),fileName);
            }

        } catch (HttpClientErrorException e) {
            Log.e(TAG, "Http Status: " + e.getStatusCode());
            Log.e(TAG, "Http Error: " + e.getResponseBodyAsString());
            return new RSUploadImageResponse(e.getStatusCode(),
                    e.getStatusText());
        } catch (HttpServerErrorException e) {
            Log.e(TAG, "Http Status: " + e.getStatusCode());
            Log.e(TAG, "Http Error: " + e.getResponseBodyAsString());
            return new RSUploadImageResponse(e.getStatusCode(),
                    e.getStatusText());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return new RSUploadImageResponse(e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(RSUploadImageResponse rsUploadImageResponse) {
        super.onPostExecute(rsUploadImageResponse);
        returnData.returnDataOnPostExecute(rsUploadImageResponse);
    }
}

