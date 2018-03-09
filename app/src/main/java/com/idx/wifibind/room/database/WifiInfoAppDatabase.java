package com.ryan.wifibind.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.ryan.wifibind.room.Convertors;
import com.ryan.wifibind.room.dao.WiFiInfoDao;
import com.ryan.wifibind.room.entity.WiFiInfo;

/**
 * Created by ryan on 18-3-5.
 * Email: Ryan_chan01212@yeah.net
 */

@Database(entities = {WiFiInfo.class},version = 1,exportSchema = false)
@TypeConverters(Convertors.class)
public abstract class WifiInfoAppDatabase extends RoomDatabase {
    private static WifiInfoAppDatabase INSTANCE;
    //WifiDao
    public abstract WiFiInfoDao wiFiInfoDao();

    private static Object mLock = new Object();
    public static WifiInfoAppDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (mLock){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),WifiInfoAppDatabase.class,
                            "WifiInfo.db").build();
                }
            }
        }
        return INSTANCE;
    }

    public void release(){
        if (INSTANCE != null){
            INSTANCE.close();
        }
    }
}
