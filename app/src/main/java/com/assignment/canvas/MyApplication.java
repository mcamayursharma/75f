package com.assignment.canvas;

import android.app.Application;

import com.kinvey.android.Client;

/**
 * Created by mayursharma on 4/4/17.
 */

public class MyApplication extends Application {

    private static Client client;

    @Override
    public void onCreate() {
        super.onCreate();
        initiateClient();
    }

    private void initiateClient() {
        client = new Client.Builder("kid_Bk5GA5Anx", "a047a406d8c24317b1efe39f4753231a", getApplicationContext()).build();
    }

    public static Client getClient(){
        return client;
    }



}
