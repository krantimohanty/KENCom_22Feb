package com.swash.kencommerce;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.MyShoppingListAdapter;
import com.swash.kencommerce.adapter.MyShoppingListAllItemAdapter;
import com.swash.kencommerce.dbhelper.AddToCartTable;
import com.swash.kencommerce.model.CartTotalRequest;
import com.swash.kencommerce.model.MyShoppingListAllltemRequest;
import com.swash.kencommerce.model.ShoppingListDetailRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

public class MyShoppingListAllItemsActivity extends AppCompatActivity {

    ExpandableHeightListView lv_shoping_list;
    Toolbar toolbar;
    LinearLayout lin_add_new_to_list;
    RelativeLayout rl_main;
    ImageView imgBack;
    TextView tv_signin;
    public static TextView tv_shopping_list_count,tv_shopping_list_name;
    LinearLayout lin_create_new_list;
    EditText et_search;
    Boolean isSearched;
    Utils utils;
    LinearLayout lin_rename_list, lin_share_list, lin_other_list;
    LinearLayout lin_cart;

    String userId="";

    int i=0;
    private Button btn_count;
    AddToCartTable mAddToCartTable;

    private static String TAG = "PermissionDemo";
    private static final int REQUEST_WRITE_STORAGE = 112;

    public static ArrayList<HashMap<String, String>> listWishList = new ArrayList<HashMap<String, String>>();


    SharedPreferences sharedPreferenceUser;
    RestService restService;
    private ProgressDialog pw;

    public static String Key_review_summary = "product_review_summery";
    public static String Key_review_count = "product_review_count";
    public static String Key_product_id = "id";
    public static String Key_product_name = "name";
    public static String Key_product_image = "product_image";
    public static String Key_product_available = "product_is_available";
    public static String Key_product_is_special = "product_is_special";
    public static String Key_product_special_price = "product_special_price";
    public static String Key_product_normal_price = "product_normal_price";

