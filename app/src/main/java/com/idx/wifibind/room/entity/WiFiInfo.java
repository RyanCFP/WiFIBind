package com.ryan.wifibind.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by ryan on 18-3-5.
 * Email: Ryan_chan01212@yeah.net
 */
@Entity(tableName = "wifi_info")
public class WiFiInfo {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "user_name")
    public String username;
    @ColumnInfo(name = "device_name")
    public String device;
    @ColumnInfo(name = "SSID")
    public String ssid;
    @ColumnInfo(name = "wifi_pwd")
    public String password;

    @Override
    public String toString() {
        return "WiFiInfo = { user = "+username+
                ",device = "+device+
                ",ssid = "+ssid+
                ",password = "+password+
                ";}";
    }
}
