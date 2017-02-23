package com.swash.kencommerce;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

//import com.appsflyer.AppsFlyerLib;
import com.appsflyer.AppsFlyerLib;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.DrinkCatSpinnerAdapter;
import com.swash.kencommerce.crashreport.ExceptionHandler;
import com.swash.kencommerce.dbhelper.AddToCartTable;
import com.swash.kencommerce.model.AddToWishListRequest;
import com.swash.kencommerce.model.CartTotalRequest;
import com.swash.kencommerce.model.ProductDetailsRequest;
import com.swash.kencommerce.model.RelatedData;
import com.swash.kencommerce.model.SubscriptionRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

public class ProductDetailsActivity extends AppCompatActivity {

    Utils utils;
    ///////For Add to cart and number of item to add///////////////////
    int prodCount = 1;
    ///////////////Toolbar for making header as material desig///////////////
    Toolbar toolbar;
    ////////////Toolbar top buttonn////////////////////////
    ImageView imgBack, imgSearch,imgFilterIcon, imgWishlist, imgCart;
    /////////////For displaying all review inside scrollable view/////////////////
    //ExpandableHeightListView expandableHeightListView;
    //////////////////ViewPager////////////
    private ViewPager viewPager;
    ImageView imgPrev, imgNext;
    int count = 0;
    Timer timer;
    private int currentOffset = 0;
    // behaves similar to offset in range `[0..1)`
    private float currentScale = 0;

    boolean isSwiped=false;
    // -1 - left, 0 - center, 1 - right
    private int scroll = 0;
    // set only on `onPageSelected` use it in `onPageScrolled`
    // if currentPage < page - we swipe from left to right
    // if currentPage == page - we swipe from right to left  or centered
    private int currentPage = 0;
    private LinearLayout lin_vPager;
    private ArrayList<String> arrName;
    private FeaturedViewPagerAdapter featuredViewPagerAdapter;
    //////////////////ViewPager/////////////////////////////
    //ArrayList<String> arrRatingList;
    TextView tvSubHeaderText;
    private ImageView btn_back;
    TextView tv_signin;
    RatingBar ratingBar;
    ProgressBar pb1, pb2, pb3, pb4, pb5;
    private PopupWindow pw;
    EditText etDrinkItem;
    View vwDrink;
    private Button btnEmailNotify, btnRateReview;
    private Button imgPlus, imgMinus;
    TextView txt_count_cart_add;
    private Button btnAddToCart;
    LinearLayout lin_add_to_shoping_cart;
    RelativeLayout rel_all_details, rel_view_all_reviews;
    private TextView txt_available, txt_out_of_stock;
    private TextView txt_all_details;
    private  String strIsAvailable="", strIsSpecial="";
    RelativeLayout rel_subheader, rel_bottom_bar;
    LinearLayout ll_email;
    EditText et_email_address;
    ScrollView scview_parent;
    private int width=0, height=0;
    ImageView img_all_details_arrow;
    TextView txt_real_price, txt_old_price;
    LinearLayout lin_cross;
    private ImageView imgSpecialTag;
    ImageView img_rate_1, img_rate_2, img_rate_3, img_rate_4, img_rate_5;

    public static ArrayList<HashMap<String, String>> listProductList = new ArrayList<HashMap<String, String>>();
    public static String Key_ProductName = "ProductName";
    public static String Key_ProductPrice = "ProductPrice";
    public static String Key_ProductQuantity = "ProductQnty";
    public static String Key_ProductAttId = "ProductAttId";
    public static String Key_ProductId = "ProductId";
    private ArrayList<Integer> arrCatName;
    String str_cat_type="";
    String strProductName="", strProductPrice="", strProductRealPrice="", strProductQnt="";
    String productImage="";
    ImageView imgProduct;
    TextView txt_product_name, txt_price;
    EditText et_product_qnty;
    private RelativeLayout rel_drink_item;
    private AddToCartTable mAddToCartTable;
    private LinearLayout lin_cart, ll_wish_list;
    private ImageView img_wishlist_icon;

    int i=0;
    private Button btn_count;
    private ProgressDialog pDialog;
    private RestService restService;
    SharedPreferences sharedPreferenceUser;
    SharedPreferences.Editor sharedPrefEditior;
    private String product_id="";
    ArrayList<HashMap<String, String>> arrItem=new ArrayList<>();
    private TextView txt_count_review;
    String shortDesc="", longDesc="";
    TextView txt_rating_avg, txt_rating_review_count;
    String isWishList="";
    String str_option_id="", str_att_id="";
    private String customer_id="";
    private String customer_name="";
    private ImageView img_landing_icon;
    NumberFormat nf;

    ArrayList<HashMap<String, String>> data=new ArrayList<HashMap<String, String>>();
    private double tota1_price=0;
    private int qtycount=0;
    private String prevQty="";
    WebView web_dtl;
    private List<RelatedData> relatedArr;
    String deliveryType="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_details);
        mAddToCartTable=new AddToCartTable(this);
        i=mAddToCartTable.getCount();
        data=mAddToCartTable.getAll();
        utils=new Utils(ProductDetailsActivity.this);
        restService =new RestService();
        sharedPreferenceUser=this.getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
        customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");

