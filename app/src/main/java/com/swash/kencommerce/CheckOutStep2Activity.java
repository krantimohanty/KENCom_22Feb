package com.swash.kencommerce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;

/**
 * Created by Kranti on 12/12/2016.
 */

public class CheckOutStep2Activity extends AppCompatActivity {

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
    Bundle extras;
    ImageView imgBack;

    RelativeLayout rel_add;
    private TextView txt_addr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_out);
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");
        utils=new Utils(CheckOutStep2Activity.this);
        extras=getIntent().getExtras();
        if(extras!=null){
            fname=extras.getString("fname");
            lName=extras.getString("lname");
            shipingAdd=extras.getString("shippingaddress");
            bilingAdd=extras.getString("bilingaddress");
            forshipingbiling=extras.getString("valuefor");
        }
        shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);
        initFields();
        if (extras!=null){
            extfirstname.setText(fname);
            extlastname.setText(lName);
            if(forshipingbiling.toString().equalsIgnoreCase("biling"))

            txt_ship_addr.setText(shipingAdd);
            else if(forshipingbiling.toString().equalsIgnoreCase("shiping")){
                txt_bill_addr.setText(bilingAdd);
            }
        }
    }

    private void initFields() {
        shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);

        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CheckOutStep2Activity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        rel_add=(RelativeLayout)findViewById(R.id.rel_addr);
        rel_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutStep2Activity.this, MapActivity.class);
                intent.putExtra("fname",extfirstname.getText().toString());
                intent.putExtra("lname", extlastname.getText().toString());
                intent.putExtra("shippingaddress", txt_ship_addr.getText().toString());
                intent.putExtra("bilingaddress", txt_bill_addr.getText().toString());
                intent.putExtra("valuefor", "shiping");
                startActivity(intent);
            }
        });
        txt_addr=(TextView)findViewById(R.id.txt_addr);
        String callFromMap = shPrefDeliverAddr.getString(Constants.strShPrefDelCallFrom, "No");
        if (callFromMap.equalsIgnoreCase("Yes")/*&&forshipingbiling.equalsIgnoreCase("shiping")*/){
            String strLat = shPrefDeliverAddr.getString(Constants.strShPrefDelLat,"");
            String strLong = shPrefDeliverAddr.getString(Constants.strShPrefDelLong,"");
            String strAddr = shPrefDeliverAddr.getString(Constants.strShPrefDelAddr,"");
            txt_addr.setText(strAddr);

        }
        /*txt_ship_addr = (TextView) findViewById(R.id.txt_ship_addr);
        txt_bill_addr = (TextView) findViewById(R.id.txt_bill_addr);

        extfirstname=(EditText)findViewById(R.id.et_first_name);
        extlastname=(EditText)findViewById(R.id.et_last_name);

        img_yes = (ImageView) findViewById(R.id.img_yes);
        img_no = (ImageView) findViewById(R.id.img_no);

        ll_yes = (LinearLayout) findViewById(R.id.ll_yes);
        ll_no = (LinearLayout) findViewById(R.id.ll_no);

        Intent getCurIntent = getIntent();
        if (getCurIntent.getBundleExtra("lat")!=null){
            String strLat = getCurIntent.getStringExtra("lat");
            String strLong = getCurIntent.getStringExtra("lat");
        }

        String callFromMap = shPrefDeliverAddr.getString(Constants.strShPrefDelCallFrom, "No");
        if (callFromMap.equalsIgnoreCase("Yes")&&forshipingbiling.equalsIgnoreCase("shiping")){
            String strLat = shPrefDeliverAddr.getString(Constants.strShPrefDelLat,"");
            String strLong = shPrefDeliverAddr.getString(Constants.strShPrefDelLong,"");
            String strAddr = shPrefDeliverAddr.getString(Constants.strShPrefDelAddr,"");
            txt_ship_addr.setText(strAddr);

        }else if(callFromMap.equalsIgnoreCase("Yes")&&forshipingbiling.equalsIgnoreCase("biling")){
            String strLat = shPrefDeliverAddr.getString(Constants.strShPrefDelLat,"");
            String strLong = shPrefDeliverAddr.getString(Constants.strShPrefDelLong,"");
            String strAddr = shPrefDeliverAddr.getString(Constants.strShPrefDelAddr,"");
            txt_bill_addr.setText(strAddr);
        }

        txt_ship_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutStep2Activity.this, MapActivity.class);
                intent.putExtra("fname",extfirstname.getText().toString());
                intent.putExtra("lname", extlastname.getText().toString());
                intent.putExtra("shippingaddress", txt_ship_addr.getText().toString());
                intent.putExtra("bilingaddress", txt_bill_addr.getText().toString());
                intent.putExtra("valuefor", "shiping");
                startActivity(intent);
            }
        });
        txt_bill_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutStep2Activity.this, MapActivity.class);
                intent.putExtra("fname",extfirstname.getText().toString());
                intent.putExtra("lname", extlastname.getText().toString());
                intent.putExtra("shippingaddress", txt_ship_addr.getText().toString());
                intent.putExtra("bilingaddress", txt_bill_addr.getText().toString());
                intent.putExtra("valuefor", "biling");
                startActivity(intent);
            }
        });
        ll_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_yes.setImageDrawable(getResources().getDrawable(R.drawable.active));
                img_no.setImageDrawable(getResources().getDrawable(R.drawable.normal));
                txt_bill_addr.setText(txt_ship_addr.getText().toString());
            }
        });
        ll_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_yes.setImageDrawable(getResources().getDrawable(R.drawable.normal));
                img_no.setImageDrawable(getResources().getDrawable(R.drawable.active));
                txt_bill_addr.setText("");
            }
        });
        btn_continue=(Button)findViewById(R.id.btn_continue);
        btn_continue.setTypeface(typeFaceSegoeuiBold);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constants.fName = extfirstname.getText().toString();
                        Constants.lName = extlastname.getText().toString();
                        Constants.address=txt_ship_addr.getText().toString();
                        if (Constants.fName.equalsIgnoreCase("")) {
                            utils.displayAlert("Please enter your First Name");
                        } else if (Constants.lName.equalsIgnoreCase("")) {
                            utils.displayAlert("Please enter your Last Name");
                        }
                        else if (Constants.address.equalsIgnoreCase("")) {
                            utils.displayAlert("Please enter your Shipping Address");
                        }else {
                            toEditAddr = shPrefDeliverAddr.edit();
                            toEditAddr.putString(Constants.fName, Constants.fName);
                            toEditAddr.putString(Constants.lName, Constants.lName);
                            toEditAddr.putString(Constants.address, Constants.address);
                            toEditAddr.commit();
                            Intent intent = new Intent(CheckOutStep2Activity.this, CheckOutStep3Activity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        }

                    }
                });
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        CheckOutStep2Activity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
