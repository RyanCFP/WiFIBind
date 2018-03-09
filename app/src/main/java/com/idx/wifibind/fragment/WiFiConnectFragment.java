package com.idx.wifibind.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.idx.wifibind.R;
import com.idx.wifibind.util.SharePrefUtils;
import com.idx.wifibind.util.ToastUtils;
import com.idx.wifibind.BaseFragment;
import com.idx.wifibind.room.classic.WifiInfoInjection;
import com.idx.wifibind.room.classic.WifiInfoRepository;
import com.idx.wifibind.room.entity.WiFiInfo;
import com.idx.wifibind.util.GlobalConstant;

import java.util.Random;

/**
 * Created by ryan on 18-3-3.
 * Email: Ryan_chan01212@yeah.net
 */

public class WiFiConnectFragment extends BaseFragment {
    private static final String TAG = WiFiConnectFragment.class.getSimpleName();
    private Context mContext;
    private View view;
    private Button mDevice_connect;
    private BtnNextLisener mBtnNextLisener;
    private SharePrefUtils mSharePrefUtils;
    private WiFiInfo mWiFiInfo;
    private WifiInfoRepository mWifiInfoRepository;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (getActivity() instanceof BtnNextLisener){
            mBtnNextLisener = (BtnNextLisener) getActivity();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharePrefUtils = new SharePrefUtils(mContext);
        mWifiInfoRepository = WifiInfoInjection.getRoomRepository(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wifi_connect,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mWiFiInfo = null;
        mDevice_connect = view.findViewById(R.id.wifi_device_connect);
        mDevice_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWiFiInfo = new WiFiInfo();
                mWiFiInfo.device = mSharePrefUtils.getBindWifi(GlobalConstant.WifiBind.DEVICE_KIND_NAME);
                mWiFiInfo.ssid = mSharePrefUtils.getBindWifi(GlobalConstant.WifiBind.WIFI_SSID);
                mWiFiInfo.password = mSharePrefUtils.getBindWifi(GlobalConstant.WifiBind.WIFI_PASSWORD);
                mWiFiInfo.username = ""+new Random().nextInt(1000);
                try{
                    mWifiInfoRepository.insertWifiInfo(mWiFiInfo);
                    Log.i(TAG, "onClick: insertInfo");
                }catch (NullPointerException exception){
                    exception.printStackTrace();
                }catch (Exception exception){
                    exception.printStackTrace();
                }
                if (mBtnNextLisener != null){
                    Log.i("ryan.chan", "onClick: "+TAG);
                    mBtnNextLisener.onBtnNextClickLisener(R.id.wifi_device_connect);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        ToastUtils.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext = null;
        if (mBtnNextLisener != null){
            mBtnNextLisener = null;
        }
        mSharePrefUtils = null;
        mWifiInfoRepository = null;
    }
}
