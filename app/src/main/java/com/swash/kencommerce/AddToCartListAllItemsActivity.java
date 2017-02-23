package com.swash.kencommerce;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.AddToCartListAllItemAdapter;
import com.swash.kencommerce.adapter.AddToCartLocalListAllItemAdapter;
import com.swash.kencommerce.dbhelper.AddToCartTable;
import com.swash.kencommerce.model.EmptyCardRequest;
import com.swash.kencommerce.model.GetCartListRequest;
import com.swash.kencommerce.model.UpdateCartRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//import com.appsflyer.AppsFlyerLib;


public class AddToCartListAllItemsActivity extends AppCompatActivity {

    ExpandableHeightListView lv_add_to_cart_list;
    public static ArrayList<HashMap<String, String>> listAddToCartList = new ArrayList<HashMap<String, String>>();
    public static String Key_shoppingList = "AddToCartList";
    public static String Key_shoppingList_Count = "AddToCartListCount";
    Toolbar toolbar;
    ImageView imgBack;
    TextView tv_signin;
    LinearLayout lin_create_new_list;
    EditText et_search;
    Boolean isSearched;
    Utils utils;
    LinearLayout lin_rename_list, lin_share_list, lin_other_list;
    public static AddToCartTable mAddToCartTable;
    public static double total_price=0;
    TextView tv_total_price;
    TextView tv_checkout;
    SharedPreferences shPrefDeliverAddr;
    SharedPreferences.Editor toEditAddr;
    ImageView imgWishlist, imgFIlter;
    LinearLayout lin_cart;
    RestService restService;
    private SharedPreferences sharedPreferenceUser;
    private String customer_id="";
    LinearLayout lin_no_item;
    TextView txt_no_item;
    ScrollView srcl_view;

    public static int size=0;
    private LinearLayout lin_vendor_name;

    String strInt = "";
    Class<?> nIntent = null;
    private TextView txt_clear_cart;

