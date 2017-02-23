package com.swash.kencommerce;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.TipCatSpinnerAdapter;

import java.util.ArrayList;
import java.util.Vector;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

//import com.appsflyer.AppsFlyerLib;

/**
 * Created by Kranti on 12/12/2016.
 */

public class AddPaymentActivity extends AppCompatActivity {

    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;
    Utils utils;
    SharedPreferences shPrefDeliverAddr;
    SharedPreferences.Editor toEdit;
    String strName="", strCardNumber="", strExpiryMonth="",strExpiryYear="",strCVV="", forAct="", strNickName="";
    Bundle extras;
    ImageView imgBack, imgCamera;

    //ZenyEditText txt_expiry_year;
    RelativeLayout rel_add, rel_pay_with, rl_saved_card;
    private EditText txt_expiry_month,  txt_cvv, txt_card_number, txt_card_person_name, et_nickname;
    TextView txt_save_card;
    String strText = "";
    private String softketback;
    int count1=0;
    private LinearLayout lin_mm;
    private PopupWindow pw;
    int width=0, height=0;
    private TextView txt_mm, txt_yyyy;
    private LinearLayout lin_yyyy;
    RelativeLayout rel_card_type;
    TextView txt_card_type;
    private String strCardType="";
    private String card_short_type="";

