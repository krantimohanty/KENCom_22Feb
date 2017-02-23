package com.swash.kencommerce;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.swash.kencommerce.adapter.MyShoppingListAllItemAdapter;
import com.swash.kencommerce.adapter.ShoppingListAdapter;
import com.swash.kencommerce.dbhelper.AddToCartTable;
import com.swash.kencommerce.model.AddItemToShoppingListRequest;
import com.swash.kencommerce.model.CartTotalRequest;
import com.swash.kencommerce.model.MoveShoppingListItemRequest;
import com.swash.kencommerce.model.ShoppingListRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

public class ShoppingListActivity extends AppCompatActivity {

    ExpandableHeightListView lv_shoping_list;
    public static ArrayList<HashMap<String, String>> listShoppingList = new ArrayList<HashMap<String, String>>();
    public static String Key_shoppingList = "shoppingList";
    LinearLayout lin_add_new_to_list;
    ImageView imgBack;
    TextView tv_signin;
    private Utils utils;
    Button btn_done;
    LinearLayout lin_cart;
    RestService restService;
    private ProgressDialog pw;

    int i = 0;
    private Button btn_count;
    AddToCartTable mAddToCartTable;
    SharedPreferences sharedPreferenceUser;
    String cust_id = "", productid = "", qnty = "";
    ArrayList<String> new_list;
    private ProgressDialog pDialog;


