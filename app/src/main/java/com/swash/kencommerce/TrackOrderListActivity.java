package com.swash.kencommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AppsFlyerLib;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.model.TrackOrder;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

import java.util.ArrayList;
import java.util.HashMap;

public class TrackOrderListActivity extends AppCompatActivity {

    ExpandableHeightListView lv_shoping_list;
    public static ArrayList<HashMap<String, String>> listPastOrders = new ArrayList<HashMap<String, String>>();
    public static String Key_PastOrdersNameList = "PastOrdersNameList";
    public static String Key_PastOrdersPriceList = "PastOrdersPriceList";
    public static String Key_PastOrdersStatusList = "PastOrdersStatusList";
    Toolbar toolbar;
    ImageView imgBack;
    TextView tv_signin;
    Utils utils;

    RestService restService;
    String address="", lat="", longi="", orderid="";
    TextView txtName, txt_add;
    Button btn_track;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_track_order_list);
        utils=new Utils(TrackOrderListActivity.this);
        restService=new RestService();
        initFields();

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);
    }

    private void initFields() {
        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                TrackOrderListActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        btn_track=(Button)findViewById(R.id.btn_track);
        btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTrack=new Intent(TrackOrderListActivity.this, SAllDriverLocationActivity.class);
                startActivity(intentTrack);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);

        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                PastOrderListActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        tv_signin=(TextView)findViewById(R.id.tv_signin);
        tv_signin.setText("Past Orders");
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        listPastOrders = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> mapShopList = new HashMap<String, String>();
        mapShopList.put(Key_PastOrdersNameList, "Apothic Red Blend");
        mapShopList.put(Key_PastOrdersPriceList, "R68.95");
        mapShopList.put(Key_PastOrdersStatusList, "Delivered");
        listPastOrders.add(mapShopList);
        HashMap<String, String> mapShopList2 = new HashMap<String, String>();
        mapShopList2.put(Key_PastOrdersNameList, "Apothic Red Blend");
        mapShopList2.put(Key_PastOrdersPriceList, "R72.90");
        mapShopList2.put(Key_PastOrdersStatusList, "Paid & Processed");
        listPastOrders.add(mapShopList2);
        HashMap<String, String> mapShopList3 = new HashMap<String, String>();
        mapShopList3.put(Key_PastOrdersNameList, "Apothic Red Blend");
        mapShopList3.put(Key_PastOrdersPriceList, "R58.95");
        mapShopList3.put(Key_PastOrdersStatusList, "Dispatched");
        listPastOrders.add(mapShopList3);
        HashMap<String, String> mapShopList4 = new HashMap<String, String>();
        mapShopList4.put(Key_PastOrdersNameList, "Apothic Red Blend");
        mapShopList4.put(Key_PastOrdersPriceList, "R58.50");
        mapShopList4.put(Key_PastOrdersStatusList, "Dispatched");
        listPastOrders.add(mapShopList4);

        lv_shoping_list=(ExpandableHeightListView)findViewById(R.id.lv_past_order_list);
        lv_shoping_list.setAdapter(new PastOrderListAdapter(PastOrderListActivity.this, listPastOrders));
        lv_shoping_list.setExpanded(true);
        lv_shoping_list.setFocusable(false);*/

        txtName=(TextView)findViewById(R.id.txt_order);
        txt_add=(TextView)findViewById(R.id.txt_add);
        getDeptList();

    }
    private void getDeptList(){

        pDialog=new ProgressDialog(TrackOrderListActivity.this);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait");
        restService.getDepartmentpID(new RestCallback<TrackOrder>() {
            @Override
            public void success(TrackOrder object) {

                address=object.getAddress();
                lat=object.getLatitude();
                longi=object.getLongitude();
                orderid=object.getOrderId();
                txtName.setText(orderid);
                txt_add.setText(address);

                if(pDialog!=null)
                    pDialog.dismiss();
            }

            @Override
            public void invalid() {

                if(pDialog!=null)
                    pDialog.dismiss();

                Toast.makeText(TrackOrderListActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if(pDialog!=null)
                    pDialog.dismiss();

                Toast.makeText(TrackOrderListActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        TrackOrderListActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
