package com.swash.kencommerce;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AppsFlyerLib;
import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.AddToCartListAllItemAdapter;
import com.swash.kencommerce.adapter.TipCatSpinnerAdapter;
import com.swash.kencommerce.model.CartTotalRequest;
import com.swash.kencommerce.model.GetDeliveryFeeRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;
import com.swash.kencommerce.webview.KenCommerceWebViewActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Vector;

//import com.appsflyer.AppsFlyerLib;

/**
 * Created by Kranti on 12/12/2016.
 */

public class CheckOutActivity extends AppCompatActivity {

    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;

    Utils utils;
    SharedPreferences shPrefDeliverAddr;
    SharedPreferences.Editor toEditAddr;
    String fname="", lName="", shipingAdd="", bilingAdd="", forshipingbiling="";
    Bundle extras;
    ImageView imgBack;

    RelativeLayout rel_add, rel_pay_with, rel_delivery_type, rel_tips;
    private TextView txt_addr, txt_addr_right, txt_card, txt_card_right;
    String isAsap="";
    View vw_tips;
    private PopupWindow pw;
    int width=0, height=0;
    TextView txt_tips, txt_subheader;
    TextView txt_next;
    private String strIsCard="";
    private String strMap;
    private TextView txt_delivery_type, txt_sub_total, txt_tip_percent, txt_total;
    private double tip=0;
    NumberFormat nf;
    private RestService restService;
    TextView txt_delivery_fee;
    private TextView txt_tip;
    private ProgressDialog pDialog;
    private SharedPreferences sharedPreferenceUser;
    String customer_id="";
    SharedPreferences shPrefUserSelection;
    SharedPreferences.Editor toEdit;
    LinearLayout lin_cart;
    ImageView imgCart;
    Button btn_count;

