package com.swash.kencommerce;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckOutStep1Activity extends Activity {
    //Button btn_drinks, btn_foods, btn_day, btn_gift;
    ImageView img_drink, img_food, img_day, img_gift;
            //btn_grocery;

    SharedPreferences shPrefUserSelection;
    SharedPreferences.Editor toEdit;
    Button btn_continue;
    Typeface typeFaceSegoeuiBold;
    ImageView imgBack;
    SharedPreferences shPrefDeliverAddr;
    SharedPreferences.Editor toEditAddr;
    EditText ext_email_add;
    EditText ext_mobile_num;
    TextView txt_country_code;
    Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_out1);
        shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(), "ROBOTO-BOLD_0.TTF");
        utils=new Utils(CheckOutStep1Activity.this);
        initFields();
    }

    private void initFields() {

        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CheckOutStep1Activity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        ext_email_add=(EditText)findViewById(R.id.ext_email);
        ext_mobile_num=(EditText)findViewById(R.id.ext_mobile);
        txt_country_code=(TextView)findViewById(R.id.txt_country_code);

        btn_continue=(Button)findViewById(R.id.btn_continue);
        btn_continue.setTypeface(typeFaceSegoeuiBold);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constants.emailAdd = ext_email_add.getText().toString();
                        Constants.mobilenum = ext_mobile_num.getText().toString();
                        if (Constants.emailAdd.equalsIgnoreCase("")) {
                            utils.displayAlert("Please enter your Email Address");
                        }
                        else if(!emailValidator(Constants.emailAdd)){
                            utils.displayAlert("Please provide correct Email Address");
                        }
                        else if (Constants.mobilenum.equalsIgnoreCase("")) {
                            utils.displayAlert("Please enter your Mobile number");
                        } else {
                            toEditAddr = shPrefDeliverAddr.edit();
                            toEditAddr.putString(Constants.emailAdd, Constants.emailAdd);
                            toEditAddr.putString(Constants.mobilenum, Constants.mobilenum);
                            toEditAddr.commit();
                            Intent intent = new Intent(CheckOutStep1Activity.this, CheckOutStep2Activity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        }
                    }
                });
            }
        });
    }
    public static boolean emailValidator(String mail) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mail);
        return matcher.matches();
    }
    private void getDialogAge() {
        final Dialog dialog = new Dialog(CheckOutStep1Activity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_age);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView header=(TextView)dialog.findViewById(R.id.header);
        TextView msg=(TextView)dialog.findViewById(R.id.msg);
        Button btn_no=(Button)dialog.findViewById(R.id.btn_no);
        Button btn_yes=(Button)dialog.findViewById(R.id.btn_yes);

        //header.setText("Error");
        msg.setText("Are you sure, you want to go the next page?");
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Put Grocery selection for landing page
                toEdit = shPrefUserSelection.edit();
                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopMainProduct);
                toEdit.commit();
                Intent intentSlider = new Intent(CheckOutStep1Activity.this, PinCodeActivity.class);
                //finish();
                startActivity(intentSlider);
                dialog.dismiss();
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
        finish();
        CheckOutStep1Activity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
