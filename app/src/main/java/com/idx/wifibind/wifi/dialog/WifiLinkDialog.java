package com.idx.wifibind.wifi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.idx.wifibind.R;
import com.idx.wifibind.wifi.WiFiSupport;

/**
 * Created by ryan on 18-3-3.
 * Email: Ryan_chan01212@yeah.net
 */
public class WifiLinkDialog extends Dialog implements View.OnClickListener{
    private static final String TAG = WifiLinkDialog.class.getSimpleName();
    private TextView wifi_ssid;
    private EditText wifi_password;
    private Button cancel_button;
    private Button confirm_button;
    private String ssid;
    private String password;
    private String capabilities;
    private Context mContext;
    private WiFiAccountListener mWiFiAccountListener;
    private InputMethodManager mInputMethodManager;
    public interface WiFiAccountListener{
        void onWiFiAccount(String ssid,String password);
    }

    public void setWiFiAccountListener(WiFiAccountListener wiFiAccountListener){
        mWiFiAccountListener = wiFiAccountListener;
    }

    public WifiLinkDialog(@NonNull Context context, @StyleRes int themeResId, String ssid, String capabilities) {
        super(context, themeResId);
        this.ssid = ssid;
        this.capabilities = capabilities;
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(mContext).inflate(R.layout.setting_wifi_link_dialog, null);
        setContentView(view);
        initView(view);
        setListener();
    }

    private void setListener() {
        cancel_button.setOnClickListener(this);
        confirm_button.setOnClickListener(this);
        wifi_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if((capabilities.contains("WPA") || capabilities.contains("WPA2") || capabilities.contains("WPS"))){
                    if(wifi_password.getText() == null && wifi_password.getText().toString().length() < 8){
                        confirm_button.setClickable(false);
                    }else{
                        confirm_button.setClickable(true);
                    }
                }else if(capabilities.contains("WEP")){
                    if(wifi_password.getText() == null && wifi_password.getText().toString().length() < 8){
                        confirm_button.setClickable(false);
                    }else{
                        confirm_button.setClickable(true);
                    }
                }
                password = s.toString();
                Log.i(TAG, "afterTextChanged: password = "+password);
            }
        });
    }

    private void initView(View view) {
        wifi_ssid =  view.findViewById(R.id.wifi_link_ssid);
        wifi_password = view.findViewById(R.id.wifi_link_password);
        cancel_button =  view.findViewById(R.id.wifi_link_cancel);
        confirm_button = view.findViewById(R.id.wifi_link_confirm);

        wifi_ssid.setText(ssid);
        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wifi_link_confirm:{
                WifiConfiguration exitsConfig  = WiFiSupport.isExsits(ssid,getContext());
                if(exitsConfig == null){
                    WifiConfiguration wifiConfiguration =  WiFiSupport.createWifiConfig(ssid,password,
                            WiFiSupport.getWifiCipher(capabilities));
                    WiFiSupport.addNetWork(wifiConfiguration,getContext());
                }else{
                    WiFiSupport.addNetWork(exitsConfig,getContext());
                }
                if (mWiFiAccountListener != null){
                    mWiFiAccountListener.onWiFiAccount(ssid,password);
                }
                toggleSoftInputstatus();
                dismiss();
                break;
            }
            case R.id.wifi_link_cancel:{
                toggleSoftInputstatus();
                dismiss();
                break;
            }
        }
    }

    //当前输入法正在显示就隐藏，隐藏就显示
    public void toggleSoftInputstatus(){
        Log.i(TAG, "toggleSoftInputstatus: ");
        mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void release(){
        ssid = null;
        mContext = null;
        mWiFiAccountListener = null;
        mInputMethodManager = null;
    }
}
