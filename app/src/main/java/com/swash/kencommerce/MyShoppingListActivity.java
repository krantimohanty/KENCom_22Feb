package com.swash.kencommerce;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;
import android.widget.Toast;

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import java.util.ArrayList;
import java.util.HashMap;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.MyShoppingListAdapter;
import com.swash.kencommerce.dbhelper.AddToCartTable;
import com.swash.kencommerce.model.CartTotalRequest;
import com.swash.kencommerce.model.MyShoppingListItemRequest;
import com.swash.kencommerce.model.ShoppingListRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

public class MyShoppingListActivity extends AppCompatActivity {

    ExpandableHeightListView lv_shoping_list;
    public static ArrayList<HashMap<String, String>> listShoppingList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listSearchedShoppingList = new ArrayList<HashMap<String, String>>();

    public static String Key_shoppingListId = "id";
    public static String Key_shoppingListName = "name";
    public static String Key_shoppingList_Count = "item_count";
    Toolbar toolbar;
    LinearLayout lin_add_new_to_list;
    ImageView imgBack;
    TextView tv_signin;
    public static TextView tv_shopping_list_count;
    LinearLayout lin_create_new_list;
    EditText et_search;
    Boolean isSearched;
    Utils utils;
    SharedPreferences sharedPreferenceUser;
    RestService restService;
    private ProgressDialog pw;
    LinearLayout lin_cart;
    int i = 0;
    private Button btn_count;
    AddToCartTable mAddToCartTable;
    private ImageView img_landing_icon;
    private ImageView img_wishlist_icon;
    private String customer_id = "", customer_name = "";
    private ProgressDialog pDialog;
    Dialog dialog_new_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_shopping_list);
        utils = new Utils(MyShoppingListActivity.this);
        restService = new RestService();
        mAddToCartTable = new AddToCartTable(this);
        i = mAddToCartTable.getCount();
        sharedPreferenceUser = this.getSharedPreferences(Constants.strShPrefUserPrefName, Activity.MODE_PRIVATE);
        customer_id = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        customer_name = sharedPreferenceUser.getString(Constants.strShPrefUserFname, "");

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();

        if(utils.isConnectionPossible()) {
            getShoppingMyListItem(sharedPreferenceUser.getString(Constants.strShPrefUserId, ""));
            getCartTotal(customer_id);
        }
        else{
            utils.displayAlert("Internet connection is not available. Try again later.");
        }
        //getShoppingMyListItem("5");

    }

    private void getShoppingMyListItem(String customar_id) {
        pw = new ProgressDialog(MyShoppingListActivity.this);
        pw.show();
        pw.setMessage("Loading... Please wait.");

        restService.viewShoppingList(customar_id, new RestCallback<ShoppingListRequest>() {
            @Override
            public void success(ShoppingListRequest object) {

                int reqStatus = object.getStatus();
                int reqSuccess = object.getSuccess();

                if (reqStatus == 200 && reqSuccess == 1) {
                    Log.d("Response", String.valueOf(object.getSuccess()));
                    listShoppingList.clear();
                    int count = object.getData().getShoppingList().size();
                    for (int i = 0; i < count; i++) {
                        HashMap<String, String> mapShopList = new HashMap<String, String>();
                        mapShopList.put(Key_shoppingListId, object.getData().getShoppingList().get(i).getId().toString());
                        mapShopList.put(Key_shoppingListName, object.getData().getShoppingList().get(i).getName().toString());
                        mapShopList.put(Key_shoppingList_Count, object.getData().getShoppingList().get(i).getItemCount().toString());

                        listShoppingList.add(mapShopList);
                    }
                    if(count==1) {
                        tv_shopping_list_count.setText("Saved Shopping List");
                    }else if (count>1){
                        tv_shopping_list_count.setText("Saved Shopping List(s)" );
                    }
                    //tv_shopping_list_count.setText("Shopping list" + " " + "(" + String.valueOf(count) + ")");
                    lv_shoping_list = (ExpandableHeightListView) findViewById(R.id.lv_shoping_list);
                    lv_shoping_list.setAdapter(new MyShoppingListAdapter(MyShoppingListActivity.this, listShoppingList));
                    lv_shoping_list.setExpanded(true);
                    lv_shoping_list.setFocusable(false);

                    // Dismiss Dialog
                    pw.dismiss();

                } else {
                    utils.displayAlert(object.getErrorMsg());
                    pw.dismiss();
                }
            }

            @Override
            public void invalid() {

                if (pw != null)
                    pw.dismiss();
                Toast.makeText(MyShoppingListActivity.this, "Sorry for the inconveniance. Please try again later.", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pw != null)
                    pw.dismiss();

                Toast.makeText(MyShoppingListActivity.this, "Connection timed out. Please try again.", Toast.LENGTH_LONG).show();

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
                Toast.makeText(MyShoppingListActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(MyShoppingListActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getDialogOK(String strMsg) {
        final Dialog dialog = new Dialog(MyShoppingListActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_response_ok);

        TextView txt_header = (TextView) dialog.findViewById(R.id.txt_header);
        TextView txt_msg = (TextView) dialog.findViewById(R.id.txt_msg);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);

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

    private void initFields() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        img_landing_icon = (ImageView) findViewById(R.id.img_landing_icon);
        img_landing_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyShoppingListActivity.this, LandingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        img_wishlist_icon = (ImageView) findViewById(R.id.img_wishlist_icon);
        img_wishlist_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyShoppingListActivity.this, MyWishListActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MyShoppingListActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        btn_count = (Button) findViewById(R.id.btn_count);
        btn_count.setText("" + i);

        tv_signin = (TextView) findViewById(R.id.tv_signin);

        if (customer_id != null & !customer_id.equalsIgnoreCase("")) {
            tv_signin.setText(customer_name);
        } else{
            displayAlert("To proceed, kindly sign into your account.");
        }
       /* tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyShoppingListActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });*/


        lin_cart = (LinearLayout) findViewById(R.id.lin_cart);
        lin_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyShoppingListActivity.this, AddToCartListAllItemsActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.MyShoppingListActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        tv_shopping_list_count = (TextView) findViewById(R.id.tv_shopping_list_count);
        lin_create_new_list = (LinearLayout) findViewById(R.id.lin_create_new_list);
        lin_create_new_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog();
            }
        });

        lv_shoping_list = (ExpandableHeightListView) findViewById(R.id.lv_shoping_list);


        et_search = (EditText) findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (listShoppingList != null) {
                    if (count > 0) {
                        isSearched = true;
                        String keyword = et_search.getText().toString();

                        //ArrayList<HashMap<String, String>> listSearchedShoppingList = new ArrayList<HashMap<String, String>>();

                        //listSearchedShoppingList.put(listShoppingList)
                        listSearchedShoppingList.clear();
                        keyword = keyword.toLowerCase();
                        for (int i = 0; i < listShoppingList.size(); i++) {

                            if (listShoppingList.get(i).get(Key_shoppingListName).toString().toLowerCase().contains(keyword)) {

                                HashMap<String, String> mapShopSearchList = new HashMap<String, String>();
                                mapShopSearchList.put(Key_shoppingListId, listShoppingList.get(i).get(Key_shoppingListId).toString());
                                mapShopSearchList.put(Key_shoppingListName, listShoppingList.get(i).get(Key_shoppingListName).toString());
                                mapShopSearchList.put(Key_shoppingList_Count, listShoppingList.get(i).get(Key_shoppingList_Count).toString());
                                //Log.d("",listShoppingList)
                                listSearchedShoppingList.add(mapShopSearchList);


                            }
                        }
                        lv_shoping_list = (ExpandableHeightListView) findViewById(R.id.lv_shoping_list);
                        lv_shoping_list.setAdapter(new MyShoppingListAdapter(MyShoppingListActivity.this, listSearchedShoppingList));
                        lv_shoping_list.setExpanded(true);
                        lv_shoping_list.setFocusable(false);
                        //tv_shopping_list_count.setText("Shopping list (" + listSearchedShoppingList.size() + ")");
                        if((listSearchedShoppingList.size())<=1) {
                            tv_shopping_list_count.setText("Saved Shopping List");
                        }else if (listSearchedShoppingList.size()>1){
                            tv_shopping_list_count.setText("Saved Shopping List(s)");
                        }


                    } else if (count == 0) {

                        lv_shoping_list = (ExpandableHeightListView) findViewById(R.id.lv_shoping_list);
                        lv_shoping_list.setAdapter(new MyShoppingListAdapter(MyShoppingListActivity.this, listShoppingList));
                        lv_shoping_list.setExpanded(true);
                        lv_shoping_list.setFocusable(false);
                        //tv_shopping_list_count.setText("Shopping list (" + listShoppingList.size() + ")");
                        if(listShoppingList.size()<=1) {
                            tv_shopping_list_count.setText("Saved Shopping List");
                        }else if (listShoppingList.size()>1){
                            tv_shopping_list_count.setText("Saved Shopping List(s)");
                        }

                    }
                }

                //String keyword = et_search.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void displayAlert(String message) {
        // TODO Auto-generated method stub
        message="To proceed, kindly sign into your account.";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent=new Intent(MyShoppingListActivity.this, LoginActivity.class);
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

    private void getDialog() {
        dialog_new_list = new Dialog(MyShoppingListActivity.this);
        Window window = dialog_new_list.getWindow();
        dialog_new_list.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_new_list.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_new_list.setContentView(R.layout.dialog_new_list_item);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView btn_yes = (TextView) dialog_new_list.findViewById(R.id.btn_ok);
        TextView btn_no = (TextView) dialog_new_list.findViewById(R.id.btn_cancel);
        final EditText et_provide_name = (EditText) dialog_new_list.findViewById(R.id.edit_msg);

       /* btn_yes.setText("Ok");
        btn_no.setText("Cancel");*/

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dialog.dismiss();
                //dialog.dismiss();
                if (et_provide_name.getText().toString().trim().isEmpty() || et_provide_name.getText().toString().trim().equalsIgnoreCase("")) {
                    utils.displayAlert("Please provide shopping list name");
                } else {

                    getNewListData(sharedPreferenceUser.getString(Constants.strShPrefUserId, ""), et_provide_name.getText().toString().trim());
                }

            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_new_list.dismiss();
            }
        });
        dialog_new_list.setCancelable(false);
        dialog_new_list.setCanceledOnTouchOutside(false);
        dialog_new_list.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        MyShoppingListActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void getNewListData(String custmmerid, String name) {

        final ProgressDialog pDialog = new ProgressDialog(MyShoppingListActivity.this);
        dialog_new_list.dismiss();
        pDialog.show();
        pDialog.setMessage("Please wait..");

        restService.getViewMyShoppingListItem(custmmerid, name, new RestCallback<MyShoppingListItemRequest>() {
            @Override
            public void success(MyShoppingListItemRequest response) {
                //Log.d("Result", response.getSuccess_msg());
                pDialog.dismiss();
                String reqSuccess = String.valueOf(response.getSuccess());
                Log.d("reqSuccess", reqSuccess);

                if (reqSuccess.equalsIgnoreCase(String.valueOf(response.getSuccess()))) {
                    //Toast.makeText(MyShoppingListActivity.this, response.getSuccess_msg().toString(), Toast.LENGTH_LONG).show();
                    getShoppingMyListItem(sharedPreferenceUser.getString(Constants.strShPrefUserId, ""));
                    utils.displayAlert(response.getData().getSuccessMsg());

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
}
