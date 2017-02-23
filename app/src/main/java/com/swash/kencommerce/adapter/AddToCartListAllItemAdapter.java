package com.swash.kencommerce.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.swash.kencommerce.AddToCartListAllItemsActivity;
import com.swash.kencommerce.R;
import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.model.CartData;
import com.swash.kencommerce.model.DeleteItemFromCartRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kranti on 12/12/2016.
 */
public class AddToCartListAllItemAdapter extends BaseAdapter {

    public static ArrayList<String> arrDeliveryType;
    private double total_price;
    private SharedPreferences sharedPreferenceUser;
    private Activity activity;
    public static List<CartData> data;
    private static LayoutInflater inflater=null;
    ViewHolder holder = null;
    private ProgressDialog pDialog;
    RestService restService;
    private Utils utils;
    String customer_id="";
    public static ArrayList<HashMap<String, String>> listAddToCartList = new ArrayList<HashMap<String, String>>();
    public static String Key_shoppingList = "AddToCartList";
    public static String Key_shoppingList_Count = "AddToCartListCount";
    TextView txt_empty_cart;
    TextView tv_checkout;
    LinearLayout lin_vendor_name;
    TextView tv_total_price;
    NumberFormat nf;


    ScrollView srcl_view;
    LinearLayout lin_no_item;
    private double item_price=0.0;

