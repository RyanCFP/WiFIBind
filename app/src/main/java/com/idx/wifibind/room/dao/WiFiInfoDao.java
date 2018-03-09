package com.ryan.wifibind.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ryan.wifibind.room.entity.WiFiInfo;

import java.util.List;

/**
 * Created by ryan on 18-3-5.
 * Email: Ryan_chan01212@yeah.net
 */
@Dao
public interface WiFiInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWifiInfo(WiFiInfo wiFiInfo);

    @Query("SELECT * FROM wifi_info WHERE SSID LIKE :ssid")
    WiFiInfo getWifiInfo(String ssid);

    @Query(("SELECT * FROM wifi_info"))
    List<WiFiInfo> getAll();

    @Delete
    void deleteWifiInfo(WiFiInfo wiFiInfo);
}
