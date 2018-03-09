package com.ryan.wifibind.wifi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.ryan.wifibind.R;
import com.ryan.wifibind.callback.DeviceUnbindCallback;

/**
 * Created by ryan on 18-3-7.
 * Email: Ryan_chan01212@yeah.net
 */

public class DeviceUnbindDialog extends Dialog implements View.OnClickListener{
    private static final String TAG = DeviceUnbindDialog.class.getSimpleName();
    private Button mCancel;
    private Button mConfirm;
    private Context mContext;
    private DeviceUnbindCallback mDeviceUnbindCallback;
    private boolean mIsDeviceUnbind;

    public DeviceUnbindDialog(@NonNull Context context, int themeResId, DeviceUnbindCallback deviceUnbindCallback) {
        super(context, themeResId);
        mContext = context;
        mDeviceUnbindCallback = deviceUnbindCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_unbind_dialog,null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {
        mCancel = view.findViewById(R.id.item_delete_cancel);
        mConfirm = view.findViewById(R.id.item_delete_confirm);

        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_delete_cancel:
                mIsDeviceUnbind = false;
                dismiss();
                break;
            case R.id.item_delete_confirm:
                mIsDeviceUnbind = true;
                dismiss();
                break;
            default:break;
        }
        if (mDeviceUnbindCallback != null){
            mDeviceUnbindCallback.onDeviceUnbind(mIsDeviceUnbind);
        }
    }

    public void release(){
        mContext = null;
        mDeviceUnbindCallback = null;
        mIsDeviceUnbind = false;
    }
}
