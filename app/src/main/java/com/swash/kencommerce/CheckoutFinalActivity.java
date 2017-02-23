package com.swash.kencommerce;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
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

import com.appsflyer.AppsFlyerLib;
import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.AddToCartListAllItemAdapter;
import com.swash.kencommerce.adapter.ViewAllCheckoutItemListAdapter;
import com.swash.kencommerce.model.CartTotalRequest;
import com.swash.kencommerce.model.PaymentRequest;
import com.swash.kencommerce.model.ServiceTaxRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

//import com.appsflyer.AppsFlyerLib;

/**
 * Created by Kranti on 12/12/2016.
 */

public class CheckoutFinalActivity extends AppCompatActivity {

    TextView txt_ship_addr, txt_bill_addr;
    LinearLayout ll_yes, ll_no;
    ImageView img_yes, img_no;
    RelativeLayout rel_add_payment;

    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;

    SharedPreferences.Editor toEdit;

    EditText extfirstname, extlastname;

    Button btn_continue;

    Utils utils;
    SharedPreferences shPrefDeliverAddr;
    SharedPreferences shP;
    SharedPreferences.Editor toEditAddr;
    String fname="", lName="", shipingAdd="", bilingAdd="", forshipingbiling="";
    Bundle extras;
    ImageView imgBack;

    RelativeLayout rel_add, rel_pay_with;
    private ProgressDialog pDialog;
    RestService restService;
    TextView txt_place_order;
    private TextView txt_addr;

    int width=0, height=0;
    TextView txt_card, txt_date;
    private String strDate="";
    private ArrayList<HashMap<String, String>> listAddToCartList;
    TextView ext_comment;
    TextView txt_sub_total_price, txt_delivery_fee, txt_driver_tip, txt_total;
    private NumberFormat nf;
    ExpandableHeightListView lv_item_list;

    LinearLayout lin_cart;
    ImageView imgCart;
    Button btn_count;

