package com.idx.wifibind.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by ryan on 18-3-1.
 * Email: Ryan_chan01212@yeah.net
 */

public class NetStatusUtils {
    // 判断当前手机是否连上Wifi.
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                if (mWiFiNetworkInfo.isAvailable()) {
                    return mWiFiNetworkInfo.isConnected();
                } else {
                    return false;
                }
            }
        }
        return false;
    }

     //判断当前手机的网络是否可用.
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                if (mMobileNetworkInfo.isAvailable()) {
                    return mMobileNetworkInfo.isConnected();
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    // 判断当前网络是手机网络还是WIFI.
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取代表联网状态的NetWorkInfo对象
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            // 判断NetWorkInfo对象是否为空；判断当前的网络连接是否可用
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 以下是获得App版本信息的工具方法
     * @param
     * @return 版本名
     * @error
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * @parama
     * @return 版本号
     * @error
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }
}
