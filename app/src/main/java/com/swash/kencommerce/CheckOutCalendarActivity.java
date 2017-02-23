package com.swash.kencommerce;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;

/**
 * Created by Kranti on 12/12/2016.
 */

public class CheckOutCalendarActivity extends AppCompatActivity {

    TextView txt_ship_addr, txt_bill_addr;
    LinearLayout ll_yes, ll_no;
    ImageView img_yes, img_no;

    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;

    SharedPreferences.Editor toEdit;

    EditText extfirstname, extlastname;

    Button btn_continue;

    Utils utils;
    SharedPreferences shPrefDeliverAddr;
    SharedPreferences.Editor toEditAddr;
    String fname="", lName="", shipingAdd="", bilingAdd="", forshipingbiling="";
    ImageView imgBack;

    RelativeLayout rel_add, rel_pay_with, rel_delivery_type;
    private TextView txt_addr;
    String isAsap="";
    ImageView img_calendar;
    Bundle extras;
    String delivery_type="";
    private Button img_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_out_calendar);
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");
        utils=new Utils(CheckOutCalendarActivity.this);
        extras=getIntent().getExtras();
        if(extras!=null)
        {
            delivery_type=extras.getString("isasap", "");
        }
        shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();
    }

    private void initFields() {
        shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);

        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CheckOutCalendarActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        img_calendar=(ImageView)findViewById(R.id.img_calendar);
        img_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delivery_type.equalsIgnoreCase("no"))
                    utils.displayAlert("Calendar is clickable");
                else
                    utils.displayAlert("Calendar is not clickable");
            }
        });

        img_next=(Button)findViewById(R.id.btn_next);
        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                //CheckOutCalendarActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        /*rel_add=(RelativeLayout)findViewById(R.id.rel_addr);
        rel_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutCalendarActivity.this, MapActivity.class);
                intent.putExtra("fname",extfirstname.getText().toString());
                intent.putExtra("lname", extlastname.getText().toString());
                intent.putExtra("shippingaddress", txt_ship_addr.getText().toString());
                intent.putExtra("bilingaddress", txt_bill_addr.getText().toString());
                intent.putExtra("valuefor", "shiping");
                startActivity(intent);
            }
        });
        rel_pay_with=(RelativeLayout)findViewById(R.id.rel_pay_with);
        rel_pay_with.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rel_delivery_type=(RelativeLayout)findViewById(R.id.rel_delivery_type);
        rel_delivery_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialogCoverage();
            }
        });
        txt_addr=(TextView)findViewById(R.id.txt_addr);
        String callFromMap = shPrefDeliverAddr.getString(Constants.strShPrefDelCallFrom, "No");
        if (callFromMap.equalsIgnoreCase("Yes")*//*&&forshipingbiling.equalsIgnoreCase("shiping")*//*){
            String strLat = shPrefDeliverAddr.getString(Constants.strShPrefDelLat,"");
            String strLong = shPrefDeliverAddr.getString(Constants.strShPrefDelLong,"");
            String strAddr = shPrefDeliverAddr.getString(Constants.strShPrefDelAddr,"");
            txt_addr.setText(strAddr);

        }*/

    }
    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(CheckOutCalendarActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_delivery_schedule);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        LinearLayout lin_asap=(LinearLayout)dialog.findViewById(R.id.lin_asap);
        LinearLayout lin_no_rush=(LinearLayout)dialog.findViewById(R.id.lin_asap);
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
        lin_asap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAsap="Yes";
            }
        });
        lin_no_rush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAsap="No";
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
        CheckOutCalendarActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
