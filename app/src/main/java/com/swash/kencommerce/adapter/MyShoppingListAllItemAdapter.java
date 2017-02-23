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
import com.swash.kencommerce.MyShoppingListActivity;
import com.swash.kencommerce.MyShoppingListAllItemsActivity;
import com.swash.kencommerce.R;
import com.swash.kencommerce.ShoppingListActivity;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.model.AddToWishListRequest;
import com.swash.kencommerce.model.DeleteShoppingListItemRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kranti on 12/12/2016.
 */
public class MyShoppingListAllItemAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    ViewHolder holder = null;
    String strCustId = "";
    RestService restService;
    Utils utils;
    public static String itemid = "",flag_my_shopping_all_item_list="";

    public MyShoppingListAllItemAdapter(Activity a, ArrayList<HashMap<String, String>> d, String strCustId) {
        activity = a;
        data = d;
        this.strCustId = strCustId;
        restService = new RestService();
        restService = new RestService();
        utils = new Utils(activity);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public class ViewHolder {
        // TextView txt_shoping_list, tv_item_count;
        ImageView img_rate_1, img_rate_2, img_rate_3, img_rate_4, img_rate_5;
        ImageView img_special, img_no_stock, iv_product;
        TextView tv_available, tv_normal_price, txt_name, txt_count_revirw, txt_real_price, txt_old_price, txt_productid;
        LinearLayout lin_cross;
        RelativeLayout rel_parent;
        LinearLayout lin_remove,ll_move;
        Button btn_add_to_cart;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_my_shopping_list_all_item_1, null);
            holder = new ViewHolder();
            holder.rel_parent = (RelativeLayout) convertView.findViewById(R.id.rel_parent);
            holder.lin_remove = (LinearLayout) convertView.findViewById(R.id.lin_remove);
            holder.ll_move = (LinearLayout) convertView.findViewById(R.id.ll_move);

            holder.img_rate_1 = (ImageView)convertView.findViewById(R.id.img_rate_1);
            holder.img_rate_2 = (ImageView)convertView.findViewById(R.id.img_rate_2);
            holder.img_rate_3 = (ImageView)convertView.findViewById(R.id.img_rate_3);
            holder.img_rate_4 = (ImageView)convertView.findViewById(R.id.img_rate_4);
            holder.img_rate_5 = (ImageView)convertView.findViewById(R.id.img_rate_5);

            holder.tv_available = (TextView) convertView.findViewById(R.id.txt_availability);
            holder.tv_normal_price = (TextView) convertView.findViewById(R.id.txt_old_price);
            holder.lin_cross = (LinearLayout) convertView.findViewById(R.id.lin_cross);
            holder.img_no_stock = (ImageView) convertView.findViewById(R.id.img_no_stock);
            holder.img_special = (ImageView) convertView.findViewById(R.id.img_special);
            holder.btn_add_to_cart = (Button) convertView.findViewById(R.id.btn_add_to_cart);
            holder.iv_product = (ImageView) convertView.findViewById(R.id.iv_product);
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txt_count_revirw = (TextView) convertView.findViewById(R.id.txt_count_revirw);
            holder.txt_real_price = (TextView) convertView.findViewById(R.id.txt_real_price);
            holder.txt_old_price = (TextView) convertView.findViewById(R.id.txt_old_price);
            holder.txt_productid = (TextView) convertView.findViewById(R.id.txt_productid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ll_move.setTag(position);
        holder.lin_remove.setTag(position);
        holder.btn_add_to_cart.setTag(position);


        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i=Integer.parseInt(v.getTag().toString());
                //getDialogCoverage();
                addToCart(data.get(i).get(MyShoppingListAllItemsActivity.Key_product_id),
                        /*data.get(position).get(MyShoppingListAllItemsActivity.Key_item_qty)*/"1",
                        data.get(i).get(MyShoppingListAllItemsActivity.Key_option_id), strCustId,
                        data.get(i).get(MyShoppingListAllItemsActivity.Key_attribute_id));
            }
        });
        holder.ll_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(v.getTag().toString());
                getDialogMove(i);
            }
        });
        holder.lin_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(v.getTag().toString());
                getDialog(i);
            }
        });
        holder.rel_parent.setTag(position);
        holder.lin_remove.setTag(position);
        holder.rel_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int i = Integer.parseInt(v.getTag().toString());
                getDialog(i);
                return false;
            }
        });
        /*new SwipeDetector(holder.rel_parent).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
            @Override
            public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {
               *//* if (swipeType == SwipeDetector.SwipeTypeEnum.LEFT_TO_RIGHT)
                   // getActivity().onBackPressed();
                   // Toast.makeText(activity, "Swipe Left to Right", Toast.LENGTH_LONG).show();
               // getDialog();
               // else if(swipeType==SwipeDetector.SwipeTypeEnum.RIGHT_TO_LEFT)
                    //Toast.makeText(activity, "Swipe Right to Left", Toast.LENGTH_LONG).show();
                //*//*
            }
        });*/

         /*holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(buttonView.isChecked()){
                    buttonView.setChecked(true);
                    buttonView.setButtonDrawable(R.drawable.close_offer_checkbox_sel);
                }else{

                    buttonView.setChecked(false);
                    buttonView.setButtonDrawable(R.drawable.close_offer_checkbox);
                }
            }
        });*/
       /* HashMap<String, String> mapShopList = new HashMap<String, String>();
        mapShopList = data.get(position);
        ///////////Special//////////
        if(position%3==0){

            holder.img_special.setVisibility(View.VISIBLE);
            holder.img_no_stock.setVisibility(View.INVISIBLE);
            holder.tv_available.setText("Available");
            holder.tv_available.setTextColor(Color.parseColor("#70B74E"));
            //holder.btn_add_to_cart.setVisibility(View.VISIBLE);
            holder.lin_cross.setVisibility(View.VISIBLE);
            holder.tv_special_price.setVisibility(View.VISIBLE);
            holder.tv_special_price.setTextColor(Color.RED);
            holder.tv_special_price.setTypeface(null, Typeface.BOLD);
            holder.tv_normal_price.setVisibility(View.VISIBLE);
        }
        ///////////Out of stock//////////
        else if(position%3==1){
            holder.img_special.setVisibility(View.INVISIBLE);
            holder.img_no_stock.setVisibility(View.VISIBLE);
            holder.tv_available.setText("Out of stock");
            holder.tv_available.setTextColor(Color.RED);
            holder.lin_cross.setVisibility(View.VISIBLE);
            //holder.btn_add_to_cart.setVisibility(View.GONE);

        }
        ///////////Normal//////////
        else{
            holder.img_special.setVisibility(View.INVISIBLE);
            holder.img_no_stock.setVisibility(View.INVISIBLE);
            holder.tv_special_price.setVisibility(View.VISIBLE);
            holder.tv_special_price.setTextColor(Color.BLACK);
            holder.tv_special_price.setTypeface(null, Typeface.NORMAL);
            holder.tv_normal_price.setVisibility(View.GONE);
            holder.tv_normal_price.setGravity(Gravity.CENTER);
            holder.lin_cross.setVisibility(View.GONE);
        }*/

        HashMap<String, String> mapShopList = new HashMap<String, String>();
        mapShopList = data.get(position);

        String strRating = mapShopList.get(MyShoppingListAllItemsActivity.Key_review_count);
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
                .load(mapShopList.get(MyShoppingListAllItemsActivity.Key_product_image)) //URL/FILE
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
                .into(holder.iv_product);

        holder.txt_name.setText(mapShopList.get(MyShoppingListAllItemsActivity.Key_product_name));
        holder.txt_count_revirw.setText("( " + mapShopList.get(MyShoppingListAllItemsActivity.Key_review_count) + " )");
        holder.txt_real_price.setText("R" + mapShopList.get(MyShoppingListAllItemsActivity.Key_product_normal_price));
        holder.txt_productid.setText(mapShopList.get(MyShoppingListAllItemsActivity.Key_product_id));

        //holder.tv_normal_price.setText(mapShopList.get(MyShoppingListAllItemsActivity.Key_product_price));

        //Available
        if (mapShopList.get(MyShoppingListAllItemsActivity.Key_product_available).equalsIgnoreCase("false")) {
            holder.img_special.setVisibility(View.INVISIBLE);
            holder.img_no_stock.setVisibility(View.VISIBLE);
            holder.tv_available.setText("Out of stock");
            holder.tv_available.setTextColor(Color.RED);
            holder.lin_cross.setVisibility(View.VISIBLE);
            holder.txt_real_price.setVisibility(View.VISIBLE);
            holder.txt_real_price.setTextColor(Color.BLACK);
            holder.txt_real_price.setTypeface(null, Typeface.NORMAL);
            holder.txt_real_price.setText("R"+mapShopList.get(MyShoppingListAllItemsActivity.Key_product_normal_price));
        } else {
            holder.img_special.setVisibility(View.INVISIBLE);
            holder.img_no_stock.setVisibility(View.INVISIBLE);
            holder.txt_real_price.setVisibility(View.VISIBLE);
            holder.txt_real_price.setTextColor(Color.BLACK);
            holder.txt_real_price.setTypeface(null, Typeface.NORMAL);
            holder.tv_normal_price.setVisibility(View.GONE);
            holder.tv_normal_price.setGravity(Gravity.CENTER);
            holder.lin_cross.setVisibility(View.GONE);
            holder.txt_real_price.setText("R"+mapShopList.get(MyShoppingListAllItemsActivity.Key_product_normal_price));
        }

        // Special
        if (mapShopList.get(MyShoppingListAllItemsActivity.Key_product_is_special).equalsIgnoreCase("1")) {
            /*holder.tv_available.setText("Available");
            holder.tv_available.setTextColor(Color.parseColor("#70B74E"));*/
            holder.img_special.setVisibility(View.VISIBLE);
            //holder.img_no_stock.setVisibility(View.INVISIBLE);
            holder.lin_cross.setVisibility(View.VISIBLE);
            holder.txt_real_price.setVisibility(View.VISIBLE);
            holder.txt_real_price.setTextColor(Color.RED);
            holder.txt_real_price.setTypeface(null, Typeface.BOLD);
            holder.tv_normal_price.setVisibility(View.VISIBLE);
            holder.txt_old_price.setText("R"+mapShopList.get(MyShoppingListAllItemsActivity.Key_product_normal_price));
            holder.txt_real_price.setText("R"+mapShopList.get(MyShoppingListAllItemsActivity.Key_product_special_price));
        }
        // Not Special
        else {
            holder.img_special.setVisibility(View.GONE);
            //holder.img_no_stock.setVisibility(View.GONE);
            holder.lin_cross.setVisibility(View.GONE);
            //holder.txt_real_price_currency.setVisibility(View.GONE);
            holder.txt_real_price.setVisibility(View.VISIBLE);
            holder.tv_normal_price.setVisibility(View.VISIBLE);
            holder.txt_old_price.setVisibility(View.GONE);
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

    private void addToCart(String product_id,  String qnt, String option_id, String customer_id, String Att_id) {
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait.");
        restService.addToCart(product_id, /*txt_count_cart_add.getText().toString()*/qnt, Att_id, customer_id, option_id, new RestCallback<AddToWishListRequest>() {
            @Override
            public void success(AddToWishListRequest object) {

                if (pDialog != null)
                    pDialog.dismiss();
                if(object.getStatus()==200&&object.getSuccess()==1){
                    displayAlert(object.getData().getSuccessMsg());

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


    private void getDialog(final int i) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_age);
        activity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvHeader = (TextView) dialog.findViewById(R.id.header);
        tvHeader.setText("KenCommerce");
        TextView tvMsg = (TextView) dialog.findViewById(R.id.msg);
        tvMsg.setText("Are you sure you wish to delete this item?");
        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);


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
                //data.remove(i);
                removeItemFromShoppingList(data.get(i).get(MyShoppingListAllItemsActivity.Key_product_id), strCustId,MyShoppingListAdapter.customar_list_id);
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
                Intent intent = new Intent(activity, MyShoppingListActivity.class);
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


    private void displayAlert(String message) {
        // TODO Auto-generated method stub
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(activity);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(activity, MyShoppingListActivity.class);
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

        TextView header = (TextView) dialog.findViewById(R.id.header);
        TextView msg = (TextView) dialog.findViewById(R.id.msg);
        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
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

    private void getDialogMove(final int i) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_age);
        activity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView header = (TextView) dialog.findViewById(R.id.header);
        TextView msg = (TextView) dialog.findViewById(R.id.msg);
        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
        btn_yes.setText("Ok");
        btn_no.setText("Cancel");

        header.setText("KenCommerce");
        msg.setText("Do you want to move this item?");
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemid = data.get(i).get(MyShoppingListAllItemsActivity.Key_product_id);
                flag_my_shopping_all_item_list = "yes";
                Intent shoppingList = new Intent(activity, ShoppingListActivity.class);
                activity.finish();
                activity.startActivity(shoppingList);

            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private void removeItemFromShoppingList(final String itemId, String strCustId, String list_id) {
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.show();
        pDialog.setMessage("Processing your data..");
        restService.deleteShoppingListItem(itemId, strCustId, list_id, new RestCallback<DeleteShoppingListItemRequest>() {
            @Override
            public void success(DeleteShoppingListItemRequest responce) {
                pDialog.dismiss();
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();


                if (reqStatus == 200 && reqSuccess == 1) {
                    displayAlert("Item has been deleted successfully", itemId);
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