    public static String Key_shoppingListId = "id";
    public static String Key_shoppingListName = "name";
    public static String Key_shoppingList_Count = "item_count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shopping_list);
        utils = new Utils(ShoppingListActivity.this);
        restService = new RestService();
        mAddToCartTable = new AddToCartTable(this);
        i = mAddToCartTable.getCount();
        sharedPreferenceUser = this.getSharedPreferences(Constants.strShPrefUserPrefName, Activity.MODE_PRIVATE);
        cust_id = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productid = extras.getString("productid");
            qnty = extras.getString("qnty");
        }

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();

        //getShoppingMyListItem("5");
        String strCustId = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");

        if (MyShoppingListAllItemAdapter.flag_my_shopping_all_item_list.equalsIgnoreCase("yes")){

            MyShoppingListAdapter.customar_list_id =  MyShoppingListAdapter.customar_list_id;
        }else {

            MyShoppingListAdapter.customar_list_id="";
        }

        if (!strCustId.equalsIgnoreCase("") & utils.isConnectionPossible()) {
            getShoppingMyListItem(sharedPreferenceUser.getString(Constants.strShPrefUserId, ""));
            getCartTotal(strCustId);
        } else {
            utils.displayAlert("Internet connection is not available. Try again later.");
        }
    }

    private void getCartTotal(String customer_id) {

        pDialog = new ProgressDialog(this);
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
                Toast.makeText(ShoppingListActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(ShoppingListActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void initFields() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ShoppingListActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        btn_count = (Button) findViewById(R.id.btn_count);
        btn_count.setText("" + i);
        tv_signin = (TextView) findViewById(R.id.tv_signin);
        tv_signin.setText("Add To Shopping List");
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(ShoppingListActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);*/
            }
        });
        lin_cart = (LinearLayout) findViewById(R.id.lin_cart);
        lin_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingListActivity.this, AddToCartListAllItemsActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.ShoppingListActivity");
                startActivity(intent);
            }
        });
        btn_count = (Button) findViewById(R.id.btn_count);
        lin_add_new_to_list = (LinearLayout) findViewById(R.id.lin_add_new_list);
        lin_add_new_to_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog();

            }
        });


        lv_shoping_list = (ExpandableHeightListView) findViewById(R.id.lv_shoping_list);


        btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MyShoppingListAllItemAdapter.flag_my_shopping_all_item_list.equalsIgnoreCase("yes")) {
                    if (ShoppingListAdapter.arrIDs != null && ShoppingListAdapter.arrIDs.size() > 0) {
                        MyShoppingListAllItemAdapter.flag_my_shopping_all_item_list = "";

                        String list_title = "";
                        moveItemFromShoppingList(ShoppingListAdapter.list_ids_all, cust_id, list_title);
                    } else {
                        utils.displayAlert("Please select atleast one list to add.");
                    }
                } else {
                    if (ShoppingListAdapter.arrIDs != null && ShoppingListAdapter.arrIDs.size() > 0) {

                        String list_title = "";
                        addItemToShoppingList(qnty, ShoppingListAdapter.list_ids_all, list_title, productid, cust_id);
                    } else {
                        utils.displayAlert("Please select atleast one list to add.");
                    }

                }
            }
        });
    }

    private void moveItemFromShoppingList(String list_id, String strCustId, String list_title) {
        final ProgressDialog pDialog = new ProgressDialog(ShoppingListActivity.this);
        pDialog.show();
        pDialog.setMessage("Processing your data..");
        restService.moveShoppingListItem(MyShoppingListAllItemAdapter.itemid, list_id, MyShoppingListAdapter.customar_list_id, strCustId, list_title, new RestCallback<MoveShoppingListItemRequest>() {
            @Override
            public void success(MoveShoppingListItemRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    MyShoppingListAdapter.customar_list_id = "";
                    Intent intent = new Intent(ShoppingListActivity.this, MyShoppingListActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    //getDialogOK(responce.getErrorMsg());
                    utils.displayAlert(responce.getErrorMsg());
                }
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


    private void addItemToShoppingList(String qty, String listId, String list_title, String productid, String strCustId) {
        final ProgressDialog pDialog = new ProgressDialog(ShoppingListActivity.this);
        pDialog.show();
        pDialog.setMessage("Processing your data..");
        restService.insertItemToShoppinglist(qty, listId, list_title, productid, strCustId, new RestCallback<AddItemToShoppingListRequest>() {
            @Override
            public void success(AddItemToShoppingListRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    Intent intent = new Intent(ShoppingListActivity.this, MyShoppingListActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    //getDialogOK(responce.getErrorMsg());
                    utils.displayAlert(responce.getErrorMsg());
                }
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

    private void getDialog() {

        final Dialog dialog = new Dialog(ShoppingListActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText et_provide_name = (EditText) dialog.findViewById(R.id.msg);
        TextView btn_no = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView btn_yes = (TextView) dialog.findViewById(R.id.btn_ok);

       /* btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if(et_provide_name.getText().toString().isEmpty()||et_provide_name.getText().toString().equalsIgnoreCase("")){
                    utils.displayAlert("Please provide shopping name");
                }else{
                    HashMap<String, String> mapShopList = new HashMap<String, String>();
                    mapShopList.put(Key_shoppingList, et_provide_name.getText().toString());
                    listShoppingList.add(mapShopList);
                    lv_shoping_list=(ExpandableHeightListView)findViewById(R.id.lv_shoping_list);
                    lv_shoping_list.setAdapter(new MyShoppingListAdapter(ShoppingListActivity.this, listShoppingList));
                    lv_shoping_list.setExpanded(true);
                    lv_shoping_list.setFocusable(false);

                }

            }
        });*/

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ShoppingListAdapter.arrIDs!= null) {
                    ShoppingListAdapter.arrIDs.add("0");
                }else{
                    ShoppingListAdapter.arrIDs=new ArrayList<>();
                    ShoppingListAdapter.arrIDs.add("0");
                }

                if (et_provide_name.getText().toString().equalsIgnoreCase("") || et_provide_name.getText().toString().isEmpty()) {

                    utils.displayAlert("Please provide new shopping list name");
                } else {
                    if (MyShoppingListAllItemAdapter.flag_my_shopping_all_item_list.equalsIgnoreCase("yes")) {
                        MyShoppingListAllItemAdapter.flag_my_shopping_all_item_list = "";
                        String list_id = ShoppingListAdapter.arrIDs.toString();
                        ;
                        String list_title = et_provide_name.getText().toString();
                        dialog.dismiss();
                        moveItemFromShoppingList(list_id, cust_id, list_title);

                    } else {
                        String list_title = et_provide_name.getText().toString();
                        String list_ids = ShoppingListAdapter.arrIDs.toString();
                        dialog.dismiss();
                        addItemToShoppingList(qnty, list_ids, list_title, productid, cust_id);

                    }
                }


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
        MyShoppingListAdapter.customar_list_id ="";
        ShoppingListActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(ShoppingListActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_age);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

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


    private void getShoppingMyListItem(String customar_id) {
        pw = new ProgressDialog(ShoppingListActivity.this);
        pw.show();
        pw.setMessage("Loading... Please wait.");

        restService.viewShoppingList(customar_id, new RestCallback<ShoppingListRequest>() {
            @Override
            public void success(ShoppingListRequest object) {

                /*Log.d("Response", String.valueOf(object.getSuccess()));
                lv_shoping_list.setAdapter(new ShoppingListAdapter(ShoppingListActivity.this, object.getData().getShoppingList()));
                lv_shoping_list.setExpanded(true);
                lv_shoping_list.setFocusable(false);*/

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

                    lv_shoping_list.setAdapter(new ShoppingListAdapter(ShoppingListActivity.this, listShoppingList));
                    lv_shoping_list.setExpanded(true);
                    lv_shoping_list.setFocusable(false);

                }


                if (pw != null)
                    pw.dismiss();

            }

            @Override
            public void invalid() {

                if (pw != null)
                    pw.dismiss();

                Toast.makeText(ShoppingListActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pw != null)
                    pw.dismiss();

                Toast.makeText(ShoppingListActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }

        });

    }
}
