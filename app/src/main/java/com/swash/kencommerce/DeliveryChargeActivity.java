package com.swash.kencommerce;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.AdapterDeliveryCharge;

public class DeliveryChargeActivity extends Activity {

    SharedPreferences shPrefUserSelection;
    SharedPreferences.Editor toEdit;
    Typeface typeFaceSegoeuiBold;
    ImageView imgBack;
    SharedPreferences shPrefDeliverAddr;
    SharedPreferences.Editor toEditAddr;

    Utils utils;
    ExpandableHeightListView lvTimeSchedule;
    ArrayList<HashMap<String, String>> listDeliveryCharge;
    HashMap<String, String> mapDelCharge = new HashMap<String, String>();
    String delivery_type;
    ImageView img_calendar;
    TextView txt_select_delivery;
    LinearLayout lin_time_slot_parent;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    private TextView ext_delivery;
    Format format, format1;
    private FrameLayout lin_delivery_date;
    ImageView img_schedule;

    ArrayList<String> arrayListDeliverySession;

    ExpandableHeightListView lvSession;
    TextView txt_delivery_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_out_calendar);
        shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);
        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(), "ROBOTO-BOLD_0.TTF");
        listDeliveryCharge = new ArrayList<HashMap<String, String>>();
        utils=new Utils(DeliveryChargeActivity.this);
        Bundle extras=getIntent().getExtras();
        if(extras!=null)
        {
            delivery_type=extras.getString("isasap", "");
        }
        //format= new SimpleDateFormat("EEEE, dd MMMM");
        format= new SimpleDateFormat("EEE, dd MMMM yyyy");

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        //setDateTimeField();

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        arrayListDeliverySession=new ArrayList<>();
        arrayListDeliverySession.add("Morning");
        arrayListDeliverySession.add("Midday");
        arrayListDeliverySession.add("Afternoon");
        arrayListDeliverySession.add("Evening");
        initFields();
    }

    private void initFields() {
        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DeliveryChargeActivity.this, CheckOutActivity.class);
                finish();
                startActivity(intent);
                DeliveryChargeActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        txt_delivery_day=(TextView)findViewById(R.id.txt_delivery_day);
        img_schedule=(ImageView)findViewById(R.id.img_schedule);
        lvSession=(ExpandableHeightListView)findViewById(R.id.lv_schedule_time);
        lvSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*if(ext_delivery.getText().toString().contains("Morning")||ext_delivery.getText().toString().contains("Midday")||
                        ext_delivery.getText().toString().contains("Afternoon")||ext_delivery.getText().toString().contains("Evening")){
                    ext_delivery.setText(ext_delivery.getText().toString().trim());
                    String[] day=ext_delivery.getText().toString().split(",");
                    ext_delivery.setText("");
                    Constants.deliveryDate="";
                    for(int i=0; i<day.length-1; i++){
                        Constants.deliveryDate=Constants.deliveryDate+","+day[i];
                    }
                    Constants.deliveryDate=Constants.deliveryDate.substring(1, Constants.deliveryDate.length());
                    ext_delivery.setText(Constants.deliveryDate);
                }
                Constants.deliverytime=arrayListDeliverySession.get(position);
                ext_delivery.setText(ext_delivery.getText().toString()+", "+arrayListDeliverySession.get(position));
                Constants.deliveryDate=ext_delivery.getText().toString();

                String[] date=Constants.deliveryDate.toString().split(",");
                if(date.length>0){
                    Constants.deliveryDay=date[0].toString();
                }*/
            }
        });
        lin_time_slot_parent=(LinearLayout)findViewById(R.id.lin_time_slot_parent);
        lin_time_slot_parent.setVisibility(View.GONE);
        ext_delivery=(TextView)findViewById(R.id.ext_delivery);
        ext_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField();
            }
        });
        img_calendar=(ImageView)findViewById(R.id.img_calendar);
        img_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField();
            }
        });
        txt_select_delivery=(TextView)findViewById(R.id.txt_select_delivery);
        txt_select_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getDialogCoverage();
                if(ext_delivery.getText().toString().equalsIgnoreCase("")){
                    utils.displayAlert("Please select a delivery date");
                }else{
                    //Tuesday, 17 January 2017
                    if(!ext_delivery.getText().toString().contains("No Rush")){
                        Constants.deliveryDate="No Rush "+ext_delivery.getText().toString();
                    }else{
                        Constants.deliveryDate=ext_delivery.getText().toString();
                    }

                    Intent intent=new Intent(DeliveryChargeActivity.this, CheckOutActivity.class);
                    finish();
                    startActivity(intent);
                    DeliveryChargeActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            }
        });
        ext_delivery=(TextView)findViewById(R.id.ext_delivery);
        if(Constants.deliveryDate!=null &!Constants.deliveryDate.isEmpty()){
            ext_delivery.setText(Constants.deliveryDate);
            img_schedule.setImageResource(R.drawable.checkout_radio_active_blue);
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
        final Dialog dialog = new Dialog(DeliveryChargeActivity.this);
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
        Intent intent=new Intent(DeliveryChargeActivity.this, CheckOutActivity.class);
        finish();
        startActivity(intent);
        DeliveryChargeActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void setDateTimeField() {
        //final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.list_item, arrayListDeliverySession);
        final AdapterDeliveryCharge arrDelSession=new AdapterDeliveryCharge(DeliveryChargeActivity.this, arrayListDeliverySession, ext_delivery);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                ext_delivery.setText(format.format(newDate.getTime()));

                String[] date=ext_delivery.getText().toString().split(",");
                if(date.length>0){
                    Constants.deliveryDay=date[0].toString();
                }
                Constants.deliveryDateCheckout=dateFormatter.format(newDate.getTime());
                img_schedule.setImageResource(R.drawable.checkout_radio_active_blue);
                lin_time_slot_parent.setVisibility(View.VISIBLE);

                txt_delivery_day.setText(Constants.deliveryDate);
                lvSession.setAdapter(arrDelSession);
                lvSession.setExpanded(true);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 1000 * 3600 * 24);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);
        fromDatePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        fromDatePickerDialog.show();
    }

}