    private SharedPreferences sharedPreferenceUser;
    String customer_id="";
    LinearLayout lin_place_order;
    TextView txt_header;
    TextView txt_delivery_fee_view;
    Format format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_out_complete_order);
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");
        utils=new Utils(CheckoutFinalActivity.this);
        restService=new RestService();
        sharedPreferenceUser=this.getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
        customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        extras=getIntent().getExtras();
        if(extras!=null){
            strDate=extras.getString("date","");
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();
        getTax();
        if(!customer_id.equalsIgnoreCase("")){
            getCartTotal(customer_id);
        }

    }
    private void getCartTotal(String customer_id) {

        restService.getCartTotal(customer_id, new RestCallback<CartTotalRequest>() {
            @Override
            public void success(CartTotalRequest object) {

                if(object.getStatus()==200&&object.getSuccess()==1){

                    btn_count.setText(object.getData().getTotalQty().toString());
                    //txt_total_price.setText(object.getData().getTotalPrice());
                }

            }

            @Override
            public void invalid() {

                Toast.makeText(CheckoutFinalActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {
                Toast.makeText(CheckoutFinalActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void getTax(){
        pDialog=new ProgressDialog(CheckoutFinalActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        restService=new RestService();
        restService.getServiceTax(new RestCallback<ServiceTaxRequest>() {
            @Override
            public void success(ServiceTaxRequest obj) {
                //if (obj.getSuccess() == 200 && obj.getStatus() == 1) {
                    Constants.serviceTax = Float.parseFloat(obj.getData().getTaxData().get(0).getTax());

                    //AddToCartListAllItemsActivity.total_price=AddToCartListAllItemsActivity.total_price/Constants.serviceTax;
                    //txt_vat.setText("R"+nf.format(AddToCartListAllItemsActivity.total_price*14/100));
                    //txt_service_tax.setText("R" + nf.format(AddToCartListAllItemsActivity.total_price*Constants.serviceTax/100));
                    //double service_fee=((AddToCartListAllItemsActivity.total_price*Constants.serviceTax)/100);
                    //double vat=((AddToCartListAllItemsActivity.total_price*14)/100);
                    /*double driver_tip=Double.parseDouble(txt_driver_tip.getText().toString());
                    double delivery_fee=Double.parseDouble(txt_delivery_fee.getText().toString());*/
                    double total=AddToCartListAllItemsActivity.total_price+Constants.driver_tip+Constants.deliveryfee;
                    txt_total.setText("R"+nf.format(total));

                if(pDialog!=null){
                    pDialog.dismiss();
                }
                //}
            }

            @Override
            public void invalid() {

                if(pDialog!=null){
                    pDialog.dismiss();
                }
            }

            @Override
            public void failure() {

                if(pDialog!=null){
                    pDialog.dismiss();
                }
            }
        });
    }
    private void initFields() {
        {
            shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);
            sharedPreferenceUser = this.getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);

            ext_comment=(TextView) findViewById(R.id.ext_comment);
            if(Constants.deliveryInstruction!=null){
                ext_comment.setText(Constants.deliveryInstruction);
            }
            imgBack = (ImageView) findViewById(R.id.img_back);
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    CheckoutFinalActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            });
            txt_header=(TextView)findViewById(R.id.txt_header);

            lv_item_list=(ExpandableHeightListView)findViewById(R.id.lv_item_list);
            lv_item_list.setAdapter(new ViewAllCheckoutItemListAdapter(CheckoutFinalActivity.this, AddToCartListAllItemAdapter.listAddToCartList));
            lv_item_list.setExpanded(true);
            lv_item_list.setFocusable(false);



            txt_sub_total_price=(TextView)findViewById(R.id.txt_sub_total_price);
            txt_sub_total_price.setText("R"+nf.format(AddToCartListAllItemsActivity.total_price));
            txt_delivery_fee=(TextView)findViewById(R.id.txt_delivery_fee);
            txt_delivery_fee.setText("R"+nf.format(Constants.driver_tip));
            txt_delivery_fee_view=(TextView)findViewById(R.id.txt_delivery_fee_view);

            txt_driver_tip=(TextView)findViewById(R.id.txt_driver_tip);
            txt_driver_tip.setText("R"+nf.format(Constants.deliveryfee));
            if(Constants.isFirstTimeDelivery.equalsIgnoreCase("0")){
                txt_delivery_fee_view.setText("Free 1st 60' Order");
            }
            txt_total=(TextView)findViewById(R.id.txt_total);
            rel_add_payment = (RelativeLayout) findViewById(R.id.rel_add_payment);
            rel_add_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CheckoutFinalActivity.this, AddPaymentActivity.class);
                    intent.putExtra("for", "CheckoutFinal");
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            });

            rel_add = (RelativeLayout) findViewById(R.id.rel_addr);
            rel_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CheckoutFinalActivity.this, MapActivity.class);
                    intent.putExtra("for", "CheckoutFinal");
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            });

            rel_pay_with = (RelativeLayout) findViewById(R.id.rel_pay_with);
            rel_pay_with.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CheckoutFinalActivity.this, AddPaymentActivity.class);
                    intent.putExtra("for", "CheckoutFinal");
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            });

            txt_date = (TextView) findViewById(R.id.txt_date);
            if (Constants.deliveryDateCheckout != null && !Constants.deliveryDateCheckout.equalsIgnoreCase("")) {
                if(Constants.deliveryDate.contains("No Rush")) {
                    if(Constants.deliverytime.equalsIgnoreCase("")||Constants.deliverytime.isEmpty()){
                        txt_date.setText("No Rush: " +Constants.deliveryDay+", "+ Constants.deliveryDateCheckout.replace("-", "/"));
                    }else{
                        txt_date.setText("No Rush: " +Constants.deliveryDay+", "+ Constants.deliveryDateCheckout.replace("-", "/")+", "+Constants.deliverytime);
                    }

                }
                else{
                    txt_date.setText("ASAP");
                }
            }
            lin_place_order=(LinearLayout)findViewById(R.id.lin_place_order);
            txt_place_order = (TextView) findViewById(R.id.txt_place_order);
            txt_place_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Constants.fromOrderStatus.equalsIgnoreCase("Yes")){
                        Intent intent=new Intent(CheckoutFinalActivity.this, OrderStatusActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    }else {
                        getPayment();
                    }
                }
            });
            txt_addr = (TextView) findViewById(R.id.txt_addr);
            String callFromMap = shPrefDeliverAddr.getString(Constants.strShPrefDelCallFrom, "No");
            if (callFromMap.equalsIgnoreCase("Yes")/*&&forshipingbiling.equalsIgnoreCase("shiping")*/) {
                String strLat = shPrefDeliverAddr.getString(Constants.strShPrefDelLat, "");
                String strLong = shPrefDeliverAddr.getString(Constants.strShPrefDelLong, "");
                String strAddr = shPrefDeliverAddr.getString(Constants.strShPrefDelAddr, "");
                txt_addr.setText(strAddr);
            }
            txt_card = (TextView) findViewById(R.id.txt_card);
            txt_card.setText(shPrefDeliverAddr.getString("nickname", ""));

            imgCart=(ImageView)findViewById(R.id.img_cart_icon);
            lin_cart=(LinearLayout)findViewById(R.id.lin_cart);
            btn_count=(Button)findViewById(R.id.btn_count);
            lin_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CheckoutFinalActivity.this, AddToCartListAllItemsActivity.class);
                    intent.putExtra("context_act1", "webskitters.com.stockup.CheckoutFinalActivity");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            });

            if(Constants.fromOrderStatus.equalsIgnoreCase("Yes")){
                txt_place_order.setText("ORDER STATUS");
                imgBack.setVisibility(View.GONE);
                txt_header.setText("Order Details");
                rel_add.setClickable(false);
                rel_pay_with.setClickable(false);
                lin_cart.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {

        if(Constants.fromOrderStatus.equalsIgnoreCase("Yes")){

        }else {
            super.onBackPressed();
            finish();
            CheckoutFinalActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
    }

    private void getPayment(){

        Constants.fName=sharedPreferenceUser.getString(Constants.strShPrefUserFname, "");
        Constants.lName=sharedPreferenceUser.getString(Constants.strShPrefUserLname,"");
        Constants.emailAdd=sharedPreferenceUser.getString(Constants.strShPrefUserEmail,"");
        Constants.mobilenum=shPrefDeliverAddr.getString(Constants.strShPrefUserPhone,"");
        Constants.address=shPrefDeliverAddr.getString(Constants.strShPrefDelAddr,"");
        Constants.latitude=shPrefDeliverAddr.getString(Constants.strShPrefDelLat,"");
        Constants.longitude=shPrefDeliverAddr.getString(Constants.strShPrefDelLong,"");

        Constants.cc_owner=shPrefDeliverAddr.getString("card_person_name","");
        Constants.cc_number=shPrefDeliverAddr.getString("card_number","");
        Constants.cc_type=shPrefDeliverAddr.getString("card_type","");
        Constants.cc_cvv=shPrefDeliverAddr.getString("cvv_cvv","");
        Constants.cc_expiry_month=shPrefDeliverAddr.getString("card_expiry_month","");
        Constants.cc_expiry_year=shPrefDeliverAddr.getString("card_expiry_year","");
        Constants.cc_owner=shPrefDeliverAddr.getString("card_type","");
        Constants.cc_card_type=shPrefDeliverAddr.getString("card_type_short","");
        //Constants.cc_type=shPrefDeliverAddr.getString("cvv_number","");

        if(strDate.equalsIgnoreCase("NeededASAP")){
            Constants.deliveryDateCheckout="asap";
        }else{

        }
        JSONArray jsonArray=new JSONArray();
        JSONObject jObjImages;
        for(int i=0; i< AddToCartListAllItemAdapter.listAddToCartList.size(); i++){
            jObjImages=new JSONObject();
            try {
                jObjImages.put("attribute_id", AddToCartListAllItemAdapter.listAddToCartList.get(i).get("attribute_id"));
                jObjImages.put("product_id", AddToCartListAllItemAdapter.listAddToCartList.get(i).get("product_id"));
                jObjImages.put("option_id", AddToCartListAllItemAdapter.listAddToCartList.get(i).get("option_id"));
                jObjImages.put("qty",  AddToCartListAllItemAdapter.listAddToCartList.get(i).get("qty"));

                jsonArray.put(i,jObjImages);
                //Toast.makeText(getApplicationContext(), "JSONDATA: "+jsonArray.toString(), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        pDialog=new ProgressDialog(CheckoutFinalActivity.this);
        pDialog.show();
        pDialog.setCancelable(true);
        pDialog.setMessage("Loading... Please wait");
        restService.getPayment(Constants.fName, Constants.lName, Constants.emailAdd, "9937885085", Constants.address, Constants.latitude, Constants.longitude, jsonArray.toString(), "peach_payment",Constants.cc_number, Constants.cc_owner, Constants.cc_card_type,Constants.cc_cvv, Constants.cc_expiry_month, Constants.cc_expiry_year, Constants.deliverytypeCheckout, Constants.deliveryDateCheckout, ext_comment.getText().toString(), new RestCallback<PaymentRequest>() {
            @Override
            public void success(PaymentRequest obj) {
                pDialog.dismiss();

                if (obj.getSuccess().toString().equalsIgnoreCase("1")) {

                    getDialogAge(obj.getData().getOrderId());

                } else {
                    utils.displayAlert(obj.getErrorMsg());
                }
            }

            @Override
            public void invalid() {
                pDialog.dismiss();
                utils.displayAlert("Problem while requesting.");
            }

            @Override
            public void failure() {
                pDialog.dismiss();
                utils.displayAlert("Internet connection is not available, try again.");
            }
        });
    }

    private void getDialogAge(String orderid) {
        final Dialog dialog = new Dialog(CheckoutFinalActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_place_order);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView header=(TextView)dialog.findViewById(R.id.header);
        header.setTypeface(typeFaceSegoeuiBold);
        TextView msg=(TextView)dialog.findViewById(R.id.msg);
        msg.setTypeface(typeFaceSegoeuiReg);
        TextView btn_yes=(TextView)dialog.findViewById(R.id.btn_ok);
        AddToCartListAllItemsActivity.size=0;
        Constants.orderID=orderid;
        //header.setText("Error");
        msg.setText("Your order " + orderid + " has been successfully placed. We are soooo onto it!\n\n" +
                "Check back soon to track your order making its way to you!");

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                //Log.e("time", now.getTime().toString());
                now.add(Calendar.MINUTE,40);
                //Log.e("time", now.getTime().toString());


                Calendar now1 = Calendar.getInstance();
                //Log.e("time", now.getTime().toString());
                now1.add(Calendar.MINUTE,15);
               // Log.e("time", now1.getTime().toString());

                format= new SimpleDateFormat("dd MMMM yyyy, HH:mm");


                Constants.lastDeliveryTime40=format.format(now.getTime());
                Constants.lastDeliveryTime15=format.format(now1.getTime());
                // Put Grocery selection for landing page
                /*if(Constants.strTipPercent!=null)
                    Constants.strTipPercent="";
                dialog.dismiss();

                Constants.deliverytypeCheckout="";
                Constants.deliverytypeCart="";
                Constants.deliveryDate="";
                Constants.deliveryDateCheckout="";*/
                dialog.dismiss();
                if(txt_date.getText().toString().equalsIgnoreCase("ASAP")){
                Intent intent=new Intent(CheckoutFinalActivity.this, OrderStatusActivity.class);
                startActivity(intent);
                finish();}
                else{
                    if(Constants.strTipPercent!=null)
                        Constants.strTipPercent="";

                    Constants.deliverytypeCheckout="";
                    Constants.deliverytypeCart="";
                    Constants.deliveryDate="";
                    Constants.deliveryDateCheckout="";
                    Intent intent=new Intent(CheckoutFinalActivity.this, LandingActivity.class);
                    startActivity(intent);
                    finish();

                }
                CheckoutFinalActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
