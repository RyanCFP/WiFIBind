package com.idx.wifibind.wifi.bean;

import android.support.annotation.NonNull;

/**
 * Created by ryan on 18-3-2.
 * Email: Ryan_chan01212@yeah.net
 */

public class WiFiBean implements Comparable<WiFiBean>{
    private String SSID;
    /**
     * @parama 已连接 未连接 正在连接 三种状态
     */
    private String state;
    private String level; //WiFi 信号强度
    private String Compatiables; //WiFi 加密方式

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCompatiables() {
        return Compatiables;
    }

    public void setCompatiables(String compatiables) {
        Compatiables = compatiables;
    }

    @Override
    public String toString() {
        return "WiFiBean = {SSID = "+SSID+"\n,"+
                "state = "+state+"\n,"+
                "level = "+level+"\n,"+
                "compatiables = "+Compatiables+
                "}";
    }

    @Override
    public int compareTo(@NonNull WiFiBean wiFiBean) {
        int level = Integer.parseInt(this.level);
        int level2 = Integer.parseInt(wiFiBean.level);
        return level-level2;
    }
}
