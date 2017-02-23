package com.swash.kencommerce;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import java.util.ArrayList;
import java.util.HashMap;

import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.AdapterReOrderItem;
import com.swash.kencommerce.model.PastOrderInfoRequest;
import com.swash.kencommerce.model.ReOrderRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

public class ReOrderActivity extends AppCompatActivity {

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
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_re_order);

        utils = new Utils(ReOrderActivity.this);
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
                Intent intent = new Intent(ReOrderActivity.this, PastOrderListActivity.class);
                startActivity(intent);
                finish();
                ReOrderActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        final String finalStrOrderId = strOrderId;
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                /*Intent intent = new Intent(ReOrderActivity.this, OrderDetailsSummaryActivity.class);
                intent.putExtra(PastOrderListActivity.Key_order_id, finalStrOrderId);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);*/
                //getDialogCoverage();

                reOrder(finalStrOrderId);
            }
        });

        if (utils.isConnectionPossible()){
            //String strOrderId = "100000189";
            getPastOrder(strOrderId);
        }
    }

    private void reOrder(final String order_id) {

        pDialog=new ProgressDialog(this);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait");
        restService.reorder(order_id, new RestCallback<ReOrderRequest>() {
            @Override
            public void success(ReOrderRequest object) {

                if (pDialog != null)
                    pDialog.dismiss();

                if(object.getStatus()==200&&object.getSuccess()==1)
                {
                    displayAlert(object.getData().getSuccessMsg(), order_id);
                }
                else
                {
                    utils.displayAlert(object.getErrorMsg());
                }
            }

            @Override
            public void invalid() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(ReOrderActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {
                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(ReOrderActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void displayAlert(String message, final String strOrdId)
    {
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                Intent intent=new Intent(ReOrderActivity.this, AddToCartListAllItemsActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.ReOrderActivity");
                intent.putExtra(PastOrderListActivity.Key_order_id, strOrdId);
                startActivity(intent);
                finish();
                ReOrderActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        TextView myMsg = new TextView(this);
        myMsg.setText(message);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setTextSize(16);
        //myMsg.setTypeface(typeFaceSegoeuiReg);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
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
    private void getPastOrder(final String strOrderId) {
        final ProgressDialog pDialog=new ProgressDialog(ReOrderActivity.this);
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
                    lv_past_order.setAdapter(new AdapterReOrderItem(ReOrderActivity.this, listReOrder));
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

    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(ReOrderActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        ReOrderActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
