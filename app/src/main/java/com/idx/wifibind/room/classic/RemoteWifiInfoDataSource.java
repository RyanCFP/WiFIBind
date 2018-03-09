package com.ryan.wifibind.room.classic;

import com.ryan.wifibind.room.callback.WifiInfoDataSource;
import com.ryan.wifibind.room.dao.WiFiInfoDao;
import com.ryan.wifibind.room.entity.WiFiInfo;
import com.ryan.wifibind.util.AppExcutors;

import java.util.List;

/**
 * Created by ryan on 18-3-5.
 * Email: Ryan_chan01212@yeah.net
 */

public class RemoteWifiInfoDataSource implements WifiInfoDataSource {
    private static volatile RemoteWifiInfoDataSource INSTANCE;

    private WiFiInfoDao mWiFiInfoDao;
    private AppExcutors mAppExcutors;
    private WiFiInfo mWiFiInfo;
    private List<WiFiInfo> mWifiInfos;

    public RemoteWifiInfoDataSource(AppExcutors appExcutors,
                                    WiFiInfoDao wiFiInfoDao){
        mAppExcutors = appExcutors;
        mWiFiInfoDao = wiFiInfoDao;
    }

    public static RemoteWifiInfoDataSource getInstance(AppExcutors appExcutors,
                                                       WiFiInfoDao wiFiInfoDao){
        if (INSTANCE == null){
             synchronized (RemoteWifiInfoDataSource.class){
                 if (INSTANCE == null){
                     INSTANCE = new RemoteWifiInfoDataSource(appExcutors,wiFiInfoDao);
                 }
             }
        }
        return INSTANCE;
    }

    /**
     * @param wifiInfo 插入当前所绑定的设备的信息
     * @return
     * @error
     */
    @Override
    public void insertWifiInfo(final WiFiInfo wifiInfo) {
        mAppExcutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWiFiInfoDao.insertWifiInfo(wifiInfo);
            }
        });
    }

    /**
     * @param loadWifiInfoListCallback 回调拿到所有已绑定设备List<WiFiInfo>的信息
     * @return
     * @error
     */
    @Override
    public void queryWifiInfoListInfo(final LoadWifiInfoListCallback loadWifiInfoListCallback) {
        mAppExcutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWifiInfos = mWiFiInfoDao.getAll();
                mAppExcutors.getMainIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (loadWifiInfoListCallback != null){
                            if (mWifiInfos != null && mWifiInfos.size() > 0){
                                loadWifiInfoListCallback.onWifiInfoLoaded(mWifiInfos);
                            }else{
                                loadWifiInfoListCallback.onDataNotFound();
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * @param ssid 当前WiFi账号
     * @param loadWifiInfoCallback 回调拿到数据库根据ssid查询已绑定设备的WiFiInfo信息
     * @return
     * @error
     */
    @Override
    public void queryWifiInfo(final String ssid, final LoadWifiInfoCallback loadWifiInfoCallback) {
        mAppExcutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWiFiInfo = mWiFiInfoDao.getWifiInfo(ssid);
                mAppExcutors.getMainIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (loadWifiInfoCallback != null){
                            if (mWiFiInfo != null){
                                loadWifiInfoCallback.onWifiInfoLoaded(mWiFiInfo);
                            }else{
                                loadWifiInfoCallback.onDataNotFound();
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void deleteWiFiInfo(final WiFiInfo wiFiInfo) {
        mAppExcutors.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mWiFiInfoDao.deleteWifiInfo(wiFiInfo);
            }
        });
    }
}
