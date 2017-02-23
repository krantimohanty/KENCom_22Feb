package com.swash.kencommerce;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.model.PastOrderSummaryRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

public class OrderDetailsSummaryActivity extends AppCompatActivity {

    ImageView img_back;
    Button btn_next;
    TextView txt_addr, txt_ph_no, txt_order_id, txt_order_date, txt_quantity, txt_tot_price_currency, txt_tot_price,
            txt_tot_price_pay_type, txt_client_name, txt_delevery_date, txt_review;

    Utils utils;
    RestService restService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_details);

        utils = new Utils(OrderDetailsSummaryActivity.this);
        restService = new RestService();

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();
    }

    private void initFields() {
        txt_addr = (TextView) findViewById(R.id.txt_addr);
        txt_ph_no = (TextView) findViewById(R.id.txt_ph_no);
        txt_order_id = (TextView) findViewById(R.id.txt_order_id);
        txt_order_date = (TextView) findViewById(R.id.txt_order_date);
        txt_quantity = (TextView) findViewById(R.id.txt_quantity);
        txt_tot_price_currency = (TextView) findViewById(R.id.txt_tot_price_currency);
        txt_tot_price = (TextView) findViewById(R.id.txt_tot_price);
        txt_tot_price_pay_type = (TextView) findViewById(R.id.txt_tot_price_pay_type);
        txt_client_name = (TextView) findViewById(R.id.txt_client_name);
        txt_delevery_date = (TextView) findViewById(R.id.txt_delevery_date);
        txt_review = (TextView) findViewById(R.id.txt_review);

        img_back = (ImageView) findViewById(R.id.img_back);
        btn_next = (Button) findViewById(R.id.btn_continue);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                OrderDetailsSummaryActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailsSummaryActivity.this, SubCategoryActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        Intent intentPrev = getIntent();
        //String strOrderId = "100000189";
        String strOrderId = "";
        if (intentPrev.hasExtra(PastOrderListActivity.Key_order_id)) {
            strOrderId = intentPrev.getExtras().getString(PastOrderListActivity.Key_order_id);
        }
        if (utils.isConnectionPossible()){
            //String strOrderId = "100000189";
            getPastOrder(strOrderId);
        }
    }

    private void getPastOrder(final String strOrderId) {
        final ProgressDialog pDialog=new ProgressDialog(OrderDetailsSummaryActivity.this);
        pDialog.show();
        pDialog.setMessage("Loading...");
        restService.getPastOrderorderSummary(strOrderId, new RestCallback<PastOrderSummaryRequest>() {
            @Override
            public void success(PastOrderSummaryRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    String strOrderNo = responce.getData().getOrderSummary().getOrderId();
                    String str_delivery_address = responce.getData().getOrderSummary().getDeliveryAddress();
                    String str_telephone = responce.getData().getOrderSummary().getTelephone();
                    String str_date = responce.getData().getOrderSummary().getDate();
                    String str_qty = responce.getData().getOrderSummary().getQty();
                    String str_currency = responce.getData().getOrderSummary().getCurrency();
                    String str_order_total = responce.getData().getOrderSummary().getOrderTotal();
                    String str_payment_method = responce.getData().getOrderSummary().getPaymentMethod();
                    String str_client_name = responce.getData().getOrderSummary().getClientName();
                    String str_delivery_date = responce.getData().getOrderSummary().getDeliveryDate();
                    String str_rating = responce.getData().getOrderSummary().getRating();
                    String str_content = responce.getData().getOrderSummary().getContent();

                    txt_addr.setText(str_delivery_address);
                    txt_ph_no.setText(str_telephone);
                    txt_order_id.setText(strOrderNo);
                    txt_order_date.setText(str_date);
                    txt_quantity.setText(str_qty);
                    txt_tot_price_currency.setText(str_currency);
                    txt_tot_price.setText(str_order_total);
                    txt_tot_price_pay_type.setText(str_payment_method);
                    txt_client_name.setText(str_client_name);
                    txt_delevery_date.setText(str_delivery_date);
                    txt_review.setText(str_content);
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
        OrderDetailsSummaryActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(OrderDetailsSummaryActivity.this);
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
}
