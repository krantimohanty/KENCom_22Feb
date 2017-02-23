package com.swash.kencommerce;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AppsFlyerLib;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.ViewAllRatesReviewListAdapter;
import com.swash.kencommerce.model.ViewAllReviewRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

import java.util.ArrayList;

//import com.appsflyer.AppsFlyerLib;

public class ViewAllRatesReviewsListActivity extends AppCompatActivity {

    ExpandableHeightListView lv_rates_reviews_list;
    ImageView imgBack;
    TextView tv_signin;
    private Utils utils;
    private ArrayList<String> arrRatingList;
    RestService restService;
    private ProgressDialog pw;

    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;
    private String customer_id="", product_id="", product_image="", product_name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_all_rates_reviews_list);
        utils=new Utils(ViewAllRatesReviewsListActivity.this);
        restService=new RestService();
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");

        typeFaceSegoeuiBold = Typeface.createFromAsset(getAssets(),
                "ROBOTO-BOLD_0.TTF");

        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            product_image=extras.getString("productImage","");
            product_name=extras.getString("productName","");
            product_id=extras.getString("productId","");
        }

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();

        if(utils.isConnectionPossible()){

            getAllRatesAndReviews("1");
        }else{
            utils.displayAlert("Internet connection is not available, Try again later");
        }


    }

    private void getAllRatesAndReviews(String product_id) {
        pw = new ProgressDialog(ViewAllRatesReviewsListActivity.this);
        pw.show();
        pw.setMessage("Loading... Please wait.");
        restService.viewAllReviews(product_id, new RestCallback<ViewAllReviewRequest>() {
            @Override
            public void success(ViewAllReviewRequest object) {

                if (pw != null)
                    pw.dismiss();
                if (object.getData().getRating().size() > 0) {

                    lv_rates_reviews_list.setAdapter(new ViewAllRatesReviewListAdapter(ViewAllRatesReviewsListActivity.this, object.getData().getRating()));
                    lv_rates_reviews_list.setExpanded(true);
                    lv_rates_reviews_list.setFocusable(false);
                } else {
                    //utils.displayAlert(object.getErrorMsg());
                    getDeliverytype();
                }


            }

            @Override
            public void invalid() {

                if (pw != null)
                    pw.dismiss();

                Toast.makeText(ViewAllRatesReviewsListActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pw != null)
                    pw.dismiss();

                Toast.makeText(ViewAllRatesReviewsListActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }

        });
    }

    private void initFields() {
        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ViewAllRatesReviewsListActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        tv_signin=(TextView)findViewById(R.id.tv_signin);
        //tv_signin.setText("Add Shopping List");
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(ShoppingListActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);*/
            }
        });

        lv_rates_reviews_list=(ExpandableHeightListView)findViewById(R.id.lv_rates_reviews_list);


    }

    private void getDeliverytype() {
        final Dialog dialog = new Dialog(ViewAllRatesReviewsListActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_delivery_type);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView header=(TextView)dialog.findViewById(R.id.header);
        header.setTypeface(typeFaceSegoeuiBold);
        TextView msg=(TextView)dialog.findViewById(R.id.msg);
        msg.setTypeface(typeFaceSegoeuiReg);
        TextView btn_yes=(TextView)dialog.findViewById(R.id.btn_ok);
        TextView btn_no=(TextView)dialog.findViewById(R.id.btn_cancel);
        //header.setText("Error");
        msg.setText("We could not find any reviews for this product.");
        btn_yes.setText("Submit Review");
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Put Grocery selection for landing page
                dialog.dismiss();
                Intent intent = new Intent(ViewAllRatesReviewsListActivity.this, RateReviewActivity.class);
                intent.putExtra("productImage",product_image);
                intent.putExtra("productId",product_id);
                intent.putExtra("productName", product_name);
                startActivity(intent);
                finish();

                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        btn_no.setText("Cancel");
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Put Grocery selection for landing page
                dialog.dismiss();
                Intent intent=new Intent(ViewAllRatesReviewsListActivity.this, ProductDetailsActivity.class);
                startActivity(intent);
                finish();
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
        ViewAllRatesReviewsListActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(ViewAllRatesReviewsListActivity.this);
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
}
