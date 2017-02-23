package com.swash.kencommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.swash.kencommerce.OrderHistoryActivity;
import com.swash.kencommerce.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kranti on 12/12/2016.
 */
public class AdapterReOrderItem extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    ArrayList<Integer> arrCatName;

    public AdapterReOrderItem(Activity a, ArrayList<HashMap<String, String>> d) {
        this.activity = a;
        this.data=d;
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
        TextView txt_item_name, txt_quantity, txt_total_price_currency, txt_total_price;
        ImageView img_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_oder_history_item , null);
            holder = new ViewHolder();

            holder.txt_item_name = (TextView) convertView.findViewById(R.id.txt_item_name);
            holder.txt_quantity = (TextView) convertView.findViewById(R.id.txt_quantity);
            holder.txt_total_price_currency = (TextView) convertView.findViewById(R.id.txt_total_price_currency);
            holder.txt_total_price = (TextView) convertView.findViewById(R.id.txt_total_price);
            holder.img_item = (ImageView) convertView.findViewById(R.id.img_item);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, String> mapOrderHistory = new HashMap<String, String>();
        mapOrderHistory = data.get(position);

        holder.txt_item_name.setText(mapOrderHistory.get(OrderHistoryActivity.Key_product_name));
        holder.txt_quantity.setText(mapOrderHistory.get(OrderHistoryActivity.Key_product_qty));
        holder.txt_total_price_currency.setText(mapOrderHistory.get(OrderHistoryActivity.Key_currency));
        holder.txt_total_price.setText(mapOrderHistory.get(OrderHistoryActivity.Key_total_price));

        final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.progress);
        Glide.with(activity) //Context
                .load(mapOrderHistory.get(OrderHistoryActivity.Key_product_image)) //URL/FILE
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.img_item);

        return convertView;
    }
}
