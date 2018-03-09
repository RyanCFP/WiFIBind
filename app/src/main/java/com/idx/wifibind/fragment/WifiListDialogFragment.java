package com.idx.wifibind.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.idx.wifibind.R;
import com.idx.wifibind.util.CollectionUtils;
import com.idx.wifibind.util.GlobalConstant;
import com.idx.wifibind.util.SharePrefUtils;
import com.idx.wifibind.util.ToastUtils;
import com.idx.wifibind.wifi.WiFiSupport;
import com.idx.wifibind.wifi.adapter.WifiListAdapter;
import com.idx.wifibind.wifi.bean.WiFiBean;
import com.idx.wifibind.wifi.dialog.WifiLinkDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ryan on 18-3-1.
 * Email: Ryan_chan01212@yeah.net
 */

public class WifiListDialogFragment extends DialogFragment implements WifiLinkDialog.WiFiAccountListener{
    private static final String TAG = WifiListDialogFragment.class.getSimpleName();
    private View mView;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private SharePrefUtils mSharePrefUtils;
    private WifiLinkDialog mWifiLinkDialog;
    private SelectWiFiListener mSelectWiFiListener = null;
    public void setSelectWiFiListener(SelectWiFiListener selectWiFiCallback){
        mSelectWiFiListener = selectWiFiCallback;
    }

    @Override
    public void onWiFiAccount(String ssid, String password) {
        if (mSelectWiFiListener != null){
            Log.i(TAG, "onWiFiAccount: ssid = "+ssid+",password = "+password);
            mSelectWiFiListener.onSelectWifiSSID(ssid,password);
        }
    }

    public enum WiFiConnectStatus{
        WIFI_STATE_CONNECT,
        WIFI__STATE_UNCONNECT,
        WIFI_STATE_CONNECTING
    }

    private boolean mHasPermission;
    private boolean mHasPermissionAndOnOff;
    List<WiFiBean> realWifiList;
    private WifiListAdapter mAdapter;
    private WifiBroadcastReceiver wifiReceiver;

