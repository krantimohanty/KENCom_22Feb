package com.swash.kencommerce;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.dbhelper.AddToCartTable;
import com.swash.kencommerce.model.CartTotalRequest;
import com.swash.kencommerce.model.CustomerDetailsRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

public class MyDetailsActivity extends AppCompatActivity {
    ImageView img_back;
    Toolbar toolbar;
    Button btn_sign_up;
    TextView txt_header,txt_name,txt_last_name, txt_name_1,txt_last_name_1, txt_email_add, txt_email_add_1, txt_phone, txt_phone_1, txt_dob, txt_b_day, txt_password, txt_password_1, txt_edit_my_details;
    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;
    LinearLayout lin_cart;

    int i=0;
    private Button btn_count;
    AddToCartTable mAddToCartTable;

    RestService restService;
    SharedPreferences sharedPreferenceUser;
    SharedPreferences.Editor sharedPrefEditior;
    private ImageView img_landing_icon;
    private ImageView img_wishlist_icon;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_details);
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");
        mAddToCartTable=new AddToCartTable(this);
        i=mAddToCartTable.getCount();
        restService=new RestService();

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();
    }

    private void initFields() {
        sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
        img_landing_icon=(ImageView)findViewById(R.id.img_landing_icon);
        img_landing_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDetailsActivity.this, LandingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });

        img_wishlist_icon=(ImageView)findViewById(R.id.img_wishlist_icon);
        img_wishlist_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDetailsActivity.this, MyWishListActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MyDetailsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        btn_count=(Button)findViewById(R.id.btn_count);
        btn_count.setText("" + i);
        lin_cart=(LinearLayout)findViewById(R.id.lin_cart);
        lin_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDetailsActivity.this, AddToCartListAllItemsActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.MyDetailsActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        txt_header=(TextView)findViewById(R.id.tv_signin);
        txt_header.setText("My Details");
        txt_header.setTypeface(typeFaceSegoeuiReg);
        txt_name=(TextView)findViewById(R.id.txt_name);
        txt_name.setTypeface(typeFaceSegoeuiReg);
        txt_name_1=(TextView)findViewById(R.id.txt_name_1);
        txt_name_1.setTypeface(typeFaceSegoeuiReg);

        txt_last_name=(TextView)findViewById(R.id.txt_last_name);
        txt_last_name.setTypeface(typeFaceSegoeuiReg);
        txt_last_name_1=(TextView)findViewById(R.id.txt_last_name_1);
        txt_last_name_1.setTypeface(typeFaceSegoeuiReg);

        txt_dob=(TextView)findViewById(R.id.txt_dob);
        txt_dob.setTypeface(typeFaceSegoeuiReg);
        txt_b_day=(TextView)findViewById(R.id.txt_b_day);
        txt_b_day.setTypeface(typeFaceSegoeuiReg);

        txt_email_add=(TextView)findViewById(R.id.txt_email_add);
        txt_email_add.setTypeface(typeFaceSegoeuiReg);
        txt_email_add_1=(TextView)findViewById(R.id.txt_email_add_1);
        txt_email_add_1.setTypeface(typeFaceSegoeuiReg);
        txt_phone=(TextView)findViewById(R.id.txt_phone);
        txt_phone.setTypeface(typeFaceSegoeuiReg);
        txt_phone_1=(TextView)findViewById(R.id.txt_phone_1);
        txt_phone_1.setTypeface(typeFaceSegoeuiReg);
        txt_password=(TextView)findViewById(R.id.txt_password);
        txt_password.setTypeface(typeFaceSegoeuiReg);
        txt_password_1=(TextView)findViewById(R.id.txt_password_1);
        txt_password_1.setTypeface(typeFaceSegoeuiReg);

        txt_edit_my_details=(TextView)findViewById(R.id.txt_edit_my_details);
        txt_edit_my_details.setTypeface(typeFaceSegoeuiBold);
        txt_edit_my_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDetailsActivity.this, EditMyDetailsActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        String strCustId = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        if (!strCustId.equalsIgnoreCase("")) {
            getCustomerDetails(strCustId);
            getCartTotal(strCustId);
        }else {
            Toast.makeText(MyDetailsActivity.this, "Please login..", Toast.LENGTH_LONG).show();
        }
    }

    private void getCustomerDetails(String custId) {
        final ProgressDialog pDialog=new ProgressDialog(MyDetailsActivity.this);
        pDialog.show();
        pDialog.setMessage("Checking your data..");
        restService.getCustomerDetails(custId, new RestCallback<CustomerDetailsRequest>() {

            @Override
            public void success(CustomerDetailsRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    sharedPrefEditior = sharedPreferenceUser.edit();
                    sharedPrefEditior.putString(Constants.strShPrefUserId, responce.getData().getCustomerData().getId());
                    sharedPrefEditior.putString(Constants.strShPrefUserFname, responce.getData().getCustomerData().getFname());
                    sharedPrefEditior.putString(Constants.strShPrefUserLname, responce.getData().getCustomerData().getLname());
                    sharedPrefEditior.putString(Constants.strShPrefUserEmail, responce.getData().getCustomerData().getEmail());
                    sharedPrefEditior.putString(Constants.strShPrefUserPhone, responce.getData().getCustomerData().getPhone());
                    sharedPrefEditior.commit();
                    txt_name_1.setText(responce.getData().getCustomerData().getFname());
                    txt_last_name_1.setText(responce.getData().getCustomerData().getLname());
                    txt_email_add_1.setText(responce.getData().getCustomerData().getEmail());
                    if(responce.getData().getCustomerData().getPhone()!=null)
                    txt_phone_1.setText("+27"+responce.getData().getCustomerData().getPhone());
                    else{
                        txt_phone_1.setText("+27");
                    }
                    txt_password_1.setText(responce.getData().getCustomerData().getPassword());
                    txt_b_day.setText(responce.getData().getCustomerData().getDob());
                } else {
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
        final Dialog dialog = new Dialog(MyDetailsActivity.this);
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
        MyDetailsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void getCartTotal(String customer_id) {

        pDialog=new ProgressDialog(MyDetailsActivity.this);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait");
        restService.getCartTotal(customer_id, new RestCallback<CartTotalRequest>() {
            @Override
            public void success(CartTotalRequest object) {

                if(object.getStatus()==200&&object.getSuccess()==1){

                    btn_count.setText(object.getData().getTotalQty().toString());

                }
                if (pDialog != null)
                    pDialog.dismiss();
            }

            @Override
            public void invalid() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(MyDetailsActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(MyDetailsActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }
}