    ScrollView mSrScrollView;
    private int MY_SCAN_REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_payment);
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        utils=new Utils(AddPaymentActivity.this);
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            forAct=extras.getString("for");
        }
        shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);
        strName=shPrefDeliverAddr.getString("card_person_name", "");
        strCardNumber=shPrefDeliverAddr.getString("card_number","");
        strExpiryMonth=shPrefDeliverAddr.getString("card_expiry_month","");
        strExpiryYear=shPrefDeliverAddr.getString("card_expiry_year","");
        strCardType=shPrefDeliverAddr.getString("card_type","");
        card_short_type=shPrefDeliverAddr.getString("card_type_short","");
        strCVV=shPrefDeliverAddr.getString("cvv_cvv","");
        strNickName=shPrefDeliverAddr.getString("nickname","");

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();
    }
    public void onScanPress(View v) {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
        // else handle other activity results
    }
    private void initFields() {
        shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);
        mSrScrollView=(ScrollView)findViewById(R.id.scrollview);
        rel_card_type=(RelativeLayout)findViewById(R.id.rel_card_type);
        rl_saved_card=(RelativeLayout)findViewById(R.id.rl_saved_card);
        rel_card_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(AddPaymentActivity.this);
                callPopUpCardType(rel_card_type);
            }
        });
        txt_card_type=(TextView)findViewById(R.id.txt_card_type);
        txt_card_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(AddPaymentActivity.this);
                callPopUpCardType(rel_card_type);
            }
        });

        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forAct.equalsIgnoreCase("Checkout")) {
                    Intent intent = new Intent(AddPaymentActivity.this, CheckOutActivity.class);
                    finish();
                    startActivity(intent);
                } else  if (forAct.equalsIgnoreCase("CheckoutFinal")) {
                    Intent intent = new Intent(AddPaymentActivity.this, CheckoutFinalActivity.class);
                    finish();
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(AddPaymentActivity.this, CheckoutFinalActivity.class);
                    finish();
                    startActivity(intent);
                }

                AddPaymentActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        imgCamera=(ImageView)findViewById(R.id.img_camera);
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialogCoverage();
            }
        });
        txt_card_number=(EditText)findViewById(R.id.txt_card_number);
        if(!strCardNumber.equalsIgnoreCase("")&&strCardNumber!=null){
            txt_card_number.setText(strCardNumber);
        }

        if(!strCardType.equalsIgnoreCase("")&&strCardType!=null){
            txt_card_type.setText(strCardType);
        }

        /*txt_expiry_year=(ZenyEditText)findViewById(R.id.et_exp_dt);
        if(!strExpiryMonthYear.equalsIgnoreCase("")&&strExpiryMonthYear!=null){
            txt_expiry_year.setText(strExpiryMonthYear);
        }*/

        lin_mm=(LinearLayout)findViewById(R.id.lin_mm);
        lin_mm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(AddPaymentActivity.this);
                callPopUpMonth(lin_mm);

            }
        });

        lin_yyyy=(LinearLayout)findViewById(R.id.lin_yyyy);
        lin_yyyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(AddPaymentActivity.this);
                callPopUpYear(lin_yyyy);
            }
        });

        txt_mm=(TextView)findViewById(R.id.txt_mm);
        if(!strExpiryMonth.equalsIgnoreCase("")&&strExpiryMonth!=null){
            txt_mm.setText(strExpiryMonth);
        }
        txt_yyyy=(TextView)findViewById(R.id.txt_yyyy);

        if(!strExpiryYear.equalsIgnoreCase("")&&strExpiryYear!=null){
            txt_yyyy.setText(strExpiryYear);
        }

        txt_cvv=(EditText)findViewById(R.id.et_cvv);
        if(!strCVV.equalsIgnoreCase("")&&strCVV!=null){
            txt_cvv.setText(strCVV);
        }
        txt_card_person_name=(EditText)findViewById(R.id.et_name);
        if(!strName.equalsIgnoreCase("")&&strName!=null){
            txt_card_person_name.setText(strName);
        }
        et_nickname=(EditText)findViewById(R.id.et_nickname);
        if(!strNickName.equalsIgnoreCase("")&&strNickName!=null){
            et_nickname.setText(strNickName);
        }
        et_nickname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSrScrollView.scrollTo(width, height*2);
                return false;
            }
        });
        txt_save_card=(TextView)findViewById(R.id.txt_save_card);
        txt_save_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_card_person_name.getText().toString().trim().equalsIgnoreCase("")) {
                    utils.displayAlert("Provide person name.");
                } else if (txt_card_number.getText().toString().trim().equalsIgnoreCase("")) {
                    utils.displayAlert("Provide card number.");
                }else if (txt_card_type.getText().toString().trim().equalsIgnoreCase("")) {
                    utils.displayAlert("Provide the card type for this card.");
                } else if (txt_mm.getText().toString().trim().equalsIgnoreCase("")) {
                    utils.displayAlert("Provide a valid card expiry month.");
                } else if (txt_yyyy.getText().toString().trim().equalsIgnoreCase("")) {
                    utils.displayAlert("Provide a valid card expiry year.");
                } else if (txt_cvv.getText().toString().trim().equalsIgnoreCase("")) {
                    utils.displayAlert("Please enter card CVV number.");
                } else if (et_nickname.getText().toString().trim().equalsIgnoreCase("")) {
                    utils.displayAlert("Provide a nickname for this card.");
                } else {
                    toEdit = shPrefDeliverAddr.edit();
                    toEdit.putString("card_person_name", txt_card_person_name.getText().toString());
                    toEdit.putString("card_number", txt_card_number.getText().toString());
                    toEdit.putString("card_expiry_month", txt_mm.getText().toString());
                    toEdit.putString("card_expiry_year", txt_yyyy.getText().toString());
                    toEdit.putString("card_type", txt_card_type.getText().toString());
                    toEdit.putString("card_type_short", card_short_type);
                    toEdit.putString("cvv_cvv", txt_cvv.getText().toString());
                    toEdit.putString("nickname", et_nickname.getText().toString());
                    toEdit.putString("is_card_details_inputed", "Yes");
                    toEdit.commit();

                    if (forAct.equalsIgnoreCase("Checkout")) {
                        Intent intent = new Intent(AddPaymentActivity.this, CheckOutActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(AddPaymentActivity.this, CheckoutFinalActivity.class);
                        finish();
                        startActivity(intent);
                    }

                    AddPaymentActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }

            }
        });
        rl_saved_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialogSavedCard();
            }
        });
    }

    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(AddPaymentActivity.this);
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
    } public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (forAct.equalsIgnoreCase("Checkout")) {
            Intent intent = new Intent(AddPaymentActivity.this, CheckOutActivity.class);
            finish();
            startActivity(intent);
        } else {
            Intent intent = new Intent(AddPaymentActivity.this, CheckoutFinalActivity.class);
            finish();
            startActivity(intent);
        }
        AddPaymentActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
    private void callPopUpCardType(View anchorView) {

        pw = new PopupWindow(callPopUpCardType(R.layout.pop_up_menu, new Vector()),anchorView.getWidth(),height/2, true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchorView);
        pw.update();
    }

    private View callPopUpCardType(int layout, Vector menuItem)
    {
        View view = null;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null, false);
        //String [] values = MyProfileEditListParser.LangNameList.toArray(new String[0]);

        final ArrayList<String> arrItem=new ArrayList<>();

        arrItem.add("Visa");
        arrItem.add("MasterCard");
        arrItem.add("Discover");
        arrItem.add("JCB");
        final ArrayList<String> arrItemShortType=new ArrayList<>();

        arrItemShortType.add("VI");
        arrItemShortType.add("MC");
        arrItemShortType.add("DI");
        arrItemShortType.add("JCB");

        TipCatSpinnerAdapter searchLangAdapter = new TipCatSpinnerAdapter(AddPaymentActivity.this, arrItem);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(Registration1Activity.this,android.R.layout.simple_list_item_1,android.R.id.text1,values);

        ListView listView = (ListView)view.findViewById(R.id.pop_up_menu_list);
        listView.setAdapter(searchLangAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                txt_card_type.setText(arrItem.get(position));
                card_short_type=arrItemShortType.get(position);
                //AtnCareApplicationClass.strPrefLang = MyProfileEditListParser.LangIdList.get(position).toString();
                //AtnCareApplicationClass.strPrefLangName = MyProfileEditListParser.LangNameList.get(position);
                //etPrefLang.setTag("1");
                //etPrefLang.setTextSize(LoginActivity.textSizelogin + 4);
                pw.dismiss();

            }
        });

        return view;
    }
    private void callPopUpMonth(View anchorView) {

        pw = new PopupWindow(dropDownMenuMonth(R.layout.pop_up_menu, new Vector()),anchorView.getWidth(),height/2, true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchorView);
        pw.update();
    }

    private View dropDownMenuMonth(int layout, Vector menuItem)
    {
        View view = null;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null, false);
        //String [] values = MyProfileEditListParser.LangNameList.toArray(new String[0]);

        final ArrayList<String> arrItem=new ArrayList<>();

        arrItem.add("01");
        arrItem.add("02");
        arrItem.add("03");
        arrItem.add("04");
        arrItem.add("05");
        arrItem.add("06");
        arrItem.add("07");
        arrItem.add("08");
        arrItem.add("09");
        arrItem.add("10");
        arrItem.add("11");
        arrItem.add("12");

        TipCatSpinnerAdapter searchLangAdapter = new TipCatSpinnerAdapter(AddPaymentActivity.this, arrItem);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(Registration1Activity.this,android.R.layout.simple_list_item_1,android.R.id.text1,values);

        ListView listView = (ListView)view.findViewById(R.id.pop_up_menu_list);
        listView.setAdapter(searchLangAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                txt_mm.setText(arrItem.get(position));
                //AtnCareApplicationClass.strPrefLang = MyProfileEditListParser.LangIdList.get(position).toString();
                //AtnCareApplicationClass.strPrefLangName = MyProfileEditListParser.LangNameList.get(position);
                //etPrefLang.setTag("1");
                //etPrefLang.setTextSize(LoginActivity.textSizelogin + 4);
                pw.dismiss();

            }
        });

        return view;
    }

    private void callPopUpYear(View anchorView) {

        pw = new PopupWindow(dropDownMenuYear(R.layout.pop_up_menu, new Vector()),anchorView.getWidth(),height/2, true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchorView);
        pw.update();
    }
    private View dropDownMenuYear(int layout, Vector menuItem)
    {
        View view = null;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null, false);
        final ArrayList<String> arrItem=new ArrayList<>();
        arrItem.add("2016");
        arrItem.add("2017");
        arrItem.add("2018");
        arrItem.add("2019");
        arrItem.add("2020");
        arrItem.add("2021");
        arrItem.add("2022");
        arrItem.add("2023");
        arrItem.add("2024");
        arrItem.add("2025");
        arrItem.add("2026");
        arrItem.add("2027");
        arrItem.add("2028");
        arrItem.add("2029");
        arrItem.add("2030");
        TipCatSpinnerAdapter searchLangAdapter = new TipCatSpinnerAdapter(AddPaymentActivity.this, arrItem);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(Registration1Activity.this,android.R.layout.simple_list_item_1,android.R.id.text1,values);
        ListView listView = (ListView)view.findViewById(R.id.pop_up_menu_list);
        listView.setAdapter(searchLangAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                txt_yyyy.setText(arrItem.get(position));
                pw.dismiss();

            }
        });

        return view;
    }
    private void getDialogSavedCard() {
        final Dialog dialog = new Dialog(AddPaymentActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_saved_card);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ListView listview = (ListView) dialog.findViewById(R.id.listview);
        TextView txt_ok = (TextView) dialog.findViewById(R.id.txt_ok);
        TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get the selected value
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(forAct.equalsIgnoreCase("Checkout")){
            Intent intent = new Intent(AddPaymentActivity.this, CheckOutActivity.class);
            finish();
            startActivity(intent);
        }else{
            Intent intent = new Intent(AddPaymentActivity.this, CheckoutFinalActivity.class);
            finish();
            startActivity(intent);
        }

        AddPaymentActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }*/
}
