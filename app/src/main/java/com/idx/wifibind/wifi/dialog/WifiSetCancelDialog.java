package com.idx.wifibind.wifi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.idx.wifibind.R;

/**
 * Created by ryan on 18-3-3.
 * Email: Ryan_chan01212@yeah.net
 */

public class WifiSetCancelDialog extends Dialog implements View.OnClickListener{
    private static final String TAG = WifiSetCancelDialog.class.getSimpleName();
    private Button cancel;
    private Button confirm;
    private boolean mIsCancelled;
    private WiFiBindCancelCallback mWiFiBindCancelCallback;
    public interface WiFiBindCancelCallback{
        void onWiFiBindCancel(boolean isCanceled);
    }

    public WifiSetCancelDialog(@NonNull Context context, int themeResId,WiFiBindCancelCallback wiFiBindCancelCallback) {
        super(context, themeResId);
        mWiFiBindCancelCallback = wiFiBindCancelCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.item_wifi_set_cancel,null,false);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {
        cancel = view.findViewById(R.id.wifi_set_cancel);
        confirm = view.findViewById(R.id.wifi_set_confirm);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wifi_set_cancel:
                mIsCancelled = false;
                dismiss();
                break;
            case R.id.wifi_set_confirm:
                mIsCancelled = true;
                dismiss();
                break;
            default:break;
        }
        if (mWiFiBindCancelCallback != null){
            mWiFiBindCancelCallback.onWiFiBindCancel(mIsCancelled);
        }
    }
}