    private int connectType = 0;//1：连接成功 2 正在连接（如果wifi热点列表发生变需要该字段）

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realWifiList = new ArrayList<>();
        mAdapter = new WifiListAdapter(mContext,realWifiList);
        mSharePrefUtils = new SharePrefUtils(mContext);
        mHasPermissionAndOnOff = mSharePrefUtils.getWiFiPermissionAndOnOff(
                GlobalConstant.WiFiPermissionAndOnOff.WIFI_HAS_PERMISSION);
        mHasPermission = mSharePrefUtils.getWiFiPermissionAndOnOff(
                GlobalConstant.WiFiPermissionAndOnOff.WIFI_OPEN_OR_CLOSE);
        Log.i(TAG, "onCreate: mHasPermissionAndOnOff = "+mHasPermissionAndOnOff);
    }

    @Override
    public void onResume() {
        super.onResume();
        wifiReceiver = new WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态广播,是否连接了一个有效路由
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
        getActivity().registerReceiver(wifiReceiver, filter);

        Dialog mDialog = getDialog();
        if (mDialog != null){
            setDialogScreenSize(mDialog);
        }
    }

    private void setDialogScreenSize(Dialog mDialog) {
        mDialog.setCanceledOnTouchOutside(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mDialog.getWindow().setLayout(displayMetrics.widthPixels*2/3,displayMetrics.heightPixels*2/3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null){
            Log.i(TAG, "onCreateView: dialog");
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mView = getActivity().getLayoutInflater().inflate(R.layout.wifi_list_dialog_fragment,null,false);
        initView();
        return getAlertDialog(mView);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(wifiReceiver);
    }

    private void initView() {
        mProgressBar = mView.findViewById(R.id.refresh_wifi_list);
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration mDivider = new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL);
        mDivider.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        mRecyclerView.addItemDecoration(mDivider);

        if(WiFiSupport.isOpenWifi(mContext) && mHasPermissionAndOnOff){
            sortScaResult();
        }else{
            ToastUtils.showMessage(mContext,"WIFI处于关闭状态或权限获取失败");
        }

        mAdapter.setOnItemClickListener(new WifiListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Object o) {
                WiFiBean WiFiBean = realWifiList.get(postion);
                if(WiFiBean.getState().equals(GlobalConstant.WiFiConnectStatus.WIFI_STATE_UNCONNECT) || WiFiBean.getState().equals(GlobalConstant.WiFiConnectStatus.WIFI_STATE_CONNECT)){
                    String capabilities = realWifiList.get(postion).getCompatiables();
                    if(WiFiSupport.getWifiCipher(capabilities) == WiFiSupport.WifiCipherType.WIFICIPHER_NOPASS){//无需密码
                        WifiConfiguration tempConfig  = WiFiSupport.isExsits(WiFiBean.getSSID(),mContext);
                        if(tempConfig == null){
                            WifiConfiguration exsits = WiFiSupport.createWifiConfig(WiFiBean.getSSID(), null, WiFiSupport.WifiCipherType.WIFICIPHER_NOPASS);
                            WiFiSupport.addNetWork(exsits, mContext);
                        }else{
                            WiFiSupport.addNetWork(tempConfig, mContext);
                        }
                    }else{   //需要密码，弹出输入密码dialog
                        noConfigurationWifi(postion);
                    }
                }
            }
        });
    }

    public AlertDialog getAlertDialog(View view){
        return new AlertDialog.Builder(mContext)
                .setView(view)
                .setCancelable(false)
                .create();
    }

    //监听wifi状态
    public class WifiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state){
                    /**
                     * WIFI_STATE_DISABLED    WLAN已经关闭
                     * WIFI_STATE_DISABLING   WLAN正在关闭
                     * WIFI_STATE_ENABLED     WLAN已经打开
                     * WIFI_STATE_ENABLING    WLAN正在打开
                     * WIFI_STATE_UNKNOWN     未知
                     */
                    case WifiManager.WIFI_STATE_DISABLED:{
                        Log.d(TAG,"已经关闭");
                        ToastUtils.showMessage(mContext,"WIFI处于关闭状态");
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING:{
                        Log.d(TAG,"正在关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLED:{
                        Log.d(TAG,"已经打开");
                        sortScaResult();
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLING:{
                        Log.d(TAG,"正在打开");
                        break;
                    }
                    case WifiManager.WIFI_STATE_UNKNOWN:{
                        Log.d(TAG,"未知状态");
                        break;
                    }
                }
            }else if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                Log.d(TAG, "--NetworkInfo--" + info.toString());
                if(NetworkInfo.State.DISCONNECTED == info.getState()){//wifi没连接上
                    Log.d(TAG,"wifi没连接上");
                    hidingProgressBar();
                    for(int i = 0;i < realWifiList.size();i++){//没连接上将 所有的连接状态都置为“未连接”
                        realWifiList.get(i).setState(GlobalConstant.WiFiConnectStatus.WIFI_STATE_UNCONNECT);
                    }
                    mAdapter.notifyDataSetChanged();
                }else if(NetworkInfo.State.CONNECTED == info.getState()){//wifi连接上了
                    Log.d(TAG,"wifi连接上了");
                    hidingProgressBar();
                    WifiInfo connectedWifiInfo = WiFiSupport.getConnectedWifiInfo(mContext);

                    //连接成功 跳转界面 传递ip地址
                    ToastUtils.showMessage(mContext,"wifi连接上了");

                    connectType = 1;
                    wifiListSet(connectedWifiInfo.getSSID(),connectType);
                }else if(NetworkInfo.State.CONNECTING == info.getState()){//正在连接
                    Log.d(TAG,"wifi正在连接");
                    showProgressBar();
                    WifiInfo connectedWifiInfo = WiFiSupport.getConnectedWifiInfo(mContext);
                    connectType = 2;
                    wifiListSet(connectedWifiInfo.getSSID(),connectType );
                }
            }else if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())){
                Log.d(TAG,"网络列表变化了");
                wifiListChange();
            }
        }
    }

    private void noConfigurationWifi(int position) {//之前没配置过该网络， 弹出输入密码界面
        mWifiLinkDialog = new WifiLinkDialog(mContext,R.style.dialog_download,realWifiList.get(position).getSSID(), realWifiList.get(position).getCompatiables());
        mWifiLinkDialog.setWiFiAccountListener(this);
        if(!mWifiLinkDialog.isShowing()){
            mWifiLinkDialog.show();
        }
    }

    //网络状态发生改变 调用此方法！
    public void wifiListChange(){
        sortScaResult();
        WifiInfo connectedWifiInfo = WiFiSupport.getConnectedWifiInfo(mContext);
        if(connectedWifiInfo != null){
            wifiListSet(connectedWifiInfo.getSSID(),connectType);
        }
    }

    /**
     * 将"已连接"或者"正在连接"的wifi热点放置在第一个位置
     * @param wifiName
     * @param type
     */
    public void wifiListSet(String wifiName , int type){
        int index = -1;
        WiFiBean wifiInfo = new WiFiBean();
        if(CollectionUtils.isNullOrEmpty(realWifiList)){
            return;
        }
        for(int i = 0;i < realWifiList.size();i++){
            realWifiList.get(i).setState(GlobalConstant.WiFiConnectStatus.WIFI_STATE_UNCONNECT);
        }
        Collections.sort(realWifiList);//根据信号强度排序
        for(int i = 0;i < realWifiList.size();i++){
            WiFiBean WiFiBean = realWifiList.get(i);
            if(index == -1 && ("\"" + WiFiBean.getSSID() + "\"").equals(wifiName)){
                index = i;
                wifiInfo.setLevel(WiFiBean.getLevel());
                wifiInfo.setSSID(WiFiBean.getSSID());
                wifiInfo.setCompatiables(WiFiBean.getCompatiables());
                if(type == 1){
                    wifiInfo.setState(GlobalConstant.WiFiConnectStatus.WIFI_STATE_CONNECT);
                }else{
                    wifiInfo.setState(GlobalConstant.WiFiConnectStatus.WIFI_STATE_ON_CONNECTING);
                }
            }
        }
        if(index != -1){
            realWifiList.remove(index);
            realWifiList.add(0, wifiInfo);
            mAdapter.notifyDataSetChanged();
        }
    }

    //获取wifi列表然后将bean转成自己定义的WiFiBean
    public void sortScaResult(){
        List<ScanResult> scanResults = WiFiSupport.noSameName(WiFiSupport.getWifiScanResult(mContext));
        realWifiList.clear();
        if(!CollectionUtils.isNullOrEmpty(scanResults)){
            for(int i = 0;i < scanResults.size();i++){
                WiFiBean WiFiBean = new WiFiBean();
                WiFiBean.setSSID(scanResults.get(i).SSID);
                WiFiBean.setState(GlobalConstant.WiFiConnectStatus.WIFI_STATE_UNCONNECT);   //只要获取都假设设置成未连接，真正的状态都通过广播来确定
                WiFiBean.setCompatiables(scanResults.get(i).capabilities);
                WiFiBean.setLevel(WiFiSupport.getLevel(scanResults.get(i).level)+"");
                realWifiList.add(WiFiBean);

                //排序
                Collections.sort(realWifiList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hidingProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSelectWiFiListener != null){
            mSelectWiFiListener = null;
        }
        mHasPermissionAndOnOff = false;
        mHasPermission = false;
        if (realWifiList != null) {
            realWifiList.clear();
            realWifiList = null;
        }
        if (mWifiLinkDialog != null){
            if (mWifiLinkDialog.isShowing()){
                mWifiLinkDialog.dismiss();
            }
            mWifiLinkDialog.release();
            mWifiLinkDialog = null;
        }
        mAdapter = null;
    }
}
