package com.example.futin.parkingtickettracker.RESTService.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.futin.parkingtickettracker.RESTService.data.RSDataSingleton;
import com.example.futin.parkingtickettracker.RESTService.listeners.AsyncTaskReturnData;
import com.example.futin.parkingtickettracker.RESTService.request.RSSendSmsRequest;
import com.example.futin.parkingtickettracker.RESTService.response.RSSendSmsResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Futin on 6/8/16.
 */
public class RSSendSmsTask extends AsyncTask<Void, Void, RSSendSmsResponse> {

    RestTemplate restTemplate;
    RSSendSmsRequest request;
    AsyncTaskReturnData returnData;

    final String TAG="SendSmsTask";

    public RSSendSmsTask( AsyncTaskReturnData returnData, RSSendSmsRequest request) {
        this.request=request;
        this.returnData = returnData;
        restTemplate=new RestTemplate();
    }

    @Override
    protected RSSendSmsResponse doInBackground(Void... params) {
        try {
            HttpHeaders header = new HttpHeaders();
            header.set("Connection", "Keep-Alive");
            header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String message=request.getMessage();

            HttpEntity<String> entity = new HttpEntity<>(message, header);
            String address = RSDataSingleton.getInstance().getServerUrl().getApiSendSms();

            Log.i(TAG, "Before response "+address);
            ResponseEntity response = restTemplate.exchange(address, HttpMethod.POST, entity, String.class);
            Log.i(TAG, "After response ");
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new RSSendSmsResponse(HttpStatus.NOT_FOUND,
                        HttpStatus.NOT_FOUND.name());
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return new RSSendSmsResponse(HttpStatus.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST.name());
            }else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                return new RSSendSmsResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        HttpStatus.INTERNAL_SERVER_ERROR.name());
            } else {
                Log.i(TAG, "Response ok ");
                String jsonBody=response.getBody().toString();
                Log.i(TAG, "Body: "+jsonBody+"");

                return new RSSendSmsResponse(HttpStatus.OK,
                        HttpStatus.OK.name());
            }
        } catch (HttpClientErrorException e) {
            Log.e(TAG, "Http Status: " + e.getStatusCode());
            Log.e(TAG, "Http Error: " + e.getResponseBodyAsString());
            return new RSSendSmsResponse(e.getStatusCode(),
                    e.getStatusText());
        } catch (HttpServerErrorException e) {
            Log.e(TAG, "Http Status: " + e.getStatusCode());
            Log.e(TAG, "Http Error: " + e.getResponseBodyAsString());
            return new RSSendSmsResponse(e.getStatusCode(),
                    e.getStatusText());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return new RSSendSmsResponse(null, null);
        }
    }

    @Override
    protected void onPostExecute(RSSendSmsResponse rsSendSmsResponse) {
        super.onPostExecute(rsSendSmsResponse);
        returnData.returnDataOnPostExecute(rsSendSmsResponse);
    }
}
