package com.swash.kencommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.swash.kencommerce.R;
import com.swash.kencommerce.Utils.Constants;

import java.util.ArrayList;

/**
 * Created by Kranti on 12/12/2016.
 */
public class AdapterDeliveryCharge extends BaseAdapter {
    private Activity activity;
    private ArrayList<String> data;
    private static LayoutInflater inflater=null;
    TextView ext_delivery;

    public AdapterDeliveryCharge(Activity a, ArrayList<String> d, TextView ext_del) {
        activity = a;
        data=d;
        this.ext_delivery=ext_del;
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
        TextView txt_start_time;
        ImageView img_check;
    }

    @Override
    public View getView(int position, View convertView,final ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_delivery_session , null);
            holder = new ViewHolder();

            holder.txt_start_time = (TextView) convertView.findViewById(R.id.txt_start_time);
            holder.img_check=(ImageView)convertView.findViewById(R.id.img_check);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt_start_time.setText(data.get(position));
        holder.txt_start_time.setTag(position);
        holder.txt_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a=Integer.parseInt(v.getTag().toString());
                for(int i=0; i<4; i++){

                    if(i==a){
                        ImageView img=(ImageView)parent.getChildAt(i).findViewById(R.id.img_check);
                        img.setVisibility(View.VISIBLE);
                    }else{
                        ImageView img=(ImageView)parent.getChildAt(i).findViewById(R.id.img_check);
                        img.setVisibility(View.GONE);
                    }
                }

                if(ext_delivery.getText().toString().contains("Morning")||ext_delivery.getText().toString().contains("Midday")||
                        ext_delivery.getText().toString().contains("Afternoon")||ext_delivery.getText().toString().contains("Evening")){
                    ext_delivery.setText(ext_delivery.getText().toString().trim());
                    String[] day=ext_delivery.getText().toString().split(",");
                    ext_delivery.setText("");
                    Constants.deliveryDate="";
                    for(int i=0; i<day.length-1; i++){
                        Constants.deliveryDate=Constants.deliveryDate+","+day[i];
                    }
                    Constants.deliveryDate=Constants.deliveryDate.substring(1, Constants.deliveryDate.length());
                    ext_delivery.setText(Constants.deliveryDate);
                }
                Constants.deliverytime=data.get(a);
                ext_delivery.setText(ext_delivery.getText().toString()+", "+data.get(a));
                Constants.deliveryDate=ext_delivery.getText().toString();

                String[] date=Constants.deliveryDate.toString().split(",");
                if(date.length>0){
                    Constants.deliveryDay=date[0].toString();
                }
            }
        });
        return convertView;
    }
}
