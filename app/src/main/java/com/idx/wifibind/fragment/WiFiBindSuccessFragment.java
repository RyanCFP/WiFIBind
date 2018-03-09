package com.idx.wifibind.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.idx.wifibind.R;
import com.idx.wifibind.util.EmptyCheckUtils;
import com.idx.wifibind.util.SharePrefUtils;
import com.idx.wifibind.BaseFragment;
import com.idx.wifibind.room.callback.WifiInfoDataSource;
import com.idx.wifibind.room.classic.WifiInfoInjection;
import com.idx.wifibind.room.classic.WifiInfoRepository;
import com.idx.wifibind.room.entity.WiFiInfo;
import com.idx.wifibind.util.GlobalConstant;

/**
 * Created by ryan on 18-3-3.
 * Email: Ryan_chan01212@yeah.net
 */

public class WiFiBindSuccessFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = WiFiBindSuccessFragment.class.getSimpleName();
    private Context mContext;
    private TextView user_account_name;
    private TextView device_kind_name;
    private Button wifi_bind_success;
    private BtnNextLisener mBtnNextLisener;
    private WifiInfoRepository mWifiInfoRepository;
    private SharePrefUtils mSharePreUtils;
    private String ssid;

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
        mWifiInfoRepository = WifiInfoInjection.getRoomRepository(mContext);
        mSharePreUtils = new SharePrefUtils(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wifi_bind_success,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        user_account_name = view.findViewById(R.id.user_account_show);
        device_kind_name = view.findViewById(R.id.device_kind_show);
        wifi_bind_success = view.findViewById(R.id.wifi_device_success);
        wifi_bind_success.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        ssid = mSharePreUtils.getBindWifi(GlobalConstant.WifiBind.WIFI_SSID);
        if (EmptyCheckUtils.isEmptyOrNull(ssid)) {
            mWifiInfoRepository.queryWifiInfo(ssid, new WifiInfoDataSource.LoadWifiInfoCallback() {
                @Override
                public void onWifiInfoLoaded(WiFiInfo wifiInfo) {
                    if (wifiInfo != null) {
                        user_account_name.setText(wifiInfo.username);
                        device_kind_name.setText(wifiInfo.device);
                    }
                }

                @Override
                public void onDataNotFound() {
                    if (mSharePreUtils != null) {
                        user_account_name.setText(ssid);
                        String device = mSharePreUtils.getBindWifi(GlobalConstant.WifiBind.DEVICE_KIND_NAME);
                        if (EmptyCheckUtils.isEmptyOrNull(device)) {
                            device_kind_name.setText(device);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext = null;
        mWifiInfoRepository = null;
        mSharePreUtils = null;
        mBtnNextLisener = null;
        ssid = "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wifi_device_success:
                mSharePreUtils.savaBindSuccess(GlobalConstant.BindSuccess.DEVICE_BIND_SUCCESS,true);
                if (mBtnNextLisener != null){
                    mBtnNextLisener.onBtnNextClickLisener(R.id.wifi_device_success);
                }
                break;
            default:break;
        }
    }
}
