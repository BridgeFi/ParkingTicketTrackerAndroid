package com.example.futin.parkingtickettracker.RESTService.data;

/**
 * Created by Futin on 6/2/16.
 */
public class RSDataSingleton {

    private static RSDataSingleton instance;

    public static RSDataSingleton getInstance() {
        if (instance == null) {
            instance = new RSDataSingleton();
        }
        return instance;
    }

    public RSServerUrl getServerUrl(){
        return new RSServerUrl();
    }
}