package com.swash.kencommerce;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.swash.kencommerce.GCM.Config;
import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.dbhelper.AddToCartTable;
import com.swash.kencommerce.model.FbRegisterRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;
import com.swash.kencommerce.webview.KenCommerceWebViewActivity;

public class SignUpActivity extends AppCompatActivity {

    ImageView img_back;
    EditText et_first_name, et_last_name, et_mobile_number, et_mail, et_password, et_conf_password;
    Button btn_sign_up;
    TextView  txt_terms_conditions, txt_header, txt_pin_code, et_b_day;
    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;

    RestService restService;
    Utils utils;
    SharedPreferences sharedPreferenceUser;
    SharedPreferences.Editor sharedPrefEditior;
    ProgressDialog pDialog;

    private SimpleDateFormat dateFormatter;
    Format format;
    private DatePickerDialog fromDatePickerDialog;
    String strDate = "";

    String strFbId = "";

    SharedPreferences sharedPrefActivitySel;
    SharedPreferences.Editor editorShPrefActivitySel;
    private AddToCartTable mAddToCartTable;
    ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    View vwPss, vwConfPass;
    String firebaseRegid= "";
    static final String TAG = "FCM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        restService = new RestService();
        utils = new Utils(SignUpActivity.this);
        mAddToCartTable=new AddToCartTable(this);

