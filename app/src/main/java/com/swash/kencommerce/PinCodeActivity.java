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
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.DialogType;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.MenuListAdapter;
import com.swash.kencommerce.dialog.ConfirmationDialog;
import com.swash.kencommerce.model.LogOutRequest;
import com.swash.kencommerce.model.SlideMenuOption;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;
import com.swash.kencommerce.webview.KenCommerceWebViewActivity;

public class PinCodeActivity extends AppCompatActivity {

    Button btn_proceed;
    TextView et_pin;
    ImageView img_background;
    LinearLayout ll_top_img/*, ll_bottom_img*/;
    RelativeLayout ll_coverage;
    TextView txt_header, txt_subheader, txt_top_bar;
    SharedPreferences shPrefUserSelection;
    SharedPreferences.Editor toEdit;
    String strUserSelectionShopType = "";
    ImageView img_bottom_img;

    //////////////////NavView////////////////////
    MenuListAdapter listAdapter;
    ExpandableListView expListView;
    DrawerLayout drawer;
    private Toolbar toolbar;
    private ImageView img_profile;
    private TextView txt_name, txt_email;

    private Button btn_nav;
    private String strPincode="";
    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;
    private TextView txt_coverage;
    Utils utils;
    LinearLayout ll_two;
    Bundle extras;
    SharedPreferences shPrefDeliverAddr;
    SharedPreferences.Editor toEditAddr;
    private String customer_id="";
    String strAge;

    RestService restService;
    SharedPreferences sharedPreferenceUser;
    SharedPreferences.Editor sharedPrefEditior;
    private LinearLayout img_landing_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pin_code);
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");
        utils=new Utils(PinCodeActivity.this);
        shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);
        toEditAddr=shPrefDeliverAddr.edit();
        restService=new RestService();

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();
        setDrawer();
        setNavigation();
        setDrawerMenu();
    }

    private void initFields() {
        sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);

        shPrefUserSelection = this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
        strUserSelectionShopType = shPrefUserSelection.getString(Constants.strShUserShopSelected, "");
        customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        strPincode=shPrefUserSelection.getString("PINCODE", "");
        strAge = shPrefUserSelection.getString(Constants.strAge, "");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        img_landing_icon=(LinearLayout)findViewById(R.id.lin_landing);
        img_landing_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PinCodeActivity.this, LandingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });

        ll_two=(LinearLayout)findViewById(R.id.ll_two);
        ll_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PinCodeActivity.this, MapActivity.class);
                intent.putExtra("for", "Pincode");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        String callFromMap = shPrefDeliverAddr.getString(Constants.strShPrefDelCallFrom, "");


        btn_nav=(Button)findViewById(R.id.btn_nav);
        btn_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

        btn_proceed = (Button) findViewById(R.id.btn_proceed);
        btn_proceed.setTypeface(typeFaceSegoeuiBold);
        et_pin = (TextView) findViewById(R.id.et_pin);
        et_pin.setTypeface(typeFaceSegoeuiReg);
        et_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PinCodeActivity.this, MapActivity.class);
                intent.putExtra("for", "Pincode");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        if (callFromMap.equalsIgnoreCase("Yes")/*&&forshipingbiling.equalsIgnoreCase("shiping")*/){
            String strLat = shPrefDeliverAddr.getString(Constants.strShPrefDelLat,"");
            String strLong = shPrefDeliverAddr.getString(Constants.strShPrefDelLong,"");
            String strAddr = shPrefDeliverAddr.getString(Constants.strShPrefDelAddr,"");
            et_pin.setText(strAddr);

        }
        ll_top_img = (LinearLayout) findViewById(R.id.ll_top_img);
        //ll_bottom_img = (LinearLayout) findViewById(R.id.ll_bottom_img);
        img_bottom_img=(ImageView)findViewById(R.id.img_bottom_img);
        ll_coverage = (RelativeLayout) findViewById(R.id.ll_coverage);
        txt_coverage=(TextView)findViewById(R.id.txt_coverage);
        txt_coverage.setTypeface(typeFaceSegoeuiBold);
        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_header.setTypeface(typeFaceSegoeuiBold);
        txt_subheader = (TextView) findViewById(R.id.txt_subheader);
        txt_subheader.setTypeface(typeFaceSegoeuiBold);
        txt_top_bar = (TextView) findViewById(R.id.txt_top_bar);
        txt_top_bar.setTypeface(typeFaceSegoeuiReg);
