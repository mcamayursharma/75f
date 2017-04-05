package com.assignment.canvas;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.assignment.canvas.util.NetworkManager;
import com.assignment.canvas.util.PermissionManager;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

/**
 * Created by mayursharma on 4/4/17.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!PermissionManager.hasStoragePermission(this))
            PermissionManager.verifyStoragePermissions(this);
        else
            proceed();
    }

    private void proceed() {
        if(!NetworkManager.nManager.checkAndShowNetworkAlert(this))
        {
            return;
        }

        if(MyApplication.getClient().user().isUserLoggedIn()) {
            navigateToMainActivity();
            return;
        }

        MyApplication.getClient().user().login("admin", "admin", new KinveyUserCallback() {
            @Override
            public void onSuccess(User user) {
                navigateToMainActivity();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("Login Failure", throwable.getCause().toString());
                finish();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermissionManager.REQUEST_EXTERNAL_STORAGE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                proceed();
            } else {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("To download and export image, we require the External storage permission. Please grant the permission to continue further.");
                alertBuilder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PermissionManager.verifyStoragePermissions(SplashActivity.this);
                    }
                });
                alertBuilder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SplashActivity.this.finish();
                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        }

    }
}
