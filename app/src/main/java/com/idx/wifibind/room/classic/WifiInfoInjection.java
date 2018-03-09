package com.ryan.wifibind.room.classic;

import android.content.Context;

import com.ryan.wifibind.room.database.WifiInfoAppDatabase;
import com.ryan.wifibind.util.AppExcutors;

/**
 * Created by ryan on 18-3-5.
 * Email: Ryan_chan01212@yeah.net
 */

public class WifiInfoInjection {
    public static WifiInfoRepository getRoomRepository(Context context) {
        WifiInfoAppDatabase wifiInfoAppDatabase = WifiInfoAppDatabase.getInstance(context);
        return WifiInfoRepository.getInstance(RemoteWifiInfoDataSource.getInstance(new AppExcutors(), wifiInfoAppDatabase.wiFiInfoDao()));
    }
}
