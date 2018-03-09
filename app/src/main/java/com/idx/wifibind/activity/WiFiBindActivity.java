package com.idx.wifibind.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.idx.wifibind.R;
import com.idx.wifibind.fragment.BtnNextLisener;
import com.idx.wifibind.fragment.DeviceInfoFragment;
import com.idx.wifibind.wifi.dialog.WifiSetCancelDialog;
import com.idx.wifibind.BaseActivity;
import com.idx.wifibind.fragment.WiFiBindSuccessFragment;
import com.idx.wifibind.fragment.WiFiConnectFragment;
import com.idx.wifibind.fragment.WifiGetFragment;
import com.idx.wifibind.util.ActivityUtils;
import com.idx.wifibind.util.GlobalConstant;
import com.idx.wifibind.util.SharePrefUtils;

/**
 * Created by ryan on 18-2-28.
 * Email: Ryan_chan01212@yeah.net
 */

public class WiFiBindActivity extends BaseActivity implements BtnNextLisener {
    private static final String TAG = WiFiBindActivity.class.getSimpleName();
    private Fragment content_fragment;
    private DeviceInfoFragment mDeviceInfoFragment;
    private WifiGetFragment mWifiGetFragment;
    private WiFiConnectFragment mWiFiConnectFragment;
    private WiFiBindSuccessFragment mWiFiBindSuccessFragment;
    private boolean mBindWifiIsCancelled;
    private WifiSetCancelDialog mWifiSetCancelDialog;
    private SharePrefUtils mSharePrefUtils;

    public enum WhichFragment{
        DeviceInfo("DeviceInfo"),
        WifiGet("WifiGet"),
        WifiConnect("WifiConnect"),
        WifiBindSuccess("WifiBindSuccess");
        private String flag;
        WhichFragment(String flag){
            this.flag = flag;
        }
        public String getFlag(){
            return this.flag;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_wifi_activity);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mSharePrefUtils.saveBindWifi(GlobalConstant.WifiBind.DEVICE_KIND_NAME,"");
        mSharePrefUtils.saveBindWifi(GlobalConstant.WifiBind.WIFI_SSID,"");
        mSharePrefUtils.saveBindWifi(GlobalConstant.WifiBind.WIFI_PASSWORD,"");
        mSharePrefUtils.saveBindWifi(GlobalConstant.WifiBind.USER_LOGIN_ACCOUNT,"");
    }

    private void initView() {
        content_fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (content_fragment == null){
            changeFragment(WhichFragment.DeviceInfo);
        }
        mWifiSetCancelDialog = new WifiSetCancelDialog(this, R.style.dialog_download, new WifiSetCancelDialog.WiFiBindCancelCallback() {
            @Override
            public void onWiFiBindCancel(boolean isCanceled) {
                Log.i(TAG, "onWiFiBindCancel: mBindWifiIsCancelled = "+isCanceled);
                mBindWifiIsCancelled = isCanceled;
                if (mBindWifiIsCancelled){
                    mBindWifiIsCancelled = false;
                    initData();
                    finish();
                }
            }
        });

        mSharePrefUtils = new SharePrefUtils(this);
    }

    @Override
    public void onBtnNextClickLisener(int resId) {
        switch (resId){
            case R.id.wifi_device_info:
                changeFragment(WhichFragment.WifiGet);
                break;
            case R.id.wifi_device_get:
                changeFragment(WhichFragment.WifiConnect);
                break;
            case R.id.wifi_device_connect:
                changeFragment(WhichFragment.WifiBindSuccess);
                break;
            case R.id.wifi_device_success:
                finish();
                break;
            default:break;
        }
    }

    private void changeFragment(WhichFragment flag) {
        Log.i(TAG, "changeFragment: flag = "+flag.getFlag());
        switch (flag.getFlag()){
            case "DeviceInfo":
                if (mDeviceInfoFragment == null) {
                    mDeviceInfoFragment = new DeviceInfoFragment();
                }
                ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                        mDeviceInfoFragment,R.id.content);
                break;
            case "WifiGet":
                if (mWifiGetFragment == null) {
                    mWifiGetFragment = new WifiGetFragment();
                }
                ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                        mWifiGetFragment,R.id.content);
                break;
            case "WifiConnect":
                if (mWiFiConnectFragment == null){
                    mWiFiConnectFragment = new WiFiConnectFragment();
                }
                ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                        mWiFiConnectFragment,R.id.content);
                break;
            case "WifiBindSuccess":
                if (mWiFiBindSuccessFragment == null){
                    mWiFiBindSuccessFragment = new WiFiBindSuccessFragment();
                }
                ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                        mWiFiBindSuccessFragment,R.id.content);
                break;
            default:break;
        }
    }
    
    @Override
    public void onBackPressed() {
        if (!mWifiSetCancelDialog.isShowing()){
            mWifiSetCancelDialog.show();
        }
//        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initData();
        mSharePrefUtils = null;
        content_fragment = null;
        mDeviceInfoFragment = null;
        mWifiGetFragment = null;
        mWiFiConnectFragment = null;
        mWiFiBindSuccessFragment = null;
        mBindWifiIsCancelled = false;
        if (mWifiSetCancelDialog != null){
            if (mWifiSetCancelDialog.isShowing()){
                mWifiSetCancelDialog.dismiss();
            }
            mWifiSetCancelDialog = null;
        }
        mSharePrefUtils = null;
    }
}
