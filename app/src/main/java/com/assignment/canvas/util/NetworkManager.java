package com.assignment.canvas.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public enum NetworkManager {
    nManager;

    private Context context;

    public static boolean isNetworkAvailable(Context context) {
        if(context == null)
            return false;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected =  activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return isConnected;
    }


    public boolean checkAndShowNetworkAlert(Context context){
        this.context = context;
        boolean isConnected = NetworkManager.nManager.isNetworkAvailable(context);
        if(!isConnected){
//            Utility.showAlertDialog(context, "", "Network is not connected. Please retry later.");
            Toast.makeText(context, "No Internet Connection.", Toast.LENGTH_LONG).show();
        }
        return isConnected;
    }
}
