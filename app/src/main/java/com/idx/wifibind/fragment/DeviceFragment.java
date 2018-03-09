package com.idx.wifibind.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.idx.wifibind.BaseFragment;
import com.idx.wifibind.R;
import com.idx.wifibind.activity.WiFiBindActivity;
import com.idx.wifibind.adapter.DeviceBindAdapter;
import com.idx.wifibind.callback.DeviceUnbindCallback;
import com.idx.wifibind.room.callback.WifiInfoDataSource;
import com.idx.wifibind.room.classic.WifiInfoInjection;
import com.idx.wifibind.room.classic.WifiInfoRepository;
import com.idx.wifibind.room.entity.WiFiInfo;
import com.idx.wifibind.util.GlobalConstant;
import com.idx.wifibind.util.SharePrefUtils;
import com.idx.wifibind.wifi.dialog.DeviceInfoDialog;
import com.idx.wifibind.wifi.dialog.DeviceUnbindDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 18-3-8.
 * Email: Ryan_chan01212@yeah.net
 */

public class DeviceFragment extends BaseFragment {
    private static final String TAG = DeviceFragment.class.getSimpleName();
    public final int REFRESH_DEVICE_LIST = 0x12;
    private Context mContext;
    private ImageButton device_bind;
    private ListView mListView;
    private RelativeLayout rl_device_bind_none;
    private ImageView mDevice_bind_none_add;
    private DeviceBindAdapter mAdpter;
    private List<WiFiInfo> mDeviceBindList;
    private WifiInfoRepository mWifiInfoRepository;
    private DeviceInfoDialog mDeviceInfoDialog;
    private DeviceUnbindDialog mDeviceUnbindDialog;
    private SharePrefUtils mSharePrefUtils;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH_DEVICE_LIST:
                    // TODO: 18-3-7 设备绑定成功
                    mWifiInfoRepository.queryWifiInfoListInfo(new WifiInfoDataSource.LoadWifiInfoListCallback() {
                        @Override
                        public void onWifiInfoLoaded(List<WiFiInfo> wifiInfoList) {
                            listShow();
                            if (mDeviceBindList != null){
                                mDeviceBindList.clear();
                            }
                            Log.i("ryan.chan", "onWifiInfoLoaded: size = "+wifiInfoList.size());
                            mDeviceBindList = wifiInfoList;
                            mAdpter.refreshData(mDeviceBindList);
                        }

                        @Override
                        public void onDataNotFound() {
                            listGone();
                        }
                    });
                    break;
                default:break;
            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharePrefUtils = new SharePrefUtils(mContext);
        mWifiInfoRepository = WifiInfoInjection.getRoomRepository(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSharePrefUtils.getBindSuccess(GlobalConstant.BindSuccess.DEVICE_BIND_SUCCESS)){
            mSharePrefUtils.savaBindSuccess(GlobalConstant.BindSuccess.DEVICE_BIND_SUCCESS,false);
            mHandler.sendEmptyMessage(REFRESH_DEVICE_LIST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_fragment,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void startBindActivity(){
        startActivity(new Intent(mContext,WiFiBindActivity.class));
    }

    private void initView(View view) {
        device_bind = view.findViewById(R.id.device_bind);
        device_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startBindActivity();}
        });
        rl_device_bind_none = view.findViewById(R.id.rl_device_bind_none);
        mDevice_bind_none_add = view.findViewById(R.id.device_bind_none_add);

        mDevice_bind_none_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBindActivity();
            }
        });

        mListView = view.findViewById(R.id.bind_device_list);
        mDeviceBindList = new ArrayList<>();
        mAdpter =  new DeviceBindAdapter(mContext,mDeviceBindList);
        mListView.setAdapter(mAdpter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WiFiInfo wiFiInfo = mDeviceBindList.get(position);
                if (wiFiInfo != null){
                    mDeviceInfoDialog = new DeviceInfoDialog(mContext,
                            R.style.dialog_download,wiFiInfo);
                    if (!mDeviceInfoDialog.isShowing()){
                        mDeviceInfoDialog.show();
                    }
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final WiFiInfo wiFiInfo = mDeviceBindList.get(position);
                mDeviceUnbindDialog = new DeviceUnbindDialog(mContext, R.style.dialog_download,
                        new DeviceUnbindCallback() {
                            @Override
                            public void onDeviceUnbind(boolean isUnbinded) {
                                if (isUnbinded){
                                    if (wiFiInfo != null) {
                                        mWifiInfoRepository.deleteWiFiInfo(wiFiInfo);
                                    }
                                    mHandler.sendEmptyMessage(REFRESH_DEVICE_LIST);
                                }
                            }
                        });
                if (!mDeviceUnbindDialog.isShowing()){
                    mDeviceUnbindDialog.show();
                }
                return true;
            }
        });


    }

    private void initData() {
        //所有已绑定设备
        mWifiInfoRepository.queryWifiInfoListInfo(new WifiInfoDataSource.LoadWifiInfoListCallback() {
            @Override
            public void onWifiInfoLoaded(List<WiFiInfo> wifiInfoList) {
                listShow();
                mDeviceBindList = wifiInfoList;
                mAdpter.refreshData(wifiInfoList);
            }

            @Override
            public void onDataNotFound() {
                Log.i(TAG, "onDataNotFound: 没有绑定任何设备");
                listGone();
            }
        });
    }

    public void listShow(){
        rl_device_bind_none.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    public void listGone(){
        rl_device_bind_none.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mWifiInfoRepository = null;
        mSharePrefUtils = null;
        mAdpter = null;
        if (mDeviceBindList != null){
            mDeviceBindList.clear();
            mDeviceBindList = null;
        }
        if (mDeviceInfoDialog != null){
            mDeviceInfoDialog.release();
            mDeviceInfoDialog = null;
        }
        if (mDeviceUnbindDialog != null){
            mDeviceInfoDialog.release();
            mDeviceInfoDialog = null;
        }
        mHandler = null;
    }
}
