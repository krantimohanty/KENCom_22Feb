package com.swash.kencommerce;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.swash.kencommerce.Utils.Constants;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//import com.appsflyer.AppsFlyerLib;

public class SliderActivity extends Activity {

    private ViewPager viewPager;
    private AdapterViewPager adapterViewPager;
    private ArrayList<Integer> listOfItems;

    private LinearLayout dotsLayout;
    private int dotsCount=4;
    private TextView[] dots;
    //int noofsize = 5;
    //    ViewPager myPager = null;
    int count = 0;
    Timer timer;

    // -1 - left, 0 - center, 1 - right
    private int scroll = 0;
    // set only on `onPageSelected` use it in `onPageScrolled`
    // if currentPage < page - we swipe from left to right
    // if currentPage == page - we swipe from right to left  or centered
    private int currentPage = 0;
    // if currentPage < page offset goes from `screen width` to `0`
    // as you reveal right fragment.
    // if currentPage == page , offset goes from `0` to `screen width`
    // as you reveal right fragment
    // You can use it to see
    //if user continue to reveal next fragment or moves it back
    private int currentOffset = 0;
    // behaves similar to offset in range `[0..1)`
    private float currentScale = 0;

    boolean isSwiped=false;

    SharedPreferences shPrefUserSelection;
    SharedPreferences.Editor toEdit;
    String strUserSelectionShopType = "";
    TextView tv_start_shopping;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_slider);

        Window window = getWindow();
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if(currentapiVersion>=19){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if(currentapiVersion>=21){
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initViews();
        setViewPagerItemsWithAdapter();
        setUiPageViewController();
    }

    private void setUiPageViewController() {
        dotsLayout = (LinearLayout)findViewById(R.id.viewPagerCountDots);
        dotsCount = adapterViewPager.getCount();
        dots = new TextView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(Color.parseColor("#ffffff"));
            dotsLayout.addView(dots[i]);
        }

        dots[0].setTextColor(Color.parseColor("#C4D82D"));
    }

    private void setViewPagerItemsWithAdapter() {
        adapterViewPager = new AdapterViewPager(listOfItems);
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(0);
        setTimePage();
//        viewPager.setOnPageChangeListener(viewPagerPageChangeListener);
    }

    private void initViews() {
        shPrefUserSelection = this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
        strUserSelectionShopType = shPrefUserSelection.getString(Constants.strShUserShopSelected, "");
        tv_start_shopping=(TextView)findViewById(R.id.tv_start_shopping);
        tv_start_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SliderActivity.this, LandingActivity.class);
                finish();
                startActivity(intent);
                timer.cancel();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        // getActionBar().hide();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        listOfItems = new ArrayList<Integer>();
        listOfItems.add(R.drawable.info_screen_1);
        listOfItems.add(R.drawable.info_screen_2);
        listOfItems.add(R.drawable.info_screen_3);
        listOfItems.add(R.drawable.info_screen_4);
       /* // Load info screen according to selection
        if (strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopDrink)){
            listOfItems.add(R.drawable.info_1);
            listOfItems.add(R.drawable.info_2);
            listOfItems.add(R.drawable.info_drink_3);
            listOfItems.add(R.drawable.info_4);
            listOfItems.add(R.drawable.info_5);
        }
        else if (strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopGift)){
            listOfItems.add(R.drawable.info_1);
            listOfItems.add(R.drawable.info_2);
            listOfItems.add(R.drawable.info_gift_3);
            listOfItems.add(R.drawable.info_4);
            listOfItems.add(R.drawable.info_5);
        }
        else if (strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopDay)){
            listOfItems.add(R.drawable.info_1);
            listOfItems.add(R.drawable.info_2);
            listOfItems.add(R.drawable.info_day_3);
            listOfItems.add(R.drawable.info_4);
            listOfItems.add(R.drawable.info_5);
        }
        else *//*(strUserSelectionShopType.equalsIgnoreCase(Constants.strShUserShopFood))*//*{
            listOfItems.add(R.drawable.info_1);
            listOfItems.add(R.drawable.info_2);
            listOfItems.add(R.drawable.info_food_3);
            listOfItems.add(R.drawable.info_4);
            listOfItems.add(R.drawable.info_5);
        }*/

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int page, float scale, int offset) {
                if(scale != 0f && offset > 10) {
                    isSwiped=true;
                    if (currentOffset > offset && currentScale > scale && (page + 1) == currentPage) {
                        if (scroll != -1) {
                            //newImageSet = true;
                            scroll = -1;
                            Log.e("scroll", "left");
                            /*if (count<5) {
                                dots[count].setTextColor(Color.parseColor("#ffffff"));
                            }
                            count--;
                            if (count < 5) {
                                dots[count].setTextColor(Color.parseColor("#C4D82D"));
                            }*/
                            if (count==4) {
                                count--;
                                viewPager.setCurrentItem(count);
                                dots[count].setTextColor(Color.parseColor("#C4D82D"));
                                for (int j = 0; j < count; j++) {
                                    dots[j].setTextColor(Color.parseColor("#ffffff"));
                                }
                                // count++;
                            }

                            else if (count>0) {
                                if(count>1)
                                    count=count-2;

                                viewPager.setCurrentItem(count);
                                dots[count+2].setTextColor(Color.WHITE);
                                dots[count].setTextColor(Color.parseColor("#C4D82D"));
                                for (int j = 0; j < 4; j++) {
                                    dots[j].setTextColor(Color.parseColor("#ffffff"));
                                }
                            }

                            /*viewPager.setCurrentItem(count);
                            dots[count].setTextColor(Color.parseColor("#C4D82D"));
                            for (int j = 0; j < count; j++) {
                                dots[j].setTextColor(Color.parseColor("#ffffff"));
                            }*/
                        }
                    } else if (currentOffset < offset && currentScale < scale && page == currentPage) {
                        if (scroll != 1) {
                            //newImageSet = true;
                            scroll = 1;
                            Log.e("scroll", "right");
                            /*if (count < 5) {
                                dots[count].setTextColor(Color.parseColor("#ffffff"));
                            }
                            count++;
                            if (count<5) {
                                dots[count].setTextColor(Color.parseColor("#C4D82D"));
                            }*/

                           /* viewPager.setCurrentItem(count);
                            dots[count].setTextColor(Color.parseColor("#C4D82D"));
                            for (int j = 0; j < count; j++) {
                                dots[j].setTextColor(Color.parseColor("#ffffff"));
                            }
                            */

                            if (count < 4) {
                                viewPager.setCurrentItem(count);
                                dots[count].setTextColor(Color.parseColor("#C4D82D"));
                                for (int j = 0; j < count; j++) {
                                    dots[j].setTextColor(Color.parseColor("#ffffff"));
                                }
                               // count++;
                            }
                            if(count<4)
                                count++;
                            /*for (int j = 0; j < count; j++) {
                                dots[j].setTextColor(Color.parseColor("#ffffff"));
                            }*/
                        }
                    }
                    currentOffset = offset;
                    currentScale = scale;
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("scroll", "centre");
                // we are centerd, reset class variables
                currentPage = position;
                currentScale = 0;
                currentOffset = 0;
                scroll = 0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setTimePage(){
        timer  = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                            if (count < 4) {
                                viewPager.setCurrentItem(count);
                                dots[count].setTextColor(Color.parseColor("#C4D82D"));
                                for (int j = 0; j < count; j++) {
                                    dots[j].setTextColor(Color.parseColor("#ffffff"));
                                }
                                count++;
                            } else {
                                count = 0;
                                Intent intent = new Intent(SliderActivity.this, LandingActivity.class);
                                startActivity(intent);
                                finish();
                                timer.cancel();
                                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            }
                    }
                });
            }
        }, 500, 5000);
    }


    //	adapter
    public class AdapterViewPager extends PagerAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<Integer> items;

        public AdapterViewPager(ArrayList<Integer> listOfItems) {
            this.items = listOfItems;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.listitem_slider, container,false);
            LinearLayout slider = (LinearLayout)view.findViewById(R.id.ll_slider);
            //TextView skip = (TextView)view.findViewById(R.id.tv_skip);
            Button btn_skip = (Button) view.findViewById(R.id.btn_skip);

            //tView.setText(listOfItems.get(position).toString());
            slider.setBackgroundResource(listOfItems.get(position));
            btn_skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SliderActivity.this, LandingActivity.class);
                    finish();
                    startActivity(intent);
                    timer.cancel();
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            });
            ((ViewPager) container).addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View)obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View)object;
            ((ViewPager) container).removeView(view);
        }
    }

}
