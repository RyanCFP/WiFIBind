package com.idx.wifibind.wifi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idx.wifibind.R;
import com.idx.wifibind.util.GlobalConstant;
import com.idx.wifibind.wifi.bean.WiFiBean;

import java.util.List;

/**
 * Created by ryan on 18-3-3.
 * Email: Ryan_chan01212@yeah.net
 */
public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.MyViewHolder> {

    private Context mContext;
    private List<WiFiBean> resultList;
    private onItemClickListener onItemClickListener;

    public interface onItemClickListener{
        void onItemClick(View view, int postion, Object o);
    }

    public void setOnItemClickListener(WifiListAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public WifiListAdapter(Context mContext, List<WiFiBean> resultList) {
        this.mContext = mContext;
        this.resultList = resultList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_wifi_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WiFiBean bean = resultList.get(position);
        holder.tvItemWifiName.setText(bean.getSSID());
        holder.tvItemWifiStatus.setText("("+bean.getState()+")");

        //可以传递给adapter的数据都是经过处理的，已连接或者正在连接状态的wifi都是处于集合中的首位
        if(position == 0  && (GlobalConstant.WiFiConnectStatus.WIFI_STATE_ON_CONNECTING.equals(bean.getState())
                || GlobalConstant.WiFiConnectStatus.WIFI_STATE_CONNECT.equals(bean.getState()))){
            holder.tvItemWifiName.setTextColor(mContext.getResources().getColor(R.color.wifi_connect_color));
            holder.tvItemWifiStatus.setTextColor(mContext.getResources().getColor(R.color.wifi_connect_color));
        }else{
            holder.tvItemWifiName.setTextColor(mContext.getResources().getColor(R.color.wifi_unconnect_color));
            holder.tvItemWifiStatus.setTextColor(mContext.getResources().getColor(R.color.wifi_unconnect_color));
        }

        holder.itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view,position,bean);
            }
        });
    }

    public void replaceAll(List<WiFiBean> datas) {
        if (resultList.size() > 0) {
            resultList.clear();
        }
        resultList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        View itemview;
        TextView tvItemWifiName, tvItemWifiStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            tvItemWifiName = itemView.findViewById(R.id.wifi_item_ssid_name);
            tvItemWifiStatus = itemView.findViewById(R.id.wifi_item_status);
        }
    }
}
