package com.swash.kencommerce.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.swash.kencommerce.MyShoppingListActivity;
import com.swash.kencommerce.OrderHistoryActivity;
import com.swash.kencommerce.PastOrderListActivity;
import com.swash.kencommerce.R;
import com.swash.kencommerce.ReOrderActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kranti on 12/12/2016.
 */
public class PastOrderListAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    ViewHolder holder = null;
    Typeface typeFaceSegoeuiReg, typeFaceSegoeuiBold, typeFaceSegoeuiMedium;
    public PastOrderListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        typeFaceSegoeuiReg = Typeface.createFromAsset(activity.getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiMedium = Typeface.createFromAsset(activity.getAssets(),
                "Roboto-Light.ttf");
        typeFaceSegoeuiBold = Typeface.createFromAsset(activity.getAssets(),
                "ROBOTO-BOLD_0.TTF");


    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        /*TextView txt_name, txt_price, txt_status_message;
        RelativeLayout rel_parent;*/
        TextView txt_order_no, txt_order_amt_currency, txt_date, txt_order_amt, txt_order;
        Button btn_delivered, btn_view_dtl, btn_re_order;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_past_order_list, null);
            holder = new ViewHolder();
            holder.txt_order = (TextView) convertView.findViewById(R.id.txt_ord);
            holder.txt_order_no = (TextView) convertView.findViewById(R.id.txt_order_no);
            holder.txt_date = (TextView) convertView.findViewById(R.id.txt_date);
            holder.txt_order_amt_currency = (TextView) convertView.findViewById(R.id.txt_order_amt_currency);
            holder.txt_order_amt = (TextView) convertView.findViewById(R.id.txt_order_amt);
            holder.btn_delivered = (Button) convertView.findViewById(R.id.btn_delivered);
            holder.btn_view_dtl = (Button) convertView.findViewById(R.id.btn_view_dtl);
            holder.btn_re_order = (Button) convertView.findViewById(R.id.btn_re_order);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt_order.setTypeface(typeFaceSegoeuiReg);
        holder.txt_order_no.setTypeface(typeFaceSegoeuiReg);
        holder.txt_date.setTypeface(typeFaceSegoeuiReg);
        holder.txt_order_amt.setTypeface(typeFaceSegoeuiReg);

        holder.btn_delivered.setTypeface(typeFaceSegoeuiMedium);
        holder.btn_view_dtl.setTypeface(typeFaceSegoeuiMedium);
        holder.btn_re_order.setTypeface(typeFaceSegoeuiMedium);

        HashMap<String, String> mapShopList = new HashMap<String, String>();
        mapShopList = data.get(position);

        holder.txt_order_no.setText(mapShopList.get(PastOrderListActivity.Key_order_id));
        holder.txt_order_amt_currency.setText(mapShopList.get(PastOrderListActivity.Key_currency));
        holder.txt_order_amt.setText(mapShopList.get(PastOrderListActivity.Key_order_amount));
        holder.txt_date.setText(mapShopList.get(PastOrderListActivity.Key_order_date));
        holder.btn_delivered.setText(mapShopList.get(PastOrderListActivity.Key_order_status));

        /*holder.txt_name.setText(mapShopList.get(PastOrderListActivity.Key_PastOrdersNameList));
        holder.txt_price.setText(mapShopList.get(PastOrderListActivity.Key_PastOrdersPriceList));
        if(mapShopList.get(PastOrderListActivity.Key_PastOrdersStatusList).toString().equalsIgnoreCase("Delivered")){
            holder.txt_status_message.setTextColor(Color.parseColor("#70B74E"));
        }else
        {
            holder.txt_status_message.setTextColor(Color.BLACK);
        }
        holder.txt_status_message.setText(mapShopList.get(PastOrderListActivity.Key_PastOrdersStatusList));*/

        holder.btn_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code here
                //getDialogCoverage();
            }
        });
        holder.btn_view_dtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code here
                Intent intent=new Intent(activity, OrderHistoryActivity.class);
                intent.putExtra(PastOrderListActivity.Key_order_id, data.get(position).get(PastOrderListActivity.Key_order_id));
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        holder.btn_re_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code here
                Intent intent=new Intent(activity, ReOrderActivity.class);
                intent.putExtra(PastOrderListActivity.Key_order_id, data.get(position).get(PastOrderListActivity.Key_order_id));
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });

        return convertView;
    }

    private void getDialog(final int i) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_age);
        activity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvHeader=(TextView)dialog.findViewById(R.id.header);
        tvHeader.setText("KenCommerce");
        TextView tvMsg=(TextView)dialog.findViewById(R.id.msg);
        tvMsg.setText("Are you sure you wish to delete this shopping list?");
        Button btn_no=(Button)dialog.findViewById(R.id.btn_no);
        Button btn_yes=(Button)dialog.findViewById(R.id.btn_yes);

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                data.remove(i);
                notifyDataSetChanged();
                MyShoppingListActivity.tv_shopping_list_count.setText("Shopping list (" + data.size()+")");
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_age);
        activity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView header=(TextView)dialog.findViewById(R.id.header);
        TextView msg=(TextView)dialog.findViewById(R.id.msg);
        Button btn_no=(Button)dialog.findViewById(R.id.btn_no);
        Button btn_yes=(Button)dialog.findViewById(R.id.btn_yes);
        btn_yes.setText("Ok");
        btn_no.setText("Cancel");

        header.setText("KenCommerce");
        msg.setText("Coming Soon");
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


}