        data=mAddToCartTable.getAll();
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");
       /* mAddToCartTable=new AddToCartTable(this);

        data=mAddToCartTable.getAll();*/

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        displayFirebaseRegId();
        initFields();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        firebaseRegid = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + firebaseRegid);


    }

    private void initFields() {

        ///////ActivitySelectedSharedPref

        /*sharedPrefActivitySel = this.getSharedPreferences(Constants.strShPrefActivitySelection, Context.MODE_PRIVATE);
        editorShPrefActivitySel=sharedPrefActivitySel.edit();*/

        sharedPreferenceUser=this.getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                SignUpActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        et_first_name = (EditText) findViewById(R.id.et_first_name);
        et_first_name.setTypeface(typeFaceSegoeuiReg);

        et_last_name = (EditText) findViewById(R.id.et_last_name);
        et_last_name.setTypeface(typeFaceSegoeuiReg);

        et_mobile_number = (EditText) findViewById(R.id.et_mobile_number);
        et_mobile_number.setTypeface(typeFaceSegoeuiReg);

        et_b_day = (TextView) findViewById(R.id.et_b_day);
        et_b_day.setTypeface(typeFaceSegoeuiReg);

        format= new SimpleDateFormat("dd MMMM yyyy");
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        et_b_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField();
            }
        });

        et_mail = (EditText) findViewById(R.id.et_mail);
        et_mail.setTypeface(typeFaceSegoeuiReg);

        txt_pin_code=(TextView)findViewById(R.id.txt_pin_code);
        txt_pin_code.setTypeface(typeFaceSegoeuiReg);

        vwPss=(View)findViewById(R.id.view_pass);

        et_password = (EditText) findViewById(R.id.et_password);
        et_password.setTypeface(typeFaceSegoeuiReg);

        vwConfPass=(View)findViewById(R.id.view_conf_pass);

        et_conf_password = (EditText) findViewById(R.id.et_conf_password);
        et_conf_password.setTypeface(typeFaceSegoeuiReg);

        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
        btn_sign_up.setTypeface(typeFaceSegoeuiBold);

        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_header.setTypeface(typeFaceSegoeuiReg);


        txt_terms_conditions=(TextView)findViewById(R.id.txt_terms_condition);
        txt_terms_conditions.setTypeface(typeFaceSegoeuiReg);
        customCheckBoxTextView(txt_terms_conditions);

        Intent intentLogin = getIntent();
        if (intentLogin.hasExtra("id")) {
            String strId = intentLogin.getExtras().getString("id");
            strFbId = strId;
            String strFname = intentLogin.getExtras().getString("first_name");
            et_first_name.setText(strFname);
            String strLname = intentLogin.getExtras().getString("last_name");
            et_last_name.setText(strLname);
            String strEmail = intentLogin.getExtras().getString("email");
            et_mail.setText(strEmail);

            et_password.setVisibility(View.GONE);
            et_conf_password.setVisibility(View.GONE);
            vwPss.setVisibility(View.GONE);
            vwConfPass.setVisibility(View.GONE);
        }

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {

                    JSONArray jsonArray=new JSONArray();
                    JSONObject jObjImages;
                    for(int i=0; i< data.size(); i++){
                        jObjImages=new JSONObject();
                        try {
                            jObjImages.put("attribute_id", data.get(i).get("productattid"));
                            jObjImages.put("product_id", data.get(i).get("productid"));
                            jObjImages.put("option_id", data.get(i).get("productoptionid"));
                            jObjImages.put("qty",  data.get(i).get("qty"));

                            jsonArray.put(i,jObjImages);
                            //Toast.makeText(getApplicationContext(), "JSONDATA: "+jsonArray.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (jsonArray.length()>0) {
                        doRegister(jsonArray.toString());
                    }
                    else {
                        doRegister("");
                    }
                }
            }
        });

    }

    private void doRegister(String  JsonArray) {
        pDialog=new ProgressDialog(SignUpActivity.this);
        pDialog.show();
        pDialog.setMessage("Registration in progress...");
        String deviceID="";
        String strFname = et_first_name.getText().toString().trim();
        String strLname = et_last_name.getText().toString().trim();
        String strMobile = et_mobile_number.getText().toString().trim();
        String strMail = et_mail.getText().toString().trim();
        String strPwd = et_password.getText().toString().trim();
        if (strPwd.equalsIgnoreCase("")){
            strPwd = "facebook";
        }
        String strDOB = et_b_day.getText().toString().trim();
        restService.getFbRegistrationResponse(strFbId, firebaseRegid, JsonArray, strFname, strLname, strMobile, strDate, strMail, strPwd, new RestCallback<FbRegisterRequest>() {

            @Override
            public void success(FbRegisterRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    sharedPrefEditior = sharedPreferenceUser.edit();
                    sharedPrefEditior.putString(Constants.strShPrefUserId, responce.getData().getCustomerId());
                    sharedPrefEditior.putString(Constants.strShPrefUserFname, responce.getData().getFirstname());
                    sharedPrefEditior.putString(Constants.strShPrefUserLname, responce.getData().getLastname());
                    sharedPrefEditior.putString(Constants.strShPrefUserEmail, responce.getData().getEmail());
                    sharedPrefEditior.putString(Constants.strShPrefUserPhone, responce.getData().getPhone());
                    sharedPrefEditior.commit();
                    mAddToCartTable.deleteAll();
                    displayAlert(responce.getData().getMsg());
                    //Toast.makeText(SignUpActivity.this, "Successfull Registration", Toast.LENGTH_LONG).show();
                } else {
                    utils.displayAlert(responce.getErrorMsg());
                    //Toast.makeText(SignUpActivity.this, responce.getErrorMsg(), Toast.LENGTH_LONG).show();
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

    private boolean validate() {
        if (et_first_name.getText().toString().trim().length() == 0){
            Toast.makeText(SignUpActivity.this, "Please enter First Name", Toast.LENGTH_SHORT).show();
            et_first_name.requestFocus();
            return false;
        }
        else if (et_last_name.getText().toString().trim().length() == 0){
            Toast.makeText(SignUpActivity.this, "Please enter Surname", Toast.LENGTH_SHORT).show();
            et_last_name.requestFocus();
            return false;
        }
        else if (et_mail.getText().toString().trim().length() == 0){
            Toast.makeText(SignUpActivity.this, "Please enter Email Address.", Toast.LENGTH_SHORT).show();
            et_mail.requestFocus();
            return false;
        }
        else if (!utils.isEmailValid(et_mail.getText().toString().trim())){
            Toast.makeText(SignUpActivity.this, "Please provide a valid email address.", Toast.LENGTH_SHORT).show();
            et_mail.requestFocus();
            return false;
        }
        else if (et_mobile_number.getText().toString().trim().length() < 6){
            Toast.makeText(SignUpActivity.this, "Please enter valid mobile number.", Toast.LENGTH_SHORT).show();
            et_mobile_number.requestFocus();
            return false;
        }
        else if (strFbId.equalsIgnoreCase("") && et_password.getText().toString().trim().length() == 0){
            Toast.makeText(SignUpActivity.this, "Please enter password.", Toast.LENGTH_SHORT).show();
            et_password.requestFocus();
            return false;
        }
        else if (et_b_day.getText().toString().trim().length() == 0){
            Toast.makeText(SignUpActivity.this, "Please enter your DOB.", Toast.LENGTH_SHORT).show();
            //et_password.requestFocus();
            return false;
        }
        else if (strFbId.equalsIgnoreCase("") && et_conf_password.getText().toString().trim().length() == 0){
            Toast.makeText(SignUpActivity.this, "Please fill up confirm password.", Toast.LENGTH_SHORT).show();
            et_conf_password.requestFocus();
            return false;
        }
        else if (strFbId.equalsIgnoreCase("") && (!et_conf_password.getText().toString().trim().equalsIgnoreCase(et_password.getText().toString().trim()))){
            Toast.makeText(SignUpActivity.this, "Password & Confirm Password mismatch.", Toast.LENGTH_SHORT).show();
            et_conf_password.requestFocus();
            return false;
        }
        else {

           /* for(int i=0; i<data.size(); i++){
                HashMap<String, String> mapShopList = new HashMap<String, String>();
                mapShopList.put("product_id", data.get(i).get("productid"));
                mapShopList.put("attribute_id", data.get(i).get("productattid"));
                mapShopList.put("option_id", data.get(i).get("productoptionid"));
                mapShopList.put("qty", data.get(i).get("qty"));
                listAddToCartList.add(mapShopList);

            }*/
            return true;
        }
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                strDate = new SimpleDateFormat("yyyy-MM-dd").format(newDate.getTime());
                et_b_day.setText(format.format(newDate.getTime()));
                //Constants.deliveryDateCheckout=dateFormatter.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        //fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 1000  3600  24);

        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR) - 18, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis() - 1000);
        fromDatePickerDialog.show();
    }



    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(SignUpActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        SignUpActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
    public static boolean emailValidator(String mail) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mail);
        return matcher.matches();
    }

    private void customCheckBoxTextView(TextView view) {
        String init = "By clicking on Sign Up above you agree to our ";
        String terms = "Terms and Conditions";
        String and = " and ";
        String privacy = "Privacy Policy";
        String last = ".";

        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                init);
        spanTxt.append(terms);

        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //callTermsCondition("file:///android_asset/Terms_and_Conditions.html", "Terms & Conditions");
                Intent urlPP = new Intent(SignUpActivity.this, KenCommerceWebViewActivity.class);
                urlPP.putExtra("header","Terms and Conditions");
                urlPP.putExtra("url","http://www.swashconvergence.com/KenCommerce.html");
                startActivity(urlPP);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        }, init.length(), init.length() + terms.length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#048BCD")), init.length(), init.length() + terms.length(), 0);
        spanTxt.append(and);
        spanTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), init.length() + terms.length(), init.length() + terms.length() + and.length(), 0);
        spanTxt.append(privacy);
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //callTermsCondition("file:///android_asset/privacy policy.html", "Privacy Policy");
                Intent urlPP = new Intent(SignUpActivity.this, KenCommerceWebViewActivity.class);
                urlPP.putExtra("header","Privacy Policy");
                urlPP.putExtra("url","http://www.swashconvergence.com/KenCommerce.html");
                startActivity(urlPP);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        }, init.length() + terms.length() + and.length(), init.length() + terms.length() + and.length() + privacy.length(), 0);

        spanTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#048BCD")), init.length() + terms.length() + and.length(), init.length() + terms.length() + and.length() + privacy.length(), 0);


        spanTxt.append(last);

        spanTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), init.length() + terms.length() + and.length()+privacy.length(), init.length() + terms.length() + and.length() + privacy.length()+last.length(), 0);

       view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    public void displayAlert(String message)
    {
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intentLogin = new Intent(SignUpActivity.this, PinCodeActivity.class);
                finish();
                startActivity(intentLogin);
            }
        });

        TextView myMsg = new TextView(SignUpActivity.this);
        myMsg.setText(message);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setTextSize(18);
        myMsg.setTypeface(typeFaceSegoeuiReg);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL);
        alertDialogBuilder.setView(myMsg);

        TextView title = new TextView(SignUpActivity.this);
        // You Can Customise your Title here
        title.setVisibility(View.GONE);
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
