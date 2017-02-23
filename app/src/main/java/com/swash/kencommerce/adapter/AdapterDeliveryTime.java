package com.swash.kencommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swash.kencommerce.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kranti on 12/12/2016.
 */
public class AdapterDeliveryTime extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    public AdapterDeliveryTime(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder
    {
        TextView txt_start_time, txt_end_time, txt_amt;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_delivery_time , null);
            holder = new ViewHolder();

            holder.txt_start_time = (TextView) convertView.findViewById(R.id.txt_start_time);
            holder.txt_end_time = (TextView) convertView.findViewById(R.id.txt_end_time);
            holder.txt_amt = (TextView) convertView.findViewById(R.id.txt_amt);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt_end_time.setTag(position);
        HashMap<String, String> mapDeliveryCharge = new HashMap<String, String>();
        mapDeliveryCharge = data.get(position);

        holder.txt_start_time.setText(mapDeliveryCharge.get("start_time"));
        holder.txt_end_time.setText(mapDeliveryCharge.get("end_time"));
        holder.txt_amt.setText(mapDeliveryCharge.get("amount"));



        return convertView;
    }
}
