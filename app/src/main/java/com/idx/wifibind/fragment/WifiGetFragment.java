package com.idx.wifibind.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.idx.wifibind.R;
import com.idx.wifibind.room.classic.WifiInfoInjection;
import com.idx.wifibind.util.EmptyCheckUtils;
import com.idx.wifibind.util.GlobalConstant;
import com.idx.wifibind.util.SharePrefUtils;
import com.idx.wifibind.util.ToastUtils;
import com.idx.wifibind.wifi.WiFiSupport;
import com.idx.wifibind.BaseFragment;
import com.idx.wifibind.room.callback.WifiInfoDataSource;
import com.idx.wifibind.room.classic.WifiInfoRepository;
import com.idx.wifibind.room.entity.WiFiInfo;

/**
 * Created by ryan on 18-2-28.
 * Email: Ryan_chan01212@yeah.net
 */

public class WifiGetFragment extends BaseFragment implements SelectWiFiListener {
    private static final String TAG = WifiGetFragment.class.getSimpleName();
    private View view;
    private Context mContext;
    private TextView current_wifi_ssid;
    private EditText wifi_ssid;
    private EditText wifi_password;
    private ImageView wifi_list;
    private CheckBox wifi_password_show_status;
    private Button wifi_btn_next;
    private SharePrefUtils mSharePrefUtils;
    private BtnNextLisener mBtnNextLisener;
    private String ssid;
    private String password;
    private WifiListDialogFragment mWifiListDialog;
    private boolean mHasPermission;
    private boolean mHasPermissionAndOnOff;
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
        mWifiListDialog = new WifiListDialogFragment();
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mHasPermission =  mSharePrefUtils.getWiFiPermissionAndOnOff(
                GlobalConstant.WiFiPermissionAndOnOff.WIFI_HAS_PERMISSION);
        Log.i(TAG, "onCreate: mHasPermission = "+mHasPermission);
        /*if (WiFiSupport.isOpenWifi(mContext) && mHasPermission){*/
        mHasPermissionAndOnOff = mSharePrefUtils.getWiFiPermissionAndOnOff(
                GlobalConstant.WiFiPermissionAndOnOff.WIFI_OPEN_OR_CLOSE);
        Log.i(TAG, "onCreate: mHasPermissionAndOnOff = "+mHasPermissionAndOnOff);
        if (mHasPermission ){
            if (mHasPermissionAndOnOff){
                ssid = WiFiSupport.getCurentWifiSSID(mContext);
                Log.i(TAG, "onCreate: isWiFiOpen:SSID = "+ssid);
            } else{
                ToastUtils.showMessage(mContext,"WIFI处于关闭状态,请打开WiFi");
                WiFiSupport.openWifi(mContext);
            }
        }else {
            ToastUtils.showMessage(mContext,"获取权限失败或WiFi未打开,请去设置中设置");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wifi_get,container,false);
        initView();
        return view;
    }

    private void initView() {
        current_wifi_ssid = view.findViewById(R.id.current_wifi_ssid);
        wifi_ssid = view.findViewById(R.id.wifi_ssid);
        wifi_password = view.findViewById(R.id.wifi_password);
        wifi_btn_next = view.findViewById(R.id.wifi_device_get);
        wifi_list = view.findViewById(R.id.wifi_list);
        wifi_password_show_status = view.findViewById(R.id.wifi_password_show_status);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListener();
        if (EmptyCheckUtils.isEmptyOrNull(ssid)) {
            wifi_ssid.setText(ssid);
        }

        mWifiInfoRepository.queryWifiInfo(ssid, new WifiInfoDataSource.LoadWifiInfoCallback() {
            @Override
            public void onWifiInfoLoaded(WiFiInfo wifiInfo) {
                if (EmptyCheckUtils.isEmptyOrNull(wifiInfo.password)) {
                    wifi_password.setText(wifiInfo.password);
                    wifi_password_show_status.setBackgroundResource(R.mipmap.wifi_password_show);
                    wifi_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }

            @Override
            public void onDataNotFound() {

            }
        });

        if (savedInstanceState != null){
            ssid = savedInstanceState.getString(GlobalConstant.WifiBind.WIFI_SSID);
            password = savedInstanceState.getString(GlobalConstant.WifiBind.WIFI_PASSWORD);
            if (EmptyCheckUtils.isEmptyOrNull(ssid)){
                wifi_ssid.setText(ssid);
            }
            if (EmptyCheckUtils.isEmptyOrNull(password)) {
                wifi_password.setText(password);
            }
        }
    }

    private void setListener() {
       wifi_ssid.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {}

           @Override
           public void afterTextChanged(Editable s) {
                ssid = s.toString();
               Log.i(TAG, "afterTextChanged: ssid = "+ssid);
               mSharePrefUtils.saveBindWifi(GlobalConstant.WifiBind.WIFI_SSID,ssid);
           }
       });
        wifi_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();
                Log.i(TAG, "afterTextChanged: password = "+password);
                mSharePrefUtils.saveBindWifi(GlobalConstant.WifiBind.WIFI_PASSWORD,password);
            }
        });
        wifi_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case KeyEvent.ACTION_DOWN:
                        toggleSoftInputstatus();
                        break;
                    default:break;
                }
                return false;
            }
        });
        wifi_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSoftInputstatus();
                if (mWifiListDialog != null){
                    mWifiListDialog.setSelectWiFiListener(WifiGetFragment.this);
                    mWifiListDialog.show(getActivity().getSupportFragmentManager(),"wifi_list");
                }
            }
        });
        wifi_password_show_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "onCheckedChanged: isCHecked = "+isChecked);
                if (isChecked){
                    wifi_password_show_status.setBackgroundResource(R.mipmap.wifi_password_show);
                    wifi_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    wifi_password_show_status.setBackgroundResource(R.mipmap.wifi_password_hide);
                    wifi_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        wifi_btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EmptyCheckUtils.isEmptyOrNull(ssid)){
                    ToastUtils.showMessage(mContext,"WIFI SSID 不能为空");
                    return;
                }
                if (!EmptyCheckUtils.isEmptyOrNull(password)){
                    ToastUtils.showMessage(mContext,"WIFI 密码不能为空");
                    return;
                }
                if (mBtnNextLisener != null){
                    mBtnNextLisener.onBtnNextClickLisener(R.id.wifi_device_get);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GlobalConstant.WifiBind.WIFI_SSID,ssid);
        outState.putString(GlobalConstant.WifiBind.WIFI_PASSWORD,password);
    }

    @Override
    public void onPause() {
        super.onPause();
        ToastUtils.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSharePrefUtils != null){
            mSharePrefUtils = null;
        }
        if (mBtnNextLisener != null){
            mBtnNextLisener = null;
        }
        if (mWifiListDialog != null){
            mWifiListDialog = null;
        }
        mHasPermission = false;
        mHasPermissionAndOnOff = false;
        mWifiInfoRepository = null;
    }

    @Override
    public void onSelectWifiSSID(String SSID,String password) {
        Log.i(TAG, "onSelectWifiSSID: SSID = "+SSID+",password = "+password);
        if (EmptyCheckUtils.isEmptyOrNull(SSID) && EmptyCheckUtils.isEmptyOrNull(password)){
            this.ssid = SSID;
            this.password = password;
            wifi_ssid.setText(ssid);
            wifi_password.setText(password);
            mSharePrefUtils.saveBindWifi(GlobalConstant.WifiBind.WIFI_SSID,ssid);
            mSharePrefUtils.saveBindWifi(GlobalConstant.WifiBind.WIFI_PASSWORD,password);
        }
    }
}
