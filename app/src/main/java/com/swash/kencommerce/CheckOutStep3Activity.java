package com.swash.kencommerce;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.retrofit_call.RestService;

/**
 * Created by Kranti on 12/12/2016.
 */

public class CheckOutStep3Activity extends Activity {
    //Button btn_drinks, btn_foods, btn_day, btn_gift;
    ImageView img_drink, img_food, img_day, img_gift;
            //btn_grocery;

    SharedPreferences shPrefUserSelection;
    SharedPreferences.Editor toEdit;

    LinearLayout lin_mm, lin_yyyy;
    TextView txt_mm, txt_yyyy;
    EditText ext_cvv;
    private PopupWindow pw;
    int width=0;
    int height=0;
    ImageView imgBack, imgCOD, imgCardPayment;
    RestService restService;
    TextView txt_pay;
    private ProgressDialog pDialog;
    Utils utils;
    LinearLayout ll_cod, ll_card_payment;
    EditText ext_card_number;
    SharedPreferences shPrefDeliverAddr;
    SharedPreferences.Editor toEditAddr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_out5);
        shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);
        restService=new RestService();
        utils=new Utils(CheckOutStep3Activity.this);
        initFields();
        Constants.paymenttype="cardpayment";
    }

    private void initFields() {

        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CheckOutStep3Activity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        ext_card_number=(EditText)findViewById(R.id.et_card_number);

        ll_cod=(LinearLayout)findViewById(R.id.ll_cod);
        ll_card_payment=(LinearLayout)findViewById(R.id.ll_card_payment);


        imgCOD=(ImageView)findViewById(R.id.img_cod);
        imgCardPayment=(ImageView)findViewById(R.id.img_card_payment);


        lin_mm=(LinearLayout)findViewById(R.id.lin_mm);
        lin_mm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(CheckOutStep3Activity.this);
                //callPopUpMonth(lin_mm);

            }
        });
        lin_yyyy=(LinearLayout)findViewById(R.id.lin_yyyy);
        lin_yyyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(CheckOutStep3Activity.this);
                //callPopUpYear(lin_yyyy);
            }
        });
        txt_mm=(TextView)findViewById(R.id.txt_mm);
        txt_yyyy=(TextView)findViewById(R.id.txt_yyyy);
        ext_cvv=(EditText)findViewById(R.id.ext_cvv);

        txt_pay=(TextView)findViewById(R.id.txt_pay);
        txt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if (Constants.paymenttype.equalsIgnoreCase("cashondelivery")) {
//
//                    getPayment("");
//                } else if (Constants.paymenttype.equalsIgnoreCase("cardpayment")) {
//                    if (ext_card_number.getText().toString().equalsIgnoreCase("")) {
//                        utils.displayAlert("Please enter your card number");
//                    } else if (ext_cvv.getText().toString().equalsIgnoreCase("")) {
//                        utils.displayAlert("Please enter CVV");
//                    } else if (txt_mm.getText().toString().equalsIgnoreCase("")) {
//                        utils.displayAlert("Please select card expiry month");
//                    } else if (txt_yyyy.getText().toString().equalsIgnoreCase("")) {
//                        utils.displayAlert("Please select card expiry year");
//                    }else {
//                        getPayment("1");
//                    }
//                }

            }
        });
        ll_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCOD.setImageDrawable(getResources().getDrawable(R.drawable.active));
                imgCardPayment.setImageDrawable(getResources().getDrawable(R.drawable.normal));
                ext_card_number.setEnabled(false);
                ext_card_number.setClickable(false);
                ext_cvv.setEnabled(false);
                ext_cvv.setClickable(false);
                lin_mm.setEnabled(false);
                lin_mm.setClickable(false);
                lin_yyyy.setEnabled(false);
                lin_yyyy.setClickable(false);
                Constants.paymenttype = "cashondelivery";

            }
        });
        ll_card_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCardPayment.setImageDrawable(getResources().getDrawable(R.drawable.active));
                imgCOD.setImageDrawable(getResources().getDrawable(R.drawable.normal));
                ext_card_number.setEnabled(true);
                ext_card_number.setClickable(true);
                ext_cvv.setEnabled(true);
                ext_cvv.setClickable(true);
                lin_mm.setEnabled(true);
                lin_mm.setClickable(true);
                lin_yyyy.setEnabled(true);
                lin_yyyy.setClickable(true);
                Constants.paymenttype = "cardpayment";
            }
        });

    }
    /*private void getPayment(String id){

        Constants.fName=shPrefDeliverAddr.getString(Constants.fName, "");
        Constants.lName=shPrefDeliverAddr.getString(Constants.lName,"");
        Constants.emailAdd=shPrefDeliverAddr.getString(Constants.emailAdd,"");
        Constants.mobilenum=shPrefDeliverAddr.getString(Constants.mobilenum,"");
        Constants.address=shPrefDeliverAddr.getString(Constants.address,"");
        Constants.latitude=shPrefDeliverAddr.getString(Constants.strShPrefDelLat,"");
        Constants.longitude=shPrefDeliverAddr.getString(Constants.strShPrefDelLong,"");

        pDialog=new ProgressDialog(CheckOutStep3Activity.this);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait");
        restService.getPayment("Cedric", "Ntumba", "cedric@gmail.com", "918013815158", Constants.address, Constants.latitude, Constants.longitude, "Wine1", Constants.paymenttype, new RestCallback<PaymentRequest>() {
            @Override
            public void success(PaymentRequest obj) {
                pDialog.dismiss();

                if(obj.getIsOrderSuccess().toString().equalsIgnoreCase("1")){
                    utils.displayAlert("Thank you, your order has been placed successfully. Your OrderID is "+obj.getOrderId());
                }else
                {
                    utils.displayAlert("Problem while requesting.");
                }
            }

            @Override
            public void invalid() {
                pDialog.dismiss();
                utils.displayAlert("Problem while requesting.");
            }

            @Override
            public void failure() {
                pDialog.dismiss();
                utils.displayAlert("Internet connection is not available, try again.");
            }
        });
    }*/
    private void getDialogAge() {
        final Dialog dialog = new Dialog(CheckOutStep3Activity.this);
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
                dialog.dismiss();
                Intent intentSlider = new Intent(CheckOutStep3Activity.this, PinCodeActivity.class);
                startActivity(intentSlider);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
   /* private void callPopUpMonth(View anchorView) {

        pw = new PopupWindow(dropDownMenuMonth(R.layout.pop_up_menu, new Vector()),anchorView.getWidth(),height/2, true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchorView);
        pw.update();
    }*/
    /*private View dropDownMenuMonth(int layout, Vector menuItem)
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

        DrinkCatSpinnerAdapter searchLangAdapter = new DrinkCatSpinnerAdapter(CheckOutStep3Activity.this, arrItem);
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
    }*/

    /*private void callPopUpYear(View anchorView) {

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
        DrinkCatSpinnerAdapter searchLangAdapter = new DrinkCatSpinnerAdapter(CheckOutStep3Activity.this, arrItem);
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
    }*/
    /////////////////////Hiding soft keyboard/////////////
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        CheckOutStep3Activity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
