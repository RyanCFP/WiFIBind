package com.idx.wifibind.util;

/**
 * Created by ryan on 18-2-28.
 * Email: Ryan_chan01212@yeah.net
 */

public interface GlobalConstant {

    interface WifiBind{
        String DEVICE_KIND_NAME = "device.kind.name";
        String WIFI_SSID = "wifi.ssid";
        String WIFI_PASSWORD = "wifi.password";
        String USER_LOGIN_ACCOUNT = "user.login.account";
    }

    interface BindSuccess{
        String DEVICE_BIND_SUCCESS = "device.bind.success";
    }

    interface WiFiConnectStatus{
        String WIFI_STATE_CONNECT = "已连接";
        String WIFI_STATE_ON_CONNECTING = "正在连接";
        String WIFI_STATE_UNCONNECT = "未连接";
        String WIFI_STATE_SAVE = "已保存";
    }

    interface WiFiEncryptWay{
        String WIFI_ENCRYPT_NOPASS = "OPEN";
        String WIFI_ENCRYPT_WEP = "WEP";
        String WIFI_Encrypt_WPA = "WPA";
        String WIFI_ENCRYPT_WPA2 = "WPA2";
        String WIFI_ENCRYPT_WPA_WPA2 = "WPA/WPA2";
        String WIFI_Encrypt_WPS = "WPS";
    }

    interface WiFiPermissionAndOnOff{
        String WIFI_HAS_PERMISSION = "wifi.has.permission";
        String WIFI_OPEN_OR_CLOSE = "wifi.open.or.close";
    }
}
