package com.lge.testapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int REQEUST_CODE_ACCOUNT_PERMISSION = 101;
    private static final String[] PERMISSIONS_ACCOUNT = {
            Manifest.permission.READ_CONTACTS,
            Build.VERSION.SDK_INT >= 30/*Build.VERSION_CODES.R*/ ?
                    Manifest.permission.READ_PHONE_NUMBERS : Manifest.permission.READ_PHONE_STATE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launchLoginWithPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQEUST_CODE_ACCOUNT_PERMISSION) {
            boolean isAllGrant = true;
            boolean showRationale = true;
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    isAllGrant = false;
                    showRationale = shouldShowRequestPermissionRationale(permission);
                }
                Log.d("kks", "permission: " + permission + ", " + grantResults[i]);
            }
            if (isAllGrant) {
                doSomething();
            } else if (!showRationale) {
                Toast.makeText(this, getString(R.string.lt_no_permissions_toast,
                        getString(R.string.lt_lg_account)), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void launchLoginWithPermission() {
        Log.d("kks", "checking permission - " + Build.VERSION.SDK_INT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissions(PERMISSIONS_ACCOUNT)) {
                Log.d("kks", "verified permission");
                doSomething();
            } else {
                Log.d("kks", "request permission");
                requestPermissions(PERMISSIONS_ACCOUNT, REQEUST_CODE_ACCOUNT_PERMISSION);
            }
        } else {
            Log.d("kks", "verified permission");
            doSomething();
        }
    }

    private boolean checkPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void doSomething() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tm.getLine1Number();
        Log.d("kks", phoneNumber);
    }
}