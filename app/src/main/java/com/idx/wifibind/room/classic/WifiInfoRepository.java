package com.ryan.wifibind.room.classic;

import com.ryan.wifibind.room.callback.WifiInfoDataSource;
import com.ryan.wifibind.room.entity.WiFiInfo;

/**
 * Created by ryan on 18-3-5.
 * Email: Ryan_chan01212@yeah.net
 */

public class WifiInfoRepository implements WifiInfoDataSource {
    private static WifiInfoRepository INSTANCE = null;
    private static Object mLock = new Object();
    private RemoteWifiInfoDataSource mRemoteWifiInfoDataSource;

    public WifiInfoRepository(RemoteWifiInfoDataSource remoteWifiInfoDataSource){
        mRemoteWifiInfoDataSource = remoteWifiInfoDataSource;
    }

    public static WifiInfoRepository getInstance(RemoteWifiInfoDataSource remoteWifiInfoDataSource){
        if (INSTANCE == null){
            synchronized (mLock){
                if (INSTANCE == null){
                    INSTANCE = new WifiInfoRepository(remoteWifiInfoDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void insertWifiInfo(WiFiInfo wifiInfo) {
        mRemoteWifiInfoDataSource.insertWifiInfo(wifiInfo);
    }

    @Override
    public void queryWifiInfoListInfo(LoadWifiInfoListCallback loadWifiInfoListCallback) {
        mRemoteWifiInfoDataSource.queryWifiInfoListInfo(loadWifiInfoListCallback);
    }

    @Override
    public void queryWifiInfo(String ssid, LoadWifiInfoCallback loadWifiInfoCallback) {
        mRemoteWifiInfoDataSource.queryWifiInfo(ssid,loadWifiInfoCallback);
    }

    @Override
    public void deleteWiFiInfo(WiFiInfo wiFiInfo) {
        mRemoteWifiInfoDataSource.deleteWiFiInfo(wiFiInfo);
    }
}
