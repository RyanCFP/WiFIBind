package com.idx.wifibind.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.idx.wifibind.util.GlobalConstant;
import com.idx.wifibind.wifi.bean.WiFiBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 18-3-2.
 * Email: Ryan_chan01212@yeah.net
 */

public class WiFiSupport {
    private static final String TAG = "WiFiSupport";

    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    public enum WiFiEncryptWay{
        WEP(GlobalConstant.WiFiEncryptWay.WIFI_ENCRYPT_WEP),
        WPA(GlobalConstant.WiFiEncryptWay.WIFI_Encrypt_WPA),
        WPA2(GlobalConstant.WiFiEncryptWay.WIFI_ENCRYPT_WPA2),
        WPS(GlobalConstant.WiFiEncryptWay.WIFI_Encrypt_WPS),
        WPAWPA2(GlobalConstant.WiFiEncryptWay.WIFI_ENCRYPT_WPA_WPA2),
        NOPASS(GlobalConstant.WiFiEncryptWay.WIFI_ENCRYPT_NOPASS);

        private String encryptWay;
        WiFiEncryptWay(String encryptWay){
           this.encryptWay = encryptWay;
        }

        public String getEncryptWay(){
            return encryptWay;
        }
    }

    public WiFiSupport() {}

    public static List<ScanResult> getWifiScanResult(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getScanResults();
    }

    public static boolean isWifiEnable(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled();
    }

    public static WifiInfo getConnectedWifiInfo(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
    }

    public static List getConfigurations(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConfiguredNetworks();
    }


    public static WifiConfiguration createWifiConfig(String SSID, String password, WifiCipherType type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        if (type == WifiCipherType.WIFICIPHER_NOPASS) {
//            config.wepKeys[0] = "";  //注意这里
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            config.wepTxKeyIndex = 0;
        }

        if (type == WifiCipherType.WIFICIPHER_WEP) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }

        if (type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;
    }

    //接入某个wifi热点
    public static boolean addNetWork(WifiConfiguration config, Context context) {
        WifiManager wifimanager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiinfo = wifimanager.getConnectionInfo();
        if (null != wifiinfo) {
            wifimanager.disableNetwork(wifiinfo.getNetworkId());
        }

        boolean result = false;

        if (config.networkId > 0) {
            result = wifimanager.enableNetwork(config.networkId, true);
            wifimanager.updateNetwork(config);
        } else {
            int i = wifimanager.addNetwork(config);
            result = false;
            if (i > 0) {
                wifimanager.saveConfiguration();
                return wifimanager.enableNetwork(i, true);
            }
        }
        return result;
    }

    //判断wifi热点支持的加密方式
    public static WifiCipherType getWifiCipher(String encryptWay) {
        if (encryptWay.isEmpty()) {
            return WifiCipherType.WIFICIPHER_INVALID;
        } else if (encryptWay.contains(GlobalConstant.WiFiEncryptWay.WIFI_ENCRYPT_WEP)) {
            return WifiCipherType.WIFICIPHER_WEP;
        } else if (encryptWay.contains(GlobalConstant.WiFiEncryptWay.WIFI_Encrypt_WPA) ||
                encryptWay.contains(GlobalConstant.WiFiEncryptWay.WIFI_ENCRYPT_WPA2) ||
                encryptWay.contains(GlobalConstant.WiFiEncryptWay.WIFI_Encrypt_WPS)) {
            return WifiCipherType.WIFICIPHER_WPA;
        } else {
            return WifiCipherType.WIFICIPHER_NOPASS;
        }
    }

