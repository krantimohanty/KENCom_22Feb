package com.swash.kencommerce.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.swash.kencommerce.AddToCartListAllItemsActivity;
import com.swash.kencommerce.MyShoppingListActivity;
import com.swash.kencommerce.R;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.dbhelper.AddToCartTable;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kranti on 12/12/2016.
 */



public class AddToCartLocalListAllItemAdapter extends BaseAdapter {
    private final NumberFormat nf;
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    ViewHolder holder = null;
    TextView tv_total_price;
    private double tota1_price=0.0;

    AddToCartTable mAddToCartTable;
    private Utils utils;
    TextView txt_empty_cart;
    ScrollView srcl_view;
    LinearLayout lin_no_item;
    TextView tv_checkout;
    LinearLayout lin_vendor_name;
    private double item_price=0.0;

    public AddToCartLocalListAllItemAdapter(Activity a, ArrayList<HashMap<String, String>> d, TextView tv_total_price, TextView txt_empty_cart, TextView tv_checkout, LinearLayout lin_vendor_name, ScrollView srcl_view, LinearLayout lin_no_item) {

        this.activity = a;
        this.data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tv_total_price=tv_total_price;
        this.txt_empty_cart=txt_empty_cart;
        this.lin_vendor_name=lin_vendor_name;
        this.srcl_view=srcl_view;
        this.lin_no_item=lin_no_item;
        this.tv_checkout=tv_checkout;
        utils=new Utils(activity);
        mAddToCartTable=new AddToCartTable(activity);

        nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
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

    public class ViewHolder
    {
        TextView tv_name;
        RelativeLayout rel_parent, ll_blink;
        Button imgPlus, imgMinus;
        TextView txt_count_cart_add, txt_asap, txt_item_price;
        LinearLayout lin_on_demand;
        ImageView img_on_demand;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_add_to_cart, null);
            holder = new ViewHolder();
            holder.rel_parent=(RelativeLayout)convertView.findViewById(R.id.rel_parent);
            holder.ll_blink=(RelativeLayout)convertView.findViewById(R.id.ll_blink);
            holder.imgPlus=(Button)convertView.findViewById(R.id.img_plus);
            holder.imgMinus=(Button)convertView.findViewById(R.id.img_minus);
            holder.txt_count_cart_add=(TextView)convertView.findViewById(R.id.txt_count_cart_add);
            holder.tv_name=(TextView)convertView.findViewById(R.id.txt_name);
            holder.lin_on_demand=(LinearLayout)convertView.findViewById(R.id.lin_on_demand);
            holder.img_on_demand=(ImageView)convertView.findViewById(R.id.img_on_demand);
            holder.txt_asap=(TextView)convertView.findViewById(R.id.txt_asap);
            holder.txt_item_price=(TextView)convertView.findViewById(R.id.txt_item_price);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        item_price=Double.parseDouble(data.get(position).get("price").toString());
        item_price=(item_price)*Integer.parseInt(data.get(position).get("qty").toString());
        holder.txt_item_price.setText("R"+nf.format(item_price));
        holder.rel_parent.setTag(position);

        if(data.get(position).get("deltype").toString().equalsIgnoreCase("No Rush")){
            holder.img_on_demand.setVisibility(View.GONE);
            holder.txt_asap.setVisibility(View.GONE);
        }else{
            holder.img_on_demand.setVisibility(View.VISIBLE);
            holder.txt_asap.setVisibility(View.VISIBLE);
        }
        //holder.ll_delete_from_cart.setTag(position);

        HashMap<String, String> mapShopList = new HashMap<String, String>();
        mapShopList = data.get(position);
        holder.tv_name.setText(mapShopList.get("productname"));
        //holder.tv_real_price.setText(mapShopList.get("Price"));
        holder.imgMinus.setTag(position);
        holder.imgPlus.setTag(position);

        holder.txt_count_cart_add.setText(mapShopList.get("qty"));
        holder.txt_count_cart_add.setTag(position);
        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i=Integer.parseInt(v.getTag().toString());
                Animation startAnimation1 = AnimationUtils.loadAnimation(activity, R.anim.anim_sequence_2);
                RelativeLayout ll_blink=(RelativeLayout)parent.getChildAt(i).findViewById(R.id.ll_blink);
                ll_blink.startAnimation(startAnimation1);
                TextView txt_count_cart_add=(TextView)parent.getChildAt(i).findViewById(R.id.txt_count_cart_add);
                String strCount = txt_count_cart_add.getText().toString().trim();
                int intCount = Integer.parseInt(strCount);
                data.get(i).put("qty", txt_count_cart_add.getText().toString());
                tota1_price=tota1_price+Double.parseDouble(data.get(i).get("price").toString());
                tv_total_price.setText("R" + nf.format(tota1_price));
                intCount++;
                txt_count_cart_add.setText(String.valueOf(intCount));
                TextView txt_item_price = (TextView) parent.getChildAt(i).findViewById(R.id.txt_item_price);

                item_price=Double.parseDouble(data.get(i).get("price").toString())*Integer.parseInt(data.get(i).get("qty").toString());

                txt_item_price.setText("R"+nf.format(item_price));
                mAddToCartTable.deleteitem(data.get(i).get("productid").toString());
                mAddToCartTable.insert(data.get(i).get("productid").toString(), data.get(i).get("productname").toString(), data.get(i).get("qty").toString(), data.get(i).get("productoptionid").toString(),data.get(i).get("productattid").toString(),data.get(i).get("price").toString(),data.get(i).get("deltype").toString());

            }
        });

        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i=Integer.parseInt(v.getTag().toString());
                Animation startAnimation1 = AnimationUtils.loadAnimation(activity, R.anim.anim_sequence_2);
                RelativeLayout ll_blink=(RelativeLayout)parent.getChildAt(i).findViewById(R.id.ll_blink);
                ll_blink.startAnimation(startAnimation1);
                TextView txt_count_cart_add=(TextView)parent.getChildAt(i).findViewById(R.id.txt_count_cart_add);
                String strCount = txt_count_cart_add.getText().toString().trim();
                final TextView txt_name=(TextView)parent.getChildAt(i).findViewById(R.id.txt_name);
                int intCount = Integer.parseInt(strCount);
                if (intCount>0) {
                    intCount--;
                    txt_count_cart_add.setText(String.valueOf(intCount));
                    data.get(i).put("qty", txt_count_cart_add.getText().toString());

                    TextView txt_item_price = (TextView) parent.getChildAt(i).findViewById(R.id.txt_item_price);

                    item_price=Double.parseDouble(data.get(i).get("price").toString())*Integer.parseInt(data.get(i).get("qty").toString());

                    txt_item_price.setText("R" + nf.format(item_price));


                    tota1_price=tota1_price-Double.parseDouble(data.get(i).get("price").toString());
                    tv_total_price.setText("R" + nf.format(tota1_price));
                }



                    mAddToCartTable.deleteitem(data.get(i).get("productid").toString());
                    //utils.displayAlert("Successfully removed from cart.");
                    mAddToCartTable.insert(data.get(i).get("productid").toString(), data.get(i).get("productname").toString(), data.get(i).get("qty").toString(), data.get(i).get("productoptionid").toString(), data.get(i).get("productattid").toString(), data.get(i).get("price").toString(),data.get(i).get("deltype").toString());


                if(txt_count_cart_add.getText().toString().equalsIgnoreCase("0")){
                    AddToCartListAllItemsActivity.mAddToCartTable.deleteitem(data.get(i).get("productid").toString());
                    data.remove(i);
                    notifyDataSetChanged();
                    i=i+1;

                    if(data.size()==0){
                        AddToCartListAllItemsActivity.size=0;
                        tv_checkout.setText("Continue Shopping");
                        tv_total_price.setVisibility(View.GONE);
                        txt_empty_cart.setVisibility(View.GONE);
                        lin_vendor_name.setVisibility(View.GONE);
                        srcl_view.setVisibility(View.GONE);
                        lin_no_item.setVisibility(View.VISIBLE);
                        /*tv_checkout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(activity, DrinkCategoriesActivity.class);
                                activity.finish();
                                activity.startActivity(intent);

                            }
                        });*/
                    }

                }
            }
        });
        tota1_price=0.0;
        for(int i=0; i<data.size(); i++){
            String single_price=data.get(i).get("price");
            if(single_price.startsWith("R")){
                single_price= single_price.substring(1, single_price.length());
            }
            if(single_price!=null ||!single_price.equalsIgnoreCase("")){
                double a=Double.parseDouble(single_price);
                a=a*Integer.parseInt(data.get(i).get("qty"));
                tota1_price=tota1_price+a;
            }
            tv_total_price.setVisibility(View.VISIBLE);
            tv_total_price.setText("R" + nf.format(tota1_price));
        }


        return convertView;
    }


    private void getDialog(final int i) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_age);
        activity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvHeader=(TextView)dialog.findViewById(R.id.header);
        tvHeader.setText("KenCommerce");
        TextView tvMsg=(TextView)dialog.findViewById(R.id.msg);
        tvMsg.setText("Are you sure you wish to delete this product?");
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
