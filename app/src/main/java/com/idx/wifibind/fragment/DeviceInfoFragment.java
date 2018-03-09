package com.idx.wifibind.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.idx.wifibind.R;
import com.idx.wifibind.util.EmptyCheckUtils;
import com.idx.wifibind.BaseFragment;
import com.idx.wifibind.util.GlobalConstant;
import com.idx.wifibind.util.SharePrefUtils;
import com.idx.wifibind.util.ToastUtils;

/**
 * Created by ryan on 18-2-28.
 * Email: Ryan_chan01212@yeah.net
 */

public class DeviceInfoFragment extends BaseFragment {
    private static final String TAG = DeviceInfoFragment.class.getSimpleName();
    private Context mContext;
    private View view;
    private EditText device_kind;
    private Button btn_next;
    private String device_kind_name;
    private SharePrefUtils mSharePrefUtils;
    private BtnNextLisener mBtnNextLisener;

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
        mSharePrefUtils = new SharePrefUtils(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.device_info,container,false);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addLisenter();
    }

    private void addLisenter() {
        device_kind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                device_kind_name = s.toString();
                Log.i(TAG, "afterTextChanged: device_kind_name = "+device_kind_name);
                mSharePrefUtils.saveBindWifi(GlobalConstant.WifiBind.DEVICE_KIND_NAME,device_kind_name);
            }
        });

        device_kind.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EmptyCheckUtils.isEmptyOrNull(device_kind_name)){
                    ToastUtils.showMessage(mContext,"产品型号名称不能为空");
                    return;
                }
                if (mBtnNextLisener != null) {
                    mBtnNextLisener.onBtnNextClickLisener(R.id.wifi_device_info);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        ToastUtils.release();
    }

    private void initView() {
        device_kind = view.findViewById(R.id.device_kind_input);
        btn_next = view.findViewById(R.id.wifi_device_info);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSharePrefUtils != null){
            mSharePrefUtils = null;
        }
        if(mBtnNextLisener != null) {
            mBtnNextLisener = null;
        }
    }
}
