package com.ryan.wifibind.room.callback;

import com.ryan.wifibind.room.entity.WiFiInfo;

import java.util.List;

/**
 * Created by ryan on 18-3-5.
 * Email: Ryan_chan01212@yeah.net
 */

public interface WifiInfoDataSource {
    interface LoadWifiInfoCallback{
        void onWifiInfoLoaded(WiFiInfo wifiInfo);
        void onDataNotFound();
    }
    interface LoadWifiInfoListCallback{
        void onWifiInfoLoaded(List<WiFiInfo> wifiInfoList);
        void onDataNotFound();
    }
    //insert WifiInfo
    void insertWifiInfo(WiFiInfo wifiInfo);

    //List<WiFiInfo>
    void queryWifiInfoListInfo(LoadWifiInfoListCallback loadWifiInfoListCallback);

    //<WifiInfo> from ssid
    void queryWifiInfo(String ssid,LoadWifiInfoCallback loadWifiInfoCallback);

    //delete WiFiInfo
    void deleteWiFiInfo(WiFiInfo wiFiInfo);
}