//        if (strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopMainProduct)){
            if (strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopFood)){
            Constants.strCatName="Chocolates";
            ll_top_img.setBackgroundResource(R.drawable.top_image_gift);
            //ll_bottom_img.setBackgroundResource(R.drawable.bottom_image_drink);
            img_bottom_img.setImageResource(R.drawable.bottom_image_gift);
            txt_header.setText("THE ON-DEMAND\n\nDELIVERY APP");
            txt_subheader.setText("Shifting from on-premises  \nsystem to Cloud ERP");
            txt_top_bar.setText("Chocolates");
        }

        // Navigration
        else if (strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopGift)){
            Constants.strCatName="Gifts";
            ll_top_img.setBackgroundResource(R.drawable.top_image_gift);
            //ll_bottom_img.setBackgroundResource(R.drawable.bottom_image_gift);
            img_bottom_img.setImageResource(R.drawable.bottom_image_gift);
            txt_header.setText("THE ON-DEMAND\nGIFTS\nDELIVERY APP");
            //txt_subheader.setText("The things you want\n delivered in under an hour");
            txt_subheader.setText("The things you want, delivered\nto your door in under an hour");
            txt_top_bar.setText("Gifts");
        }
        else if (strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopDay)){
            Constants.strCatName="Day-To-Day";
            ll_top_img.setBackgroundResource(R.drawable.day_heroimage);
            //ll_bottom_img.setBackgroundResource(R.drawable.bottom_image_day);
            img_bottom_img.setImageResource(R.drawable.bottom_image_day);
            txt_header.setText("THE ON-DEMAND\nDAY-TO-DAY\nDELIVERY APP");
            txt_subheader.setText("The things you want, delivered\nto your door in under an hour");
            txt_top_bar.setText("Day-To-Day");
        }
        else if(strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopFood)){
            Constants.strCatName="Foods";
            ll_top_img.setBackgroundResource(R.drawable.food_heroimage);
            //ll_bottom_img.setBackgroundResource(R.drawable.bottom_image_food);
            img_bottom_img.setImageResource(R.drawable.app_bottom_image);
            txt_header.setText("THE ON-DEMAND\nFOODS TREATS\nDELIVERY APP");
            txt_subheader.setText("The things you want, delivered\nto your door in under an hour");
            txt_top_bar.setText("Foods");
        }else if(strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopEvent)){
            Constants.strCatName="Events & Services";
            ll_top_img.setBackgroundResource(R.drawable.landing_upper_img_event);
            //ll_bottom_img.setBackgroundResource(R.drawable.bottom_image_food);
            img_bottom_img.setImageResource(R.drawable.landing_bottom_img_event);
            txt_header.setText("THE ON-DEMAND\nEVENTS & SERVICES \nDELIVERY APP");
            txt_subheader.setText("The things you want,\n delivered to you within minutes");
            txt_top_bar.setText("Events & Services");
        }

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_pin.getText().toString().trim().length() > 4) {


                    if (strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopMainProduct)) {
                        toEdit = shPrefUserSelection.edit();
                        toEdit.putString("PINCODE", et_pin.getText().toString());
                        toEdit.putString(Constants.strShUserStoreID, "10");
                        toEdit.commit();

                        Intent intent = new Intent(PinCodeActivity.this, DrinkCategoriesActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    } else if (strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopGift)) {
                        toEdit = shPrefUserSelection.edit();
                        toEdit.putString("PINCODE", et_pin.getText().toString());
                        toEdit.putString(Constants.strShUserStoreID, "13");
                        toEdit.commit();

                        Intent intent = new Intent(PinCodeActivity.this, GiftCategoriesActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    } else if (strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopDay)) {

                        toEdit = shPrefUserSelection.edit();
                        toEdit.putString("PINCODE", et_pin.getText().toString());
                        toEdit.putString(Constants.strShUserStoreID, "12");
                        toEdit.commit();
                        Intent intent = new Intent(PinCodeActivity.this, Day2DayCategoriesActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    } else if (strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopFood)) {
                        toEdit = shPrefUserSelection.edit();
                        toEdit.putString("PINCODE", et_pin.getText().toString());
                        toEdit.putString(Constants.strShUserStoreID, "11");
                        toEdit.commit();
                        Intent intent = new Intent(PinCodeActivity.this, FoodCategoriesActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    }else if (strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopEvent)) {

                        toEdit = shPrefUserSelection.edit();
                        toEdit.putString("PINCODE", et_pin.getText().toString());
                        toEdit.putString(Constants.strShUserStoreID, "14");
                        toEdit.commit();

                        Intent intent = new Intent(PinCodeActivity.this, EventCategoriesActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    }

                } else {
                    utils.displayAlert("Provide delivery address");
                    //Toast.makeText(PinCodeActivity.this, "Please enter your address and postal code?", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ll_coverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getDialogCoverage();
                /*String urlCoverage = "http://hireipho.nextmp.net/stockup/coverage";
                Intent intentCoverage = new Intent(Intent.ACTION_VIEW);
                intentCoverage.setData(Uri.parse(urlCoverage));
                startActivity(intentCoverage);*/

                Intent urlCoverage = new Intent(PinCodeActivity.this, KenCommerceWebViewActivity.class);
                urlCoverage.putExtra("header","Coverage");
                urlCoverage.putExtra("url","http://www.swashconvergence.com/KenCommerce.html");
                startActivity(urlCoverage);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
    }
    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(PinCodeActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
    private void getDialogSignOut() {
        final Dialog dialog = new Dialog(PinCodeActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_age);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView header=(TextView)dialog.findViewById(R.id.header);
        TextView msg=(TextView)dialog.findViewById(R.id.msg);
        Button btn_no=(Button)dialog.findViewById(R.id.btn_no);
        Button btn_yes=(Button)dialog.findViewById(R.id.btn_yes);
        btn_yes.setText("Ok");
        btn_no.setText("Cancel");

        header.setText("KenCommerce");
        msg.setText("Are you sure, you wish to Sign Out from the application?");
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
    private void setDrawerMenu() {

        expListView = (ExpandableListView) findViewById(R.id.menu_exp_list);
        final ArrayList<SlideMenuOption> slideMenuOptions = prepareListData();
        View footerView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.menu_footer, null, false);
        expListView.addFooterView(footerView);
        listAdapter = new MenuListAdapter(PinCodeActivity.this, slideMenuOptions);
        expListView.setAdapter(listAdapter);
        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                optionSelefctedNew(slideMenuOptions.get(groupPosition).getId());
                return false;
            }
        });
        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                optionSelefctedNew(slideMenuOptions.get(groupPosition).getSlideMenuOptionsChild().get(childPosition).getId());
                return false;
            }
        });
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer(0);
                ConfirmationDialog confirmationDialog = new ConfirmationDialog(PinCodeActivity.this, DialogType.LOGOUT, new ConfirmationDialog.Confiramtion() {
                    @Override
                    public void accept() {
                        //logOut();
                    }
                });
                if (!confirmationDialog.isShowing())
                    confirmationDialog.show();
            }
        });
    }

    private void optionSelefcted(int id) {

        switch (id){


            case 3:
               /* Intent intentAddToCart=new Intent(PinCodeActivity.this, AddToCartListAllItemsActivity.class);
                startActivity(intentAddToCart);*/
                Intent intent = new Intent(PinCodeActivity.this, AddToCartListAllItemsActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.PinCodeActivity");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;

            case 4:
                if(!customer_id.equalsIgnoreCase("")){
                    //getProductDetails(customer_id);
                    doLogOut();
                }
                else{
                    Intent intentSign=new Intent(PinCodeActivity.this, LoginActivity.class);
                    intentSign.putExtra("context_act1", "webskitters.com.stockup.PinCodeActivity");
                    startActivity(intentSign);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }

                break;

            case 5:
                getDialogCoverage();
                break;

            case 6:
                getDialogCoverage();
                break;


            case 8:
                if (strAge.equalsIgnoreCase("")) {
                    getDialogAge();
                } else {
                    Constants.strCatName = "Chocolates";
                    if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    toEdit = shPrefUserSelection.edit();
                    toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopMainProduct);
                    toEdit.putString("PINCODE", et_pin.getText().toString());
                    toEdit.putString(Constants.strShUserStoreID, "10");
                    toEdit.commit();
                    initFields();
                    setDrawer();
                    setNavigation();
                    setDrawerMenu();
                }
                //getDialogAge();
                break;
            case 9:
                Constants.strCatName="Foods";
                if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);}
                toEdit = shPrefUserSelection.edit();
                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopFood);
                toEdit.putString(Constants.strShUserStoreID, "11");
                toEdit.putString("PINCODE", et_pin.getText().toString());
                toEdit.commit();
                initFields();
                setDrawer();
                setNavigation();
                setDrawerMenu();
                break;
            case 10:
                Constants.strCatName="Day-To-Day";

                if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                toEdit = shPrefUserSelection.edit();
                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopDay);
                toEdit.putString(Constants.strShUserStoreID, "12");
                toEdit.putString("PINCODE", et_pin.getText().toString());
                toEdit.commit();
                initFields();
                setDrawer();
                setNavigation();
                setDrawerMenu();
                break;
            case 11:
                Constants.strCatName="Gifts";
                if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                toEdit = shPrefUserSelection.edit();
                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopGift);
                toEdit.putString(Constants.strShUserStoreID, "13");
                toEdit.putString("PINCODE", et_pin.getText().toString());
                toEdit.commit();
                initFields();
                setDrawer();
                setNavigation();
                setDrawerMenu();
                break;
            case 12:

                //getDialogCoverage();
                Constants.strCatName="Events";
                if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                toEdit = shPrefUserSelection.edit();
                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopEvent);
                toEdit.putString("PINCODE", et_pin.getText().toString());
                toEdit.commit();
                initFields();
                setDrawer();
                setNavigation();
                setDrawerMenu();
                break;
            case 13:
                sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
                customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                if (customer_id.equalsIgnoreCase("")){
                    //utils.displayAlert("Please SignIn to continue..");
                    displayAlert("To proceed, kindly sign into your account");
                }
                else {
                    Intent intentMyDetails = new Intent(PinCodeActivity.this, MyDetailsActivity.class);
                    startActivity(intentMyDetails);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                break;
            case 14:
                getDialogCoverage();
                break;
            case 15:
                //getDialogCoverage();
                sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
                customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                if (customer_id.equalsIgnoreCase("")){
                    displayAlert("To proceed, kindly sign into your account");
                }
                else {
                    Intent intentMyAddress = new Intent(PinCodeActivity.this, MyAddressActivity.class);
                    startActivity(intentMyAddress);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }

                break;
            case 16:
                sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
                customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                if (customer_id.equalsIgnoreCase("")){
                    displayAlert("To proceed, kindly sign into your account");
                }
                else {
                    Intent intentMyWishList = new Intent(PinCodeActivity.this, MyWishListActivity.class);
                    startActivity(intentMyWishList);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                break;
            case 17:
                sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
                customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                if (customer_id.equalsIgnoreCase("")){
                    displayAlert("To proceed, kindly sign into your account");
                }
                else {
                    Intent intentShoppingList = new Intent(PinCodeActivity.this, MyShoppingListActivity.class);
                    startActivity(intentShoppingList);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                break;
            case 18:
                sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
                customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                if (customer_id.equalsIgnoreCase("")){
                    displayAlert("To proceed, kindly sign into your account");
                }
                else {
                    Intent intentPastOrders = new Intent(PinCodeActivity.this, PastOrderListActivity.class);
                    startActivity(intentPastOrders);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                break;
            case 19:
                getDialogCoverage();
                /*Intent intentTrackOrders=new Intent(PinCodeActivity.this, TrackOrderListActivity.class);
                startActivity(intentTrackOrders);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);*/
                break;
            case 20:
                //getDialogCoverage();
                /*String urlServe = "http://hireipho.nextmp.net/stockup/where-serve";
                Intent intentServe = new Intent(Intent.ACTION_VIEW);
                intentServe.setData(Uri.parse(urlServe));
                startActivity(intentServe);*/

                Intent intentServe = new Intent(PinCodeActivity.this, KenCommerceWebViewActivity.class);
                intentServe.putExtra("header","Where We Serve");
                intentServe.putExtra("url","http://www.swashconvergence.com/KenCommerce.html");
                startActivity(intentServe);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case 21:
                //getDialogCoverage();
                /*String urlFaq = "http://hireipho.nextmp.net/stockup/faqs";
                Intent intentFaq = new Intent(Intent.ACTION_VIEW);
                intentFaq.setData(Uri.parse(urlFaq));
                startActivity(intentFaq);*/

                Intent intentFaq = new Intent(PinCodeActivity.this, KenCommerceWebViewActivity.class);
                intentFaq.putExtra("header","FAQs");
                intentFaq.putExtra("url","http://www.swashconvergence.com/KenCommerce.html");
                startActivity(intentFaq);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case 22:
                //getDialogCoverage();
                /*String urlPP = "http://hireipho.nextmp.net/stockup/terms-condition";
                Intent intentPP = new Intent(Intent.ACTION_VIEW);
                intentPP.setData(Uri.parse(urlPP));
                startActivity(intentPP);*/

                Intent urlPP = new Intent(PinCodeActivity.this, KenCommerceWebViewActivity.class);
                urlPP.putExtra("header","Privacy Policy");
                urlPP.putExtra("url","http://www.swashconvergence.com/KenCommerce.html");
                startActivity(urlPP);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case 23:
                //getDialogCoverage();
                /*String urlTandC = "http://hireipho.nextmp.net/stockup/terms-condition";
                Intent intentTandC = new Intent(Intent.ACTION_VIEW);
                intentTandC.setData(Uri.parse(urlTandC));
                startActivity(intentTandC);*/

                Intent urlTandC = new Intent(PinCodeActivity.this, KenCommerceWebViewActivity.class);
                urlTandC.putExtra("header","Terms and Conditions");
                urlTandC.putExtra("url", "http://www.swashconvergence.com/KenCommerce.html");
                startActivity(urlTandC);
                break;

        }
    }

    private void optionSelefctedNew(int id) {

        switch (id){


            case 3:
               /* Intent intentAddToCart=new Intent(PinCodeActivity.this, AddToCartListAllItemsActivity.class);
                startActivity(intentAddToCart);*/
                Intent intent = new Intent(PinCodeActivity.this, AddToCartListAllItemsActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.PinCodeActivity");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;

            case 4:
                if(!customer_id.equalsIgnoreCase("")){
                    //getProductDetails(customer_id);
                    doLogOut();
                }
                else{
                    Intent intentSign=new Intent(PinCodeActivity.this, LoginActivity.class);
                    intentSign.putExtra("context_act1", "webskitters.com.stockup.PinCodeActivity");
                    startActivity(intentSign);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }

                break;



            case 6:
                if (strAge.equalsIgnoreCase("")) {
                    getDialogAge();
                } else {
                    Constants.strCatName = "Chocolates";
                    if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    toEdit = shPrefUserSelection.edit();
                    toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopFood);
//                    toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopMainProduct);
                    toEdit.putString("PINCODE", et_pin.getText().toString());
                    toEdit.putString(Constants.strShUserStoreID, "10");
                    toEdit.commit();
                    initFields();
                    setDrawer();
                    setNavigation();
                    setDrawerMenu();
                }
                //getDialogAge();
                break;
            case 7:
                Constants.strCatName="Foods";
                if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);}
                toEdit = shPrefUserSelection.edit();
                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopFood);
                toEdit.putString(Constants.strShUserStoreID, "11");
                toEdit.putString("PINCODE", et_pin.getText().toString());
                toEdit.commit();
                initFields();
                setDrawer();
                setNavigation();
                setDrawerMenu();
                break;
            case 8:
                Constants.strCatName="Day-To-Day";

                if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                toEdit = shPrefUserSelection.edit();
                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopDay);
                toEdit.putString(Constants.strShUserStoreID, "12");
                toEdit.putString("PINCODE", et_pin.getText().toString());
                toEdit.commit();
                initFields();
                setDrawer();
                setNavigation();
                setDrawerMenu();
                break;
            case 9:
                Constants.strCatName="Gifts";
                if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                toEdit = shPrefUserSelection.edit();
                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopGift);
                toEdit.putString(Constants.strShUserStoreID, "13");
                toEdit.putString("PINCODE", et_pin.getText().toString());
                toEdit.commit();
                initFields();
                setDrawer();
                setNavigation();
                setDrawerMenu();
                break;
            case 10:

                //getDialogCoverage();
                Constants.strCatName="Events";
                if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                toEdit = shPrefUserSelection.edit();
                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopEvent);
                toEdit.putString("PINCODE", et_pin.getText().toString());
                toEdit.commit();
                initFields();
                setDrawer();
                setNavigation();
                setDrawerMenu();
                break;
            case 11:
                sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
                customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                if (customer_id.equalsIgnoreCase("")){
                    //utils.displayAlert("Please SignIn to continue..");
                    displayAlert("To proceed, kindly sign into your account");
                }
                else {
                    Intent intentMyDetails = new Intent(PinCodeActivity.this, MyDetailsActivity.class);
                    startActivity(intentMyDetails);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                break;
            case 12:
                getDialogCoverage();
                break;
            case 13:
                //getDialogCoverage();
                sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
                customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                if (customer_id.equalsIgnoreCase("")){
                    displayAlert("To proceed, kindly sign into your account");
                }
                else {
                    Intent intentMyAddress = new Intent(PinCodeActivity.this, MyAddressActivity.class);
                    startActivity(intentMyAddress);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }

                break;
            case 14:
                sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
                customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                if (customer_id.equalsIgnoreCase("")){
                    displayAlert("To proceed, kindly sign into your account");
                }
                else {
                    Intent intentMyWishList = new Intent(PinCodeActivity.this, MyWishListActivity.class);
                    startActivity(intentMyWishList);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                break;
            case 15:
                sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
                customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                if (customer_id.equalsIgnoreCase("")){
                    displayAlert("To proceed, kindly sign into your account");
                }
                else {
                    Intent intentShoppingList = new Intent(PinCodeActivity.this, MyShoppingListActivity.class);
                    startActivity(intentShoppingList);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                break;
            case 16:
                sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
                customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                if (customer_id.equalsIgnoreCase("")){
                    displayAlert("To proceed, kindly sign into your account.");
                }
                else {
                    Intent intentPastOrders = new Intent(PinCodeActivity.this, PastOrderListActivity.class);
                    startActivity(intentPastOrders);
                    finish();
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                break;

            case 17:
                //getDialogCoverage();
                /*String urlServe = "http://hireipho.nextmp.net/stockup/where-serve";
                Intent intentServe = new Intent(Intent.ACTION_VIEW);
                intentServe.setData(Uri.parse(urlServe));
                startActivity(intentServe);*/

                Intent intentServe = new Intent(PinCodeActivity.this, KenCommerceWebViewActivity.class);
                intentServe.putExtra("header","Where We Serve");
                intentServe.putExtra("url","http://www.swashconvergence.com/KenCommerce.html");
                startActivity(intentServe);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case 18:
                //getDialogCoverage();
                /*String urlFaq = "http://hireipho.nextmp.net/stockup/faqs";
                Intent intentFaq = new Intent(Intent.ACTION_VIEW);
                intentFaq.setData(Uri.parse(urlFaq));
                startActivity(intentFaq);*/

                Intent intentFaq = new Intent(PinCodeActivity.this, KenCommerceWebViewActivity.class);
                intentFaq.putExtra("header","FAQs");
                intentFaq.putExtra("url","http://www.swashconvergence.com/KenCommerce.html");
                startActivity(intentFaq);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case 19:
                //getDialogCoverage();
                /*String urlPP = "http://hireipho.nextmp.net/stockup/terms-condition";
                Intent intentPP = new Intent(Intent.ACTION_VIEW);
                intentPP.setData(Uri.parse(urlPP));
                startActivity(intentPP);*/

                Intent urlPP = new Intent(PinCodeActivity.this, KenCommerceWebViewActivity.class);
                urlPP.putExtra("header","Privacy Policy");
                urlPP.putExtra("url","http://www.swashconvergence.com/KenCommerce.html");
                startActivity(urlPP);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case 20:
                //getDialogCoverage();
                /*String urlTandC = "http://hireipho.nextmp.net/stockup/terms-condition";
                Intent intentTandC = new Intent(Intent.ACTION_VIEW);
                intentTandC.setData(Uri.parse(urlTandC));
                startActivity(intentTandC);*/

                Intent urlTandC = new Intent(PinCodeActivity.this, KenCommerceWebViewActivity.class);
                urlTandC.putExtra("header","Terms and Conditions");
                urlTandC.putExtra("url", "http://www.swashconvergence.com/KenCommerce.html");
                startActivity(urlTandC);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;

        }
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
                Intent intent=new Intent(PinCodeActivity.this, LoginActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.PinCodeActivity");
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

    public void displayAlertLogOut(String message)
    {
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                initFields();
                setDrawer();
                setNavigation();
                setDrawerMenu();
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
        title.setVisibility(View.GONE);
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

    private void closeDrawer(long time) {
        Handler handler
                = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawers();
            }
        }, time);
    }
    private void setDrawer() {
        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.drawable.inner_logo, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }


    private void setNavigation() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        img_profile = (ImageView) findViewById(R.id.img_profile);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_email = (TextView) findViewById(R.id.txt_email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            Intent intent=new Intent(PinCodeActivity.this, LandingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            PinCodeActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
    }
    //parse data from asset
    private ArrayList<SlideMenuOption> prepareListData() {
        ArrayList<SlideMenuOption> parentSlideMenuOptions = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(getdata());
            JSONArray jsonArray = jsonObject.optJSONArray("parentMenu");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                SlideMenuOption slideMenuOption = new SlideMenuOption();
                slideMenuOption.setId(jsonObject1.optInt("menuId"));
                slideMenuOption.setName(jsonObject1.optString("menuName"));


                JSONArray jsonArraychild = jsonObject1.optJSONArray("menuoption");
                ArrayList<SlideMenuOption> childSlideMenuOptions = new ArrayList<>();
                if (jsonArraychild != null) {
                    for (int j = 0; j < jsonArraychild.length(); j++) {
                        JSONObject jsonObject2 = jsonArraychild.optJSONObject(j);
                        SlideMenuOption slideMenuOption1 = new SlideMenuOption();
                        slideMenuOption1.setId(jsonObject2.optInt("menuId"));
                        slideMenuOption1.setName(jsonObject2.optString("menuName"));
                        slideMenuOption1.setImage(jsonObject2.optInt("menuImage"));
                        /*if(strCatName.equalsIgnoreCase(jsonObject2.optString("menuName").toString())){

                        }else{*/
                            childSlideMenuOptions.add(slideMenuOption1);
                        //}

                    }
                }
                slideMenuOption.setSlideMenuOptionsChild(childSlideMenuOptions);
                parentSlideMenuOptions.add(slideMenuOption);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parentSlideMenuOptions;
    }

    // Reading json file from assets folder
    private String getdata() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(
                    "menuoptionjson1.json")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private void doLogOut() {
        final ProgressDialog pDialog=new ProgressDialog(PinCodeActivity.this);
        pDialog.show();
        pDialog.setMessage("Logging you out from your account..");
        String strCustId = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        restService.logOut(strCustId, new RestCallback<LogOutRequest>() {
            @Override
            public void success(LogOutRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    sharedPrefEditior = sharedPreferenceUser.edit();
                    sharedPrefEditior.clear();
                    sharedPrefEditior.commit();
                    /*toEdit=shPrefUserSelection.edit();
                    toEdit.clear();
                    toEdit.commit();*/
                    //utils.displayAlert(responce.getData().getSuccess());

                    displayAlertLogOut("You have been signed out successfully.");

                    utils.disconnectFromFacebook();
                    //Toast.makeText(PinCodeActivity.this, "Successfully Logout", Toast.LENGTH_LONG).show();
                } else {
                    utils.displayAlert(responce.getData().getSuccess());
                    //Toast.makeText(PinCodeActivity.this, responce.getErrorMsg(), Toast.LENGTH_LONG).show();
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
    private void getDialogAge() {
        final Dialog dialog = new Dialog(PinCodeActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_age);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView header=(TextView)dialog.findViewById(R.id.header);
        TextView msg=(TextView)dialog.findViewById(R.id.msg);
        Button btn_no=(Button)dialog.findViewById(R.id.btn_no);
        Button btn_yes=(Button)dialog.findViewById(R.id.btn_yes);

        //header.setText("Error");
        msg.setText("Are you 18 years of age or above?");
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Put Grocery selection for landing page
                dialog.dismiss();
                Constants.strCatName="Drinks";
                if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);}
                toEdit = shPrefUserSelection.edit();
                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopMainProduct);
                toEdit.putString("PINCODE", et_pin.getText().toString());
                toEdit.putString(Constants.strShUserStoreID, "10");
                toEdit.commit();
                initFields();
                setDrawer();
                setNavigation();
                setDrawerMenu();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
