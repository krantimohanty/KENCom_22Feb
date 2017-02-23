package com.swash.kencommerce.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.swash.kencommerce.MyWishListActivity;
import com.swash.kencommerce.R;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.model.AddToWishListRequest;
import com.swash.kencommerce.model.CartTotalRequest;
import com.swash.kencommerce.model.DeleteWishListRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kranti on 12/12/2016.
 */
public class MyWishlistAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    ViewHolder holder = null;
    String strCustId = "";
    RestService restService;
    Utils utils;
    private ProgressDialog pDialog;
    Button btn_count;

    public MyWishlistAdapter(Activity a, ArrayList<HashMap<String, String>> d, String strCustId, Button btn_count) {
        activity = a;
        data=d;
        this.strCustId = strCustId;
        restService = new RestService();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        utils = new Utils(activity);
        this.btn_count=btn_count;
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
       // TextView txt_shoping_list, tv_item_count;
        ImageView img_rate_1, img_rate_2, img_rate_3, img_rate_4, img_rate_5;
        TextView txt_name, txt_count_revirw, txt_real_price_currency, txt_old_price_currency;
        ImageView img_special, img_no_stock;
        ImageView img_product;
        TextView tv_special_price, tv_available, tv_normal_price;
        LinearLayout lin_cross;
        RelativeLayout rel_parent;
        Button btn_remove;
        Button btn_add_to_cart;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_my_wishlist, null);

            holder = new ViewHolder();
            holder.rel_parent=(RelativeLayout)convertView.findViewById(R.id.rel_parent);
            holder.btn_remove=(Button)convertView.findViewById(R.id.btn_remove);

            holder.txt_name=(TextView)convertView.findViewById(R.id.txt_name);
            holder.txt_count_revirw=(TextView)convertView.findViewById(R.id.txt_count_revirw);
            holder.tv_special_price=(TextView)convertView.findViewById(R.id.txt_real_price);
            holder.tv_available=(TextView)convertView.findViewById(R.id.txt_availability);
            holder.tv_normal_price=(TextView)convertView.findViewById(R.id.txt_old_price);
            holder.txt_old_price_currency=(TextView)convertView.findViewById(R.id.txt_old_price_currency);
            holder.txt_real_price_currency=(TextView)convertView.findViewById(R.id.txt_real_price_currency);

            holder.lin_cross=(LinearLayout)convertView.findViewById(R.id.lin_cross);

            holder.img_rate_1 = (ImageView)convertView.findViewById(R.id.img_rate_1);
            holder.img_rate_2 = (ImageView)convertView.findViewById(R.id.img_rate_2);
            holder.img_rate_3 = (ImageView)convertView.findViewById(R.id.img_rate_3);
            holder.img_rate_4 = (ImageView)convertView.findViewById(R.id.img_rate_4);
            holder.img_rate_5 = (ImageView)convertView.findViewById(R.id.img_rate_5);

            holder.img_product=(ImageView)convertView.findViewById(R.id.img_product);
            holder.img_no_stock=(ImageView)convertView.findViewById(R.id.img_no_stock);
            holder.img_special=(ImageView)convertView.findViewById(R.id.img_special);
            holder.btn_add_to_cart=(Button)convertView.findViewById(R.id.btn_add_to_cart);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getDialogCoverage();
                int i = Integer.parseInt(v.getTag().toString());
                addToCart(data.get(i).get(MyWishListActivity.Key_product_id),
                        /*data.get(position).get(MyWishListActivity.Key_item_qty)*/"1",
                        data.get(i).get(MyWishListActivity.Key_option_id), strCustId,
                        data.get(i).get(MyWishListActivity.Key_attribute_id));
            }
        });

        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(v.getTag().toString());
                getDialog(i);

            }
        });
        holder.rel_parent.setTag(position);
        holder.btn_remove.setTag(position);
        holder.btn_add_to_cart.setTag(position);
        holder.rel_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });

        HashMap<String, String> mapShopList = new HashMap<String, String>();
        mapShopList = data.get(position);

        String strRating = mapShopList.get(MyWishListActivity.Key_product_rating_summary);
        float floatRate = Float.parseFloat(strRating);
        int intRate = (int) floatRate;
        float floatRateFrac = floatRate-intRate;

        if (intRate == 5){
            holder.img_rate_5.setImageResource(R.drawable.star_100);
            holder.img_rate_4.setImageResource(R.drawable.star_100);
            holder.img_rate_3.setImageResource(R.drawable.star_100);
            holder.img_rate_2.setImageResource(R.drawable.star_100);
            holder.img_rate_1.setImageResource(R.drawable.star_100);
        }
        else if (intRate == 4){
            holder.img_rate_5.setImageResource(R.drawable.star_0);
            holder.img_rate_4.setImageResource(R.drawable.star_100);
            holder.img_rate_3.setImageResource(R.drawable.star_100);
            holder.img_rate_2.setImageResource(R.drawable.star_100);
            holder.img_rate_1.setImageResource(R.drawable.star_100);
            setFraction(floatRateFrac, holder.img_rate_5);
        }
        else if (intRate == 3){
            holder.img_rate_5.setBackgroundResource(R.drawable.star_0);
            holder.img_rate_4.setBackgroundResource(R.drawable.star_0);
            holder.img_rate_3.setBackgroundResource(R.drawable.star_100);
            holder.img_rate_2.setBackgroundResource(R.drawable.star_100);
            holder.img_rate_1.setBackgroundResource(R.drawable.star_100);
            setFraction(floatRateFrac, holder.img_rate_4);
        }
        else if (intRate == 2){
            holder.img_rate_5.setImageResource(R.drawable.star_0);
            holder.img_rate_4.setImageResource(R.drawable.star_0);
            holder.img_rate_3.setImageResource(R.drawable.star_0);
            holder.img_rate_2.setImageResource(R.drawable.star_100);
            holder.img_rate_1.setImageResource(R.drawable.star_100);
            setFraction(floatRateFrac, holder.img_rate_3);
        }
        else if (intRate == 1){
            holder.img_rate_5.setImageResource(R.drawable.star_0);
            holder.img_rate_4.setImageResource(R.drawable.star_0);
            holder.img_rate_3.setImageResource(R.drawable.star_0);
            holder.img_rate_2.setImageResource(R.drawable.star_0);
            holder.img_rate_1.setImageResource(R.drawable.star_100);
            setFraction(floatRateFrac, holder.img_rate_2);
        }
        else if (intRate == 0){
            holder.img_rate_5.setImageResource(R.drawable.star_0);
            holder.img_rate_4.setImageResource(R.drawable.star_0);
            holder.img_rate_3.setImageResource(R.drawable.star_0);
            holder.img_rate_2.setImageResource(R.drawable.star_0);
            holder.img_rate_1.setImageResource(R.drawable.star_0);
            setFraction(floatRateFrac, holder.img_rate_1);
        }

        final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.progress);
        Glide.with(activity) //Context
                .load(mapShopList.get(MyWishListActivity.Key_product_img_url)) //URL/FILE
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
                .into(holder.img_product);

        holder.txt_name.setText(mapShopList.get(MyWishListActivity.Key_product_name));
        holder.txt_count_revirw.setText("( " + mapShopList.get(MyWishListActivity.Key_product_reviews_count) + " )");
        holder.txt_old_price_currency.setText(mapShopList.get(MyWishListActivity.Key_currency));
        holder.txt_real_price_currency.setText(mapShopList.get(MyWishListActivity.Key_currency));
        holder.tv_special_price.setText(mapShopList.get(MyWishListActivity.Key_product_special_price));
        holder.tv_normal_price.setText(mapShopList.get(MyWishListActivity.Key_product_price));
        // Special
        if (mapShopList.get(MyWishListActivity.Key_product_is_special).equalsIgnoreCase("1")){
            /*holder.tv_available.setText("Available");
            holder.tv_available.setTextColor(Color.parseColor("#70B74E"));*/
            holder.img_special.setVisibility(View.VISIBLE);
            //holder.img_no_stock.setVisibility(View.INVISIBLE);
            holder.lin_cross.setVisibility(View.VISIBLE);
            holder.tv_special_price.setVisibility(View.VISIBLE);
            holder.txt_real_price_currency.setVisibility(View.VISIBLE);
            holder.tv_special_price.setTextColor(Color.RED);
            holder.tv_special_price.setTypeface(null, Typeface.BOLD);
            holder.tv_normal_price.setVisibility(View.VISIBLE);
        }
        // Not Special
        else {
            holder.img_special.setVisibility(View.GONE);
            //holder.img_no_stock.setVisibility(View.GONE);
            holder.lin_cross.setVisibility(View.GONE);
            holder.txt_real_price_currency.setVisibility(View.GONE);
            holder.tv_special_price.setVisibility(View.GONE);
            holder.tv_normal_price.setVisibility(View.VISIBLE);
        }
        // In Stock
        if (mapShopList.get(MyWishListActivity.Key_product_is_salable).equalsIgnoreCase("1")){
            holder.tv_available.setText("Available");
            holder.tv_available.setTextColor(Color.parseColor("#70B74E"));
            holder.img_no_stock.setVisibility(View.GONE);
        }
        // Out Of Stock
        else {
            holder.img_no_stock.setVisibility(View.VISIBLE);
            holder.tv_available.setText("Out of stock");
            holder.tv_available.setTextColor(Color.RED);
            holder.btn_add_to_cart.setBackgroundColor(Color.GRAY);
            holder.tv_normal_price.setVisibility(View.GONE);
            holder.tv_normal_price.setGravity(Gravity.CENTER);
            holder.lin_cross.setVisibility(View.GONE);
            holder.btn_add_to_cart.setClickable(false);
        }
        if (mapShopList.get(MyWishListActivity.Key_product_is_special).equalsIgnoreCase("0")
                &&mapShopList.get(MyWishListActivity.Key_product_is_salable).equalsIgnoreCase("0")) {
            holder.tv_normal_price.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private void setFraction(float floatRateFrac, ImageView img_rate) {
        if (floatRateFrac >= 0.1 && floatRateFrac <= 0.3){
            img_rate.setImageResource(R.drawable.star_25);
        }
        else if (floatRateFrac > 0.3 && floatRateFrac <= 0.6){
            img_rate.setImageResource(R.drawable.star_50);
        }
        else if (floatRateFrac > 0.6 && floatRateFrac <= 0.8){
            img_rate.setImageResource(R.drawable.star_75);
        }
        else if (floatRateFrac > 0.8){
            img_rate.setImageResource(R.drawable.star_100);
        }
        else {
            img_rate.setImageResource(R.drawable.star_0);
        }
    }

    private void addToCart(String product_id,  String qnt, String option_id, final String customer_id, String Att_id) {
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait.");
        restService.addToCart(product_id, /*txt_count_cart_add.getText().toString()*/qnt, Att_id,  customer_id, option_id, new RestCallback<AddToWishListRequest>() {
            @Override
            public void success(AddToWishListRequest object) {

                if (pDialog != null)
                    pDialog.dismiss();
                if(object.getStatus()==200&&object.getSuccess()==1){
                    utils.displayAlert(object.getData().getSuccessMsg());

                        getCartTotal(customer_id);

                }else{
                    utils.displayAlert(object.getErrorMsg());
                }

            }

            @Override
            public void invalid() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(activity, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {
                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(activity, "Error parsing tracking list", Toast.LENGTH_LONG).show();
            }

        });
    }
    private void getCartTotal(String customer_id) {

        pDialog=new ProgressDialog(activity);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait");
        restService.getCartTotal(customer_id, new RestCallback<CartTotalRequest>() {
            @Override
            public void success(CartTotalRequest object) {

                if (object.getStatus() == 200 && object.getSuccess() == 1) {

                    btn_count.setText(object.getData().getTotalQty().toString());
                    //txt_total_price.setText(object.getData().getTotalPrice());
                }
                if (pDialog != null)
                    pDialog.dismiss();
            }

            @Override
            public void invalid() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(activity, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(activity, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }
    private void removeFromWishList(final String strWLid, String strCustId) {
        final ProgressDialog pDialog=new ProgressDialog(activity);
        pDialog.show();
        pDialog.setMessage("Processing your data..");
        restService.deleteWish(strCustId, strWLid, new RestCallback<DeleteWishListRequest>() {
            @Override
            public void success(DeleteWishListRequest responce) {
                pDialog.dismiss();
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    displayAlert("Item has been deleted successfully", strWLid);
                } else {
                    //getDialogOK(responce.getErrorMsg());
                    utils.displayAlert(responce.getErrorMsg());
                }
            }

            @Override
            public void invalid() {
                pDialog.dismiss();
            }

            @Override
            public void failure() {
                pDialog.dismiss();
            }
        });
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
        tvMsg.setText("Are you sure you wish to delete this item?");
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
                removeFromWishList(data.get(i).get(MyWishListActivity.Key_wishlist_id), strCustId);

            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void displayAlert(String message, final String id) {
        // TODO Auto-generated method stub
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(activity);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                int i = Integer.parseInt(id);
                Intent intent = new Intent(activity, MyWishListActivity.class);
                activity.finish();
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        TextView myMsg = new TextView(activity);
        myMsg.setText(message);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setTextSize(16);
        //myMsg.setTypeface(typeFaceSegoeuiReg);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL);
        alertDialogBuilder.setView(myMsg);

        TextView title = new TextView(activity);
        // You Can Customise your Title here
        title.setText("KenCommerce");
        title.setBackgroundColor(Color.TRANSPARENT);
        title.setPadding(15, 20, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        // title.setTypeface(typeFaceSegoeuiBold);
        title.setTextSize(20);

        myMsg.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams positiveButtonLLl = (LinearLayout.LayoutParams) myMsg.getLayoutParams();
        positiveButtonLLl.gravity = Gravity.CENTER|Gravity.CENTER_VERTICAL;
        myMsg.setLayoutParams(positiveButtonLLl);

        alertDialogBuilder.setCustomTitle(title);
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();



        final Button positiveButton = alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
        positiveButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER|Gravity.CENTER_VERTICAL;
        positiveButton.setTextColor(Color.parseColor("#048BCD"));
        positiveButton.setLayoutParams(positiveButtonLL);
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

    private void getDialogOK(String strMsg) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_response_ok);

        TextView txt_header=(TextView)dialog.findViewById(R.id.txt_header);
        TextView txt_msg=(TextView)dialog.findViewById(R.id.txt_msg);
        Button btn_ok=(Button)dialog.findViewById(R.id.btn_ok);

        //txt_header.setText(strHeader);
        txt_msg.setText(strMsg);
        btn_ok.setOnClickListener(new View.OnClickListener() {
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
