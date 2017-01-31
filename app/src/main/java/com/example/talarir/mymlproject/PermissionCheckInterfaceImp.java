package com.example.talarir.mymlproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by talarir on 08/01/2017.
 */

public class PermissionCheckInterfaceImp implements PermissionCheckInterface {
    public Context context;
    private static final int PERMISSION_REQUEST_CODE1 = 1;
    public PermissionCheckInterfaceImp(Context context)
    {
        this.context=context;
    }

    public void locationPermissionRequest()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission1()) {
                Toast.makeText(context, "success s", Toast.LENGTH_SHORT).show();

            } else {
                requestPermission();
                Log.e("ans1", "Permission Granted, Now you can use storage.");
            }
        }
        else
        {
            Log.e("ans2", "Permission Granted, Now you can use internet");
        }

    }

    public boolean checkPermission1()
    {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(context, "GPS ACCESSIBLE", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE1);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use GPS");
                } else {
                    Log.e("value", "Permission Denied, You cannot use GPS .");
                }
                break;
        }
    }



}
