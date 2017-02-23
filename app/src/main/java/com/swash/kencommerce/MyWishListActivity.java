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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import java.util.ArrayList;
import java.util.HashMap;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.MyWishlistAdapter;
import com.swash.kencommerce.dbhelper.AddToCartTable;
import com.swash.kencommerce.model.CartTotalRequest;
import com.swash.kencommerce.model.GetWishListRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

public class MyWishListActivity extends AppCompatActivity {

    ExpandableHeightListView lv_shoping_list;
    public static ArrayList<HashMap<String, String>> listWishList = new ArrayList<HashMap<String, String>>();

    public static String Key_wishlist_id = "wishlist_id";
    public static String Key_item_qty = "item_qty";
    public static String Key_product_id = "product_id";
    public static String Key_product_name = "product_name";
    public static String Key_product_img_url = "product_img_url";
    public static String Key_currency = "currency";
    public static String Key_product_price = "product_price";
    public static String Key_product_is_special = "product_is_special";
    public static String Key_product_special_price = "product_special_price";
    public static String Key_product_is_in_stock = "product_is_in_stock";
    public static String Key_product_is_salable = "product_is_salable";
    public static String Key_product_reviews_count = "product_reviews_count";
    public static String Key_product_rating_summary = "product_rating_summary";
    public static String Key_attribute_id = "attribute_id";
    public static String Key_option_id = "option_id";

