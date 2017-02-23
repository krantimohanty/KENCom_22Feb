package com.swash.kencommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swash.kencommerce.R;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kranti on 12/12/2016.
 */
public class ViewAllCheckoutItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    NumberFormat nf;

    public ViewAllCheckoutItemListAdapter(Context c, ArrayList<HashMap<String, String>> data) {
        this.mContext = c;
        this.data=data;
        nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{

        TextView txt_item_name, txt_item_qty, txt_item_price;
    }
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;

        ViewHolder holder = null;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = inflater.inflate(R.layout.item_checkout_list, null);
            holder = new ViewHolder();

            holder.txt_item_name = (TextView) convertView.findViewById(R.id.txt_item_name);
            holder.txt_item_qty = (TextView) convertView.findViewById(R.id.txt_item_qty);
            holder.txt_item_price = (TextView) convertView.findViewById(R.id.txt_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_item_name.setText(data.get(position).get("product_name"));
        holder.txt_item_qty.setText(data.get(position).get("qty"));
        Double price=Double.parseDouble(data.get(position).get("product_price"))*(Integer.parseInt(data.get(position).get("qty")));
        holder.txt_item_price.setText("R"+nf.format(price));

        return convertView;

    }


}
