package com.swash.kencommerce;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.dbhelper.AddToCartTable;
import com.swash.kencommerce.model.CartTotalRequest;
import com.swash.kencommerce.model.EditProfileRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

public class EditMyDetailsActivity extends AppCompatActivity {
    ImageView img_back;
    Toolbar toolbar;
    EditText ext_name,ext_surname, ext_email_add, ext_phone, ext_old_pass, ext_new_pass;
    TextView txt_header,txt_name,txt_surname,  txt_email_add,  txt_phone,  txt_password,  txt_save_the_details;
    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;
    LinearLayout lin_cart;
    int i=0;
    private Button btn_count;
    AddToCartTable mAddToCartTable;

    RestService restService;
    Utils utils;
    SharedPreferences sharedPreferenceUser;
    SharedPreferences.Editor sharedPrefEditior;
    private ImageView img_landing_icon;
    private ImageView img_wishlist_icon;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_my_details);
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");
        mAddToCartTable=new AddToCartTable(this);
        i=mAddToCartTable.getCount();
        restService=new RestService();
        utils = new Utils(EditMyDetailsActivity.this);

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
                Intent intent = new Intent(EditMyDetailsActivity.this, LandingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });

        img_wishlist_icon=(ImageView)findViewById(R.id.img_wishlist_icon);
        img_wishlist_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditMyDetailsActivity.this, MyWishListActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditMyDetailsActivity.this, MyDetailsActivity.class);
                startActivity(intent);
                finish();
                EditMyDetailsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        lin_cart=(LinearLayout)findViewById(R.id.lin_cart);
        lin_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditMyDetailsActivity.this, AddToCartListAllItemsActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.EditMyDetailsActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        btn_count=(Button)findViewById(R.id.btn_count);
        btn_count.setText("" + i);
        txt_header=(TextView)findViewById(R.id.tv_signin);
        txt_header.setText("Edit Details");
        txt_header.setTypeface(typeFaceSegoeuiReg);
        txt_name=(TextView)findViewById(R.id.txt_name);
        txt_name.setTypeface(typeFaceSegoeuiReg);
        txt_surname=(TextView)findViewById(R.id.txt_surname);
        txt_surname.setTypeface(typeFaceSegoeuiReg);
        ext_surname=(EditText)findViewById(R.id.ext_surname);
        ext_surname.setTypeface(typeFaceSegoeuiReg);
        ext_name=(EditText)findViewById(R.id.ext_name);
        ext_name.setTypeface(typeFaceSegoeuiReg);
        txt_email_add=(TextView)findViewById(R.id.txt_email_add);
        txt_email_add.setTypeface(typeFaceSegoeuiReg);
        ext_email_add=(EditText)findViewById(R.id.ext_email_add);
        ext_email_add.setTypeface(typeFaceSegoeuiReg);
        txt_phone=(TextView)findViewById(R.id.txt_pin_code);
        txt_phone.setTypeface(typeFaceSegoeuiReg);
        ext_phone=(EditText)findViewById(R.id.ext_phone);
        ext_phone.setTypeface(typeFaceSegoeuiReg);
        txt_password=(TextView)findViewById(R.id.txt_pass);
        txt_password.setTypeface(typeFaceSegoeuiReg);
        ext_old_pass=(EditText)findViewById(R.id.ext_old_pass);
        ext_old_pass.setTypeface(typeFaceSegoeuiReg);

        ext_new_pass=(EditText)findViewById(R.id.ext_new_pass);
        ext_new_pass.setTypeface(typeFaceSegoeuiReg);

        txt_save_the_details=(TextView)findViewById(R.id.txt_edit_my_details);
        txt_save_the_details.setTypeface(typeFaceSegoeuiBold);
        final String strCustFname = sharedPreferenceUser.getString(Constants.strShPrefUserFname, "");
        final String strCustLname = sharedPreferenceUser.getString(Constants.strShPrefUserLname, "");
        String strCustMail = sharedPreferenceUser.getString(Constants.strShPrefUserEmail, "");
        String strCustPhone = sharedPreferenceUser.getString(Constants.strShPrefUserPhone, "");
        ext_name.setText(strCustFname);
        ext_surname.setText(strCustLname);
        ext_email_add.setText(strCustMail);
        ext_phone.setText(strCustPhone);
        txt_save_the_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getDialogCoverage();
                if (valid()) {
                    saveEditedCredentials();
                }
            }
        });
        if(utils.isConnectionPossible()){
            String strCustId = sharedPreferenceUser.getString(Constants.strShPrefUserId,"");
            getCartTotal(strCustId);
        }
    }

    private void saveEditedCredentials() {
        final ProgressDialog pDialog=new ProgressDialog(EditMyDetailsActivity.this);
        pDialog.show();
        pDialog.setMessage("Saving profile details on progress..");
        String strCustId = sharedPreferenceUser.getString(Constants.strShPrefUserId,"");
        String strFname = ext_name.getText().toString().trim();
        String strLname = ext_surname.getText().toString().trim();
        String strMobile = ext_phone.getText().toString().trim();
        String strMail = ext_email_add.getText().toString().trim();
        String strPwdOld = ext_old_pass.getText().toString().trim();
        String strPwdNew = ext_new_pass.getText().toString().trim();
        restService.getEditProfileResponse(strCustId, strFname, strLname, strMobile, strMail, strPwdNew, strPwdOld, new RestCallback<EditProfileRequest>() {

            @Override
            public void success(EditProfileRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    /*sharedPrefEditior = sharedPreferenceUser.edit();
                    sharedPrefEditior.putString(Constants.strShPrefUserId, responce.getData().getCustomerId());
                    sharedPrefEditior.putString(Constants.strShPrefUserFname, responce.getData().getFirstname());
                    sharedPrefEditior.putString(Constants.strShPrefUserLname, responce.getData().getLastname());
                    sharedPrefEditior.putString(Constants.strShPrefUserEmail, responce.getData().getEmail());
                    sharedPrefEditior.putString(Constants.strShPrefUserPhone, responce.getData().getPhone());
                    sharedPrefEditior.commit();*/

                    utils.displayAlert(responce.getData().getSuccess());
                } else {
                    utils.displayAlert(responce.getErrorMsg());
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

    private boolean valid() {
        if (ext_name.getText().toString().trim().length()<2){
            Toast.makeText(EditMyDetailsActivity.this, "Please provide First Name", Toast.LENGTH_LONG).show();
            ext_name.requestFocus();
            return false;
        }else if (ext_surname.getText().toString().trim().length()<2){
            Toast.makeText(EditMyDetailsActivity.this, "Please provide Surname", Toast.LENGTH_LONG).show();
            ext_surname.requestFocus();
            return false;
        }else if (!emailValidator(ext_email_add.getText().toString().trim())){
            Toast.makeText(EditMyDetailsActivity.this, "Please provide valid Email Address..", Toast.LENGTH_LONG).show();
            ext_email_add.requestFocus();
            return false;
        }else if (ext_phone.getText().toString().trim().length()<6){
            Toast.makeText(EditMyDetailsActivity.this, "Please provide phone number", Toast.LENGTH_LONG).show();
            ext_phone.requestFocus();
            return false;
        }else if (ext_old_pass.getText().toString().trim().length()==0){
            Toast.makeText(EditMyDetailsActivity.this, "Please provide Old Password", Toast.LENGTH_LONG).show();
            ext_old_pass.requestFocus();
            return false;
        }else if (ext_old_pass.getText().toString().trim().length()<6){
            Toast.makeText(EditMyDetailsActivity.this, "Minimum length of Password is 6", Toast.LENGTH_LONG).show();
            ext_old_pass.requestFocus();
            return false;
        }else if (ext_new_pass.getText().toString().trim().length()<6){
            Toast.makeText(EditMyDetailsActivity.this, "Please provide New Password", Toast.LENGTH_LONG).show();
            ext_new_pass.requestFocus();
            return false;
        }else if (ext_new_pass.getText().toString().trim().length()<6){
            Toast.makeText(EditMyDetailsActivity.this, "Minimum length of Password is 6", Toast.LENGTH_LONG).show();
            ext_new_pass.requestFocus();
            return false;
        }else {
            return true;
        }
    }
    public static boolean emailValidator(String mail) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mail);
        return matcher.matches();
    }


    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(EditMyDetailsActivity.this);
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
        Intent intent=new Intent(EditMyDetailsActivity.this, MyDetailsActivity.class);
        startActivity(intent);
        finish();
        EditMyDetailsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    private void getCartTotal(String customer_id) {

        pDialog=new ProgressDialog(EditMyDetailsActivity.this);
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
                Toast.makeText(EditMyDetailsActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(EditMyDetailsActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void displayAlert(String message)
    {
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditMyDetailsActivity.this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(EditMyDetailsActivity.this, MyDetailsActivity.class);
                finish();
                startActivity(intent);
                EditMyDetailsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        TextView myMsg = new TextView(EditMyDetailsActivity.this);
        myMsg.setText(message);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setTextSize(16);
        myMsg.setTypeface(typeFaceSegoeuiReg);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL);
        alertDialogBuilder.setView(myMsg);

        TextView title = new TextView(EditMyDetailsActivity.this);
        // You Can Customise your Title here
        title.setText("KenCommerce");
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