    Toolbar toolbar;
    ImageView imgBack;
    TextView tv_signin;
    Utils utils;
    LinearLayout lin_cart;
    int i=0;
    private Button btn_count;
    AddToCartTable mAddToCartTable;
    RestService restService;
    SharedPreferences sharedPreferenceUser;
    SharedPreferences.Editor sharedPrefEditior;
    private ImageView img_landing_icon;
    private ImageView img_wishlist_icon;
    private ProgressDialog pDialog;
    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;
    private SharedPreferences shPrefUserSelection;
    String store_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_wishlist);
        utils=new Utils(MyWishListActivity.this);
        mAddToCartTable=new AddToCartTable(this);
        i=mAddToCartTable.getCount();
        restService=new RestService();
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");
        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();
    }

    private void initFields() {
        sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        img_landing_icon=(ImageView)findViewById(R.id.img_landing_icon);
        img_landing_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyWishListActivity.this, LandingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });

        img_wishlist_icon=(ImageView)findViewById(R.id.img_wishlist_icon);
        img_wishlist_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyWishListActivity.this, MyWishListActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MyWishListActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        lin_cart=(LinearLayout)findViewById(R.id.lin_cart);
        lin_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyWishListActivity.this, AddToCartListAllItemsActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.MyWishListActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        btn_count=(Button)findViewById(R.id.btn_count);
        btn_count.setText("" + i);
        tv_signin=(TextView)findViewById(R.id.tv_signin);
        tv_signin.setText("My Wishlist");
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        String strCustId = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        if (!strCustId.equalsIgnoreCase("")){
            getMyWishList(strCustId);
            getCartTotal(strCustId);
        }else{
            btn_count.setText(""+i);
        }
    }

    private void getMyWishList(String strCustId) {
        final ProgressDialog pDialog=new ProgressDialog(MyWishListActivity.this);
        pDialog.show();
        pDialog.setMessage("Fetching wish list..");
        restService.getWishList(strCustId, new RestCallback<GetWishListRequest>() {
            @Override
            public void success(GetWishListRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    int size = responce.getData().getWishList().size();
                    listWishList = new ArrayList<HashMap<String, String>>();
                    if(size>0) {
                        for (int i = 0; i < size; i++) {
                            HashMap<String, String> mapShopList = new HashMap<String, String>();
                            mapShopList.put(Key_wishlist_id, responce.getData().getWishList().get(i).getWishlistId().toString());
                            mapShopList.put(Key_item_qty, responce.getData().getWishList().get(i).getItemQty().toString());
                            mapShopList.put(Key_product_id, responce.getData().getWishList().get(i).getProductId().toString());
                            mapShopList.put(Key_product_name, responce.getData().getWishList().get(i).getProductName().toString());
                            mapShopList.put(Key_product_img_url, responce.getData().getWishList().get(i).getProductImgUrl().toString());
                            mapShopList.put(Key_currency, responce.getData().getWishList().get(i).getCurrency().toString());
                            mapShopList.put(Key_product_price, responce.getData().getWishList().get(i).getProductPrice().toString());
                            mapShopList.put(Key_product_is_special, responce.getData().getWishList().get(i).getProductIsSpecial().toString());
                            mapShopList.put(Key_product_special_price, responce.getData().getWishList().get(i).getProductSpecialPrice().toString());
                            mapShopList.put(Key_product_is_in_stock, responce.getData().getWishList().get(i).getProductIsInStock().toString());
                            mapShopList.put(Key_product_is_salable, responce.getData().getWishList().get(i).getProductIsSalable().toString());
                            mapShopList.put(Key_product_reviews_count, responce.getData().getWishList().get(i).getProductReviewsCount().toString());
                            mapShopList.put(Key_product_rating_summary, responce.getData().getWishList().get(i).getProductRatingSummary().toString());

                            if (responce.getData().getWishList().get(i).getProductSize() != null) {
                                mapShopList.put(Key_attribute_id, responce.getData().getWishList().get(i).getProductSize().get(0).getAttributeId().toString());
                                mapShopList.put(Key_option_id, responce.getData().getWishList().get(i).getProductSize().get(0).getOptionId().toString());
                            } else {
                                mapShopList.put(Key_attribute_id, "");
                                mapShopList.put(Key_option_id, "");
                            }


                            listWishList.add(mapShopList);
                        }
                        // Put Customer ID
                        String strCustId = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                        lv_shoping_list = (ExpandableHeightListView) findViewById(R.id.lv_shoping_list);
                        lv_shoping_list.setAdapter(new MyWishlistAdapter(MyWishListActivity.this, listWishList, strCustId, btn_count));
                        lv_shoping_list.setExpanded(true);
                        lv_shoping_list.setFocusable(false);
                    }
                    else{
                        displayAlert(responce.getErrorMsg());
                    }
                    // Dismiss Dialog
                    pDialog.dismiss();
                } else {
                    displayAlert(responce.getErrorMsg());
                    // Dismiss Dialog
                    pDialog.dismiss();
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyWishListActivity.this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Continue Browsing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                shPrefUserSelection = MyWishListActivity.this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
                store_id = shPrefUserSelection.getString(Constants.strShUserProductId, "");

                if (store_id.equalsIgnoreCase("")) {
                    Intent intent = new Intent(MyWishListActivity.this, PinCodeActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                } else {
                    Intent intent = new Intent(MyWishListActivity.this, SubCategoryActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }

            }
        });
        TextView myMsg = new TextView(MyWishListActivity.this);
        myMsg.setText(message);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setTextSize(18);
        myMsg.setTypeface(typeFaceSegoeuiReg);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
        alertDialogBuilder.setView(myMsg);

        TextView title = new TextView(MyWishListActivity.this);
        // You Can Customise your Title here
        title.setText("KenCommerce");
        title.setBackgroundColor(Color.TRANSPARENT);
        title.setPadding(15, 20, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTypeface(typeFaceSegoeuiBold);
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
    private void getDialog() {
        final Dialog dialog = new Dialog(MyWishListActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView btn_no=(TextView)dialog.findViewById(R.id.btn_ok);
        TextView btn_yes=(TextView)dialog.findViewById(R.id.btn_cancel);
        final EditText et_provide_name=(EditText)dialog.findViewById(R.id.msg);

        /*btn_yes.setText("Ok");
        btn_no.setText("Cancel");*/


        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                /*if(et_provide_name.getText().toString().isEmpty()||et_provide_name.getText().toString().equalsIgnoreCase("")){
                    utils.displayAlert("Please provide shopping name");
                }else{
                    HashMap<String, String> mapShopList = new HashMap<String, String>();
                    mapShopList.put(Key_shoppingList, et_provide_name.getText().toString());
                    mapShopList.put(Key_shoppingList_Count, "0 Item");
                    listWishList.add(mapShopList);
                    lv_shoping_list=(ExpandableHeightListView)findViewById(R.id.lv_shoping_list);
                    lv_shoping_list.setAdapter(new MyShoppingListAdapter(MyWishListActivity.this, listWishList));
                    lv_shoping_list.setExpanded(true);
                    lv_shoping_list.setFocusable(false);
                }*/

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
        final Dialog dialog = new Dialog(MyWishListActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        MyWishListActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void getCartTotal(String customer_id) {

        pDialog=new ProgressDialog(this);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait");
        restService.getCartTotal(customer_id, new RestCallback<CartTotalRequest>() {
            @Override
            public void success(CartTotalRequest object) {

                if(object.getStatus()==200&&object.getSuccess()==1){

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
                Toast.makeText(MyWishListActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(MyWishListActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }

}
