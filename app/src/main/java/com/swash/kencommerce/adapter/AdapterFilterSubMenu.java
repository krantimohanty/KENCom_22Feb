package com.swash.kencommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.swash.kencommerce.FilterActivity;
import com.swash.kencommerce.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kranti on 12/12/2016.
 */
public class AdapterFilterSubMenu extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    String type = "";

    public AdapterFilterSubMenu(Activity a, ArrayList<HashMap<String, String>> d, String type) {
        activity = a;
        data=d;
        this.type = type;
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
        TextView txt_submenu;
        ImageView img_check;
    }

    Boolean isChecked = false;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_filter_submenu , null);
            holder = new ViewHolder();

            holder.txt_submenu = (TextView) convertView.findViewById(R.id.txt_submenu);
            holder.img_check = (ImageView) convertView.findViewById(R.id.img_check);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, String> mapOrderHistory = new HashMap<String, String>();
        mapOrderHistory = data.get(position);

        holder.txt_submenu.setText(mapOrderHistory.get(FilterActivity.Key_filter_submenu));

        final ViewHolder finalHolder = holder;
        holder.img_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked) {
                    isChecked = false;
                    finalHolder.img_check.setImageResource(R.drawable.filter_check_inactive);
                } else {
                    isChecked = true;
                    finalHolder.img_check.setImageResource(R.drawable.filter_check_active);
                }
            }
        });

        return convertView;
    }
}
