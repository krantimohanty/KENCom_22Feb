package com.swash.kencommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import java.util.ArrayList;
import java.util.HashMap;

import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.AdapterOrderHistoryItem;
import com.swash.kencommerce.model.PastOrderInfoRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

public class OrderHistoryActivity extends AppCompatActivity {

    ImageView img_back;
    TextView txt_order_no;
    public static ArrayList<HashMap<String, String>> listReOrder = new ArrayList<HashMap<String, String>>();
    ListView lv_past_order;
    Button btn_next;
    Utils utils;
    RestService restService;

    public static String Key_product_id = "product_id";
    public static String Key_product_name = "product_name";
    public static String Key_product_image = "product_image";
    public static String Key_currency = "currency";
    public static String Key_product_qty = "product_qty";
    public static String Key_total_price = "total_price";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_history);

        utils = new Utils(OrderHistoryActivity.this);
        restService = new RestService();

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();
    }

    private void initFields() {
        Intent intentPrev = getIntent();
        //String strOrderId = "100000189";
        String strOrderId = "";
        if (intentPrev.hasExtra(PastOrderListActivity.Key_order_id)) {
            strOrderId = intentPrev.getExtras().getString(PastOrderListActivity.Key_order_id);
        }
        img_back = (ImageView) findViewById(R.id.img_back);
        lv_past_order = (ListView) findViewById(R.id.lv_past_order);
        btn_next = (Button) findViewById(R.id.btn_next);
        txt_order_no = (TextView) findViewById(R.id.txt_order_no);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderHistoryActivity.this, PastOrderListActivity.class);
                startActivity(intent);
                finish();
                OrderHistoryActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        final String finalStrOrderId = strOrderId;
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                Intent intent = new Intent(OrderHistoryActivity.this, OrderDetailsSummaryActivity.class);
                intent.putExtra(PastOrderListActivity.Key_order_id, finalStrOrderId);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        if (utils.isConnectionPossible()){
            //String strOrderId = "100000189";
            getPastOrder(strOrderId);
        }
    }

    private void getPastOrder(final String strOrderId) {
        final ProgressDialog pDialog=new ProgressDialog(OrderHistoryActivity.this);
        pDialog.show();
        pDialog.setMessage("Loading...");
        restService.getPastOrderInfo(strOrderId, new RestCallback<PastOrderInfoRequest>() {
            @Override
            public void success(PastOrderInfoRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    String strOrderNo = responce.getData().getOrderId();
                    txt_order_no.setText(strOrderNo);
                    int size = responce.getData().getOrderData().size();
                    listReOrder = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i < size; i++) {
                        HashMap<String, String> mapOrdHistList = new HashMap<String, String>();
                        mapOrdHistList.put(Key_product_id, responce.getData().getOrderData().get(i).getProductId().toString());
                        mapOrdHistList.put(Key_product_name, responce.getData().getOrderData().get(i).getProductName().toString());
                        mapOrdHistList.put(Key_product_image, responce.getData().getOrderData().get(i).getProductImage().toString());
                        mapOrdHistList.put(Key_product_qty, responce.getData().getOrderData().get(i).getProductQty().toString());
                        mapOrdHistList.put(Key_total_price, responce.getData().getOrderData().get(i).getTotalPrice().toString());
                        mapOrdHistList.put(Key_currency, responce.getData().getOrderData().get(i).getCurrency().toString());

                        listReOrder.add(mapOrdHistList);
                    }
                    lv_past_order.setAdapter(new AdapterOrderHistoryItem(OrderHistoryActivity.this, listReOrder));
                } else {
                    //getDialogOK(responce.getErrorMsg());
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
        OrderHistoryActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
