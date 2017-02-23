package com.swash.kencommerce;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import java.util.ArrayList;
import java.util.HashMap;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.PastOrderListAdapter;
import com.swash.kencommerce.model.PastOrderRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

public class PastOrderListActivity extends AppCompatActivity {

    public static ArrayList<HashMap<String, String>> listPastOrders = new ArrayList<HashMap<String, String>>();

    RestService restService;
    private ProgressDialog pDialog;
    Utils utils;
    SharedPreferences sharedPreferenceUser;
    SharedPreferences.Editor sharedPrefEditior;

    public static String Key_order_id = "order_id";
    public static String Key_order_amount = "order_amount";
    public static String Key_currency = "currency";
    public static String Key_order_status = "order_status";
    public static String Key_order_date = "order_date";

    ImageView imgBack;
    ListView lv_past_order;
    Typeface typeFaceSegoeuiReg;
    TextView txt_title, txt_order_hist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_past_order_list);
        utils=new Utils(PastOrderListActivity.this);
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");

        restService=new RestService();
        utils = new Utils(PastOrderListActivity.this);

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();
    }

    private void initFields() {
        sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);

        imgBack=(ImageView)findViewById(R.id.img_back);

        txt_title=(TextView)findViewById(R.id.txt_top_bar);
        txt_title.setTypeface(typeFaceSegoeuiReg);
        txt_order_hist=(TextView)findViewById(R.id.txt_order_dtl);
        txt_order_hist.setTypeface(typeFaceSegoeuiReg);

        lv_past_order = (ListView) findViewById(R.id.lv_past_order);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                PastOrderListActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        if (utils.isConnectionPossible()){
            String strCustId = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
            if (!strCustId.equalsIgnoreCase("")) {
                getPastOrder(strCustId);
            }
            else {}
        }
    }

    private void getPastOrder(final String strCustId) {
        final ProgressDialog pDialog=new ProgressDialog(PastOrderListActivity.this);
        pDialog.show();
        pDialog.setMessage("Loading...");
        restService.getPastOrder(strCustId, new RestCallback<PastOrderRequest>() {
            @Override
            public void success(PastOrderRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    int size = responce.getData().getOrderData().size();
                    listPastOrders = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i < size; i++) {
                        HashMap<String, String> mapPastOrdList = new HashMap<String, String>();
                        mapPastOrdList.put(Key_order_id, responce.getData().getOrderData().get(i).getOrderId().toString());
                        mapPastOrdList.put(Key_order_amount, responce.getData().getOrderData().get(i).getOrderAmount().toString());
                        mapPastOrdList.put(Key_currency, responce.getData().getOrderData().get(i).getCurrency().toString());
                        mapPastOrdList.put(Key_order_status, responce.getData().getOrderData().get(i).getOrderStatus().toString());
                        mapPastOrdList.put(Key_order_date, responce.getData().getOrderData().get(i).getOrderDate().toString());

                        listPastOrders.add(mapPastOrdList);


                    }
                    lv_past_order.setAdapter(new PastOrderListAdapter(PastOrderListActivity.this, listPastOrders));
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        PastOrderListActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