        customer_name=sharedPreferenceUser.getString(Constants.strShPrefUserFname,"");
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(ProductDetailsActivity.this));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        initView();

        if(!customer_id.equalsIgnoreCase("")){
            getCartTotal(customer_id);
        }else{
            tota1_price=0.0;
            qtycount=0;
            for(int i=0; i<data.size(); i++){

                qtycount=qtycount+Integer.parseInt(data.get(i).get("qty"));
                btn_count.setText("" + qtycount);

            }
        }


        product_id=sharedPreferenceUser.getString("ProductId", "");
        getProductDetails(product_id);
        setTimePage();

        btnEmailNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utils.isEmailValid(et_email_address.getText().toString().trim())){
                    notifyAvailability(et_email_address.getText().toString().trim(), product_id);
                }
                else {
                    utils.displayAlert("Please Enter Valid Email Address.");
                }
            }
        });


    }
    private void notifyAvailability(String strMail, String product_id) {
        final ProgressDialog pDialog=new ProgressDialog(ProductDetailsActivity.this);
        pDialog.show();
        pDialog.setMessage("Loading...");
        restService.notifyUser(strMail, product_id, new RestCallback<SubscriptionRequest>() {
            @Override
            public void success(SubscriptionRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
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
    private void getProductDetails(String product_id) {

        pDialog=new ProgressDialog(this);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait");
        restService.productDetails(product_id, customer_id, new RestCallback<ProductDetailsRequest>() {
            @Override
            public void success(ProductDetailsRequest object) {

                productImage = object.getData().getProductData().getImage();
                deliveryType=object.getData().getProductData().getProductDelivery();
                txt_product_name.setText(object.getData().getProductData().getName());
                if(object.getData().getProductData().getPrice().contains(",")){
                    String price=object.getData().getProductData().getPrice().replace(",", "");
                    txt_old_price.setText("R" + nf.format(Double.parseDouble(price)+Double.parseDouble(object.getData().getProductData().getProductSize().get(0).getPricingValue())));
                }else{
                    txt_old_price.setText("R" + nf.format(Double.parseDouble(object.getData().getProductData().getPrice())+Double.parseDouble(object.getData().getProductData().getProductSize().get(0).getPricingValue())));
                }

                strProductPrice = object.getData().getProductData().getPrice();
                if (!object.getData().getProductData().getSalePrice().toString().isEmpty()) {

                    if(object.getData().getProductData().getSalePrice().contains(",")){
                        String price=object.getData().getProductData().getSalePrice().replace(",", "");
                        txt_real_price.setText("R" + nf.format(Double.parseDouble(price)+ Double.parseDouble(object.getData().getProductData().getProductSize().get(0).getPricingValue())));
                    }else{
                        txt_real_price.setText("R" + nf.format(Double.parseDouble(object.getData().getProductData().getSalePrice()) + Double.parseDouble(object.getData().getProductData().getProductSize().get(0).getPricingValue())));
                    }

                }
                strProductRealPrice = object.getData().getProductData().getSalePrice();
                //txt_details.setText(object.getData().getProductData().getShortDescription());

                shortDesc = object.getData().getProductData().getShortDescription();
                longDesc = object.getData().getProductData().getDescription();

                if(longDesc.length()>350){
                    shortDesc=longDesc.substring(0, 140);
                }else if(longDesc.length()>250){
                    shortDesc=longDesc.substring(0, 140);
                }else if(longDesc.length()>150){
                    shortDesc=longDesc.substring(0, 140);
                }else if(longDesc.length()>100){
                    shortDesc=longDesc.substring(0, 60);
                }else if(longDesc.length()<100&&longDesc.length()>75){
                    shortDesc=longDesc.substring(0, 60);
                }else{
                    shortDesc=longDesc;
                }
                web_dtl.clearHistory();
                String strDtl = String.valueOf(Html
                        .fromHtml("<![CDATA[<body style=\"text-align:justify;color:#000000; \">"
                                + shortDesc
                                + "</body>]]>"));
                web_dtl.clearCache(true);
                web_dtl.loadDataWithBaseURL(null, strDtl, "text/html", "utf-8", null);
                //web_dtl.loadData(strDtl, "text/html", "utf-8");

                if (object.getData().getProductData().getProductDelivery().toString().toLowerCase().equalsIgnoreCase("asap")) {
                    rel_subheader.setVisibility(View.VISIBLE);
                } else {
                    rel_subheader.setVisibility(View.GONE);
                }

                // Set Rating Image
                String strRating = object.getData().getProductData().getRatings().toString();
                float floatRate = Float.parseFloat(strRating);
                int intRate = (int) floatRate;
                float floatRateFrac = floatRate - intRate;

                if (intRate == 5) {
                    img_rate_5.setImageResource(R.drawable.star_100);
                    img_rate_4.setImageResource(R.drawable.star_100);
                    img_rate_3.setImageResource(R.drawable.star_100);
                    img_rate_2.setImageResource(R.drawable.star_100);
                    img_rate_1.setImageResource(R.drawable.star_100);
                } else if (intRate == 4) {
                    img_rate_5.setImageResource(R.drawable.star_0);
                    img_rate_4.setImageResource(R.drawable.star_100);
                    img_rate_3.setImageResource(R.drawable.star_100);
                    img_rate_2.setImageResource(R.drawable.star_100);
                    img_rate_1.setImageResource(R.drawable.star_100);
                    setFraction(floatRateFrac, img_rate_5);
                } else if (intRate == 3) {
                    img_rate_5.setBackgroundResource(R.drawable.star_0);
                    img_rate_4.setBackgroundResource(R.drawable.star_0);
                    img_rate_3.setBackgroundResource(R.drawable.star_100);
                    img_rate_2.setBackgroundResource(R.drawable.star_100);
                    img_rate_1.setBackgroundResource(R.drawable.star_100);
                    setFraction(floatRateFrac, img_rate_4);
                } else if (intRate == 2) {
                    img_rate_5.setImageResource(R.drawable.star_0);
                    img_rate_4.setImageResource(R.drawable.star_0);
                    img_rate_3.setImageResource(R.drawable.star_0);
                    img_rate_2.setImageResource(R.drawable.star_100);
                    img_rate_1.setImageResource(R.drawable.star_100);
                    setFraction(floatRateFrac, img_rate_3);
                } else if (intRate == 1) {
                    img_rate_5.setImageResource(R.drawable.star_0);
                    img_rate_4.setImageResource(R.drawable.star_0);
                    img_rate_3.setImageResource(R.drawable.star_0);
                    img_rate_2.setImageResource(R.drawable.star_0);
                    img_rate_1.setImageResource(R.drawable.star_100);
                    setFraction(floatRateFrac, img_rate_2);
                } else if (intRate == 0) {
                    img_rate_5.setImageResource(R.drawable.star_0);
                    img_rate_4.setImageResource(R.drawable.star_0);
                    img_rate_3.setImageResource(R.drawable.star_0);
                    img_rate_2.setImageResource(R.drawable.star_0);
                    img_rate_1.setImageResource(R.drawable.star_0);
                    setFraction(floatRateFrac, img_rate_1);
                }

                //ratingBar.setRating(Float.parseFloat(object.getData().getProductData().getRatings().toString()));
                txt_count_review.setText(object.getData().getProductData().getReviewsCount().toString());
                txt_rating_avg.setText(object.getData().getProductData().getRatings().toString());
                txt_rating_review_count.setText(object.getData().getProductData().getReviewsCount().toString() + " Ratings and " + object.getData().getProductData().getReviewsCount().toString() + " Reviews");

                if (object.getData().getProductData().getHasProductInWishlist() == 1) {
                    img_wishlist_icon.setImageResource(R.drawable.wishlisticon_active);
                } else {
                    img_wishlist_icon.setImageResource(R.drawable.details_wishlist_icon);
                }

                isWishList = object.getData().getProductData().getHasProductInWishlist().toString();

                if ((object.getData().getProductData().getIsSalable() == 1)) {

                    txt_out_of_stock.setVisibility(View.GONE);
                    btnEmailNotify.setVisibility(View.GONE);
                    ll_email.setVisibility(View.GONE);
                    rel_bottom_bar.setVisibility(View.VISIBLE);
                    imgSpecialTag.setVisibility(View.INVISIBLE);
                    txt_real_price.setVisibility(View.GONE);
                    lin_cross.setVisibility(View.GONE);
                    txt_old_price.setGravity(Gravity.CENTER);
                } else {
                    rel_subheader.setVisibility(View.GONE);
                    txt_available.setVisibility(View.GONE);
                    ll_email.setVisibility(View.VISIBLE);
                    rel_bottom_bar.setVisibility(View.GONE);
                    imgSpecialTag.setVisibility(View.VISIBLE);
                    imgSpecialTag.setImageResource(R.drawable.outstock);

                }

                if ((object.getData().getProductData().getIsSpecial() == 1)) {
                    strIsSpecial = "1";
                    txt_out_of_stock.setVisibility(View.GONE);
                    btnEmailNotify.setVisibility(View.GONE);
                    ll_email.setVisibility(View.GONE);
                    rel_bottom_bar.setVisibility(View.VISIBLE);
                    imgSpecialTag.setVisibility(View.VISIBLE);
                    txt_real_price.setVisibility(View.VISIBLE);
                    lin_cross.setVisibility(View.VISIBLE);
                }

                if(object.getData().getProductData().getProductSize().get(0).getLabel().equalsIgnoreCase("")){
                    rel_drink_item.setVisibility(View.GONE);
                }else{
                    rel_drink_item.setVisibility(View.VISIBLE);
                }

                /*Glide.with(ProductDetailsActivity.this) //Context
                        .load(object.getData().getProductData().getImage()).centerCrop().placeholder(R.drawable.details_productbig) //URL/FILE
                        .into(imgProduct);*/
                final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
                Glide.with(ProductDetailsActivity.this)
                        .load(object.getData().getProductData().getImage())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .fitCenter()
                        .into(imgProduct);
                featuredViewPagerAdapter = new FeaturedViewPagerAdapter(object.getData().getRelatedData());
                relatedArr=object.getData().getRelatedData();
                viewPager.setAdapter(featuredViewPagerAdapter);

                if (object.getData().getProductData().getProductSize().size() > 0) {
                    arrItem = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i < object.getData().getProductData().getProductSize().size(); i++) {

                        if (i == 0) {
                            etDrinkItem.setText(object.getData().getProductData().getProductSize().get(0).getLabel());
                            str_option_id = object.getData().getProductData().getProductSize().get(0).getOptionId();
                            str_att_id = object.getData().getProductData().getProductSize().get(0).getAttributeId();
                            Double old_price = Double.parseDouble(object.getData().getProductData().getPrice()) + Double.parseDouble(object.getData().getProductData().getProductSize().get(i).getPricingValue());

                            if (!object.getData().getProductData().getSalePrice().toString().isEmpty()) {
                                Double real_price = Double.parseDouble(object.getData().getProductData().getSalePrice()) + Double.parseDouble(object.getData().getProductData().getProductSize().get(i).getPricingValue());
                                txt_real_price.setText("R" + nf.format(real_price));
                            }


                            txt_old_price.setText("R" + nf.format(old_price));

                        }

                        HashMap<String, String> mapShopList = new HashMap<String, String>();
                        mapShopList.put(Key_ProductQuantity, object.getData().getProductData().getProductSize().get(i).getLabel());
                        mapShopList.put(Key_ProductPrice, object.getData().getProductData().getProductSize().get(i).getPricingValue());
                        mapShopList.put(Key_ProductId, object.getData().getProductData().getProductSize().get(i).getOptionId());
                        //mapShopList.put(Key_ProductOptionID, object.getData().getProductData().getProductSize().get(i).getOptionId());
                        mapShopList.put(Key_ProductAttId, object.getData().getProductData().getProductSize().get(i).getAttributeId());
                        arrItem.add(mapShopList);
                    }

                    if (!object.getData().getProductData().getSpecificReviewRating().get1Star().equalsIgnoreCase("")) {

                        float i = Float.parseFloat(object.getData().getProductData().getSpecificReviewRating().get1Star());
                        i = i * 100;
                        if (i == 0.0) {
                            pb1.setProgress(0);
                        } else {
                            pb1.setProgress((int) i);
                        }

                    }
                    if (!object.getData().getProductData().getSpecificReviewRating().get2Star().equalsIgnoreCase("")) {
                        float i = Float.parseFloat(object.getData().getProductData().getSpecificReviewRating().get2Star());
                        i = i * 100;
                        if (i == 0.0) {
                            pb2.setProgress(0);
                        } else {
                            pb2.setProgress((int) i);
                        }

                    }
                    if (!object.getData().getProductData().getSpecificReviewRating().get3Star().equalsIgnoreCase("")) {
                        float i = Float.parseFloat(object.getData().getProductData().getSpecificReviewRating().get3Star());
                        i = i * 100;
                        if (i == 0.0) {
                            pb3.setProgress(0);
                        } else {
                            //pb3.setProgress(Integer.parseInt("" + i));
                            pb3.setProgress((int) i);
                        }

                    }
                    if (!object.getData().getProductData().getSpecificReviewRating().get4Star().equalsIgnoreCase("")) {
                        float i = Float.parseFloat(object.getData().getProductData().getSpecificReviewRating().get4Star());
                        i = i * 100;
                        if (i == 0.0) {
                            pb4.setProgress(0);
                        } else {
                            pb4.setProgress((int) i);
                        }

                    }
                    if (!object.getData().getProductData().getSpecificReviewRating().get5Star().equalsIgnoreCase("")) {
                        float i = Float.parseFloat(object.getData().getProductData().getSpecificReviewRating().get5Star());
                        i = i * 100;
                        if (i == 0.0) {
                            pb5.setProgress(0);
                        } else {
                            pb5.setProgress((int) i);
                        }
                    }
                }

                if (pDialog != null)
                    pDialog.dismiss();
            }

            @Override
            public void invalid() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(ProductDetailsActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(ProductDetailsActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }


    private void addToCart(String product_id, String qnt, String option_id, final String customer_id, String Att_id) {
        pDialog = new ProgressDialog(ProductDetailsActivity.this);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait.");
        restService.addToCart(product_id, txt_count_cart_add.getText().toString(), Att_id, customer_id, option_id, new RestCallback<AddToWishListRequest>() {
            @Override
            public void success(AddToWishListRequest object) {

                if(object.getStatus()==200&&object.getSuccess()==1){
                    getCartTotal(customer_id);
                    utils.displayAlert("Item has been added to cart.");
                }else{
                    utils.displayAlert(object.getErrorMsg());
                }

                if (pDialog != null)
                    pDialog.dismiss();
            }

            @Override
            public void invalid() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(ProductDetailsActivity.this, "Problem while fetching product details", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {
                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(ProductDetailsActivity.this, "Error parsing product details", Toast.LENGTH_LONG).show();
            }

        });
    }

    /////////////////////Initialising all view and control for the activity////////////////
    private void initView() {


        sharedPreferenceUser=getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);

        img_rate_1 = (ImageView)findViewById(R.id.img_rate_1);
        img_rate_2 = (ImageView)findViewById(R.id.img_rate_2);
        img_rate_3 = (ImageView)findViewById(R.id.img_rate_3);
        img_rate_4 = (ImageView)findViewById(R.id.img_rate_4);
        img_rate_5 = (ImageView)findViewById(R.id.img_rate_5);

        ////////////Toolbar for material design and looks as well as for Navigation controller//////////////
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        img_landing_icon=(ImageView)findViewById(R.id.img_landing_icon);
        img_landing_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, LandingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });


        txt_rating_review_count=(TextView)findViewById(R.id.txt_rating_review_count);
        txt_rating_avg=(TextView)findViewById(R.id.txt_rating_avg);


        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, SubCategoryActivity.class);
                startActivity(intent);

                finish();
                ProductDetailsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

       web_dtl = (WebView) findViewById(R.id.web_dtl);
        ///////////////Header Icon and text views//////////////////
        tv_signin=(TextView)findViewById(R.id.tv_signin);
        if(customer_id!=null &!customer_id.equalsIgnoreCase("")){
            tv_signin.setText(customer_name);
        }
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customer_id.equalsIgnoreCase("") && customer_name.equalsIgnoreCase("")) {
                    Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                    intent.putExtra("context_act1", "webskitters.com.stockup.ProductDetailsActivity");
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            }
        });

        imgFilterIcon=(ImageView)findViewById(R.id.img_filter_icon);
        imgFilterIcon.setVisibility(View.VISIBLE);
        imgFilterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, FilterActivity.class);
                startActivity(intent);
                //finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        imgWishlist=(ImageView)findViewById(R.id.img_wishlist_icon);
        imgWishlist.setVisibility(View.VISIBLE);
        imgWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, MyWishListActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        imgCart=(ImageView)findViewById(R.id.img_cart_icon);
        lin_cart=(LinearLayout)findViewById(R.id.lin_cart);
        btn_count=(Button)findViewById(R.id.btn_count);
        btn_count.setText(""+i);
        lin_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(ProductDetailsActivity.this, AddToCartListAllItemsActivity.class);
                startActivity(intent);*/
                Intent intent = new Intent(ProductDetailsActivity.this, AddToCartListAllItemsActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.ProductDetailsActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        txt_count_review=(TextView)findViewById(R.id.txt_count_review);

        ///////////////Header Icon and text views//////////////////
        ///////////////Sub Header textview//////////////////
        tvSubHeaderText=(TextView)findViewById(R.id.txt_subheading);
        ///////////////Sub Header textview//////////////////

        //////////////////////All Review ListView//////////////////
        /*expandableHeightListView=(ExpandableHeightListView)findViewById(R.id.lv_rating);
        expandableHeightListView.setAdapter(new ViewAllRatesReviewListAdapter(ProductDetailsActivity.this, arrRatingList));
        expandableHeightListView.setExpanded(true);
        expandableHeightListView.setFocusable(false);*/
        ////////////////////////All Review ListView/////////////////
        ///////////View Pager and its next previous buttons///////////////////////////
        viewPager = (ViewPager) findViewById(R.id.vPager);
        initViewPager();
        /*imgPrev=(ImageView)findViewById(R.id.img_prev);
        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() > 1)
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                count--;
                timer.cancel();
                setTimePage();
            }
        });*/

        /*imgNext=(ImageView)findViewById(R.id.img_next);
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() < 6)
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                count++;
                timer.cancel();
                setTimePage();
            }
        });*/

        txt_product_name=(TextView)findViewById(R.id.txt_item);
        if(strProductName!=null||!strProductName.equalsIgnoreCase("")){
            txt_product_name.setText(strProductName);
        }
        txt_real_price=(TextView)findViewById(R.id.txt_real_price);
        if(strProductPrice!=null||!strProductPrice.equalsIgnoreCase("")){
            txt_real_price.setText(strProductPrice);
        }
        imgProduct=(ImageView)findViewById(R.id.img_product);
        //imgProduct.setImageResource(productImage);

        /////////////////////hide and show all details textview/////////////////////
        img_all_details_arrow=(ImageView)findViewById(R.id.img_all_details_arrow);
        rel_all_details=(RelativeLayout)findViewById(R.id.rel_all_details);
        rel_all_details.setTag(0);

        rel_all_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(v.getTag().toString()) == 0) {
                    v.setTag(1);
                    txt_all_details.setText("Less Details");
                    //txt_details.setText(longDesc);
                    web_dtl.clearHistory();
                    String strDtl = String.valueOf(Html
                            .fromHtml("<![CDATA[<body style=\"text-align:justify;color:#000000; \">"
                                    + longDesc
                                    + "</body>]]>"));
                    web_dtl.clearCache(true);
                    web_dtl.loadDataWithBaseURL(null, strDtl, "text/html", "utf-8", null);
                    //web_dtl.loadData(strDtl, "text/html", "utf-8");
                    img_all_details_arrow.setImageResource(R.drawable.up_arrow);

                } else {

                    v.setTag(0);
                    txt_all_details.setText("All Details");
                    web_dtl.clearHistory();
                    if(longDesc.length()>350){
                        shortDesc=longDesc.substring(0, 140);
                    }else if(longDesc.length()>250){
                        shortDesc=longDesc.substring(0, 140);
                    }else if(longDesc.length()>150){
                        shortDesc=longDesc.substring(0, 140);
                    }else if(longDesc.length()>100){
                        shortDesc=longDesc.substring(0, 60);
                    }else if(longDesc.length()<100&&longDesc.length()>75){
                        shortDesc=longDesc.substring(0, 60);
                    }else{
                        shortDesc=longDesc;
                    }
                    String strDtl = String.valueOf(Html
                            .fromHtml("<![CDATA[<body style=\"text-align:justify;color:#000000; \">"
                                    + shortDesc
                                    + "</body>]]>"));
                    web_dtl.clearCache(true);
                    web_dtl.loadDataWithBaseURL(null, strDtl, "text/html", "utf-8", null);
                    img_all_details_arrow.setImageResource(R.drawable.down_arrow);
                }
            }
        });
        txt_all_details=(TextView)findViewById(R.id.txt_all_details);
        //txt_details=(TextView)findViewById(R.id.txt_details);
        rel_view_all_reviews=(RelativeLayout)findViewById(R.id.rel_view_all_reviews);
        rel_view_all_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductDetailsActivity.this, ViewAllRatesReviewsListActivity.class);
                intent.putExtra("productImage",productImage);
                intent.putExtra("productId",product_id);
                intent.putExtra("productName",txt_product_name.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        ///////////////////////add to shopping cart ad wishlist view////////////////////
        lin_add_to_shoping_cart=(LinearLayout)findViewById(R.id.lin_add_to_shoping_cart);
        lin_add_to_shoping_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strCustId = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");

                if (!strCustId.equalsIgnoreCase("")){
                    Intent intent=new Intent(ProductDetailsActivity.this, ShoppingListActivity.class);
                    intent.putExtra("productid",product_id);
                    intent.putExtra("qnty",txt_count_cart_add.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }else{
                    displayAlert("To proceed, sign into your account.");

                }
            }
        });

        //////////////////////////Bottom Bar Add to cart ad plus minus view with count view////////////////////
        rel_bottom_bar=(RelativeLayout)findViewById(R.id.rel_bottom_bar);
        btnAddToCart=(Button)findViewById(R.id.btn_add_to_cart);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getDialogDetails();
               /* if(strIsAvailable.equalsIgnoreCase("yes")){
                    mAddToCartTable.insert(strProductName, txt_old_price.getText().toString(), txt_count_cart_add.getText().toString(), 0);
                    //utils.displayAlert("Successfully added to your cart");
                    i=mAddToCartTable.getCount();
                    btn_count.setText(""+i);
                    addToCart(product_id, et_product_qnty.getText().toString(), str_option_id, customer_id, str_att_id);
                }else{
                    mAddToCartTable.insert(strProductName, txt_real_price.getText().toString(), txt_count_cart_add.getText().toString(), 0);
                    utils.displayAlert("Successfully added to your cart");
                    i=mAddToCartTable.getCount();
                    btn_count.setText(""+i);
                    addToCart(product_id, et_product_qnty.getText().toString(), str_option_id, customer_id, str_att_id);
                }*/

                if(!customer_id.equalsIgnoreCase("")){
                    addToCart(product_id, txt_count_cart_add.getText().toString(), str_option_id, customer_id, str_att_id);
                }else{
                    pDialog=new ProgressDialog(ProductDetailsActivity.this);
                    pDialog.setMessage("Loading...");
                    pDialog.show();
                    ArrayList<String> arrProductIDS=new ArrayList<String>();
                    for(int a=0; a<data.size(); a++){

                        arrProductIDS.add(data.get(a).get("productid").toString());
                        if(data.get(a).get("productid").toString().equalsIgnoreCase(product_id)){
                            prevQty=data.get(a).get("qty");
                        }

                    }
                    if(strIsSpecial.equalsIgnoreCase("1")){

                        if(arrProductIDS.contains(product_id)){
                            mAddToCartTable.deleteitem(product_id);
                            int qnt=Integer.parseInt(prevQty)+Integer.parseInt(txt_count_cart_add.getText().toString());
                            mAddToCartTable.insert(product_id,txt_product_name.getText().toString(), ""+qnt,str_option_id, str_att_id, txt_real_price.getText().toString(), deliveryType);
                        }else{
                            mAddToCartTable.insert(product_id,txt_product_name.getText().toString(), txt_count_cart_add.getText().toString(),str_option_id, str_att_id, txt_real_price.getText().toString(), deliveryType);
                        }

                    }
                    else{

                        if(arrProductIDS.contains(product_id)){
                            mAddToCartTable.deleteitem(product_id);
                            int qnt=Integer.parseInt(prevQty)+Integer.parseInt(txt_count_cart_add.getText().toString());
                            mAddToCartTable.insert(product_id,txt_product_name.getText().toString(), ""+qnt,str_option_id, str_att_id, txt_old_price.getText().toString(), deliveryType);
                        }else{
                            mAddToCartTable.insert(product_id,txt_product_name.getText().toString(), txt_count_cart_add.getText().toString(),str_option_id, str_att_id, txt_old_price.getText().toString(), deliveryType);
                        }
                        //mAddToCartTable.insert(product_id,txt_product_name.getText().toString(), txt_count_cart_add.getText().toString(),str_option_id, str_att_id, txt_real_price.getText().toString());
                    }
                    pDialog.dismiss();
                    utils.displayAlert("Item has been added to cart.");
                    data=mAddToCartTable.getAll();
                    qtycount=0;
                        for(int i=0; i<data.size(); i++) {

                            qtycount = qtycount + Integer.parseInt(data.get(i).get("qty"));
                            btn_count.setText("" + qtycount);

                        }
                }
            }
        });
        txt_count_cart_add=(TextView)findViewById(R.id.txt_count_cart_add);

        imgPlus=(Button)findViewById(R.id.img_plus);
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(strIsAvailable.equalsIgnoreCase("yes")){
                    String totalPrice=txt_old_price.getText().toString();
                    totalPrice=totalPrice.substring(1, totalPrice.length());
                    //strProductPrice=strProductPrice.substring(1, strProductPrice.length());
                    Double a=Double.parseDouble(totalPrice);
                    Double b=Double.parseDouble(strProductPrice.substring(1, strProductPrice.length()));

                    Double newprice=a+b;
                    txt_old_price.setText("R"+newprice);
                }else{
                    String totalPrice=txt_real_price.getText().toString();
                    totalPrice=totalPrice.substring(1, totalPrice.length());
                    //strProductPrice=strProductPrice.substring(1, strProductPrice.length());
                    Double a=Double.parseDouble(totalPrice);
                    Double b=Double.parseDouble(strProductPrice.substring(1, strProductPrice.length()));

                    Double newprice=a+b;
                    txt_real_price.setText("R"+newprice);
                }*/

                String strCount = txt_count_cart_add.getText().toString().trim();
                int intCount = Integer.parseInt(strCount);
                intCount++;
                txt_count_cart_add.setText(String.valueOf(intCount));
            }
        });
        imgMinus=(Button)findViewById(R.id.img_minus);
        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strCount = txt_count_cart_add.getText().toString().trim();
                int intCount = Integer.parseInt(strCount);
                if (intCount>1) {
                    intCount--;
                    txt_count_cart_add.setText(String.valueOf(intCount));
                    /*if(strIsAvailable.equalsIgnoreCase("yes")){
                        String totalPrice=txt_old_price.getText().toString();
                        totalPrice=totalPrice.substring(1, totalPrice.length());
                        //strProductPrice=strProductPrice.substring(1, strProductPrice.length());
                        Double a=Double.parseDouble(totalPrice);
                        Double b=Double.parseDouble(strProductPrice.substring(1, strProductPrice.length()));
                        Double newprice=a-b;
                        txt_old_price.setText("R"+newprice);
                    }else{
                        String totalPrice=txt_real_price.getText().toString();
                        totalPrice=totalPrice.substring(1, totalPrice.length());
                        //strProductPrice=strProductPrice.substring(1, strProductPrice.length());
                        Double a=Double.parseDouble(totalPrice);
                        Double b=Double.parseDouble(strProductPrice.substring(1, strProductPrice.length()));

                        Double newprice=a-b;
                        txt_real_price.setText("R"+newprice);
                    }*/
                }
            }
        });

        ll_wish_list=(LinearLayout)findViewById(R.id.ll_wish_list);
        img_wishlist_icon=(ImageView)findViewById(R.id.img_wishl);
        ll_wish_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If Signed in,
                String strCustId = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                if(isWishList.equalsIgnoreCase("1")){
                    utils.displayAlert("Product is already added to your wishlist.");
                }
                else if (!strCustId.equalsIgnoreCase("")){
                    addToWishList(strCustId);
                } else {
                    displayAlert("To proceed, sign into your account.");


                }
                // Guest
                /*if(v.getTag().toString().equalsIgnoreCase("0")){
                    img_wishlist_icon.setImageResource(R.drawable.wishlisticon_active);
                    v.setTag(1);
                }else {
                    img_wishlist_icon.setImageResource(R.drawable.details_wishlist_icon);
                    v.setTag(0);
                }*/

            }
        });
        ///////////////Bottom Bar Add to cart ad plus minus view with count view////////////////////

        ///////////////ScrollView with all view and container////////////////////
        scview_parent=(ScrollView)findViewById(R.id.scroll_parent);
        imgSpecialTag=(ImageView)findViewById(R.id.img_special_tag);
        ll_email=(LinearLayout)findViewById(R.id.ll_email);
        et_email_address=(EditText)findViewById(R.id.et_email);
        btnEmailNotify=(Button)findViewById(R.id.btn_email_notify);

        btnRateReview=(Button)findViewById(R.id.btn_rate_review);
        btnRateReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getDialogCoverage();
                Intent intent=new Intent(ProductDetailsActivity.this, RateReviewActivity.class);
                intent.putExtra("productImage",productImage);
                intent.putExtra("productId",product_id);
                intent.putExtra("productName",txt_product_name.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        rel_subheader = (RelativeLayout)findViewById(R.id.rel_subheader);
        txt_available=(TextView)findViewById(R.id.txt_available);
        txt_out_of_stock=(TextView)findViewById(R.id.txt_out_of_stock);

        lin_cross=(LinearLayout)findViewById(R.id.lin_cross);
        txt_real_price=(TextView)findViewById(R.id.txt_real_price);
        txt_old_price=(TextView)findViewById(R.id.txt_old_price);

        lin_cross.setMinimumWidth(txt_old_price.getWidth());
        ///////////////////////////rating bar view/////////////////
        ratingBar=(RatingBar)findViewById(R.id.rating_bar);

       // ratingBar.setEnabled(false);
        rel_drink_item=(RelativeLayout)findViewById(R.id.rel_drink_item);

        etDrinkItem=(EditText)findViewById(R.id.et_drink_item);
        vwDrink=(View)findViewById(R.id.vw_drink_cat);
        etDrinkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(ProductDetailsActivity.this);
                callPopUpDrinkItems(vwDrink);
            }
        });

        etDrinkItem.setText(strProductQnt);

        if(!strIsAvailable.equalsIgnoreCase("special")){
            txt_old_price.setText(strProductPrice);
        }

        ////////////////////////////Progressbar review view and progress style///////////////////////
        pb1=(ProgressBar)findViewById(R.id.prg1);
        Drawable bckgrndDr = new ColorDrawable(Color.parseColor("#3dae06"));
        Drawable secProgressDr = new ColorDrawable(Color.GRAY);
        Drawable progressDr = new ScaleDrawable(new ColorDrawable(Color.GREEN), Gravity.LEFT, 1, -1);
        LayerDrawable resultDr = new LayerDrawable(new Drawable[] { bckgrndDr, secProgressDr, progressDr });

        resultDr.setId(0, android.R.id.background);
        resultDr.setId(1, android.R.id.secondaryProgress);
        resultDr.setId(2, android.R.id.progress);

        pb1.setProgressDrawable(resultDr);

        Drawable bckgrndDr1 = new ColorDrawable(Color.parseColor("#3dae06"));
        Drawable secProgressDr1 = new ColorDrawable(Color.GRAY);
        Drawable progressDr1 = new ScaleDrawable(new ColorDrawable(Color.GREEN), Gravity.LEFT, 1, -1);
        LayerDrawable resultDr1 = new LayerDrawable(new Drawable[] { bckgrndDr1, secProgressDr1, progressDr1 });

        resultDr1.setId(0, android.R.id.background);
        resultDr1.setId(1, android.R.id.secondaryProgress);
        resultDr1.setId(2, android.R.id.progress);

        pb2=(ProgressBar)findViewById(R.id.prg2);

        Drawable bckgrndDr5 = new ColorDrawable(Color.parseColor("#3dae06"));
        Drawable secProgressDr5 = new ColorDrawable(Color.GRAY);
        Drawable progressDr5 = new ScaleDrawable(new ColorDrawable(Color.GREEN), Gravity.LEFT, 1, -1);
        LayerDrawable resultDr5 = new LayerDrawable(new Drawable[] { bckgrndDr5, secProgressDr5, progressDr5 });

        resultDr5.setId(0, android.R.id.background);
        resultDr5.setId(1, android.R.id.secondaryProgress);
        resultDr5.setId(2, android.R.id.progress);
        pb2.setProgressDrawable(resultDr5);


        pb3=(ProgressBar)findViewById(R.id.prg3);
        pb3.setProgressDrawable(resultDr1);

        Drawable bckgrndDr2 = new ColorDrawable(Color.parseColor("#3dae06"));
        Drawable secProgressDr2 = new ColorDrawable(Color.GRAY);
        Drawable progressDr2 = new ScaleDrawable(new ColorDrawable(Color.GREEN), Gravity.LEFT, 1, -1);
        LayerDrawable resultDr2 = new LayerDrawable(new Drawable[] { bckgrndDr2, secProgressDr2, progressDr2 });

        resultDr2.setId(0, android.R.id.background);
        resultDr2.setId(1, android.R.id.secondaryProgress);
        resultDr2.setId(2, android.R.id.progress);

        Drawable bckgrndDr3 = new ColorDrawable(Color.parseColor("#3dae06"));
        Drawable secProgressDr3 = new ColorDrawable(Color.GRAY);
        Drawable progressDr3 = new ScaleDrawable(new ColorDrawable(Color.GREEN), Gravity.LEFT, 1, -1);
        LayerDrawable resultDr3 = new LayerDrawable(new Drawable[] { bckgrndDr3, secProgressDr3, progressDr3 });

        resultDr3.setId(0, android.R.id.background);
        resultDr3.setId(1, android.R.id.secondaryProgress);
        resultDr3.setId(2, android.R.id.progress);

        pb4=(ProgressBar)findViewById(R.id.prg4);
        pb4.setProgress(45);
        pb4.setProgressDrawable(resultDr2);
        pb5=(ProgressBar)findViewById(R.id.prg5);
        pb5.setProgress(70);
        pb5.setProgressDrawable(resultDr3);
        ////////////////////////////Progressbar review view and progress style///////////////////////

    }

    private void addToWishList(String strCustId) {
        final ProgressDialog pDialog=new ProgressDialog(ProductDetailsActivity.this);
        pDialog.show();
        pDialog.setMessage("Adding to wish list..");
        restService.addToWish(strCustId, product_id, new RestCallback<AddToWishListRequest>() {

            @Override
            public void success(AddToWishListRequest responce) {
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    //getDialogOK(responce.getData().getSuccessMsg());
                    utils.displayAlert(responce.getData().getSuccessMsg());
                    img_wishlist_icon.setImageResource(R.drawable.wishlisticon_active);
                    //Toast.makeText(LoginActivity.this, responce.getData().getSuccessMsg() , Toast.LENGTH_LONG).show();
                } else {
                    //getDialogOK(responce.getErrorMsg());
                    utils.displayAlert(responce.getErrorMsg());
                    //Toast.makeText(LoginActivity.this, responce.getErrorMsg(), Toast.LENGTH_LONG).show();
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

    /////////////////////Auto slide timer for featured products auto slide views/////////////
    public void setTimePage(){
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (count == listProductList.size()) {
                    count = 0;
                }
                viewPager.setCurrentItem(count++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 2000);
    }
    /////////////////////Hiding soft keyboard/////////////
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    //////////////////Dropdown for Quantity/////////////////////////////////
    private void callPopUpDrinkItems(View anchorView, TextView txt_qnty, int pos, TextView txt_old_price, TextView txt_real_price) {

        pw = new PopupWindow(dropDownMenuDrinkItems(R.layout.pop_up_menu, new Vector(), txt_qnty),anchorView.getWidth(),height/3, true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchorView);
        pw.update();
    }


    private View dropDownMenuDrinkItems(int layout, Vector menuItem, final TextView txt_qnt)
    {
        View view = null;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null, false);
        /*final ArrayList<String> arrItem=new ArrayList<>();
        if(str_cat_type.equalsIgnoreCase("Drinks")) {
            arrItem.add("250ml Bottle");
            arrItem.add("750ml Bottle");
            arrItem.add("1ltr Bottle");
        }else if(str_cat_type.equalsIgnoreCase("Foods")||str_cat_type.equalsIgnoreCase("Day2Day")){
            arrItem.add("Small");
            arrItem.add("Medium");
            arrItem.add("Large");
        }else{
            arrItem.add("2lb");
            arrItem.add("4lb");
            arrItem.add("8lb");
        }*/
        DrinkCatSpinnerAdapter searchLangAdapter = new DrinkCatSpinnerAdapter(ProductDetailsActivity.this, arrItem);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(Registration1Activity.this,android.R.layout.simple_list_item_1,android.R.id.text1,values);

        ListView listView = (ListView)view.findViewById(R.id.pop_up_menu_list);
        listView.setAdapter(searchLangAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                txt_qnt.setText(arrItem.get(position).get(Key_ProductQuantity).toString());
                pw.dismiss();
            }
        });

        return view;
    }
    //////////////////Dropdown for Quantity/////////////////////////////////
    private void callPopUpDrinkItems(View anchorView) {

        pw = new PopupWindow(dropDownMenu(R.layout.pop_up_menu, new Vector()),anchorView.getWidth(),height/3, true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchorView);
        pw.update();
    }


    private View dropDownMenu(int layout, Vector menuItem)
    {
        View view = null;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null, false);


        DrinkCatSpinnerAdapter searchLangAdapter = new DrinkCatSpinnerAdapter(ProductDetailsActivity.this, arrItem);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(Registration1Activity.this,android.R.layout.simple_list_item_1,android.R.id.text1,values);

        ListView listView = (ListView)view.findViewById(R.id.pop_up_menu_list);
        listView.setAdapter(searchLangAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                etDrinkItem.setText(arrItem.get(position).get(Key_ProductQuantity).toString());
                str_option_id=arrItem.get(position).get(Key_ProductId).toString();
                str_att_id=arrItem.get(position).get(Key_ProductAttId).toString();
                Double old_price=Double.parseDouble(strProductPrice)+Double.parseDouble(arrItem.get(position).get(Key_ProductPrice).toString());
                if(!strProductRealPrice.toString().isEmpty()){
                    Double real_price=Double.parseDouble(strProductRealPrice)+Double.parseDouble(arrItem.get(position).get(Key_ProductPrice).toString());
                    txt_real_price.setText("R"+nf.format(real_price));
                    }

                txt_old_price.setText("R"+nf.format(old_price));

                pw.dismiss();
            }
        });

        return view;
    }

    public class FeaturedViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        List<RelatedData> relatedArr;

        public FeaturedViewPagerAdapter(List<RelatedData> relatedArr) {
            this.relatedArr = relatedArr;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // references to our images

            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.addview_feature_products_cat_item_black_border, container,false);
            //view.setBackgroundColor(Color.GREEN);

            final ImageView img_main = (ImageView) view.findViewById(R.id.img_cat);

            final ImageView img_special = (ImageView) view.findViewById(R.id.img_special);
            final ImageView img_on_demand = (ImageView) view.findViewById(R.id.img_on_demand);
            TextView tvCatName = (TextView) view.findViewById(R.id.tv_cat_name);
            TextView tvCatPrice = (TextView) view.findViewById(R.id.tv_price);
            TextView tvCatQnty = (TextView) view.findViewById(R.id.tv_cat_qnt);
            final ImageView img_plus = (ImageView) view.findViewById(R.id.img_plus);
            final ImageView img_wish = (ImageView) view.findViewById(R.id.img_wish);


            img_main.setId(position);
            img_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int i=v.getId();

                    if(relatedArr.get(i).getProductIsSalable()==1){

                        Intent intent=new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                        /*intent.putExtra("isAvailable", "yes");
                        intent.putExtra("cat_type", str_cat_type);
                        intent.putExtra("Product_Name", listProductList.get(i).get(Key_ProductName));
                        intent.putExtra("Product_Price", listProductList.get(i).get(Key_ProductPrice));
                        intent.putExtra("Product_Quantity", listProductList.get(i).get(Key_ProductQuantity));
                        intent.putExtra("Product_Image", arrCatName.get(i));*/
                        sharedPrefEditior=sharedPreferenceUser.edit();
                        sharedPrefEditior.putString("ProductId", relatedArr.get(i).getProductId());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        finish();
                    }else{
                        Intent intent=new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                        /*intent.putExtra("isAvailable", "no");
                        intent.putExtra("cat_type", str_cat_type);
                        intent.putExtra("Product_Name", listProductList.get(i).get(SubCategoryActivity.Key_ProductName));
                        intent.putExtra("Product_Price", listProductList.get(i).get(SubCategoryActivity.Key_ProductPrice));
                        intent.putExtra("Product_Quantity", listProductList.get(i).get(SubCategoryActivity.Key_ProductQuantity));
                        intent.putExtra("Product_Image", arrCatName.get(i));*/
                        sharedPrefEditior=sharedPreferenceUser.edit();
                        sharedPrefEditior.putString("ProductId", relatedArr.get(i).getProductId());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        finish();

                    }
                    if(relatedArr.get(i).getProductIsSpecial().toString().equalsIgnoreCase("1")){

                        Intent intent=new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                        /*intent.putExtra("isAvailable", "special");
                        intent.putExtra("cat_type", str_cat_type);
                        intent.putExtra("Product_Name", listProductList.get(i).get(Key_ProductName));
                        intent.putExtra("Product_Price", listProductList.get(i).get(Key_ProductPrice));
                        intent.putExtra("Product_Quantity", listProductList.get(i).get(Key_ProductQuantity));
                        intent.putExtra("Product_Image", arrCatName.get(i));*/
                        sharedPrefEditior=sharedPreferenceUser.edit();
                        sharedPrefEditior.putString("ProductId", relatedArr.get(i).getProductId());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        finish();
                    }
                }
            });

            img_plus.setTag(position);
            img_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=Integer.parseInt(v.getTag().toString());
                    String available="";
                    if(relatedArr.get(pos).getProductIsSalable()==1){
                        available="yes";
                    }else{
                        available="no";
                    }
                    getDialogDetails(pos, available);
                }
            });

            img_wish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //getDialogDetails();
                }
            });


            tvCatName.setText(relatedArr.get(position).getProductName().toString());
            tvCatPrice.setText(relatedArr.get(position).getProductPrice());
            //tvCatQnty.setText(mapShopList.get(Key_ProductQuantity));
            Glide.with(ProductDetailsActivity.this) //Context
                    .load(relatedArr.get(position).getProductImgUrl().toString()) //URL/FILE
                    .into(img_main);
            //img_main.setImageResource(arrCatName.get(position));
            /*if(position%3==0){

                img_special.setVisibility(View.INVISIBLE);
                img_on_demand.setVisibility(View.VISIBLE);
                img_on_demand.setImageResource(R.drawable.outstock);

            }else*/
            /*if(position%3==1){
                img_special.setVisibility(View.INVISIBLE);
                img_on_demand.setVisibility(View.INVISIBLE);
            }*/

            if(relatedArr.get(position).getProductIsSpecial()==1)
            {
                img_special.setVisibility(View.VISIBLE);
            }
            else
            {
                img_special.setVisibility(View.INVISIBLE);
            }

            if(relatedArr.get(position).getProductIsSalable()==0){
                img_on_demand.setVisibility(View.VISIBLE);
                img_on_demand.setImageResource(R.drawable.outstock);
            }
            ((ViewPager) container).addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return relatedArr.size();
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
        @Override
        public float getPageWidth(int position) {
            return (super.getPageWidth(position) / 2);
        }
    }

    private void initViewPager(){
        /*if(str_cat_type.equalsIgnoreCase("Drinks")){
            arrCatName=new ArrayList<Integer>();
            arrCatName.add(R.drawable.details_productbig);
            arrCatName.add(R.drawable.details_product_wine_2);
            arrCatName.add(R.drawable.details_product_wine_3);
            listProductList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapShopList = new HashMap<String, String>();
            mapShopList.put(Key_ProductName, "Wine1");
            mapShopList.put(Key_ProductPrice, "R356.00");
            mapShopList.put(Key_ProductQuantity, "250ml");
            listProductList.add(mapShopList);
            HashMap<String, String> mapShopList2 = new HashMap<String, String>();
            mapShopList2.put(Key_ProductName, "Wine2");
            mapShopList2.put(Key_ProductPrice, "R467.00");
            mapShopList2.put(Key_ProductQuantity, "750ml");
            listProductList.add(mapShopList2);
            HashMap<String, String> mapShopList3 = new HashMap<String, String>();
            mapShopList3.put(Key_ProductName, "Wine3");
            mapShopList3.put(Key_ProductPrice, "R471.00");
            mapShopList3.put(Key_ProductQuantity, "1ltr");
            listProductList.add(mapShopList3);
        }else if(str_cat_type.equalsIgnoreCase("Day2Day")){
            arrCatName=new ArrayList<Integer>();
            arrCatName.add(R.drawable.details_product_day_1);
            arrCatName.add(R.drawable.details_product_day_2);
            arrCatName.add(R.drawable.details_product_day_3);
            listProductList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapShopList = new HashMap<String, String>();
            mapShopList.put(Key_ProductName, "Day_to_day1");
            mapShopList.put(Key_ProductPrice, "R356.00");
            mapShopList.put(Key_ProductQuantity, "Small");
            listProductList.add(mapShopList);
            HashMap<String, String> mapShopList2 = new HashMap<String, String>();
            mapShopList2.put(Key_ProductName, "Day_to_day2");
            mapShopList2.put(Key_ProductPrice, "R467.00");
            mapShopList2.put(Key_ProductQuantity, "Medium");
            listProductList.add(mapShopList2);
            HashMap<String, String> mapShopList3 = new HashMap<String, String>();
            mapShopList3.put(Key_ProductName, "Day_to_day3");
            mapShopList3.put(Key_ProductPrice, "R471.00");
            mapShopList3.put(Key_ProductQuantity, "Large");
            listProductList.add(mapShopList3);
        }else if(str_cat_type.equalsIgnoreCase("Foods")){
            arrCatName=new ArrayList<Integer>();
            arrCatName.add(R.drawable.details_product_pizza);
            arrCatName.add(R.drawable.details_product_sweet);
            listProductList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapShopList = new HashMap<String, String>();
            mapShopList.put(Key_ProductName, "Food1");
            mapShopList.put(Key_ProductPrice, "R356.00");
            mapShopList.put(Key_ProductQuantity, "Small");
            listProductList.add(mapShopList);
            HashMap<String, String> mapShopList2 = new HashMap<String, String>();
            mapShopList2.put(Key_ProductName, "Food2");
            mapShopList2.put(Key_ProductPrice, "R467.00");
            mapShopList2.put(Key_ProductQuantity, "Medium");
            listProductList.add(mapShopList2);

        }else if(str_cat_type.equalsIgnoreCase("Gifts")){
            arrCatName=new ArrayList<Integer>();
            arrCatName.add(R.drawable.details_product_gift_1);
            arrCatName.add(R.drawable.details_product_gift_2);
            listProductList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapShopList = new HashMap<String, String>();
            mapShopList.put(Key_ProductName, "Gift1");
            mapShopList.put(Key_ProductPrice, "R356.00");
            mapShopList.put(Key_ProductQuantity, "2lb");
            listProductList.add(mapShopList);
            HashMap<String, String> mapShopList2 = new HashMap<String, String>();
            mapShopList2.put(Key_ProductName, "Gift2");
            mapShopList2.put(Key_ProductPrice, "R467.00");
            mapShopList2.put(Key_ProductQuantity, "4lb");
            listProductList.add(mapShopList2);
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

                            /*if (count==6) {
                                count--;
                                viewPager.setCurrentItem(count);

                                // count++;
                            }

                            else if (count>0) {
                                if(count>1)
                                    count=count-2;

                                viewPager.setCurrentItem(count);
                                timer.cancel();
                                setTimePage();
                            }*/
                            if (count == listProductList.size()) {
                                count = 0;
                            }
                            viewPager.setCurrentItem(count--, true);
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


                            /*if (count < 6) {
                                viewPager.setCurrentItem(count);
                                count++;
                                timer.cancel();
                                setTimePage();
                                // count++;
                            }*/
                            if (count == listProductList.size()) {
                                count = 0;
                            }
                            viewPager.setCurrentItem(count++, true);
                        }
                    }
                    currentOffset = offset;
                    currentScale = scale;
                }
            }


            @Override
            public void onPageSelected(int position) {
                // we are centerd, reset class variables
                count = position;
                currentScale = 0;
                currentOffset = 0;
                scroll = 0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(ProductDetailsActivity.this);
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
    /*private void getDialogDetails() {
        final Dialog dialog = new Dialog(ProductDetailsActivity.this);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_product_add_cart);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        //dialog.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) (SubCategoryActivity.width * 0.99);
        int height = (int) (SubCategoryActivity.height * 0.9);
        dialog.getWindow().setLayout(width, height);
        ImageView btn_plus = (ImageView) dialog.findViewById(R.id.img_plus);
        ImageView btn_minus = (ImageView) dialog.findViewById(R.id.img_minus);
        ImageView imgCross = (ImageView) dialog.findViewById(R.id.img_cross);
        Button btnAddToCart=(Button)dialog.findViewById(R.id.btn_add_to_cart);

        final TextView txt_count_cart_add = (TextView) dialog.findViewById(R.id.txt_count_cart_add);
        final int prodCount = 1;

        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null)
                    dialog.dismiss();
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null)
                    dialog.dismiss();
            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCount = txt_count_cart_add.getText().toString().trim();
                int intCount = Integer.parseInt(strCount);
                intCount++;
                txt_count_cart_add.setText(String.valueOf(intCount));
            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCount = txt_count_cart_add.getText().toString().trim();
                int intCount = Integer.parseInt(strCount);
                if (intCount > 0) {
                    intCount--;
                    txt_count_cart_add.setText(String.valueOf(intCount));
                }
            }
        });
       /*//* wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        wmlp.x = 200;   //x position
        wmlp.y = 200;   //y position*//**//*
        dialog.show();
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProductDetailsActivity.this, SubCategoryActivity.class);
        startActivity(intent);

        finish();
        ProductDetailsActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void getDialogDetails(final int pos, final String available) {
        final Dialog dialog = new Dialog(ProductDetailsActivity.this);
        mAddToCartTable=new AddToCartTable(ProductDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product_add_cart);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        int width = (int) (SubCategoryActivity.width * 0.99);
        int height = (int) (SubCategoryActivity.height * 0.9);
        dialog.getWindow().setLayout(width, height);

        RelativeLayout rel_qnt=(RelativeLayout)dialog.findViewById(R.id.rel_qnt);

        ImageView img_special=(ImageView)dialog.findViewById(R.id.img_special);
        LinearLayout lin_cross=(LinearLayout)dialog.findViewById(R.id.lin_cross);
        Button btn_plus = (Button) dialog.findViewById(R.id.img_plus);
        Button btn_minus = (Button) dialog.findViewById(R.id.img_minus);
        ImageView imgCross = (ImageView) dialog.findViewById(R.id.img_cross);
        Button btnAddToCart=(Button)dialog.findViewById(R.id.btn_add_to_cart);
        btnAddToCart.setTag(pos);

        TextView txt_product_name=(TextView)dialog.findViewById(R.id.txt_item);
        final TextView txt_real_price=(TextView)dialog.findViewById(R.id.txt_real_price);
        final TextView txt_old_price=(TextView)dialog.findViewById(R.id.txt_old_price);
        final TextView txt_qnty=(TextView)dialog.findViewById(R.id.txt_qnt);
        ImageView img_product=(ImageView)dialog.findViewById(R.id.img_product);

        txt_product_name.setText(relatedArr.get(pos).getProductName());
        Double price=Double.parseDouble(relatedArr.get(pos).getProductPrice().toString())+Double.parseDouble(relatedArr.get(pos).getProductSize().get(0).getPricingValue());
        txt_old_price.setText("R" + nf.format(price));
        if(!relatedArr.get(pos).getProductSpecialPrice().toString().equalsIgnoreCase("")){
            Double realprice=Double.parseDouble(relatedArr.get(pos).getProductSpecialPrice().toString())+Double.parseDouble(relatedArr.get(pos).getProductSize().get(0).getPricingValue());
            txt_real_price.setText("R"+nf.format(realprice));

        }
        //Double realprice=Double.parseDouble(str_real_price)+Double.parseDouble(data.get(pos).getProductSize().get(0).getPricingValue());
        //txt_real_price.setText("R"+nf.format(realprice));

        txt_qnty.setText(relatedArr.get(pos).getProductSize().get(0).getLabel().toString());

        if(relatedArr.get(pos).getProductSize().get(0).getLabel().toString().equalsIgnoreCase("")){
            rel_qnt.setVisibility(View.GONE);
        }else{
            rel_qnt.setVisibility(View.VISIBLE);
        }


        if(relatedArr.get(pos).getProductIsSalable()==1){
            //txt_old_price.setText(data.get(pos).getProductPrice());
            txt_old_price.setGravity(Gravity.CENTER);
            txt_real_price.setVisibility(View.GONE);
            lin_cross.setVisibility(View.GONE);
        }else
        {
            // txt_real_price.setText(data.get(pos).getProductPrice());

        }

        if(relatedArr.get(pos).getProductIsSpecial()==0){
            //txt_old_price.setText(data.get(pos).getProductPrice());
            txt_old_price.setGravity(Gravity.CENTER);
            txt_real_price.setVisibility(View.INVISIBLE);
            img_special.setVisibility(View.INVISIBLE);
            lin_cross.setVisibility(View.INVISIBLE);
        }else{
            //txt_old_price.setText(data.get(pos).getProductPrice());
            txt_real_price.setVisibility(View.VISIBLE);
            img_special.setVisibility(View.VISIBLE);
            lin_cross.setVisibility(View.VISIBLE);
        }

       /* Glide.with(mContext) //Context
                .load(data.get(pos).getProductImgUrl()).centerCrop().placeholder(R.drawable.champagne) //URL/FILE
                .into(img_product);*/
        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress);
        Glide.with(ProductDetailsActivity.this)
                .load(relatedArr.get(pos).getProductImgUrl())