    //获取当前WIFI的SSID.
    public static String getCurentWifiSSID(Context context) {
        String ssid = "";
        if (context != null) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if(wifiInfo!=null){
                ssid = wifiInfo.getSSID();
                if (ssid.substring(0, 1).equals("\"") && ssid.substring(ssid.length() - 1).equals("\"")) {
                    ssid = ssid.substring(1, ssid.length() - 1);
                    Log.i(TAG, "getCurentWifiSSID: ssid = "+ssid);
                }
            }
        }
        return ssid;
    }

    //用来获得手机扫描到的所有wifi的信息.
    public static List<ScanResult> getCurrentWifiScanResult(Context c) {
        WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        return wifiManager.getScanResults();
    }


    public static String getConnectWifiSSID(Context c) {
        String ssid = "";
        WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if(wifiInfo!=null){
            ssid = wifiInfo.getSSID();
        }
        return ssid;
    }

    //查看以前是否也配置过这个网络
    public static WifiConfiguration isExsits(@NonNull String SSID, Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> existingConfigs = wifimanager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    // 打开WIFI
    public static void openWifi(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifimanager.isWifiEnabled()) {
            wifimanager.setWifiEnabled(true);
        }
    }

    // 关闭WIFI
    public static void closeWifi(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifimanager.isWifiEnabled()) {
            wifimanager.setWifiEnabled(false);
        }
    }

    //WiFi是否打开
    public static boolean isOpenWifi(Context context){
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        boolean open = wifimanager.isWifiEnabled();
        return open;
    }

    /**
     * 将ipAddress转化成string类型的Ip字符串
     * @param ipAddress
     * @return
     */
    public static String getStringIp(int ipAddress) {
        StringBuffer sb = new StringBuffer();
        int ip = (ipAddress >> 0) & 0xff;
        sb.append(ip + ".");
        ip = (ipAddress >> 8) & 0xff;
        sb.append(ip + ".");
        ip = (ipAddress >> 16) & 0xff;
        sb.append(ip + ".");
        ip = (ipAddress >> 24) & 0xff;
        sb.append(ip);
        return sb.toString();
    }

    /**
     * 设置安全性
     * @param capabilities
     * @return 加密方式
     */
    public static String getCapabilitiesString(String capabilities) {
        if (capabilities.contains(GlobalConstant.WiFiEncryptWay.WIFI_ENCRYPT_WEP)) {
            return GlobalConstant.WiFiEncryptWay.WIFI_ENCRYPT_WEP;
        } else if (capabilities.contains(GlobalConstant.WiFiEncryptWay.WIFI_Encrypt_WPA) ||
                capabilities.contains(GlobalConstant.WiFiEncryptWay.WIFI_ENCRYPT_WPA2) ||
                capabilities.contains(GlobalConstant.WiFiEncryptWay.WIFI_Encrypt_WPS)) {
            return GlobalConstant.WiFiEncryptWay.WIFI_ENCRYPT_WPA_WPA2;
        } else {
            return GlobalConstant.WiFiEncryptWay.WIFI_ENCRYPT_NOPASS;
        }
    }

    public static boolean getIsWifiEnabled(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifimanager.isWifiEnabled();
    }

    public static void getReplace(Context context, List<WiFiBean> list) {
        WifiInfo wifi = WiFiSupport.getConnectedWifiInfo(context);
        List<WiFiBean> listCopy = new ArrayList<>();
        listCopy.addAll(list);
        for (int i = 0; i < list.size(); i++) {
            if (("\"" + list.get(i).getSSID() + "\"").equals(wifi.getSSID())) {
                listCopy.add(0, list.get(i));
                listCopy.remove(i + 1);
                listCopy.get(0).setState("已连接");
            }
        }
        list.clear();
        list.addAll(listCopy);
    }
    /**
     * 去除同名WIFI
     * @param oldScanResults 需要去除同名的列表
     * @return 返回不包含同名的列表
     */
    public static List<ScanResult> noSameName(List<ScanResult> oldScanResults) {
        List<ScanResult> newScanResults = new ArrayList<ScanResult>();
        for (ScanResult result : oldScanResults) {
            if (!TextUtils.isEmpty(result.SSID) && !containName(newScanResults, result.SSID))
                newScanResults.add(result);
        }
        return newScanResults;
    }
    /**
     * 判断一个扫描结果中，是否包含了某个名称的WIFI
     * @param scanResults 扫描结果
     * @param SSID 要查询的WiFi名称
     * @return 返回true表示包含了该名称的WIFI，返回false表示不包含
     */
    public static boolean containName(List<ScanResult> scanResults, String SSID) {
        for (ScanResult result : scanResults) {
            if (!TextUtils.isEmpty(result.SSID) && result.SSID.equals(SSID))
                return true;
        }
        return false;
    }

    //返回WiFi信号强度level 等级
    public static int getLevel(int level){
        if (Math.abs(level) < 50) {
            return 1;
        } else if (Math.abs(level) < 75) {
            return 2;
        } else if (Math.abs(level) < 90) {
            return 3;
        } else {
            return 4;
        }
    }
}