    public static String Key_attribute_id = "attribute_id";
    public static String Key_option_id = "option_id";
    private ImageView img_landing_icon;
    private ImageView img_wishlist_icon;
    String customer_name="";
    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_shopping_list_all_items);
        utils=new Utils(MyShoppingListAllItemsActivity.this);
        mAddToCartTable=new AddToCartTable(this);
        i=mAddToCartTable.getCount();
        restService=new RestService();

        sharedPreferenceUser = this.getSharedPreferences(Constants.strShPrefUserPrefName, Activity.MODE_PRIVATE);
        userId = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        customer_name = sharedPreferenceUser.getString(Constants.strShPrefUserFname, "");

        // Track Data : Add to activities where tracking needed
         AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();


        if(utils.isConnectionPossible()) {
            getShoppingDetailListItem(MyShoppingListAdapter.customar_list_id, sharedPreferenceUser.getString(Constants.strShPrefUserId, ""));
            getCartTotal(userId);
        }
        else{
            utils.displayAlert("Internet connection is not available. Try again later.");
        }
        //getShoppingDetailListItem(MyShoppingListAdapter.customar_list_id, "5");
    }


    private void getShoppingDetailListItem(String id, String customar) {
        pw = new ProgressDialog(MyShoppingListAllItemsActivity.this);
        pw.show();
        pw.setMessage("Loading... Please wait.");

        restService.viewShoppingDetailList(id, customar, new RestCallback<ShoppingListDetailRequest>() {
            @Override
            public void success(ShoppingListDetailRequest object) {
                Log.d("Response", String.valueOf(object.getSuccess()));

                int reqStatus = object.getStatus();
                int reqSuccess = object.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    int size = object.getData().getShoppingListDetailList().size();
                    listWishList = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i < size; i++) {
                        HashMap<String, String> mapShopingDetailList = new HashMap<String, String>();
                        mapShopingDetailList.put(Key_product_id, object.getData().getShoppingListDetailList().get(i).getProductId().toString());
                        mapShopingDetailList.put(Key_product_name, object.getData().getShoppingListDetailList().get(i).getProductName().toString());
                        mapShopingDetailList.put(Key_product_image, object.getData().getShoppingListDetailList().get(i).getProductImage().toString());
                        mapShopingDetailList.put(Key_product_available, object.getData().getShoppingListDetailList().get(i).getProductIsAvailable().toString());
                        mapShopingDetailList.put(Key_review_count, object.getData().getShoppingListDetailList().get(i).getProductReviewCount().toString());
                        mapShopingDetailList.put(Key_review_summary, object.getData().getShoppingListDetailList().get(i).getProductReviewSummery().toString());
                        mapShopingDetailList.put(Key_product_is_special, object.getData().getShoppingListDetailList().get(i).getProductIsSpecial().toString());
                        mapShopingDetailList.put(Key_product_special_price, object.getData().getShoppingListDetailList().get(i).getProductSpecialPrice().toString());
                        mapShopingDetailList.put(Key_product_normal_price, object.getData().getShoppingListDetailList().get(i).getProductNormalPrice().toString());


                        if (object.getData().getShoppingListDetailList().get(i).getProductSize() != null) {
                            mapShopingDetailList.put(Key_attribute_id, object.getData().getShoppingListDetailList().get(i).getProductSize().get(0).getAttributeId().toString());
                            mapShopingDetailList.put(Key_option_id, object.getData().getShoppingListDetailList().get(i).getProductSize().get(0).getOptionId().toString());
                        } else {
                            mapShopingDetailList.put(Key_attribute_id, "");
                            mapShopingDetailList.put(Key_option_id, "");
                        }

                        listWishList.add(mapShopingDetailList);


                        lv_shoping_list = (ExpandableHeightListView) findViewById(R.id.lv_shoping_list);
                        lv_shoping_list.setAdapter(new MyShoppingListAllItemAdapter(MyShoppingListAllItemsActivity.this, listWishList, userId));
                        lv_shoping_list.setExpanded(true);
                        lv_shoping_list.setFocusable(false);
                    }
                }

                if (pw != null)
                    pw.dismiss();

            }

            @Override
            public void invalid() {

                if (pw != null)
                    pw.dismiss();
                Toast.makeText(MyShoppingListAllItemsActivity.this, "Sorry for the inconveniance. Please try again later.", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pw != null)
                    pw.dismiss();

                Toast.makeText(MyShoppingListAllItemsActivity.this, "Connection timed out. Please try again.", Toast.LENGTH_LONG).show();

            }

        });

    }

    private void getCartTotal(String customer_id) {

        pDialog=new ProgressDialog(this);
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
                Toast.makeText(MyShoppingListAllItemsActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(MyShoppingListAllItemsActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }


    private void initFields() {
        rl_main = (RelativeLayout) findViewById(R.id.rl_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);

        img_landing_icon=(ImageView)findViewById(R.id.img_landing_icon);
        img_landing_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyShoppingListAllItemsActivity.this, LandingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });

        img_wishlist_icon=(ImageView)findViewById(R.id.img_wishlist_icon);
        img_wishlist_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyShoppingListAllItemsActivity.this, MyWishListActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(MyShoppingListAllItemsActivity.this,MyShoppingListActivity.class);
                finish();
                startActivity(intent);
                MyShoppingListAllItemsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        tv_shopping_list_name = (TextView) findViewById(R.id.tv_shopping_list_name);
        tv_shopping_list_name.setText(MyShoppingListAdapter.shopping_list_name);
        btn_count=(Button)findViewById(R.id.btn_count);
        btn_count.setText("" + i);
        tv_signin=(TextView)findViewById(R.id.tv_signin);
        //tv_signin.setText(customer_name);

        if (userId != null & !userId.equalsIgnoreCase("")) {
            tv_signin.setText(customer_name);
        } else{
            displayAlert("To proceed, kindly sign into your account");
        }
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(MyShoppingListAllItemsActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);*/
            }
        });
        lin_cart=(LinearLayout)findViewById(R.id.lin_cart);
        lin_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyShoppingListAllItemsActivity.this, AddToCartListAllItemsActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.MyShoppingListAllItemsActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        tv_shopping_list_count=(TextView)findViewById(R.id.tv_shopping_list_count);
        lin_rename_list=(LinearLayout)findViewById(R.id.lin_rename_list);
        lin_rename_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialogCoverage();
            }
        });
        lin_share_list=(LinearLayout)findViewById(R.id.lin_share_list);
        lin_share_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getDialogShare();

                /*int permission = ContextCompat.checkSelfPermission(MyShoppingListAllItemsActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission to record denied");

                    if (ActivityCompat.shouldShowRequestPermissionRationale(MyShoppingListAllItemsActivity.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyShoppingListAllItemsActivity.this);
                        builder.setMessage("Permission to access the SD-CARD is required for this app to share.")
                                .setTitle("Permission required");

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                Log.i(TAG, "Clicked");
                                makeRequest();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        makeRequest();
                    }
                }
                else {
                    shareGeneral();
                }*/

                shareUrl(MyShoppingListAdapter.customar_list_id);
            }
        });
        lin_other_list=(LinearLayout)findViewById(R.id.lin_other_list);
        lin_other_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getDialogCoverage();
                Intent intent= new Intent(MyShoppingListAllItemsActivity.this,MyShoppingListActivity.class);
                finish();
                startActivity(intent);
                MyShoppingListAllItemsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
       /* lin_create_new_list=(LinearLayout)findViewById(R.id.lin_create_new_list);
        lin_create_new_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog();
            }
        });*/

      /*  listShoppingList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> mapShopList = new HashMap<String, String>();
        mapShopList.put(Key_shoppingList, "Good Morning list");
        mapShopList.put(Key_shoppingList_Count, "5 Items");
        listShoppingList.add(mapShopList);
        HashMap<String, String> mapShopList2 = new HashMap<String, String>();
        mapShopList2.put(Key_shoppingList, "Summer Shopping");
        mapShopList2.put(Key_shoppingList_Count, "9 Items");
        listShoppingList.add(mapShopList2);
        HashMap<String, String> mapShopList3 = new HashMap<String, String>();
        mapShopList3.put(Key_shoppingList, "Awesome Products");
        mapShopList3.put(Key_shoppingList_Count, "3 Items");
        listShoppingList.add(mapShopList3);
        HashMap<String, String> mapShopList4 = new HashMap<String, String>();
        mapShopList4.put(Key_shoppingList, "My Choicelist");
        mapShopList4.put(Key_shoppingList_Count, "7 Items");
        listShoppingList.add(mapShopList4);*/

       /* lv_shoping_list=(ExpandableHeightListView)findViewById(R.id.lv_shoping_list);
        lv_shoping_list.setAdapter(new MyShoppingListAllItemAdapter(MyShoppingListAllItemsActivity.this, listShoppingList));
        lv_shoping_list.setExpanded(true);
        lv_shoping_list.setFocusable(false);*/

        et_search=(EditText)findViewById(R.id.et_search);
       /* et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(listShoppingList!=null){
                    if(count>0){
                        isSearched=true;
                        String keyword = et_search.getText().toString();
                        listSearchedShoppingList=new ArrayList<HashMap<String, String>>();
                        keyword=keyword.toLowerCase();
                        for (int i = 0; i < listShoppingList.size(); i++) {

                            if(listShoppingList.get(i).get("shoppingList").toString().toLowerCase().startsWith(keyword)){

                                HashMap<String, String> mapShopList = new HashMap<String, String>();
                                mapShopList.put(Key_shoppingList, listShoppingList.get(i).get("shoppingList").toString());
                                mapShopList.put(Key_shoppingList_Count, listShoppingList.get(i).get("shoppingListCount").toString());
                                listSearchedShoppingList.add(mapShopList);
                            }
                        }
                        lv_shoping_list=(ExpandableHeightListView)findViewById(R.id.lv_shoping_list);
                        lv_shoping_list.setAdapter(new MyShoppingListAdapter(MyShoppingListAllItemsActivity.this, listSearchedShoppingList));
                        lv_shoping_list.setExpanded(true);
                        lv_shoping_list.setFocusable(false);
                        tv_shopping_list_count.setText("Shopping list (" + listSearchedShoppingList.size()+")");


                    }else if(count==0){
                        lv_shoping_list=(ExpandableHeightListView)findViewById(R.id.lv_shoping_list);
                        lv_shoping_list.setAdapter(new MyShoppingListAdapter(MyShoppingListAllItemsActivity.this, listShoppingList));
                        lv_shoping_list.setExpanded(true);
                        lv_shoping_list.setFocusable(false);
                        tv_shopping_list_count.setText("Shopping list (" + listShoppingList.size()+")");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }


    private void displayAlert(String message) {
        // TODO Auto-generated method stub
        message="To proceed, kindly sign into your account.";
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(MyShoppingListAllItemsActivity.this, LoginActivity.class);
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

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void getDialog() {
        final Dialog dialog = new Dialog(MyShoppingListAllItemsActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView btn_no=(TextView)dialog.findViewById(R.id.btn_ok);
        TextView btn_yes=(TextView)dialog.findViewById(R.id.btn_cancel);
        final EditText et_provide_name=(EditText)dialog.findViewById(R.id.msg);

       /* btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               dialog.dismiss();
                if(et_provide_name.getText().toString().isEmpty()||et_provide_name.getText().toString().equalsIgnoreCase("")){
                    utils.displayAlert("Please provide shopping name");
                }else{
                    HashMap<String, String> mapShopList = new HashMap<String, String>();
                    mapShopList.put(Key_shoppingList, et_provide_name.getText().toString());
                    mapShopList.put(Key_shoppingList_Count, "0 Item");
                    listShoppingList.add(mapShopList);
                    lv_shoping_list=(ExpandableHeightListView)findViewById(R.id.lv_shoping_list);
                    lv_shoping_list.setAdapter(new MyShoppingListAdapter(MyShoppingListAllItemsActivity.this, listShoppingList));
                    lv_shoping_list.setExpanded(true);
                    lv_shoping_list.setFocusable(false);
                    tv_shopping_list_count.setText("Shopping list ("+listShoppingList.size()+")");
                }

            }
        });*/
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(MyShoppingListAllItemsActivity.this,MyShoppingListActivity.class);
        finish();
        startActivity(intent);
        MyShoppingListAllItemsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(MyShoppingListAllItemsActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_all_item);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView header=(TextView)dialog.findViewById(R.id.header);
        /*TextView msg=(TextView)dialog.findViewById(R.id.msg);*/
        final EditText et_provide_name = (EditText) dialog.findViewById(R.id.edit_msg);
        TextView btn_ok=(TextView)dialog.findViewById(R.id.btn_ok);
        TextView btn_cencel=(TextView)dialog.findViewById(R.id.btn_cancel);
        /*btn_yes.setText("Ok");
        btn_no.setText("Cancel");*/
        //header.setText("Stockup");
        //msg.setText("Coming Soon");
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*dialog.dismiss();*/

                if (et_provide_name.getText().toString().isEmpty() || et_provide_name.getText().toString().equalsIgnoreCase("")) {
                    utils.displayAlert("Please provide shopping name");
                } else {
                    dialog.dismiss();
                    String user_id = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                    Log.d("user_id", user_id);
                    getNewAllItemData(sharedPreferenceUser.getString(Constants.strShPrefUserId, ""), et_provide_name.getText().toString().trim(), MyShoppingListAdapter.customar_list_id);
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void getDialogShare() {
        final Dialog dialog = new Dialog(MyShoppingListAllItemsActivity.this);
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

    public void shareGeneral(){
        Bitmap bmpImg = screenShot(rl_main);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");

        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), bmpImg, options);*/
        int imageHeight = bmpImg.getHeight();

        /*Canvas canvas = new Canvas(bmpImg);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#048BCD")); // Text Color
        //paint.setStrokeWidth(12); // Text Size
        paint.setTextSize(35); // Text Size
        //paint.setTextAlign(Paint.Align.CENTER); // Text Align
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
        // some more settings...
        canvas.drawBitmap(bmpImg, 0, imageHeight, paint);
        int coX = 10, coY;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        coY = height - 100;
        for (String line: strShareText.split("\n"))
        {
            canvas.drawText(line, coX, coY, paint);
            coY += -paint.ascent() + paint.descent();
        }*/
        //canvas.drawText(strShareText, 10, 30, paint);

        //String strShareText = ;
        shareIntent.putExtra(Intent.EXTRA_TEXT, MyShoppingListAdapter.shopping_list_name);
        shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(MyShoppingListAllItemsActivity.this, bmpImg));
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(shareIntent);
    }

    public void shareUrl(String strId){
        Bitmap bmpImg = screenShot(rl_main);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_TEXT, MyShoppingListAdapter.shopping_list_name);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.swashconvergence.com/KenCommerce.html/"+strId);
        //shareIntent.putExtra(Intent.EXTRA_STREAM, "");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(shareIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyShoppingListAllItemsActivity.this);
                    builder.setMessage("Permission to access the SD-CARD is denied. So you won't be able to share your shopping list. For share plase provide the access.")
                            .setTitle("Permission Denied");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            Log.i(TAG, "Clicked");
                            //makeRequest();
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    Log.i(TAG, "Permission has been denied by user");

                } else {

                    shareGeneral();

                    Log.i(TAG, "Permission has been granted by user");

                }
                return;
            }
        }
    }

    public void getNewAllItemData(String custmmerid, String name, String listid) {
        final ProgressDialog pDialog = new ProgressDialog(MyShoppingListAllItemsActivity.this);
        pDialog.show();
        pDialog.setMessage("Please wait..");
        restService.getViewMyAllltemList(custmmerid, name, listid, new RestCallback<MyShoppingListAllltemRequest>() {
            @Override
            public void success(MyShoppingListAllltemRequest response) {
                //Log.d("Result", response.getSuccess_msg());
                String reSuccess = String.valueOf(response.getSuccess());
                Log.d("reqSuccess", reSuccess);

                if (reSuccess.equalsIgnoreCase(String.valueOf(response.getSuccess()))) {
                    //Toast.makeText(MyShoppingListAllItemsActivity.this, "Shopping list successfully renamed.", Toast.LENGTH_LONG).show();
                    /*Intent i =new Intent(MyShoppingListAllItemsActivity.this,MyShoppingListActivity.class);
                    startActivity(i);
                    finish();*/
                    getDialogAfrerRename(response.getData().getSuccessMsg());
                }
                if (pDialog != null)
                    pDialog.dismiss();

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


    private void getDialogAfrerRename(String errorMsg) {
        final Dialog dialogAfterRename = new Dialog(this);
        Window window = dialogAfterRename.getWindow();
        dialogAfterRename.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAfterRename.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAfterRename.setContentView(R.layout.dialog_remove_list);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvHeader = (TextView) dialogAfterRename.findViewById(R.id.header);
        tvHeader.setText("KenCommerce");
        TextView tvMsg = (TextView) dialogAfterRename.findViewById(R.id.textView_remove_list);
        tvMsg.setText(errorMsg);
        TextView btn_no = (TextView) dialogAfterRename.findViewById(R.id.btn_cancel);
        TextView btn_yes = (TextView) dialogAfterRename.findViewById(R.id.btn_ok);

        btn_no.setVisibility(View.GONE);
        btn_yes.setText("Ok");

      /*  btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAfterRename.dismiss();
                Intent intent= new Intent(MyShoppingListAllItemsActivity.this,MyShoppingListActivity.class);
                finish();
                startActivity(intent);
                MyShoppingListAllItemsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            }
        });
        dialogAfterRename.setCancelable(false);
        dialogAfterRename.setCanceledOnTouchOutside(false);
        dialogAfterRename.show();
    }

}