    String strOrdIdReOrd = "";
    private SharedPreferences shPrefUserSelection;
    SharedPreferences.Editor toEdit;
    private String store_id="";
    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;
    ProgressDialog pDialog;
    String isInRange="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_to_cart_list);
        shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);
        isInRange= shPrefDeliverAddr.getString(Constants.strShPrefDeliver, "");
        utils=new Utils(AddToCartListAllItemsActivity.this);
        restService=new RestService();

        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");

        sharedPreferenceUser=this.getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
        customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        mAddToCartTable=new AddToCartTable(this);
        listAddToCartList = new ArrayList<HashMap<String, String>>();
        listAddToCartList=mAddToCartTable.getAll();

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();
        Intent intentGet = getIntent();
        if (intentGet.hasExtra("context_act1")){
            //act = intentGet.getExtras().getParcelable("context_act");
            strInt = intentGet.getStringExtra("context_act1");
            strOrdIdReOrd = intentGet.getStringExtra(PastOrderListActivity.Key_order_id);
        }
        try {
            nIntent = Class.forName(strInt);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(customer_id.equalsIgnoreCase("")){
            if (listAddToCartList.size()>0) {
                lin_no_item.setVisibility(View.GONE);
                srcl_view.setVisibility(View.VISIBLE);
                lv_add_to_cart_list.setAdapter(new AddToCartLocalListAllItemAdapter(AddToCartListAllItemsActivity.this, listAddToCartList, tv_total_price, txt_clear_cart, tv_checkout, lin_vendor_name, srcl_view, lin_no_item));
                lv_add_to_cart_list.setExpanded(true);
                lv_add_to_cart_list.setFocusable(false);
                txt_clear_cart.setVisibility(View.VISIBLE);
                tv_checkout.setText("Proceed To Checkout");
            } else {
                lin_no_item.setVisibility(View.VISIBLE);
                srcl_view.setVisibility(View.GONE);
                txt_clear_cart.setVisibility(View.GONE);
                tv_checkout.setText("Continue Shopping");
            }

        }else{
            if (utils.isConnectionPossible()) {
                getCartDetails(customer_id);
            }
            else {
                utils.displayAlert("Internet Connection is not available. Try again later.");
            }
        }

        /*if(customer_id.equalsIgnoreCase("")){
            if(listAddToCartList.size()>0){
                tv_checkout.setText("Proceed To Checkout");
                lin_vendor_name.setVisibility(View.VISIBLE);
            }
            else
            {
                tv_checkout.setText("Continue Shopping");
                lin_vendor_name.setVisibility(View.INVISIBLE);

            }
        }*/
    }

    private void initFields() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);

        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nIntent != null) {
                    updateCart(customer_id, "nInt");

                    /*Intent intent = new Intent(AddToCartListAllItemsActivity.this, nIntent);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);*/
                } else {

                    updateCart(customer_id, "pin");
/*
                    Intent intent = new Intent(AddToCartListAllItemsActivity.this, PinCodeActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);*/
                }
                 AddToCartListAllItemsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        tv_signin = (TextView) findViewById(R.id.tv_signin);

        if (customer_id != null & !customer_id.equalsIgnoreCase("")) {
            tv_signin.setText("My Cart");
        }

        lin_no_item=(LinearLayout)findViewById(R.id.lin_no_item);
        txt_no_item=(TextView)findViewById(R.id.txt_no_item);
        srcl_view=(ScrollView)findViewById(R.id.scrl_view);

        txt_clear_cart=(TextView)findViewById(R.id.txt_clear_cart);
        txt_clear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(!customer_id.equalsIgnoreCase("")){
                    dialogEmptyCart(customer_id);
                //}
            }
        });

        tv_total_price=(TextView)findViewById(R.id.txt_total_price);
        tv_total_price.setVisibility(View.INVISIBLE);
        lin_vendor_name=(LinearLayout)findViewById(R.id.lin_vendor_name);
        tv_checkout=(TextView)findViewById(R.id.txt_checkout);


        ////////////////////////Checkout button will be only visible when there is at least one product in add to cart page////////////
        /*if(listAddToCartList.size()>0){

            tv_checkout.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_checkout.setVisibility(View.INVISIBLE);
        }*/
        tv_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if(isInRange.equalsIgnoreCase("true")) {
                    if (customer_id.equalsIgnoreCase("")) {

                        String price = "";
                        if (tv_total_price.getText().toString().contains(",")) {
                            price = tv_total_price.getText().toString().replace(",", "");
                        } else {
                            price = tv_total_price.getText().toString();
                        }
                        if (price.startsWith("R")) {
                            price = price.substring(1, price.length());
                        } else {
                            price = price;
                        }
                        Double priceTotal = Double.parseDouble(price);

                        if (listAddToCartList.size() > 0) {

                            tv_checkout.setText("Proceed To Checkout");
                            displayAlert("To proceed, kindly sign into your account.");
                        } else if ((tv_total_price.getVisibility() == View.VISIBLE) && (priceTotal) < 150) {

                            getPriceOver150();

                        } else {

                            tv_checkout.setText("Continue Shopping");
                            shPrefUserSelection = AddToCartListAllItemsActivity.this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
                            store_id = shPrefUserSelection.getString(Constants.strShUserProductId, "");

                            if (store_id.equalsIgnoreCase("")) {

                                Intent intent = new Intent(AddToCartListAllItemsActivity.this, PinCodeActivity.class);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                            } else {

                                Intent intent = new Intent(AddToCartListAllItemsActivity.this, SubCategoryActivity.class);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            }
                        }
                    } else {
                        if (size > 0) {

                            tv_checkout.setText("Proceed To Checkout");
                            if (AddToCartListAllItemAdapter.arrDeliveryType.contains("NO RUSH") || AddToCartListAllItemAdapter.arrDeliveryType.contains("NO RUSH")) {
                                Constants.deliverytypeCart = "no rush";
                                Constants.deliverytypeCheckout="No Rush";
                                Constants.deliveryDateCheckout="No Rush";
                            } else {
                                Constants.deliverytypeCart = "asap";
                                Constants.deliverytypeCheckout="asap";
                                Constants.deliveryDateCheckout="ASAP";
                            }

                            AddToCartListAllItemAdapter.listAddToCartList = new ArrayList<HashMap<String, String>>();
                            for (int i = 0; i < AddToCartListAllItemAdapter.data.size(); i++) {
                                HashMap<String, String> mapShopList = new HashMap<String, String>();
                                mapShopList.put("product_id", AddToCartListAllItemAdapter.data.get(i).getProductId());
                                mapShopList.put("attribute_id", AddToCartListAllItemAdapter.data.get(i).getAttributeId());
                                mapShopList.put("option_id", AddToCartListAllItemAdapter.data.get(i).getOptionId());
                                mapShopList.put("qty", AddToCartListAllItemAdapter.data.get(i).getProductQty().toString());
                                mapShopList.put("product_name", AddToCartListAllItemAdapter.data.get(i).getProductName().toString());
                                mapShopList.put("product_price", AddToCartListAllItemAdapter.data.get(i).getProductPrice().toString());
                                AddToCartListAllItemAdapter.listAddToCartList.add(mapShopList);
                            }
                            String productName = "";
                            Constants.arrMixed = new ArrayList<String>();
                            for (int i = 0; i < AddToCartListAllItemAdapter.data.size(); i++) {
                                Constants.arrMixed.add(AddToCartListAllItemAdapter.data.get(i).getProductDelivery().toString());
                                if (AddToCartListAllItemAdapter.data.get(i).getProductIsSalable().toString().equalsIgnoreCase("0")) {
                                    productName = AddToCartListAllItemAdapter.data.get(i).getProductName().toString();
                                }
                            }
                            String price = "";
                            if (tv_total_price.getText().toString().contains(",")) {
                                price = tv_total_price.getText().toString().replace(",", "");
                            } else {
                                price = tv_total_price.getText().toString();
                            }
                            if (price.startsWith("R")) {
                                price = price.substring(1, price.length());
                            } else {
                                price = price;
                            }
                            Double priceTotal = Double.parseDouble(price);
                            if (!productName.equalsIgnoreCase("")) {
                                utils.displayAlert(productName + " is out of stock");

                            } else if ((tv_total_price.getVisibility() == View.VISIBLE) && (priceTotal) < 150) {

                                getPriceOver150();

                            } else {

                                updateCart(customer_id, "checkOut");
                            }

                        } else {

                            tv_checkout.setText("Continue Shopping");

                            shPrefUserSelection = AddToCartListAllItemsActivity.this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
                            store_id = shPrefUserSelection.getString(Constants.strShUserProductId, "");

                            if (store_id.equalsIgnoreCase("")) {
                                Intent intent = new Intent(AddToCartListAllItemsActivity.this, PinCodeActivity.class);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            } else {
                                Intent intent = new Intent(AddToCartListAllItemsActivity.this, SubCategoryActivity.class);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            }
                        }
                    }
                /*}else{
                    displayAlertCheckOut("KenCommerce is rolling out rapidly to major hubs across South Africa in the coming weeks. Once we reach your area we will let you know, but feel free to browse so long!");
                }*/

            }
        });

        lv_add_to_cart_list=(ExpandableHeightListView)findViewById(R.id.lv_add_to_cart_list);


    }


    private void getPriceOver150() {
        final Dialog dialog = new Dialog(AddToCartListAllItemsActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_delivery_type);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView header=(TextView)dialog.findViewById(R.id.header);
        header.setTypeface(typeFaceSegoeuiBold);
        TextView msg=(TextView)dialog.findViewById(R.id.msg);
        msg.setTypeface(typeFaceSegoeuiReg);
        TextView btn_yes=(TextView)dialog.findViewById(R.id.btn_ok);
        TextView btn_no=(TextView)dialog.findViewById(R.id.btn_cancel);
        //header.setText("Error");
        msg.setText("Your cart is below the R150.00 minimum order value.");
        btn_yes.setText("Shop On");
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Put Grocery selection for landing page
                dialog.dismiss();
                shPrefUserSelection = AddToCartListAllItemsActivity.this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
                store_id=shPrefUserSelection.getString(Constants.strShUserProductId, "");

                if(store_id.equalsIgnoreCase("")){
                    Intent intent=new Intent(AddToCartListAllItemsActivity.this, PinCodeActivity.class);
                    finish();
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(AddToCartListAllItemsActivity.this, SubCategoryActivity.class);
                    finish();
                    startActivity(intent);
                }
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        btn_no.setText("Update Cart");
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Put Grocery selection for landing page
                dialog.dismiss();

            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
    private void  updateCart(String customer_id, final String strCallFrom) {
        String strItem = "";
        if(AddToCartListAllItemAdapter.data!=null) {
            if (AddToCartListAllItemAdapter.data.size() > 0) {
                JSONArray jArrItem = new JSONArray();
                for (int i = 0; i < AddToCartListAllItemAdapter.data.size(); i++) {
                    JSONObject jObj = new JSONObject();
                    try {
                        jObj.put("cart_item_id", AddToCartListAllItemAdapter.data.get(i).getCartItemId());
                        jObj.put("product_qty", AddToCartListAllItemAdapter.data.get(i).getProductQty());
                        jArrItem.put(i, jObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                strItem = jArrItem.toString();
            } else {
                strItem = "";
            }
        }
        pDialog =new ProgressDialog(AddToCartListAllItemsActivity.this);
        pDialog.show();
        pDialog.setMessage("Loading...");
        restService.updateCart(customer_id, strItem, new RestCallback<UpdateCartRequest>() {

            @Override
            public void success(UpdateCartRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                } else {
                }
                pDialog.dismiss();
                if (strCallFrom.equalsIgnoreCase("checkOut")){
                    Intent intent = new Intent(AddToCartListAllItemsActivity.this, CheckOutActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                else if (strCallFrom.equalsIgnoreCase("pin")){
                    Intent intent=new Intent(AddToCartListAllItemsActivity.this, PinCodeActivity.class);
                    finish();
                    startActivity(intent);
                    AddToCartListAllItemsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                else{
                    Intent intent = new Intent(AddToCartListAllItemsActivity.this, nIntent);
                    intent.putExtra(PastOrderListActivity.Key_order_id, strOrdIdReOrd);
                    finish();
                    startActivity(intent);
                    AddToCartListAllItemsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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

    private void dialogEmptyCart(final String user_id) {
        final Dialog dialog = new Dialog(AddToCartListAllItemsActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_remove_list);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvHeader = (TextView) dialog.findViewById(R.id.header);
        tvHeader.setText("KenCommerce");
        TextView tvMsg = (TextView) dialog.findViewById(R.id.textView_remove_list);
        tvMsg.setText("Are you sure you wish to empty cart?");
        TextView btn_no = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView btn_yes = (TextView) dialog.findViewById(R.id.btn_ok);


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
                /*data.remove(i);
                notifyDataSetChanged();
                MyShoppingListActivity.tv_shopping_list_count.setText("Shopping list (" + data.size()+")");*/
                if (!user_id.equalsIgnoreCase("")) {
                    emptyCart(user_id);
                }
                else {
                    mAddToCartTable.deleteAll();
                    Intent i = new Intent(AddToCartListAllItemsActivity.this, AddToCartListAllItemsActivity.class);
                    finish();
                    startActivity(i);
                }
                size=0;
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void emptyCart(String customerid) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.show();
        pDialog.setMessage("Please wait..");

        restService.emptyCart(customerid, new RestCallback<EmptyCardRequest>() {
            @Override
            public void success(EmptyCardRequest response) {
                //Log.d("Result", response.getSuccess_msg());
                if (pDialog != null)
                    pDialog.dismiss();
                String reqSuccess = String.valueOf(response.getSuccess());
                Log.d("reqSuccess", reqSuccess);

                if (reqSuccess.equalsIgnoreCase(String.valueOf(response.getSuccess()))) {
                    //Toast.makeText(activity, "The shopping list was successfully deleted.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AddToCartListAllItemsActivity.this, AddToCartListAllItemsActivity.class);

                    finish();
                    startActivity(i);
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
    public void displayAlert(String message)
    {
        // TODO Auto-generated method stub
        message="To proceed, sign into your account.";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent=new Intent(AddToCartListAllItemsActivity.this, LoginActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.AddToCartListAllItemsActivity");
                finish();
                startActivity(intent);
            }
        });
        TextView myMsg = new TextView(this);
        myMsg.setText(message);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setTextSize(16);
        //myMsg.setTypeface(typeFaceSegoeuiReg);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL);
        alertDialogBuilder.setView(myMsg);

        TextView title = new TextView(this);
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
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();



        final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER|Gravity.CENTER_VERTICAL;
        positiveButton.setTextColor(Color.parseColor("#048BCD"));
        positiveButton.setLayoutParams(positiveButtonLL);

    }

    public void displayAlertCheckOut(String message)
    {
        // TODO Auto-generated method stub
        //message="To proceed, sign into your account.";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("We are rolling out!") ;
        alertDialogBuilder.setPositiveButton("Sure, I get it", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (customer_id.equalsIgnoreCase("")) {

                    String price = "";
                    if (tv_total_price.getText().toString().contains(",")) {
                        price = tv_total_price.getText().toString().replace(",", "");
                    } else {
                        price = tv_total_price.getText().toString();
                    }
                    if (price.startsWith("R")) {
                        price = price.substring(1, price.length());
                    } else {
                        price = price;
                    }
                    Double priceTotal = Double.parseDouble(price);

                    if (listAddToCartList.size() > 0) {

                        tv_checkout.setText("Proceed To Checkout");
                        displayAlert("To proceed, kindly sign into your account.");
                    } else if ((tv_total_price.getVisibility() == View.VISIBLE) && (priceTotal) < 150) {

                        getPriceOver150();

                    } else {

                        tv_checkout.setText("Continue Shopping");
                        shPrefUserSelection = AddToCartListAllItemsActivity.this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
                        store_id = shPrefUserSelection.getString(Constants.strShUserProductId, "");

                        if (store_id.equalsIgnoreCase("")) {

                            Intent intent = new Intent(AddToCartListAllItemsActivity.this, PinCodeActivity.class);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                        } else {

                            Intent intent = new Intent(AddToCartListAllItemsActivity.this, SubCategoryActivity.class);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        }
                    }
                } else {
                    if (size > 0) {

                        tv_checkout.setText("Proceed To Checkout");
                        if (AddToCartListAllItemAdapter.arrDeliveryType.contains("NO RUSH") || AddToCartListAllItemAdapter.arrDeliveryType.contains("NO RUSH")) {
                            Constants.deliverytypeCart = "no rush";
                            Constants.deliverytypeCheckout="No Rush";
                            Constants.deliveryDateCheckout="No Rush";
                        } else {
                            Constants.deliverytypeCart = "asap";
                            Constants.deliverytypeCheckout="asap";
                            Constants.deliveryDateCheckout="ASAP";
                        }

                        AddToCartListAllItemAdapter.listAddToCartList = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < AddToCartListAllItemAdapter.data.size(); i++) {
                            HashMap<String, String> mapShopList = new HashMap<String, String>();
                            mapShopList.put("product_id", AddToCartListAllItemAdapter.data.get(i).getProductId());
                            mapShopList.put("attribute_id", AddToCartListAllItemAdapter.data.get(i).getAttributeId());
                            mapShopList.put("option_id", AddToCartListAllItemAdapter.data.get(i).getOptionId());
                            mapShopList.put("qty", AddToCartListAllItemAdapter.data.get(i).getProductQty().toString());
                            mapShopList.put("product_name", AddToCartListAllItemAdapter.data.get(i).getProductName().toString());
                            mapShopList.put("product_price", AddToCartListAllItemAdapter.data.get(i).getProductPrice().toString());
                            AddToCartListAllItemAdapter.listAddToCartList.add(mapShopList);
                        }
                        String productName = "";
                        Constants.arrMixed = new ArrayList<String>();
                        for (int i = 0; i < AddToCartListAllItemAdapter.data.size(); i++) {
                            Constants.arrMixed.add(AddToCartListAllItemAdapter.data.get(i).getProductDelivery().toString());
                            if (AddToCartListAllItemAdapter.data.get(i).getProductIsSalable().toString().equalsIgnoreCase("0")) {
                                productName = AddToCartListAllItemAdapter.data.get(i).getProductName().toString();
                            }
                        }
                        String price = "";
                        if (tv_total_price.getText().toString().contains(",")) {
                            price = tv_total_price.getText().toString().replace(",", "");
                        } else {
                            price = tv_total_price.getText().toString();
                        }
                        if (price.startsWith("R")) {
                            price = price.substring(1, price.length());
                        } else {
                            price = price;
                        }
                        Double priceTotal = Double.parseDouble(price);
                        if (!productName.equalsIgnoreCase("")) {
                            utils.displayAlert(productName + " is out of stock");

                        } else if ((tv_total_price.getVisibility() == View.VISIBLE) && (priceTotal) < 150) {

                            getPriceOver150();

                        } else {

                            updateCart(customer_id, "checkOut");
                        }

                    } else {

                        tv_checkout.setText("Continue Shopping");

                        shPrefUserSelection = AddToCartListAllItemsActivity.this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
                        store_id = shPrefUserSelection.getString(Constants.strShUserProductId, "");

                        if (store_id.equalsIgnoreCase("")) {
                            Intent intent = new Intent(AddToCartListAllItemsActivity.this, PinCodeActivity.class);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        } else {
                            Intent intent = new Intent(AddToCartListAllItemsActivity.this, SubCategoryActivity.class);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        }
                    }
                }
            }
        });
        TextView myMsg = new TextView(this);
        myMsg.setText(message);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setTextSize(16);
        //myMsg.setTypeface(typeFaceSegoeuiReg);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL);
        alertDialogBuilder.setView(myMsg);

        TextView title = new TextView(this);
        // You Can Customise your Title here
        title.setText("We are rolling out!");
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
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();



        final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER|Gravity.CENTER_VERTICAL;
        positiveButton.setTextColor(Color.parseColor("#048BCD"));
        positiveButton.setLayoutParams(positiveButtonLL);

    }
    private void getCartDetails(final String customer_id) {
        pDialog = new ProgressDialog(AddToCartListAllItemsActivity.this);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait.");
        restService.getCartDetails(customer_id, new RestCallback<GetCartListRequest>() {
            @Override
            public void success(GetCartListRequest object) {
                if (object.getStatus() == 200 && object.getSuccess() == 1) {
                    lin_no_item.setVisibility(View.GONE);
                    srcl_view.setVisibility(View.VISIBLE);
                    lv_add_to_cart_list.setAdapter(new AddToCartListAllItemAdapter(AddToCartListAllItemsActivity.this, object.getData().getItems(), tv_total_price, txt_clear_cart,  tv_checkout, lin_vendor_name,srcl_view , lin_no_item));
                    lv_add_to_cart_list.setExpanded(true);
                    lv_add_to_cart_list.setFocusable(false);
                    size=object.getData().getItems().size();
                    txt_clear_cart.setVisibility(View.VISIBLE);
                } else {
                    //utils.displayAlert(object.getErrorMsg());
                    lin_no_item.setVisibility(View.VISIBLE);
                    srcl_view.setVisibility(View.GONE);
                    txt_clear_cart.setVisibility(View.GONE);
                    //txt_no_item.setText(object.getErrorMsg());
                }

                if(!customer_id.equalsIgnoreCase("")){
                    if(size>0){
                        tv_checkout.setText("Proceed To Checkout");
                        lin_vendor_name.setVisibility(View.VISIBLE);
                    }
                    else {
                        tv_checkout.setText("Continue Shopping");
                        lin_vendor_name.setVisibility(View.INVISIBLE);
                    }
                }
                if (pDialog != null)
                    pDialog.dismiss();
            }

            @Override
            public void invalid() {

                if (pDialog != null)
                    pDialog.dismiss();
                //Toast.makeText(AddToCartListAllItemsActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {
                if (pDialog != null)
                    pDialog.dismiss();
                //Toast.makeText(AddToCartListAllItemsActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {

            if (nIntent != null) {
                updateCart(customer_id, "nInt");


            } else {

                updateCart(customer_id, "pin");

            }
            if(Constants.strTipPercent!=null)
                Constants.strTipPercent="";
            AddToCartListAllItemsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        return true;
    }

    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(AddToCartListAllItemsActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_age);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

    private void getDialogShare() {
        final Dialog dialog = new Dialog(AddToCartListAllItemsActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_share);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btn_no=(Button)dialog.findViewById(R.id.btn_cancel);
        Button btn_yes=(Button)dialog.findViewById(R.id.btn_ok);
        LinearLayout lin_fb=(LinearLayout)dialog.findViewById(R.id.ll_fb);
        lin_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialogCoverage();
            }
        });
        LinearLayout lin_twit=(LinearLayout)dialog.findViewById(R.id.ll_twit);
        lin_twit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialogCoverage();
            }
        });
        LinearLayout lin_gplus=(LinearLayout)dialog.findViewById(R.id.ll_gplus);
        lin_gplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialogCoverage();
            }
        });
        LinearLayout lin_app_user=(LinearLayout)dialog.findViewById(R.id.ll_app_user);
        lin_app_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialogCoverage();
            }
        });
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
