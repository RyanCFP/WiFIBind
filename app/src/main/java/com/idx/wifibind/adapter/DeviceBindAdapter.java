package com.ryan.wifibind.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ryan.wifibind.R;
import com.ryan.wifibind.room.entity.WiFiInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 18-3-5.
 * Email: Ryan_chan01212@yeah.net
 */

public class DeviceBindAdapter extends BaseAdapter {
    private static final String TAG = DeviceBindAdapter.class.getSimpleName();
    private Context mContext;
    private List<WiFiInfo> mDeviceBindList;

    public DeviceBindAdapter(Context context, List<WiFiInfo> deviceBindList){
        mContext = context;
        setDeviceBindList(deviceBindList);
    }

    public void setDeviceBindList(List<WiFiInfo> deviceBindList){
        if (mDeviceBindList == null) {
            mDeviceBindList = new ArrayList<>();
        }
        mDeviceBindList = deviceBindList;
        Log.i("ryan.chan", "setDeviceBindList: size = "+mDeviceBindList.size());
    }
    //刷新数据
    public void refreshData(List<WiFiInfo> deviceBindList){
        Log.i("ryan.chan", "refreshData: size = "+deviceBindList.size());
        setDeviceBindList(deviceBindList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDeviceBindList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeviceBindList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_device_bind,null);
            myHolder = new MyHolder(convertView);
            convertView.setTag(myHolder);
        }
        myHolder = (MyHolder) convertView.getTag();

        WiFiInfo wifiInfo = (WiFiInfo) getItem(position);
        if (wifiInfo != null) {
            Log.i("ryan.chan", "getView: device_kind = "+wifiInfo.device);
            myHolder.mItemDevice.setText(wifiInfo.device);
        }

        return convertView;
    }

    public class MyHolder{
        private TextView mItemDevice;

        public MyHolder(View view){
            mItemDevice = view.findViewById(R.id.item_device_bind);
        }
    }
}