    public AddToCartListAllItemAdapter(Activity a, List<CartData> d, TextView tv_total_price,  TextView txt_empty_cart, TextView tv_checkout, LinearLayout lin_vendor_name, ScrollView srcl_view, LinearLayout lin_no_item) {
        arrDeliveryType=new ArrayList<>();
        this.activity = a;
        this.data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.tv_total_price=tv_total_price;
        this.txt_empty_cart=txt_empty_cart;
        this.lin_vendor_name=lin_vendor_name;
        this.tv_checkout=tv_checkout;
        this.srcl_view=srcl_view;
        this.lin_no_item=lin_no_item;
        sharedPreferenceUser=activity.getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
        customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        restService=new RestService();
        utils=new Utils(activity);
        AddToCartListAllItemsActivity.total_price=0.00;
        listAddToCartList = new ArrayList<HashMap<String, String>>();
        for(int i=0; i<data.size(); i++){
            HashMap<String, String> mapShopList = new HashMap<String, String>();
            mapShopList.put("product_id", data.get(i).getProductId());
            mapShopList.put("attribute_id", data.get(i).getAttributeId());
            mapShopList.put("option_id", data.get(i).getOptionId());
            mapShopList.put("qty", data.get(i).getProductQty().toString());
            mapShopList.put("delivery_type", data.get(i).getProductDelivery().toString());

            mapShopList.put("is_saleble", data.get(i).getProductIsSalable().toString());
            total_price=Double.parseDouble(data.get(i).getProductPrice());
            total_price=Double.parseDouble(data.get(i).getProductPrice())*data.get(i).getProductQty();
            listAddToCartList.add(mapShopList);
            this.tv_total_price.setVisibility(View.VISIBLE);
            arrDeliveryType.add(data.get(i).getProductDelivery());
            AddToCartListAllItemsActivity.total_price=AddToCartListAllItemsActivity.total_price+total_price;
            total_price=0;
        }
        nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        //nf.format(AddToCartListAllItemsActivity.total_price);

       // AddToCartListAllItemsActivity.total_price =Double.parseDouble(new DecimalFormat("##.####").format(AddToCartListAllItemsActivity.total_price));
        this.tv_total_price.setText("R"+nf.format(AddToCartListAllItemsActivity.total_price));
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

        TextView tv_name;
        RelativeLayout rel_parent, ll_blink;
        Button imgPlus,imgMinus;
        LinearLayout lin_on_demand;
        TextView txt_count_cart_add, txt_asap, txt_item_price;
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

        item_price=Double.parseDouble(data.get(position).getProductPrice());
        item_price=Double.parseDouble(data.get(position).getProductPrice())*data.get(position).getProductQty();
        holder.txt_item_price.setText("R"+nf.format(item_price));
        holder.rel_parent.setTag(position);
        holder.tv_name.setText(data.get(position).getProductName());
        holder.imgMinus.setTag(position);
        holder.imgPlus.setTag(position);

        if(data.get(position).getProductDelivery().contains("ASAP")){
            holder.img_on_demand.setVisibility(View.VISIBLE);
            holder.txt_asap.setVisibility(View.VISIBLE);
        }else{
            holder.img_on_demand.setVisibility(View.GONE);
            holder.txt_asap.setVisibility(View.GONE);
        }

        tv_checkout.setVisibility(View.VISIBLE);
        holder.txt_count_cart_add.setText(data.get(position).getProductQty().toString());
        holder.txt_count_cart_add.setTag(position);
        holder.imgPlus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                item_price=0.0;
                int i = Integer.parseInt(v.getTag().toString());
                Animation startAnimation1 = AnimationUtils.loadAnimation(activity, R.anim.anim_sequence_2);
                RelativeLayout ll_blink = (RelativeLayout) parent.getChildAt(i).findViewById(R.id.ll_blink);
                ll_blink.startAnimation(startAnimation1);
                TextView txt_count_cart_add = (TextView) parent.getChildAt(i).findViewById(R.id.txt_count_cart_add);
                String strCount = txt_count_cart_add.getText().toString().trim();
                int intCount = Integer.parseInt(strCount);
                intCount++;
                txt_count_cart_add.setText(String.valueOf(intCount));
                data.get(i).setProductQty(intCount);

                TextView txt_item_price = (TextView) parent.getChildAt(i).findViewById(R.id.txt_item_price);

                item_price=Double.parseDouble(data.get(i).getProductPrice())*data.get(i).getProductQty();

                txt_item_price.setText("R"+nf.format(item_price));

                AddToCartListAllItemsActivity.total_price=AddToCartListAllItemsActivity.total_price+Double.parseDouble(data.get(i).getProductPrice());
                tv_total_price.setText("R"+nf.format(AddToCartListAllItemsActivity.total_price));
            }
        });

        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i = Integer.parseInt(v.getTag().toString());
                Animation startAnimation1 = AnimationUtils.loadAnimation(activity, R.anim.anim_sequence_2);
                RelativeLayout ll_blink = (RelativeLayout) parent.getChildAt(i).findViewById(R.id.ll_blink);
                ll_blink.startAnimation(startAnimation1);
                TextView txt_count_cart_add = (TextView) parent.getChildAt(i).findViewById(R.id.txt_count_cart_add);
                String strCount = txt_count_cart_add.getText().toString().trim();
                final TextView txt_name = (TextView) parent.getChildAt(i).findViewById(R.id.txt_name);
                int intCount = Integer.parseInt(strCount);
                if (intCount > 0) {
                    intCount--;
                    txt_count_cart_add.setText(String.valueOf(intCount));
                    data.get(i).setProductQty(intCount);
                    TextView txt_item_price = (TextView) parent.getChildAt(i).findViewById(R.id.txt_item_price);

                    item_price=Double.parseDouble(data.get(i).getProductPrice())*data.get(i).getProductQty();

                    txt_item_price.setText("R" + nf.format(item_price));
                    AddToCartListAllItemsActivity.total_price=AddToCartListAllItemsActivity.total_price-Double.parseDouble(data.get(i).getProductPrice());
                    tv_total_price.setText("R"+nf.format(AddToCartListAllItemsActivity.total_price));
                }

                if (txt_count_cart_add.getText().toString().equalsIgnoreCase("0")) {

                    deleteCartItem(customer_id, data.get(i).getCartItemId());
                    data.remove(i);
                    arrDeliveryType.remove(i);
                    notifyDataSetChanged();
                    if (data.size() == 0) {
                        AddToCartListAllItemsActivity.size=0;
                        //AddToCartListAllItemsActivity.tv_checkout.setVisibility(View.INVISIBLE);
                        tv_checkout.setText("Continue Shopping");
                        tv_total_price.setVisibility(View.GONE);
                        txt_empty_cart.setVisibility(View.GONE);
                        lin_vendor_name.setVisibility(View.GONE);
                        srcl_view.setVisibility(View.GONE);
                        lin_no_item.setVisibility(View.VISIBLE);

                    }
                }
            }
        });

        return convertView;
    }

    private void deleteCartItem(String customer_id, String product_id) {
        pDialog = new ProgressDialog(activity);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait.");
        restService.deleteItemCart(customer_id, product_id, new RestCallback<DeleteItemFromCartRequest>() {
            @Override
            public void success(DeleteItemFromCartRequest object) {
                if (pDialog != null)
                    pDialog.dismiss();
                if (object.getStatus() == 200 && object.getSuccess() == 1) {
                    utils.displayAlert("Successfully removed from cart.");
                } else
                {
                    utils.displayAlert(object.getErrorMsg());
                }
            }

            @Override
            public void invalid() {
                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(activity, "Problem while fetching cart list", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure() {
                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(activity, "Connection Timeout. Try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

}
