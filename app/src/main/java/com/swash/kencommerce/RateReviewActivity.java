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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.appsflyer.AppsFlyerLib;
import com.appsflyer.AppsFlyerLib;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.model.RateReviewRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

public class RateReviewActivity extends AppCompatActivity {

    ProgressBar progress;
    ImageView imgBack;
    TextView tv_signin;
    private Utils utils;
    private ArrayList<String> arrRatingList;
    RestService restService;
    private ProgressDialog pw;
    private ImageView img_product;
    TextView txt_product_name;
    EditText ext_title, ext_details;
    private RatingBar ratingbar;
    private Button btn_submit;
    private String customer_id="", product_id="", product_image="", product_name="";
    SharedPreferences sharedPreferenceUser;
    private Typeface typeFaceSegoeuiReg;
    private Typeface typeFaceSegoeuiBold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_give_rate_review);
        utils=new Utils(RateReviewActivity.this);
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
        sharedPreferenceUser=this.getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
         customer_id= sharedPreferenceUser.getString(Constants.strShPrefUserId, "");

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();

    }



    private void initFields() {
        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                RateReviewActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        progress=(ProgressBar)findViewById(R.id.progress);
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

        img_product=(ImageView)findViewById(R.id.img_product);

        /*Glide.with(RateReviewActivity.this) //Context
                .load(product_image) //URL/FILE
                .into(img_product);
*/

        Glide.with(RateReviewActivity.this) //Context
                .load(product_image) //URL/FILE
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(img_product);

        txt_product_name=(TextView)findViewById(R.id.txt_product_name);
        txt_product_name.setText(product_name);
        ext_title=(EditText)findViewById(R.id.ext_title);
        ext_details=(EditText)findViewById(R.id.ext_rate_details);

        ratingbar=(RatingBar)findViewById(R.id.rating_bar);
        ratingbar.setNumStars(0);
        btn_submit=(Button)findViewById(R.id.btn_done);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customer_id.isEmpty()) {
                    utils.displayAlert("Please SignIn to give Rate & Review.");
                } else if (ratingbar.getRating() < 1) {
                    utils.displayAlert("Please provide your rating.");
                } else if (ext_title.getText().toString().isEmpty()) {
                    utils.displayAlert("Please provide your rating title.");
                } else {
                    setRateReview(product_id, customer_id);
                }

            }
        });


    }
    private void setRateReview(String product_id, String customer_id) {
        pw = new ProgressDialog(RateReviewActivity.this);
        pw.show();
        pw.setMessage("Loading... Please wait.");
        restService.setRateReview(product_id,ext_title.getText().toString(),ext_details.getText().toString(),customer_id, ""+ratingbar.getRating(), new RestCallback<RateReviewRequest>() {
            @Override
            public void success(RateReviewRequest object) {
                if(object.getStatus()==200&object.getSuccess()==1){
                    displayAlert(object.getData().getSuccess());
                }else{
                    utils.displayAlert(object.getErrorMsg());
                }

                if (pw != null)
                    pw.dismiss();


            }

            @Override
            public void invalid() {

                if (pw != null)
                    pw.dismiss();

                Toast.makeText(RateReviewActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pw != null)
                    pw.dismiss();

                Toast.makeText(RateReviewActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        RateReviewActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(RateReviewActivity.this);
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

    public void displayAlert(String message)
    {
        // TODO Auto-generated method stub
        //message="To proceed, sign into your account.";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RateReviewActivity.this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intentLogin = new Intent(RateReviewActivity.this, ProductDetailsActivity.class);
                finish();
                startActivity(intentLogin);
            }
        });
        TextView myMsg = new TextView(RateReviewActivity.this);
        myMsg.setText(message);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setTextSize(16);
        myMsg.setTypeface(typeFaceSegoeuiReg);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL);
        alertDialogBuilder.setView(myMsg);

        TextView title = new TextView(RateReviewActivity.this);
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
