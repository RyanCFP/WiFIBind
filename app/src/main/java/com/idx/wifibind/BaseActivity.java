package com.idx.wifibind;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.idx.wifibind.util.GlobalConstant;
import com.idx.wifibind.util.SharePrefUtils;
import com.idx.wifibind.wifi.WiFiSupport;

import java.util.ArrayList;

/**
 * Created by ryan on 18-2-28.
 * Email: Ryan_chan01212@yeah.net
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    public static final int PERMISSION_REQUEST_CODE = 0X123;
    public boolean mHasPermission;
    public boolean mHasPermismissionAndWiFiOn;
    public SharePrefUtils mSharePrefUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharePrefUtils = new SharePrefUtils(this);
        verifySDK();
    }

    private void verifySDK() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Log.i(TAG, "verifySDK: android6.0以上");
            initPermission();
        }else {
            Log.i(TAG, "verifySDK: android6.0以下");
            mHasPermission = true;
            if (WiFiSupport.isOpenWifi(this) && mHasPermission){
                mHasPermismissionAndWiFiOn = true;
            }else {
                mHasPermismissionAndWiFiOn = false;
            }
            mSharePrefUtils.saveWiFiPermissionAndOnOff(
                    GlobalConstant.WiFiPermissionAndOnOff.WIFI_HAS_PERMISSION,mHasPermission);
            mSharePrefUtils.saveWiFiPermissionAndOnOff(
                    GlobalConstant.WiFiPermissionAndOnOff.WIFI_OPEN_OR_CLOSE,mHasPermismissionAndWiFiOn);
        }
    }

    // 6.0以上危险权限获取
    private void initPermission() {
        String permissions[] = {Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ArrayList<String> permissionList = new ArrayList<>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                //进入到这里代表没有权限.
                permissionList.add(perm);
                mHasPermission = false;
                Log.i(TAG, "initPermission: 没有权限");
            }
        }
        String tmpList[] = new String[permissionList.size()];
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(tmpList), PERMISSION_REQUEST_CODE);
        }
    }
}