    EditText ext_comment;
    TextView txt_delivery_free_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_out);

        sharedPreferenceUser=this.getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
        customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");

        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");

        utils=new Utils(CheckOutActivity.this);

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);
        toEditAddr=shPrefDeliverAddr.edit();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        initFields();

        if(!customer_id.equalsIgnoreCase("")){
            getCartTotal(customer_id);
        }

    }

    private void initFields() {

        shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);
        strIsCard=shPrefDeliverAddr.getString("is_card_details_inputed", "");
        strMap=shPrefDeliverAddr.getString(Constants.strShPrefDelCallFrom, "No");
        txt_delivery_fee=(TextView)findViewById(R.id.txt_delivery_fee);
        txt_delivery_free_view=(TextView)findViewById(R.id.txt_delivery_free_view);

        txt_sub_total=(TextView)findViewById(R.id.txt_sub_total);
        txt_sub_total.setText("R"+nf.format(AddToCartListAllItemsActivity.total_price));
        txt_tip_percent=(TextView)findViewById(R.id.txt_tip_percent);
        txt_total=(TextView)findViewById(R.id.txt_total);
        txt_total.setText("R"+nf.format(AddToCartListAllItemsActivity.total_price));
        imgBack=(ImageView)findViewById(R.id.img_back);
        ext_comment=(EditText) findViewById(R.id.ext_comment);
        if(Constants.deliveryInstruction!=null){
            ext_comment.setText(Constants.deliveryInstruction);
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutActivity.this, AddToCartListAllItemsActivity.class);
                startActivity(intent);
                finish();
                Constants.deliverytypeCheckout = "";
                Constants.deliverytypeCart = "";
                Constants.deliveryDate = "";
                Constants.deliveryDateCheckout = "";
                AddToCartListAllItemsActivity.size=0;
                Constants.deliveryInstruction=ext_comment.getText().toString();
                CheckOutActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        imgCart=(ImageView)findViewById(R.id.img_cart_icon);
        lin_cart=(LinearLayout)findViewById(R.id.lin_cart);
        btn_count=(Button)findViewById(R.id.btn_count);
        lin_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutActivity.this, AddToCartListAllItemsActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.CheckOutActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                Constants.deliveryInstruction=ext_comment.getText().toString();
            }
        });
        txt_next=(TextView)findViewById(R.id.txt_next);
        txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.deliveryInstruction=ext_comment.getText().toString();
                if(!strMap.equalsIgnoreCase("Yes")){
                    utils.displayAlert("Provide delivery address.");
                }else if(!strIsCard.equalsIgnoreCase("Yes")){
                    utils.displayAlert("Provide valid payment details.");
                }
                else if(!txt_delivery_type.getText().toString().equalsIgnoreCase("ASAP")&&!txt_delivery_type.getText().toString().contains("No Rush")){
                    utils.displayAlert("Please provide delivery times.");
                }else if(txt_delivery_type.getText().toString().contains("No Rush") & (Constants.deliveryDate==null||Constants.deliveryDate.isEmpty())){

                    utils.displayAlert("Please provide delivery times.");
                }
                else{
                    Intent intent=new Intent(CheckOutActivity.this, CheckoutFinalActivity.class);
                    if(Constants.deliveryDate!=null&!Constants.deliveryDate.isEmpty())
                    {
                        intent.putExtra("date",Constants.deliveryDate);
                    }else
                    {
                        intent.putExtra("date","Needed ASAP");
                    }
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            }
        });

        txt_subheader=(TextView)findViewById(R.id.txt_subheader);
        customCheckBoxTextView(txt_subheader);

        rel_add=(RelativeLayout)findViewById(R.id.rel_addr);
        rel_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutActivity.this, MapActivity.class);
                intent.putExtra("for", "Checkout");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                Constants.deliveryInstruction=ext_comment.getText().toString();
            }
        });

        rel_pay_with=(RelativeLayout)findViewById(R.id.rel_pay_with);
        txt_card=(TextView)findViewById(R.id.txt_card);
        txt_card.setTypeface(typeFaceSegoeuiReg);
        txt_card_right=(TextView)findViewById(R.id.txt_card_right);

        rel_pay_with.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutActivity.this, AddPaymentActivity.class);
                intent.putExtra("for", "Checkout");
                startActivity(intent);
                //finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                Constants.deliveryInstruction=ext_comment.getText().toString();
            }
        });

        rel_delivery_type=(RelativeLayout)findViewById(R.id.rel_delivery_type);
        txt_delivery_type=(TextView)findViewById(R.id.txt_delivery_type);
        txt_delivery_type.setTypeface(typeFaceSegoeuiReg);
        rel_delivery_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.deliveryInstruction=ext_comment.getText().toString();
                if (!strMap.equalsIgnoreCase("Yes")) {
                    utils.displayAlert("Provide delivery address.");
                } else {
                    if (Constants.deliverytypeCart.equalsIgnoreCase("asap")) {
                        getDialogCoverage();
                    } else {
                        if (Constants.arrMixed != null && Constants.arrMixed.contains("ASAP") && Constants.arrMixed.contains("NO RUSH")) {
                            getDeliverytype();
                        } else {
                            Intent intent = new Intent(CheckOutActivity.this, DeliveryChargeActivity.class);
                            intent.putExtra("isasap", "No");
                            startActivity(intent);
                            finish();
                            Constants.deliverytypeCheckout = "No Rush";
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        }
                    }
                }
            }
        });

        txt_tips=(TextView)findViewById(R.id.txt_tips);
        txt_tip=(TextView)findViewById(R.id.txt_tip);
        txt_tip.setText("Tip your \"any time , any where\" team");
        vw_tips=(View)findViewById(R.id.view_tips);

        rel_tips=(RelativeLayout)findViewById(R.id.rel_tips);
        rel_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideSoftKeyboard(CheckOutActivity.this);
                callPopUpSpecialDiscount(vw_tips);
            }
        });


        txt_addr=(TextView)findViewById(R.id.txt_addr);
        txt_addr.setTypeface(typeFaceSegoeuiReg);
        txt_addr_right=(TextView)findViewById(R.id.txt_addr_right);

        String callFromMap = shPrefDeliverAddr.getString(Constants.strShPrefDelCallFrom, "No");
        if (callFromMap.equalsIgnoreCase("Yes")/*&&forshipingbiling.equalsIgnoreCase("shiping")*/){
            String strLat = shPrefDeliverAddr.getString(Constants.strShPrefDelLat,"");
            String strLong = shPrefDeliverAddr.getString(Constants.strShPrefDelLong,"");
            String strAddr = shPrefDeliverAddr.getString(Constants.strShPrefDelAddr,"");
            txt_addr.setText(strAddr);
            txt_addr_right.setVisibility(View.INVISIBLE);
            txt_delivery_type.setText("Select delivery method");
        }
        if (strIsCard.equalsIgnoreCase("Yes")){
            txt_card.setText(shPrefDeliverAddr.getString("nickname", ""));
            txt_card_right.setVisibility(View.INVISIBLE);
        }
        if(Constants.deliverytypeCheckout.equalsIgnoreCase("asap")){
            txt_delivery_type.setText("ASAP");
            /*rel_delivery_type.setClickable(true);
            rel_delivery_type.setEnabled(true);*/
        }

        else if (Constants.deliverytypeCheckout.contains("No Rush")){
            if(Constants.deliveryDate!=null&!Constants.deliveryDate.isEmpty())

                if(Constants.deliverytime.equalsIgnoreCase("")||Constants.deliverytime.isEmpty()){
                    txt_delivery_type.setText("No Rush: " +Constants.deliveryDay+", "+ Constants.deliveryDateCheckout.replace("-", "/"));
                }else{
                    txt_delivery_type.setText("No Rush: " +Constants.deliveryDay+", "+ Constants.deliveryDateCheckout.replace("-", "/")+", "+Constants.deliverytime);
                }
            else{
                txt_delivery_type.setText("No Rush");
            }
        }
        else
        {
            /*rel_delivery_type.setClickable(false);
            rel_delivery_type.setEnabled(false);*/

        }
        Double total = AddToCartListAllItemsActivity.total_price + tip;
        txt_total.setText("R" + nf.format(total));

        JSONArray jsonArray=new JSONArray();
        JSONObject jObjImages;
        String ids="";
        ArrayList<String> id=new ArrayList<>();

        for(int i=0; i< AddToCartListAllItemAdapter.listAddToCartList.size(); i++){

                ids=ids+","+AddToCartListAllItemAdapter.listAddToCartList.get(i).get("product_id");

                id.add(AddToCartListAllItemAdapter.listAddToCartList.get(i).get("product_id"));
                //Toast.makeText(getApplicationContext(), "JSONDATA: "+jsonArray.toString(), Toast.LENGTH_LONG).show();

            if(ids.startsWith(",")){
                ids=ids.substring(1, ids.length());
            }
        }
        getDeliveryFee(ids, customer_id);

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

                Toast.makeText(CheckOutActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                Toast.makeText(CheckOutActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }
    private void getDeliveryFee(String ids, String customer_id) {

        ids=ids;
        pDialog=new ProgressDialog(CheckOutActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        restService=new RestService();
        restService.getDeleveryFee(ids, customer_id, new RestCallback <GetDeliveryFeeRequest> () {
            @Override
            public void success(GetDeliveryFeeRequest obj) {
                //if (obj.getSuccess() == 200 && obj.getStatus() == 1) {
                Constants.deliveryfee = Float.parseFloat(obj.getData().getDeliveryFeeData().get(0).getAmount());
                Constants.isFirstTimeDelivery=obj.getData().getDeliveryFeeData().get(0).getAmount();

                if(Constants.isFirstTimeDelivery.equalsIgnoreCase("0")){
                    txt_delivery_free_view.setText("Free 1st 60' Order");
                }
                txt_delivery_fee.setText("R"+obj.getData().getDeliveryFeeData().get(0).getAmount());
                Double price=0.00;

                if(Constants.strTipPercent!=null&&!Constants.strTipPercent.toString().equalsIgnoreCase("")){

                    if (Constants.strTipPercent.equalsIgnoreCase("0")) {
                        Constants.strTipPercent="0";
                        tip = 0.00;
                        txt_tip_percent.setText("R" + nf.format(tip));
                        Constants.driver_tip = tip;
                        price = AddToCartListAllItemsActivity.total_price + tip;

                        txt_total.setText("R" + nf.format(price));
                        txt_tips.setText("R" + nf.format(tip) + " (" + 0 + "%)");
                    } else if (Constants.strTipPercent.equalsIgnoreCase("5")) {
                        Constants.strTipPercent="5";
                        tip = AddToCartListAllItemsActivity.total_price * 5 / 100;
                        txt_tip_percent.setText("R" + nf.format(tip));
                        Constants.driver_tip = tip;
                        price = AddToCartListAllItemsActivity.total_price + tip;

                        txt_total.setText("R" + nf.format(price));
                        txt_tips.setText("R" + nf.format(tip) + " (" + 5 + "%)");

                    } else if (Constants.strTipPercent.equalsIgnoreCase("10")) {
                        Constants.strTipPercent="10";
                        tip = AddToCartListAllItemsActivity.total_price * 10 / 100;
                        txt_tip_percent.setText("R" + nf.format(tip));
                        Constants.driver_tip = tip;
                        price = AddToCartListAllItemsActivity.total_price + tip;

                        txt_total.setText("R" + nf.format(price));
                        txt_tips.setText("R" + nf.format(tip) + " (" + 10 + "%)");

                    } else if (Constants.strTipPercent.equalsIgnoreCase("15")) {
                        Constants.strTipPercent="15";
                        tip = AddToCartListAllItemsActivity.total_price * 15 / 100;
                        txt_tip_percent.setText("R" + nf.format(tip));
                        Constants.driver_tip = tip;
                        price = AddToCartListAllItemsActivity.total_price + tip;

                        txt_total.setText("R" + nf.format(price));
                        txt_tips.setText("R" + nf.format(tip) + " (" + 15 + "%)");

                    }
                }

                Double total = AddToCartListAllItemsActivity.total_price + Constants.deliveryfee;
                txt_total.setText("R" + nf.format(total));
                if (pDialog != null) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void invalid() {

                Double price=0.00;
                txt_delivery_fee.setText("R"+nf.format(price));
                Constants.isFirstTimeDelivery="0";

                if(Constants.isFirstTimeDelivery.equalsIgnoreCase("0")){
                    txt_delivery_free_view.setText("Free 1st 60' Order");
                }
                if(Constants.strTipPercent!=null&&!Constants.strTipPercent.toString().equalsIgnoreCase("")){

                    if (Constants.strTipPercent.equalsIgnoreCase("0")) {
                        Constants.strTipPercent="0";
                        tip = 0.00;
                        txt_tip_percent.setText("R" + nf.format(tip));
                        Constants.driver_tip = tip;
                        price = AddToCartListAllItemsActivity.total_price + tip;

                        txt_total.setText("R" + nf.format(price));
                        txt_tips.setText("R" + nf.format(tip) + " (" + 0 + "%)");
                    } else if (Constants.strTipPercent.equalsIgnoreCase("5")) {
                        Constants.strTipPercent="5";
                        tip = AddToCartListAllItemsActivity.total_price * 5 / 100;
                        txt_tip_percent.setText("R" + nf.format(tip));
                        Constants.driver_tip = tip;
                        price = AddToCartListAllItemsActivity.total_price + tip;

                        txt_total.setText("R" + nf.format(price));
                        txt_tips.setText("R" + nf.format(tip) + " (" + 5 + "%)");

                    } else if (Constants.strTipPercent.equalsIgnoreCase("10")) {
                        Constants.strTipPercent="10";
                        tip = AddToCartListAllItemsActivity.total_price * 10 / 100;
                        txt_tip_percent.setText("R" + nf.format(tip));
                        Constants.driver_tip = tip;
                        price = AddToCartListAllItemsActivity.total_price + tip;

                        txt_total.setText("R" + nf.format(price));
                        txt_tips.setText("R" + nf.format(tip) + " (" + 10 + "%)");

                    } else if (Constants.strTipPercent.equalsIgnoreCase("15")) {
                        Constants.strTipPercent="15";
                        tip = AddToCartListAllItemsActivity.total_price * 15 / 100;
                        txt_tip_percent.setText("R" + nf.format(tip));
                        Constants.driver_tip = tip;
                        price = AddToCartListAllItemsActivity.total_price + tip;

                        txt_total.setText("R" + nf.format(price));
                        txt_tips.setText("R" + nf.format(tip) + " (" + 15 + "%)");

                    }
                }

                Double total = AddToCartListAllItemsActivity.total_price + Constants.deliveryfee;
                txt_total.setText("R" + nf.format(total));
                if (pDialog != null) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void failure() {

                Double price=0.00;
                txt_delivery_fee.setText("R"+nf.format(price));
                Constants.isFirstTimeDelivery="0";

                if(Constants.isFirstTimeDelivery.equalsIgnoreCase("0")){
                    txt_delivery_free_view.setText("Free 1st 60' Order");
                }
                if(Constants.strTipPercent!=null&&!Constants.strTipPercent.toString().equalsIgnoreCase("")){

                    if (Constants.strTipPercent.equalsIgnoreCase("0")) {
                        Constants.strTipPercent="0";
                        tip = 0.00;
                        txt_tip_percent.setText("R" + nf.format(tip));
                        Constants.driver_tip = tip;
                        price = AddToCartListAllItemsActivity.total_price + tip;

                        txt_total.setText("R" + nf.format(price));
                        txt_tips.setText("R" + nf.format(tip) + " (" + 0 + "%)");
                    } else if (Constants.strTipPercent.equalsIgnoreCase("5")) {
                        Constants.strTipPercent="5";
                        tip = AddToCartListAllItemsActivity.total_price * 5 / 100;
                        txt_tip_percent.setText("R" + nf.format(tip));
                        Constants.driver_tip = tip;
                        price = AddToCartListAllItemsActivity.total_price + tip;

                        txt_total.setText("R" + nf.format(price));
                        txt_tips.setText("R" + nf.format(tip) + " (" + 5 + "%)");

                    } else if (Constants.strTipPercent.equalsIgnoreCase("10")) {
                        Constants.strTipPercent="10";
                        tip = AddToCartListAllItemsActivity.total_price * 10 / 100;
                        txt_tip_percent.setText("R" + nf.format(tip));
                        Constants.driver_tip = tip;
                        price = AddToCartListAllItemsActivity.total_price + tip;

                        txt_total.setText("R" + nf.format(price));
                        txt_tips.setText("R" + nf.format(tip) + " (" + 10 + "%)");

                    } else if (Constants.strTipPercent.equalsIgnoreCase("15")) {
                        Constants.strTipPercent="15";
                        tip = AddToCartListAllItemsActivity.total_price * 15 / 100;
                        txt_tip_percent.setText("R" + nf.format(tip));
                        Constants.driver_tip = tip;
                        price = AddToCartListAllItemsActivity.total_price + tip;

                        txt_total.setText("R" + nf.format(price));
                        txt_tips.setText("R" + nf.format(tip) + " (" + 15 + "%)");

                    }
                }

                Double total = AddToCartListAllItemsActivity.total_price + Constants.deliveryfee;
                txt_total.setText("R" + nf.format(total));
                if (pDialog != null) {
                    pDialog.dismiss();
                }
            }
        });
    }

    private void callPopUpSpecialDiscount(View anchorView) {
        pw = new PopupWindow(dropDownMenuSpecialDiscount(R.layout.pop_up_menu, new Vector()),anchorView.getWidth(),height/3, true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchorView);
        pw.update();
    }
    private View dropDownMenuSpecialDiscount(int layout, Vector menuItem)
    {
        View view = null;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null, false);
        final ArrayList<String> arrItem=new ArrayList<>();
        arrItem.add("0%");
        arrItem.add("5%");
        arrItem.add("10%");
        arrItem.add("15%");

        TipCatSpinnerAdapter searchLangAdapter = new TipCatSpinnerAdapter(CheckOutActivity.this, arrItem);
        ListView listView = (ListView)view.findViewById(R.id.pop_up_menu_list);
        listView.setAdapter(searchLangAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                double tip = 0;

                if (position == 0) {
                    Constants.strTipPercent="0";
                    tip = 0.00;
                    txt_tip_percent.setText("R" + nf.format(tip));
                    Constants.driver_tip = tip;
                    Double price = AddToCartListAllItemsActivity.total_price + tip;

                    txt_total.setText("R" + nf.format(price+Constants.deliveryfee));
                    txt_tips.setText("R" + nf.format(tip) + " (" + arrItem.get(position) + ")");
                } else if (position == 1) {
                    Constants.strTipPercent="5";
                    tip = AddToCartListAllItemsActivity.total_price * 5 / 100;
                    txt_tip_percent.setText("R" + nf.format(tip));
                    Constants.driver_tip = tip;
                    Double price = AddToCartListAllItemsActivity.total_price + tip;

                    txt_total.setText("R" + nf.format(price+Constants.deliveryfee));
                    txt_tips.setText("R" + nf.format(tip) + " (" + arrItem.get(position) + ")");

                } else if (position == 2) {
                    Constants.strTipPercent="10";
                    tip = AddToCartListAllItemsActivity.total_price * 10 / 100;
                    txt_tip_percent.setText("R" + nf.format(tip));
                    Constants.driver_tip = tip;
                    Double price = AddToCartListAllItemsActivity.total_price + tip;

                    txt_total.setText("R" + nf.format(price+Constants.deliveryfee));
                    txt_tips.setText("R" + nf.format(tip) + " (" + arrItem.get(position) + ")");

                } else if (position == 3) {
                    Constants.strTipPercent="15";
                    tip = AddToCartListAllItemsActivity.total_price * 15 / 100;
                    txt_tip_percent.setText("R" + nf.format(tip));
                    Constants.driver_tip = tip;
                    Double price = AddToCartListAllItemsActivity.total_price + tip;

                    txt_total.setText("R" + nf.format(price+Constants.deliveryfee));
                    txt_tips.setText("R" + nf.format(tip) + " (" + arrItem.get(position) + ")");

                }
                pw.dismiss();

            }
        });

        return view;
    }

    private void getCustomPercent() {
        final Dialog dialog = new Dialog(CheckOutActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_forget_pass);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView txt_header=(TextView)dialog.findViewById(R.id.header);
        txt_header.setText("KenCommerce");
        TextView txt_header1=(TextView)dialog.findViewById(R.id.header1);
        txt_header1.setVisibility(View.INVISIBLE);
        final EditText etEmailId=(EditText)dialog.findViewById(R.id.msg);
        etEmailId.setHint("Provide tip percent.");
        etEmailId.setTypeface(typeFaceSegoeuiReg);
        etEmailId.setMaxEms(2);
        etEmailId.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextView btn_no=(TextView)dialog.findViewById(R.id.btn_cancel);
        TextView btn_yes=(TextView)dialog.findViewById(R.id.btn_ok);
        btn_yes.setText("OK");
        btn_no.setText("Cancel");
        btn_no.setAllCaps(false);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEmailId.getText().toString().equalsIgnoreCase("0")){
                    txt_tips.setText("R 0.00" +" (" + etEmailId.getText().toString() + ")");
                    dialog.dismiss();
                }
                else if(etEmailId.getText().toString().length()>0){
                    int i=Integer.parseInt(etEmailId.getText().toString());
                    tip = AddToCartListAllItemsActivity.total_price*i / 100;
                    tip = Double.parseDouble(new DecimalFormat("##.####").format(tip));

                    txt_tip_percent.setText("R" + nf.format(tip));
                    Double price=AddToCartListAllItemsActivity.total_price + tip;
                    if(Constants.deliverytypeCheckout.equalsIgnoreCase("asap")){
                        price=price+10.00;
                        Constants.deliveryfee=10.00;
                    }else{
                        price=price+5.00;
                        Constants.deliveryfee=5.00;
                    }
                    txt_total.setText("R" + nf.format(price));
                    txt_tips.setText("R" + nf.format(tip) + " (" + etEmailId.getText().toString() + ")");
                    Constants.driver_tip=tip;
                    dialog.dismiss();
                }


            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    /////////////////////Hiding soft keyboard/////////////
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    private void getDialogCoverage() {
        int width=0, height=0;
        final Dialog dialog = new Dialog(CheckOutActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_delivery_schedule);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        width = (int) (width * 0.75);
        height = (int) (height * 0.45);
        dialog.getWindow().setLayout(width, height);

        LinearLayout lin_asap=(LinearLayout)dialog.findViewById(R.id.lin_asap);
        LinearLayout lin_no_rush=(LinearLayout)dialog.findViewById(R.id.lin_no_rush);

        final ImageView img_asap=(ImageView)dialog.findViewById(R.id.img_asap);
        final ImageView img_no_rush=(ImageView)dialog.findViewById(R.id.img_no_rush);

        TextView btn_no=(TextView)dialog.findViewById(R.id.txt_cancel);
        TextView btn_yes=(TextView)dialog.findViewById(R.id.txt_save);
        //isAsap="Yes";
        if(Constants.deliverytypeCheckout.equalsIgnoreCase("No Rush")){
            isAsap="No";
            img_asap.setImageResource(R.drawable.checkout_radio_blue_normal);
            img_no_rush.setImageResource(R.drawable.checkout_radio_active_blue);

        }else if(Constants.deliverytypeCheckout.equalsIgnoreCase("ASAP")){
            isAsap="Yes";
            img_asap.setImageResource(R.drawable.checkout_radio_active_blue);
            img_no_rush.setImageResource(R.drawable.checkout_radio_blue_normal);
        }
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
                if(isAsap.equalsIgnoreCase("Yes")){
                    txt_delivery_type.setText("ASAP");
                    Constants.deliverytypeCheckout="asap";
                    Constants.deliveryDate="Needed ASAP";
                    Constants.deliveryDateCheckout="ASAP";

                }else{
                    Constants.deliverytypeCheckout="No Rush";
                    txt_delivery_type.setText("No Rush");
                    Constants.deliveryDate="";
                    Constants.deliveryDateCheckout="No Rush";
                    Intent intent=new Intent(CheckOutActivity.this, DeliveryChargeActivity.class);
                    intent.putExtra("isasap", isAsap);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }

            }
        });
        lin_asap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAsap="Yes";
                Constants.deliverytypeCheckout="asap";
                img_asap.setImageResource(R.drawable.checkout_radio_active_blue);
                img_no_rush.setImageResource(R.drawable.checkout_radio_blue_normal);
                //Constants.deliveryDate="Needed ASAP";
            }
        });
        lin_no_rush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAsap="No";
                Constants.deliverytypeCheckout="No Rush";
                img_asap.setImageResource(R.drawable.checkout_radio_blue_normal);
                img_no_rush.setImageResource(R.drawable.checkout_radio_active_blue);
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void getDeliverytype() {
        final Dialog dialog = new Dialog(CheckOutActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_delivery_type);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView header=(TextView)dialog.findViewById(R.id.header);
        header.setTypeface(typeFaceSegoeuiBold);
        TextView msg=(TextView)dialog.findViewById(R.id.msg);
        msg.setTypeface(typeFaceSegoeuiReg);
        TextView btn_yes=(TextView)dialog.findViewById(R.id.btn_ok);
        TextView btn_no=(TextView)dialog.findViewById(R.id.btn_cancel);
        //header.setText("Error");
        msg.setText("At the moment, we are unable to process both ASAP and scheduled deliveries in one cart. To proceed with either type of delivery, ensure your cart contains only items from same delivery category.");
        btn_yes.setText("Schedule");
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Put Grocery selection for landing page
                dialog.dismiss();
                Intent intent = new Intent(CheckOutActivity.this, DeliveryChargeActivity.class);
                intent.putExtra("isasap", "No");
                startActivity(intent);
                finish();
                Constants.deliverytypeCheckout = "No Rush";
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        btn_no.setText("Update Cart");
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Put Grocery selection for landing page
                dialog.dismiss();
                Intent intent=new Intent(CheckOutActivity.this, AddToCartListAllItemsActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(CheckOutActivity.this, AddToCartListAllItemsActivity.class);
        startActivity(intent);
        finish();
        Constants.deliverytypeCart="";
        Constants.deliverytypeCheckout="";
        Constants.deliveryDate="";
        Constants.deliveryDateCheckout="";
        AddToCartListAllItemsActivity.size=0;
        CheckOutActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void customCheckBoxTextView(TextView view) {
        String init = "By continuing to use KenCommerce you agree to the most recent ";
        String terms = "Overview";
        String and = " & ";
        String privacy = "Feedback";
        String dot=".";
        //String last = "have been updated. By continuing to use KenCommerce you agree to the most recent Terms of Service and Privacy Policy. ";

        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                init);
        spanTxt.append(terms);

        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //callTermsCondition("file:///android_asset/Terms_and_Conditions.html", "Terms & Conditions");
                Intent urlPP = new Intent(CheckOutActivity.this, KenCommerceWebViewActivity.class);
                urlPP.putExtra("header", "Overview");
                urlPP.putExtra("url", "http://www.swashconvergence.com/KenCommerce.html");
                startActivity(urlPP);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        }, init.length(), init.length() + terms.length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#FBAE3C")), init.length(), init.length() + terms.length(), 0);
        spanTxt.append(and);
        spanTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), init.length() + terms.length(), init.length() + terms.length() + and.length(), 0);
        spanTxt.append(privacy);
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //callTermsCondition("file:///android_asset/privacy policy.html", "Privacy Policy");
                Intent urlPP = new Intent(CheckOutActivity.this, KenCommerceWebViewActivity.class);
                urlPP.putExtra("header", "Feedback");
                urlPP.putExtra("url", "http://www.swashconvergence.com/feedback.html");
                startActivity(urlPP);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        }, init.length() + terms.length() + and.length(), init.length() + terms.length() + and.length() + privacy.length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#FBAE3C")), init.length() + terms.length() + and.length(), init.length() + terms.length() + and.length() + privacy.length(), 0);

        spanTxt.append(dot);
        spanTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), init.length() + terms.length() + and.length() + privacy.length(), init.length() + terms.length() + and.length() + privacy.length()+ dot.length(), 0);

        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }
}
