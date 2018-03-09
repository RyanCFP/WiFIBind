package com.ryan.wifibind.wifi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ryan.wifibind.R;
import com.ryan.wifibind.room.entity.WiFiInfo;

import org.w3c.dom.Text;

/**
 * Created by ryan on 18-3-5.
 * Email: Ryan_chan01212@yeah.net
 */

public class DeviceInfoDialog extends Dialog {
    private TextView mDevice_kind;
    private TextView mAccount_user;
    private Button dialog_confirm;
    private Context mContext;
    private WiFiInfo mWiFiInfo;

    public DeviceInfoDialog(@NonNull Context context, int themeResId,WiFiInfo wiFiInfo) {
        super(context, themeResId);
        mContext = context;
        mWiFiInfo = wiFiInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bind_device_info,null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {
        mDevice_kind = view.findViewById(R.id.item_device_kind);
        mAccount_user = view.findViewById(R.id.item_account_user);
        dialog_confirm = view.findViewById(R.id.item_dialog_confirm);

        dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (mWiFiInfo != null){
            mDevice_kind.setText(mWiFiInfo.device);
            mAccount_user.setText(mWiFiInfo.username);
        }
    }

    public void release(){
        mContext = null;
        mWiFiInfo = null;
    }
}
