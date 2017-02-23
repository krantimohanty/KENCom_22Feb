package com.swash.kencommerce;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.appsflyer.AppsFlyerLib;

import com.appsflyer.AppsFlyerLib;

import java.util.ArrayList;
import java.util.HashMap;

import com.swash.kencommerce.adapter.AdapterFilterSubMenu;

public class FilterActivity extends AppCompatActivity {
    ImageView img_back, img_more;
    ImageView img_category_plus, img_price_plus, img_availability_plus, img_brand_plus;
    LinearLayout ll_accept, ll_reset;
    RelativeLayout rl_category, rl_price, rl_availability, rl_brand;
    ExpandableHeightListView lv_category, lv_price, lv_availability, lv_brand;

    Boolean isCategory = false, isPrice = false, isAvailability = false, isBrand = false;

    public static ArrayList<HashMap<String, String>> listFilterCategory = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> listFilterPrice = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> listFilterDelivery = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> listFilterBrand = new ArrayList<HashMap<String, String>>();

    public static String Key_filter_submenu = "submenu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_filter);

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initFields();
    }

    private void initFields() {
        img_back = (ImageView) findViewById(R.id.img_back);
        img_more = (ImageView) findViewById(R.id.img_more);

        img_category_plus = (ImageView) findViewById(R.id.img_category_plus);
        img_price_plus = (ImageView) findViewById(R.id.img_price_plus);
        img_availability_plus = (ImageView) findViewById(R.id.img_availability_plus);
        img_brand_plus = (ImageView) findViewById(R.id.img_brand_plus);

        ll_accept = (LinearLayout) findViewById(R.id.ll_accept);
        ll_reset = (LinearLayout) findViewById(R.id.ll_reset);
        rl_category = (RelativeLayout) findViewById(R.id.rl_category);
        rl_price = (RelativeLayout) findViewById(R.id.rl_price);
        rl_availability = (RelativeLayout) findViewById(R.id.rl_availability);
        rl_brand = (RelativeLayout) findViewById(R.id.rl_brand);
        lv_category = (ExpandableHeightListView) findViewById(R.id.lv_category);
        lv_price = (ExpandableHeightListView) findViewById(R.id.lv_price);
        lv_availability = (ExpandableHeightListView) findViewById(R.id.lv_availability);
        lv_brand = (ExpandableHeightListView) findViewById(R.id.lv_brand);

        for (int i = 0; i < 4; i++) {
            HashMap<String, String> mapPastOrdList = new HashMap<String, String>();
            mapPastOrdList.put(Key_filter_submenu, "category");

            listFilterCategory.add(mapPastOrdList);
        }
        lv_category.setAdapter(new AdapterFilterSubMenu(FilterActivity.this, listFilterCategory, "category"));
        lv_category.setExpanded(true);

        for (int i = 0; i < 3; i++) {
            HashMap<String, String> mapPastOrdList = new HashMap<String, String>();
            mapPastOrdList.put(Key_filter_submenu, "price");

            listFilterPrice.add(mapPastOrdList);
        }
        lv_price.setAdapter(new AdapterFilterSubMenu(FilterActivity.this, listFilterPrice, "price"));
        lv_price.setExpanded(true);

        for (int i = 0; i < 3; i++) {
            HashMap<String, String> mapPastOrdList = new HashMap<String, String>();
            mapPastOrdList.put(Key_filter_submenu, "delivery");

            listFilterDelivery.add(mapPastOrdList);
        }
        lv_availability.setAdapter(new AdapterFilterSubMenu(FilterActivity.this, listFilterDelivery, "delivey"));
        lv_availability.setExpanded(true);

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> mapPastOrdList = new HashMap<String, String>();
            mapPastOrdList.put(Key_filter_submenu, "brand");

            listFilterBrand.add(mapPastOrdList);
        }
        lv_brand.setAdapter(new AdapterFilterSubMenu(FilterActivity.this, listFilterBrand, "brand"));
        lv_brand.setExpanded(true);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialogMore();
            }
        });
        ll_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ll_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        rl_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCategory){
                    isCategory = false;
                    img_category_plus.setImageResource(R.drawable.plus_icon);
                    lv_category.setVisibility(View.GONE);
                    //slideToUP(lv_category);
                }
                else {
                    isCategory = true;
                    img_category_plus.setImageResource(R.drawable.minus_icon);
                    //rl_category.setVisibility(View.VISIBLE);
                    slideToBottom(lv_category);
                }
            }
        });
        rl_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPrice){
                    isPrice = false;
                    img_price_plus.setImageResource(R.drawable.plus_icon);
                    lv_price.setVisibility(View.GONE);
                    //slideToUP(lv_price);
                }
                else {
                    isPrice = true;
                    img_price_plus.setImageResource(R.drawable.minus_icon);
                    //rl_price.setVisibility(View.VISIBLE);
                    slideToBottom(lv_price);
                }
            }
        });
        rl_availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAvailability){
                    isAvailability = false;
                    img_availability_plus.setImageResource(R.drawable.plus_icon);
                    lv_availability.setVisibility(View.GONE);
                    //slideToUP(lv_availability);
                }
                else {
                    isAvailability = true;
                    img_availability_plus.setImageResource(R.drawable.minus_icon);
                    //rl_availability.setVisibility(View.VISIBLE);
                    slideToBottom(lv_availability);
                }
            }
        });
        rl_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBrand){
                    isBrand = false;
                    img_brand_plus.setImageResource(R.drawable.plus_icon);
                    lv_brand.setVisibility(View.GONE);
                    //slideToUP(lv_brand);
                }
                else {
                    isBrand = true;
                    img_brand_plus.setImageResource(R.drawable.minus_icon);
                    //rl_brand.setVisibility(View.VISIBLE);
                    slideToBottom(lv_brand);
                }
            }
        });
    }

    private void getDialogMore() {
        final Dialog dialog = new Dialog(FilterActivity.this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP | Gravity.RIGHT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter_menu);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView txt_popularity = (TextView)dialog.findViewById(R.id.txt_popularity);
        TextView txt_h2l = (TextView) dialog.findViewById(R.id.txt_h2l);
        TextView txt_l2h = (TextView) dialog.findViewById(R.id.txt_l2h);
        TextView txt_a2z = (TextView) dialog.findViewById(R.id.txt_a2z);
        TextView txt_z2a = (TextView) dialog.findViewById(R.id.txt_z2a);

        txt_popularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        txt_h2l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        txt_l2h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        txt_a2z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        txt_z2a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    // To animate view slide out from top to bottom
    public void slideToBottom(View view) {
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.down_from_top);
        view.startAnimation(anim);
        view.setVisibility(View.VISIBLE);
    }

    public void slideToUP(final View view) {
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.up_from_down);
        view.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
