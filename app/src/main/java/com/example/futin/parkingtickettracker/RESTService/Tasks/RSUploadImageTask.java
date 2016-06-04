package com.example.futin.parkingtickettracker.RESTService.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.futin.parkingtickettracker.RESTService.data.RSDataSingleton;
import com.example.futin.parkingtickettracker.RESTService.interfaces.AsyncTaskReturnData;
import com.example.futin.parkingtickettracker.RESTService.request.RSUploadImageRequest;
import com.example.futin.parkingtickettracker.RESTService.response.RSUploadImageResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
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

import java.util.ArrayList;

public class RSUploadImageTask extends AsyncTask<Void, Void, RSUploadImageResponse> {
    final String TAG="getCities";
    RSUploadImageRequest request;
    RestTemplate restTemplate;
    AsyncTaskReturnData returnData;
    String boundary = "*****";

    public RSUploadImageTask( AsyncTaskReturnData returnData, RSUploadImageRequest request) {
        this.request=request;
        this.returnData = returnData;
        restTemplate=new RestTemplate();
    }

    @Override
    protected RSUploadImageResponse doInBackground(Void... params) {
        try {
            HttpHeaders header = new HttpHeaders();
            //header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            header.set("Connection", "Keep-Alive");
            header.set("Content-Type", "multipart/form-data;boundary=" + boundary);
            String filePath=request.getFilePath();
            String fileName=request.getFileName();

            MultiValueMap<String, Object> parts =
                    new LinkedMultiValueMap<>();
            parts.add("file", filePath);
            parts.add("filename", fileName);
            HttpEntity<MultiValueMap<String, Object>> entity =
                    new HttpEntity<>(parts, header);
            String address = RSDataSingleton.getInstance().getServerUrl().getUploadImageUrl();

            Log.i(TAG, "entity: " + entity);
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
                //TODO response
                String json=response.getBody().toString();
                Log.i(TAG, json);

            }

                return new RSUploadImageResponse(HttpStatus.OK,
                        HttpStatus.OK.name());



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
            return new RSUploadImageResponse(null, null);
        }
    }

    @Override
    protected void onPostExecute(RSUploadImageResponse rsUploadImageResponse) {
        super.onPostExecute(rsUploadImageResponse);
        returnData.returnDataOnPostExecute(rsUploadImageResponse);
    }
}