//                .fitCenter()
//                .placeholder(R.drawable.champagne)
//                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //if (holder.progressBar.isShown())
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(img_product);
        /*String myString=data.get(pos).getProductPrice();
        if (myString != null && !myString.isEmpty()) {
            // doSomething
            txt_qnty.setText(data.get(pos).getProductSize().toString());
        }else{
            txt_qnty.setText("");
        }*/
        //img_product.setImageResource(arr_product_image.get(pos));

        RelativeLayout rel_add_to_shoping_list=(RelativeLayout)dialog.findViewById(R.id.rel_add_to_shoping_list);
        rel_add_to_shoping_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strCustId = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                if(!strCustId.equalsIgnoreCase("")) {
                    Intent intent = new Intent(ProductDetailsActivity.this, ShoppingListActivity.class);
                    intent.putExtra("productid", relatedArr.get(pos).getProductId());
                    intent.putExtra("qnty", txt_qnty.getText().toString());
                    startActivity(intent);

                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                else{
                    displayAlert("");
                }
            }
        });

        final TextView txt_count_cart_add = (TextView) dialog.findViewById(R.id.txt_count_cart_add);
        final int prodCount = 1;

        rel_qnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callPopUpDrinkItems(txt_qnty, txt_qnty, pos, txt_old_price, txt_real_price);
            }
        });

        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null)
                    dialog.dismiss();
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strCustId = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                if(!strCustId.equalsIgnoreCase("")){
                    addToCart(relatedArr.get(pos).getProductId().toString(), txt_count_cart_add.getText().toString(), relatedArr.get(pos).getProductSize().get(0).getOptionId(), strCustId, relatedArr.get(pos).getProductSize().get(0).getAttributeId());
                }else{
                    pDialog=new ProgressDialog(ProductDetailsActivity.this);
                    pDialog.setMessage("Loading...");
                    pDialog.show();
                    ArrayList<String> arrProductIDS=new ArrayList<String>();
                    for(int a=0; a<data.size(); a++){

                        arrProductIDS.add(data.get(a).get("productid").toString());
                        if(data.get(a).get("productid").toString().equalsIgnoreCase(relatedArr.get(pos).getProductId())){
                            prevQty=data.get(a).get("qty").toString();
                        }
                    }
                    String deltype="";
                    if(relatedArr.get(pos).getProductIsSpecial()==1){
                        deltype=relatedArr.get(pos).getProductDelivery();
                        Double price=Double.parseDouble(relatedArr.get(pos).getProductSpecialPrice().toString())+Double.parseDouble(relatedArr.get(pos).getProductSize().get(0).getPricingValue().toString());
                        if(arrProductIDS.contains(relatedArr.get(pos).getProductId())) {
                            mAddToCartTable.deleteitem(relatedArr.get(pos).getProductId());

                            int qnt = Integer.parseInt(prevQty) + Integer.parseInt(txt_count_cart_add.getText().toString());

                            mAddToCartTable.insert(relatedArr.get(pos).getProductId(), relatedArr.get(pos).getProductName(), ""+qnt,relatedArr.get(pos).getProductSize().get(0).getOptionId(), relatedArr.get(pos).getProductSize().get(0).getAttributeId(), price.toString(), deltype);
                        }else{
                            mAddToCartTable.insert(relatedArr.get(pos).getProductId(), relatedArr.get(pos).getProductName(),  txt_count_cart_add.getText().toString(),relatedArr.get(pos).getProductSize().get(0).getOptionId(), relatedArr.get(pos).getProductSize().get(0).getAttributeId(), price.toString(), deltype);
                        }
                        //mAddToCartTable.insert(data.get(pos).getProductId(), data.get(pos).getProductName(), txt_count_cart_add.getText().toString(), data.get(pos).getProductSize().get(0).getOptionId(), data.get(pos).getProductSize().get(0).getAttributeId(), data.get(pos).getProductSpecialPrice().toString());
                    }else {
                        Double price=Double.parseDouble(relatedArr.get(pos).getProductPrice().toString())+Double.parseDouble(relatedArr.get(pos).getProductSize().get(0).getPricingValue().toString());
                        if(arrProductIDS.contains(relatedArr.get(pos).getProductId())){
                            mAddToCartTable.deleteitem(relatedArr.get(pos).getProductId());

                            int qnt=Integer.parseInt(prevQty)+Integer.parseInt(txt_count_cart_add.getText().toString());
                            mAddToCartTable.insert(relatedArr.get(pos).getProductId(), relatedArr.get(pos).getProductName(), ""+qnt,relatedArr.get(pos).getProductSize().get(0).getOptionId(), relatedArr.get(pos).getProductSize().get(0).getAttributeId(), price.toString(), deltype);
                        }else{
                            mAddToCartTable.insert(relatedArr.get(pos).getProductId(), relatedArr.get(pos).getProductName(),  txt_count_cart_add.getText().toString(),relatedArr.get(pos).getProductSize().get(0).getOptionId(), relatedArr.get(pos).getProductSize().get(0).getAttributeId(), price.toString(), deltype);
                        }
                        //mAddToCartTable.insert(data.get(pos).getProductId(), data.get(pos).getProductName(), txt_count_cart_add.getText().toString(), data.get(pos).getProductSize().get(0).getOptionId(), data.get(pos).getProductSize().get(0).getAttributeId(), data.get(pos).getProductPrice());
                    }
                    pDialog.dismiss();
                    utils.displayAlert("Item has been added to cart.");
                    tota1_price=0.0;
                    data=mAddToCartTable.getAll();
                    qtycount=0;
                    for(int i=0; i<data.size(); i++){
                        String single_price=data.get(i).get("price").toString();
                        if(single_price.startsWith("R")){
                            single_price= single_price.substring(1, single_price.length());
                        }
                        if(single_price!=null ||!single_price.equalsIgnoreCase("")){
                            double a=Double.parseDouble(single_price);
                            a=a*Integer.parseInt(data.get(i).get("qty"));
                            tota1_price=tota1_price+a;
                            qtycount=qtycount+Integer.parseInt(data.get(i).get("qty"));
                        }
                        btn_count.setText("" + qtycount);
                        SubCategoryActivity.txt_total_price.setText("R" + nf.format(tota1_price));
                    }

                }

            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCount = txt_count_cart_add.getText().toString().trim();
                int intCount = Integer.parseInt(strCount);
                intCount++;
                txt_count_cart_add.setText(String.valueOf(intCount));
                /*if(available.equalsIgnoreCase("yes")){
                    String totalPrice=txt_old_price.getText().toString();
                    totalPrice=totalPrice.substring(1, totalPrice.length());
                    //strProductPrice=strProductPrice.substring(1, strProductPrice.length());
                    Double a=Double.parseDouble(totalPrice);
                    Double b=Double.parseDouble(data.get(pos).get(SubCategoryActivity.Key_ProductPrice).substring(1, data.get(pos).get(SubCategoryActivity.Key_ProductPrice).length()));

                    Double newprice=a+b;
                    txt_old_price.setText("R"+newprice);
                }else{
                    String totalPrice=txt_real_price.getText().toString();
                    totalPrice=totalPrice.substring(1, totalPrice.length());
                    Double a=Double.parseDouble(totalPrice);
                    Double b=Double.parseDouble(data.get(pos).get(SubCategoryActivity.Key_ProductPrice).substring(1, data.get(pos).get(SubCategoryActivity.Key_ProductPrice).length()));
                    Double newprice=a+b;
                    txt_real_price.setText("R"+newprice);
                }*/
            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCount = txt_count_cart_add.getText().toString().trim();
                int intCount = Integer.parseInt(strCount);
                if (intCount>1) {
                    intCount--;
                    txt_count_cart_add.setText(String.valueOf(intCount));
                    /*if(available.equalsIgnoreCase("yes")){
                        String totalPrice=txt_old_price.getText().toString();
                        totalPrice=totalPrice.substring(1, totalPrice.length());
                        //strProductPrice=strProductPrice.substring(1, strProductPrice.length());
                        Double a=Double.parseDouble(totalPrice);
                        Double b=Double.parseDouble(data.get(pos).get(SubCategoryActivity.Key_ProductPrice).substring(1, data.get(pos).get(SubCategoryActivity.Key_ProductPrice).length()));
                        Double newprice=a-b;
                        txt_old_price.setText("R"+newprice);
                    }else{
                        String totalPrice=txt_real_price.getText().toString();
                        totalPrice=totalPrice.substring(1, totalPrice.length());
                        //strProductPrice=strProductPrice.substring(1, strProductPrice.length());
                        Double a=Double.parseDouble(totalPrice);
                        Double b=Double.parseDouble(data.get(pos).get(SubCategoryActivity.Key_ProductPrice).substring(1, data.get(pos).get(SubCategoryActivity.Key_ProductPrice).length()));

                        Double newprice=a-b;
                        txt_real_price.setText("R"+newprice);
                    }*/
                }
            }
        });
        dialog.show();
    }
        public void displayAlert(String message)
        {
        // TODO Auto-generated method stub
            //message="To proceed, sign into your account.";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent=new Intent(ProductDetailsActivity.this, LoginActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.ProductDetailsActivity");
                finish();
                startActivity(intent);
            }
        });
        TextView myMsg = new TextView(this);
        myMsg.setText(message);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setTextSize(16);
        //myMsg.setTypeface(typeFaceSegoeuiReg);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL);
        alertDialogBuilder.setView(myMsg);

        TextView title = new TextView(this);
        // You Can Customise your Title here
        title.setText("KenCommerce");
        title.setBackgroundColor(Color.TRANSPARENT);
        title.setPadding(15, 20, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        // title.setTypeface(typeFaceSegoeuiBold);
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
    private void getDialogOK(String strMsg) {
        final Dialog dialog = new Dialog(ProductDetailsActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_response_ok);

        TextView txt_header=(TextView)dialog.findViewById(R.id.txt_header);
        TextView txt_msg=(TextView)dialog.findViewById(R.id.txt_msg);
        Button btn_ok=(Button)dialog.findViewById(R.id.btn_ok);

        //txt_header.setText(strHeader);
        txt_msg.setText(strMsg);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void setFraction(float floatRateFrac, ImageView img_rate) {
        if (floatRateFrac >= 0.1 && floatRateFrac <= 0.3){
            img_rate.setImageResource(R.drawable.star_25);
        }
        else if (floatRateFrac > 0.3 && floatRateFrac <= 0.6){
            img_rate.setImageResource(R.drawable.star_50);
        }
        else if (floatRateFrac > 0.6 && floatRateFrac <= 0.8){
            img_rate.setImageResource(R.drawable.star_75);
        }
        else if (floatRateFrac > 0.8){
            img_rate.setImageResource(R.drawable.star_100);
        }
        else {
            img_rate.setImageResource(R.drawable.star_0);
        }
    }

    private void getCartTotal(String customer_id) {

        restService.getCartTotal(customer_id, new RestCallback<CartTotalRequest>() {
            @Override
            public void success(CartTotalRequest object) {

                if(object.getStatus()==200&&object.getSuccess()==1){

                    btn_count.setText(object.getData().getTotalQty().toString());
                    //txt_total_price.setText(object.getData().getTotalPrice());
                }

            }

            @Override
            public void invalid() {

                Toast.makeText(ProductDetailsActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                Toast.makeText(ProductDetailsActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }

}

