package com.swash.kencommerce;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.swash.kencommerce.Utils.Constants;

import java.util.Calendar;

//import com.appsflyer.AppsFlyerLib;

public class LandingActivity extends Activity {
    //Button btn_drinks, btn_foods, btn_day, btn_gift;
    ImageView img_mainproduct, img_gourmets, img_day, img_gift;
            //btn_grocery;

    SharedPreferences shPrefUserSelection;
    SharedPreferences.Editor toEdit;
    private ImageView img_events;
    String strAge="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_landing);




        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();

        Calendar now = Calendar.getInstance();
        Log.e("time", now.getTime().toString());
        now.add(Calendar.MINUTE,40);
        Log.e("time", now.getTime().toString());
    }

    private void initFields() {

        shPrefUserSelection = this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
        strAge = shPrefUserSelection.getString(Constants.strAge, "");
        img_mainproduct = (ImageView) findViewById(R.id.img_mainproduct);
        img_gourmets = (ImageView) findViewById(R.id.img_gourmets);
        img_day = (ImageView) findViewById(R.id.img_day);
        img_gift = (ImageView) findViewById(R.id.img_gift);
        img_events=(ImageView)findViewById(R.id.img_events);
        img_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialogCoverage();
            }
        });

        img_mainproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (strAge.equalsIgnoreCase("")) {
                    getDialogAge();
                } else {
                    toEdit = shPrefUserSelection.edit();
                    toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopGift);
//                    toEdit.putString(Constants.strAge,Constants.strAgeNumber);
                    toEdit.commit();
                    Intent intentSlider = new Intent(LandingActivity.this, PinCodeActivity.class);
                    //finish();
                    startActivity(intentSlider);

                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                }
            }
        });

        // Kranti
//        img_gourmets.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Put Grocery selection for landing page
//                toEdit = shPrefUserSelection.edit();
//                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopFood);
//                toEdit.commit();
//
//                Intent intentSlider = new Intent(LandingActivity.this, PinCodeActivity.class);
//                //finish();
//                startActivity(intentSlider);
//                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
//            }
//        });
//        img_day.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Put Grocery selection for landing page
//                toEdit = shPrefUserSelection.edit();
//                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopDay);
//                toEdit.commit();
//
//                Intent intentSlider = new Intent(LandingActivity.this, PinCodeActivity.class);
//                //finish();
//                startActivity(intentSlider);
//                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
//            }
//        });
//        img_gift.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Put Grocery selection for landing page
//                toEdit = shPrefUserSelection.edit();
//                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopMainProduct);
//                toEdit.commit();
//                Intent intentSlider = new Intent(LandingActivity.this, PinCodeActivity.class);
//                //finish();
//                startActivity(intentSlider);
//                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
//            }
//        });
//        img_events.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               /* getDialogCoverage();*/
//                toEdit = shPrefUserSelection.edit();
//                toEdit.putString(Constants.strShUserShopSelected, Constants.strShUserShopEvent);
//                toEdit.commit();
//
//                Intent intentSlider = new Intent(LandingActivity.this, PinCodeActivity.class);
//                //finish();
//                startActivity(intentSlider);
//                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
//            }
//        });


    }
    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(LandingActivity.this);
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
    private void getDialogAge() {
        final Dialog dialog = new Dialog(LandingActivity.this);
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
        msg.setText("Are you sure, you want to go to the next page?");
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
                toEdit.putString(Constants.strAge,Constants.strAgeNumber);
                toEdit.commit();
                Intent intentSlider = new Intent(LandingActivity.this, PinCodeActivity.class);
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
        moveTaskToBack(true);
        LandingActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
