package com.swash.kencommerce;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;

//import com.appsflyer.AppsFlyerLib;

public class OrderStatusActivity extends AppCompatActivity {

    ProgressBar progress;
    ImageView imgBack;
    TextView tv_signin;
    private Utils utils;

    private TextView txt_order_details, txt_track_order;
    private String customer_id="", product_id="", product_image="", product_name="";
    SharedPreferences sharedPreferenceUser;
    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;

    TextView txt_name, txt_order_id, txt_price, txt_order_place;
    private String customer_name="";
    private SharedPreferences shPrefUserSelection;
    String store_id="";
    Timer t;
    ImageView img_gift_box, img_van, img_box;
    private NumberFormat nf;
    public static Runnable runnable;
    final static Handler handler = new Handler();
    TextView txt_on_the_way,txt_order_accepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_status);
        utils = new Utils(OrderStatusActivity.this);
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");


        sharedPreferenceUser = this.getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
        customer_id = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        customer_name = sharedPreferenceUser.getString(Constants.strShPrefUserFname, "");
        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();

        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                      //displayError("error");



                                      try {
                                          Thread.sleep(5000);
                                          loadBubble2(1);
                                      } catch (InterruptedException e) {
                                          e.printStackTrace();
                                      }
                                      try {
                                          Thread.sleep(5000);
                                          loadBubble2(2);
                                      } catch (InterruptedException e) {
                                          e.printStackTrace();
                                      }
                                      try {
                                          Thread.sleep(5000);
                                          loadBubble2(3);
                                      } catch (InterruptedException e) {
                                          e.printStackTrace();
                                      }

                                  }

                              },5000,
                15000);
    }
    public void loadBubble2(final int i) {
        runnable = new Runnable() {
            public void run() {

              if(i==1){
                   img_gift_box.setImageResource(R.drawable.tick_order_status);
               }else if(i==2){

                       img_van.setImageResource(R.drawable.tick_order_status);

               }else if(i==3){

                       img_box.setImageResource(R.drawable.tick_order_status);

               }

            }
            // handler.postDelayed(this, 5000);
        };
        handler.postDelayed(runnable, 0); // for initial delay..*/

    }


    private void initFields() {

        nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        txt_order_accepted=(TextView)findViewById(R.id.txt_order_accepted);
        txt_order_accepted.setText("Order accepted and getting packed!\n"+Constants.lastDeliveryTime40);
        txt_on_the_way=(TextView)findViewById(R.id.txt_on_the_way);
        txt_on_the_way.setText("Expected delivery at "+Constants.lastDeliveryTime15);
        img_gift_box=(ImageView)findViewById(R.id.img_gift_box);
        img_van=(ImageView)findViewById(R.id.img_van);
        img_box=(ImageView)findViewById(R.id.img_box);

        progress=(ProgressBar)findViewById(R.id.progress);
        txt_name=(TextView)findViewById(R.id.txt_name);
        txt_name.setText(customer_name);
        txt_order_id=(TextView)findViewById(R.id.txt_order_id);
        txt_order_id.setText("Order ID : "+Constants.orderID);
        txt_price=(TextView)findViewById(R.id.txt_price);
        txt_price.setText("R "+nf.format(AddToCartListAllItemsActivity.total_price));
        txt_order_place=(TextView)findViewById(R.id.txt_order_place);
        txt_order_place.setText("Order placed ASAP!");
        txt_order_details=(TextView)findViewById(R.id.txt_order_details);
        txt_order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.fromOrderStatus="Yes";
                Intent intent=new Intent(OrderStatusActivity.this, CheckoutFinalActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        txt_track_order=(TextView)findViewById(R.id.txt_track_order);
        txt_track_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Constants.strTipPercent!=null)
                    Constants.strTipPercent="";

                Constants.deliverytypeCheckout="";
                Constants.deliverytypeCart="";
                Constants.deliveryDate="";
                Constants.deliveryDateCheckout="";
                Constants.fromOrderStatus="";
                shPrefUserSelection = OrderStatusActivity.this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
                store_id = shPrefUserSelection.getString(Constants.strShUserProductId, "");

                if (store_id.equalsIgnoreCase("")) {

                    Intent intent = new Intent(OrderStatusActivity.this, PinCodeActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                } else {

                    Intent intent = new Intent(OrderStatusActivity.this, SubCategoryActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }

            }
        });






    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //finish();
        //OrderStatusActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(OrderStatusActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    public void displayAlert(String message)
    {
        // TODO Auto-generated method stub
        //message="To proceed, sign into your account.";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderStatusActivity.this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intentLogin = new Intent(OrderStatusActivity.this, ProductDetailsActivity.class);
                finish();
                startActivity(intentLogin);
            }
        });
        TextView myMsg = new TextView(OrderStatusActivity.this);
        myMsg.setText(message);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setTextSize(16);
        myMsg.setTypeface(typeFaceSegoeuiReg);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL);
        alertDialogBuilder.setView(myMsg);

        TextView title = new TextView(OrderStatusActivity.this);
        // You Can Customise your Title here
        title.setText("Stockup");
        title.setBackgroundColor(Color.TRANSPARENT);
        title.setPadding(15, 20, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTypeface(typeFaceSegoeuiBold);
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
}
