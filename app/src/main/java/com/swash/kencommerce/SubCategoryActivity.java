package com.swash.kencommerce;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

//import com.appsflyer.AppsFlyerLib;
import com.appsflyer.AppsFlyerLib;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.adapter.SubCatProductListAdapter;
import com.swash.kencommerce.adapter.SubCatSearchProductListAdapter;
import com.swash.kencommerce.crashreport.ExceptionHandler;
import com.swash.kencommerce.dbhelper.AddToCartTable;
import com.swash.kencommerce.model.CartTotalRequest;
import com.swash.kencommerce.model.FeaturedProductList;
import com.swash.kencommerce.model.SearchResultRequest;
import com.swash.kencommerce.model.SubCategoryList;
import com.swash.kencommerce.model.SubCategoryListRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;
import com.swash.kencommerce.webview.KenCommerceWebViewActivity;

public class SubCategoryActivity extends AppCompatActivity {

    HorizontalScrollView hs_tabs;
    LinearLayout ll_hs_item;
    private ViewPager viewPager;
    ArrayList<Integer>  arrCatName;
    private FeaturedProductViewPagerAdapter featuredProductViewPagerAdapter;
    private SubProductViewPagerAdapter subProductViewPagerAdapter;
    private ExpandableHeightGridView gridview;
    ImageView imgPrev, imgNext;
    private ImageView btn_back;
    RelativeLayout rl_header;
    LinearLayout lin_categories;
    HorizontalScrollView hSrlView;
    TextView tv_spark_wine, tv_rose_wine, tv_red_wine, tv_white_wine, tv_normal_wine;
    public static int width=0, height=0;
    Toolbar toolbar;
    ///ViewPager Auto Slide//////////////
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
    //private int currentPage = 0;
    private LinearLayout lin_vPager;
    TextView tv_signin;
    private Typeface typeFaceSegoeuiReg, typeFaceSegoeuiMedium;
    TextView txt_categories, txt_price;
    ImageView img_search_icon;
    private String str_cat_type="";

    public static ArrayList<HashMap<String, String>> listProductList = new ArrayList<HashMap<String, String>>();
    public static String Key_ProductName = "ProductName";
    public static String Key_ProductPrice = "ProductPrice";
    public static String Key_ProductQuantity = "ProductQnty";
    public static String Key_ProductImage = "ProductImg";
    private AddToCartTable mAddToCartTable;
    private PopupWindow pw;
    LinearLayout lin_total_price;
    public static TextView txt_total_price;
    private LinearLayout lin_cart;
    int i=0;
    private Button btn_count;
    private Utils utils;
    ProgressDialog pDialog;
    RestService restService;
    private ViewPager viewPagerSubCat;
    private String product_id="";
    SharedPreferences shPrefUserSelection;
    SharedPreferences.Editor toEdit;

    SharedPreferences sharedPreferenceUser;
    SharedPreferences.Editor toEditUser;
    private ArrayList<String> arrSearchResultItemID, arrSearchResultItemName;
    private String customer_id="", customer_name="" ;
    private String store_id="";
    private ImageView img_landing_icon;
    private ImageView img_wishlist_icon;
    private double tota1_price=0.0;
    ArrayList<HashMap<String, String>> data=new ArrayList<HashMap<String, String>>();
    private NumberFormat nf;
    int qtycount=0;
    private ImageView img_filter_icon;
    TextView txt_empty_product_list;
    private TextView txt_no_subitem;
    private String seeAllProduct="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(SubCategoryActivity.this));
        setContentView(R.layout.activity_horizontal_scroll_new);
        utils=new Utils(SubCategoryActivity.this);
        mAddToCartTable=new AddToCartTable(this);
        data= mAddToCartTable.getAll();
        restService=new RestService();
        i=mAddToCartTable.getCount();
        typeFaceSegoeuiReg = Typeface.createFromAsset(getAssets(),
                "Roboto-Regular.ttf");
        typeFaceSegoeuiMedium = Typeface.createFromAsset(getAssets(),
                "Roboto-Medium.ttf");
        nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        shPrefUserSelection=this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
        sharedPreferenceUser=this.getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
        customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        customer_name=sharedPreferenceUser.getString(Constants.strShPrefUserFname,"");
        store_id=shPrefUserSelection.getString(Constants.strShUserProductId, "");
        seeAllProduct=shPrefUserSelection.getString(Constants.strShStoreID, "");
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        initFields();

        // Track Data : Add to activities where tracking needed
        AppsFlyerLib.getInstance().sendDeepLinkData(this);

        if(!customer_id.equalsIgnoreCase("")||!customer_id.isEmpty()){
            getCartTotal(customer_id);
        }else{
            tota1_price=0.0;
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
                txt_total_price.setText("R" + nf.format(tota1_price));
            }
        }
        if(seeAllProduct.equalsIgnoreCase("yes")){
            getSubCatAllProduct(store_id, customer_id, "false");
        }else{
            getSubCat(store_id, customer_id, "false");
        }
        //getSubCat(store_id, customer_id, "false");
    }

    private void getCartTotal(String customer_id) {

        restService.getCartTotal(customer_id, new RestCallback<CartTotalRequest>() {
            @Override
            public void success(CartTotalRequest object) {


                if(object.getStatus()==200&&object.getSuccess()==1){

                    btn_count.setText(object.getData().getTotalQty().toString());
                    txt_total_price.setText(object.getData().getCurrency()+object.getData().getTotalPrice());
                }
            }

            @Override
            public void invalid() {

                Toast.makeText(SubCategoryActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                Toast.makeText(SubCategoryActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getSubCat(String store_id, String customer_id, final String isSubCat) {

        pDialog=new ProgressDialog(this);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait");
        restService.subCategoryList(store_id, customer_id, new RestCallback<SubCategoryListRequest>() {
            @Override
            public void success(SubCategoryListRequest object) {
                if (object.getData().getProductList().size() > 0) {
                    gridview.setAdapter(new SubCatProductListAdapter(SubCategoryActivity.this, object.getData().getProductList(), str_cat_type, btn_count));
                    gridview.setExpanded(true);
                    gridview.setFocusable(false);
                    gridview.setVisibility(View.VISIBLE);
                    txt_empty_product_list.setVisibility(View.GONE);
                } else {
                    gridview.setAdapter(new SubCatProductListAdapter(SubCategoryActivity.this, object.getData().getProductList(), str_cat_type, btn_count));
                    gridview.setExpanded(true);
                    gridview.setFocusable(false);
                    gridview.setVisibility(View.GONE);
                    txt_empty_product_list.setVisibility(View.VISIBLE);
                    txt_empty_product_list.setText("Bringing more to your area, very soon!\n\n We are signing up new partners every day to bring service to you across all our service offerings.\n\n Check back soon!");
                    //displayAlert("Regrettably we are unable to bring to you this product or service at the moment. We are working very hard at improving our coverage. Check back soon!");
                }

                if (object.getData().getFeaturedProductList().size() > 0) {
                    lin_vPager.setVisibility(View.VISIBLE);
                    featuredProductViewPagerAdapter = new FeaturedProductViewPagerAdapter(object.getData().getFeaturedProductList());
                    viewPager.setAdapter(featuredProductViewPagerAdapter);
                } else {
                    lin_vPager.setVisibility(View.GONE);
                }

                setTimePage(object.getData().getSubCategoryList().size());

                if (isSubCat.equalsIgnoreCase("false")) {

                    if (object.getData().getSubCategoryList().size() > 0) {
                        subProductViewPagerAdapter = new SubProductViewPagerAdapter(object.getData().getSubCategoryList());
                        viewPagerSubCat.setAdapter(subProductViewPagerAdapter);
                        viewPagerSubCat.setOffscreenPageLimit(object.getData().getSubCategoryList().size());
                        txt_no_subitem.setVisibility(View.GONE);
                        viewPagerSubCat.setVisibility(View.VISIBLE);
                    } else {
                        viewPagerSubCat.setVisibility(View.GONE);
                        txt_no_subitem.setVisibility(View.VISIBLE);

                    }
                }

                if (pDialog != null)
                    pDialog.dismiss();
            }

            @Override
            public void invalid() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(SubCategoryActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(SubCategoryActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }
    private void getSubCatAllProduct(String store_id, String customer_id, final String isSubCat) {

        pDialog=new ProgressDialog(this);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait");
        restService.subCategorySeeAllList(store_id, customer_id, new RestCallback<SubCategoryListRequest>() {
            @Override
            public void success(SubCategoryListRequest object) {
                if (object.getData().getProductList().size() > 0) {
                    gridview.setAdapter(new SubCatProductListAdapter(SubCategoryActivity.this, object.getData().getProductList(), str_cat_type, btn_count));
                    gridview.setExpanded(true);
                    gridview.setFocusable(false);
                    gridview.setVisibility(View.VISIBLE);
                    txt_empty_product_list.setVisibility(View.GONE);
                } else {
                    gridview.setAdapter(new SubCatProductListAdapter(SubCategoryActivity.this, object.getData().getProductList(), str_cat_type, btn_count));
                    gridview.setExpanded(true);
                    gridview.setFocusable(false);
                    gridview.setVisibility(View.GONE);
                    txt_empty_product_list.setVisibility(View.VISIBLE);
                    txt_empty_product_list.setText("Bringing more to your area, very soon!\n\n We are signing up new partners every day to bring service to you across all our service offerings.\n\n Check back soon!");
                    //displayAlert("Regrettably we are unable to bring to you this product or service at the moment. We are working very hard at improving our coverage. Check back soon!");
                }

                if (object.getData().getFeaturedProductList().size() > 0) {
                    lin_vPager.setVisibility(View.VISIBLE);
                    featuredProductViewPagerAdapter = new FeaturedProductViewPagerAdapter(object.getData().getFeaturedProductList());
                    viewPager.setAdapter(featuredProductViewPagerAdapter);
                } else {
                    lin_vPager.setVisibility(View.GONE);
                }

                setTimePage(object.getData().getSubCategoryList().size());

                if (isSubCat.equalsIgnoreCase("false")) {

                    if (object.getData().getSubCategoryList().size() > 0) {
                        subProductViewPagerAdapter = new SubProductViewPagerAdapter(object.getData().getSubCategoryList());
                        viewPagerSubCat.setAdapter(subProductViewPagerAdapter);
                        viewPagerSubCat.setOffscreenPageLimit(object.getData().getSubCategoryList().size());
                        txt_no_subitem.setVisibility(View.GONE);
                        viewPagerSubCat.setVisibility(View.VISIBLE);
                    } else {
                        viewPagerSubCat.setVisibility(View.GONE);
                        txt_no_subitem.setVisibility(View.VISIBLE);

                    }
                }

                if (pDialog != null)
                    pDialog.dismiss();
            }

            @Override
            public void invalid() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(SubCategoryActivity.this, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(SubCategoryActivity.this, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }
    private void initFields() {

        rl_header=(RelativeLayout)findViewById(R.id.rl_header);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0,0);

        lin_vPager=(LinearLayout)findViewById(R.id.lin_vPager);
        img_landing_icon=(ImageView)findViewById(R.id.img_landing_icon);
        img_landing_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryActivity.this, LandingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });

        img_wishlist_icon=(ImageView)findViewById(R.id.img_wishlist_icon);
        img_wishlist_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryActivity.this, MyWishListActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        lin_categories=(LinearLayout)findViewById(R.id.lin_categories);
        lin_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shPrefUserSelection = SubCategoryActivity.this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
                store_id=shPrefUserSelection.getString(Constants.strShUserShopSelected, "");

                if(store_id.equalsIgnoreCase("drink")){
                    Intent intent=new Intent(SubCategoryActivity.this, DrinkCategoriesActivity.class);
                    finish();
                    startActivity(intent);
                }else if(store_id.equalsIgnoreCase("food")){
                    Intent intent=new Intent(SubCategoryActivity.this, FoodCategoriesActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if(store_id.equalsIgnoreCase("day")){
                    Intent intent=new Intent(SubCategoryActivity.this, Day2DayCategoriesActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if(store_id.equalsIgnoreCase("gift")){
                    Intent intent=new Intent(SubCategoryActivity.this, GiftCategoriesActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if(store_id.equalsIgnoreCase("event")){
                    Intent intent=new Intent(SubCategoryActivity.this, EventCategoriesActivity.class);
                    finish();
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(SubCategoryActivity.this, DrinkCategoriesActivity.class);
                    finish();
                    startActivity(intent);
                }
                SubCategoryActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        img_filter_icon=(ImageView)findViewById(R.id.img_filter_icon);
        img_filter_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryActivity.this, FilterActivity.class);
                startActivity(intent);
                //finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        txt_categories=(TextView)findViewById(R.id.txt_categories);
        txt_categories.setTypeface(typeFaceSegoeuiMedium);

        lin_total_price=(LinearLayout)findViewById(R.id.lin_total_price);
        lin_total_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryActivity.this, AddToCartListAllItemsActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.SubCategoryActivity");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        txt_total_price=(TextView)findViewById(R.id.txt_total_price);
        txt_total_price.setTypeface(typeFaceSegoeuiMedium);

        txt_empty_product_list=(TextView)findViewById(R.id.txt_empty_product_list);

        img_search_icon=(ImageView)findViewById(R.id.img_search_icon_auto);
        img_search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getDialogCoverage();
                getDialogSearch();
            }
        });

        lin_cart=(LinearLayout)findViewById(R.id.lin_cart);
        lin_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryActivity.this, AddToCartListAllItemsActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.SubCategoryActivity");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        btn_count=(Button)findViewById(R.id.btn_count);

        btn_back=(ImageView)findViewById(R.id.img_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shPrefUserSelection = SubCategoryActivity.this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
                store_id=shPrefUserSelection.getString(Constants.strShUserShopSelected, "");

                if(store_id.equalsIgnoreCase("drink")){
                    Intent intent=new Intent(SubCategoryActivity.this, DrinkCategoriesActivity.class);
                    finish();
                    startActivity(intent);
                }else if(store_id.equalsIgnoreCase("food")){
                    Intent intent=new Intent(SubCategoryActivity.this, FoodCategoriesActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if(store_id.equalsIgnoreCase("day")){
                    Intent intent=new Intent(SubCategoryActivity.this, Day2DayCategoriesActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if(store_id.equalsIgnoreCase("gift")){
                    Intent intent=new Intent(SubCategoryActivity.this, GiftCategoriesActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if(store_id.equalsIgnoreCase("event")){
                    Intent intent=new Intent(SubCategoryActivity.this, EventCategoriesActivity.class);
                    finish();
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(SubCategoryActivity.this, DrinkCategoriesActivity.class);
                    finish();
                    startActivity(intent);
                }
                SubCategoryActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        tv_signin=(TextView)findViewById(R.id.tv_signin);
        if(customer_id!=null &!customer_id.equalsIgnoreCase("")){
            tv_signin.setText(customer_name);
        }
        tv_signin.setTypeface(typeFaceSegoeuiReg);

        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customer_id.equalsIgnoreCase("") && customer_name.equalsIgnoreCase("")) {
                    Intent intent = new Intent(SubCategoryActivity.this, LoginActivity.class);
                    intent.putExtra("context_act1", "webskitters.com.stockup.SubCategoryActivity");
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }

            }
        });
        lin_vPager=(LinearLayout)findViewById(R.id.lin_vPager);
        viewPager = (ViewPager) findViewById(R.id.vPager);
        viewPagerSubCat=(ViewPager)findViewById(R.id.vPagerSubCat);

        txt_no_subitem=(TextView)findViewById(R.id.txt_no_subitem);

        initGriview();
        //initViewPager();
    }

    public void displayAlert(String message)
    {
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        TextView myMsg = new TextView(this);
        myMsg.setText(message);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setTextSize(16);
        //myMsg.setTypeface(typeFaceSegoeuiReg);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
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
    private void getDialogSearch() {
        final Dialog dialog = new Dialog(SubCategoryActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_auto_search);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView img_search=(ImageView)dialog.findViewById(R.id.img_search);

        ImageView img_back=(ImageView)dialog.findViewById(R.id.img_back_search);
        final ListView lv=(ListView)dialog.findViewById(R.id.listview);
        final EditText et_search = (EditText) dialog.findViewById(R.id.et_search);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(SubCategoryActivity.this);
                if(dialog.isShowing())
                dialog.dismiss();
            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSOftKeyboard(SubCategoryActivity.this, et_search);
            }
        });
        ImageView img_clear = (ImageView) dialog.findViewById(R.id.img_clear);
        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String strSearch = et_search.getText().toString().trim();
                if (strSearch.length() > 2) {
                    getAutoSearchKey(strSearch, dialog, lv);
                }
            }
        });

        dialog.show();
    }

    private void getAutoSearchKey(final String strSearch, final Dialog dialog, final ListView lv) {

        String strCatType = "";
        customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");

        if (str_cat_type.equalsIgnoreCase("Drinks")){
            strCatType = "10";
        }
        else if (str_cat_type.equalsIgnoreCase("Foods")){
            strCatType = "11";
        }
        else if (str_cat_type.equalsIgnoreCase("Day2Day")) {
            strCatType = "12";
        }
        else if (str_cat_type.equalsIgnoreCase("Gifts")){
            strCatType = "13";
        }
        final String finalStrCatType = strCatType;
        restService.getAutoSearch(strSearch, strCatType, new RestCallback<SearchResultRequest>() {

            @Override
            public void success(SearchResultRequest responce) {

                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    int size = responce.getData().getProductData().size();
                    arrSearchResultItemID = new ArrayList<String>();
                    arrSearchResultItemName = new ArrayList<String>();
                    for (int i = 0; i < size; i++) {
                        arrSearchResultItemID.add(responce.getData().getProductData().get(i).getProductId().toString().trim());
                        arrSearchResultItemName.add(responce.getData().getProductData().get(i).getProductName().toString().trim());
                    }

                    List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i < arrSearchResultItemName.size(); i++) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("txt", arrSearchResultItemName.get(i));
                        aList.add(hm);
                    }
                    String[] from = {"txt"};
                    int[] to = {R.id.txt};
                    SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.autocomplete_layout, from, to);

                    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);

                            HashMap<String, String> hm = (HashMap<String, String>) arg0.getAdapter().getItem(position);
                            String strNameLocation = hm.get("txt");
                            String strSearchKey = arrSearchResultItemName.get(position);

                            customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");

                            toEditUser= sharedPreferenceUser.edit();
                            toEditUser.putString("ProductId", arrSearchResultItemID.get(position));
                            toEditUser.commit();
                            //dialog.dismiss();
                            Intent intent = new Intent(SubCategoryActivity.this, ProductDetailsActivity.class);
                            finish();
                            startActivity(intent);

                        }
                    };
                    lv.setOnItemClickListener(itemClickListener);
                    lv.setAdapter(adapter);
                   // lv.setThreshold(1);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                } else {
                    //utils.displayAlert("No Result Found with Search Term"+strSearch);
                    lv.setAdapter(null);


                }
            }

            @Override
            public void invalid() {

            }

            @Override
            public void failure() {

            }
        });
    }

    private void getSearchResult(String strSearchKey, String strCatType, String strCustomerId) {
        final ProgressDialog prDialog=new ProgressDialog(SubCategoryActivity.this);
        prDialog.show();
        prDialog.setMessage("Loading..");
        restService.getAutoSearch(strSearchKey, strCatType,  new RestCallback<SearchResultRequest>() {
            @Override
            public void success(SearchResultRequest responce) {
                prDialog.dismiss();
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    gridview.setAdapter(new SubCatSearchProductListAdapter(SubCategoryActivity.this, responce.getData().getProductData(), str_cat_type, btn_count));
                    gridview.setExpanded(true);
                    gridview.setFocusable(false);
                } else {
                    utils.displayAlert(responce.getErrorMsg());
                }
            }

            @Override
            public void invalid() {
                prDialog.dismiss();
            }

            @Override
            public void failure() {
                prDialog.dismiss();
            }
        });
    }

    private void getDialogDetails(final int pos, final String available) {
        final Dialog dialog = new Dialog(SubCategoryActivity.this);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product_add_cart);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        int width = (int) (SubCategoryActivity.width * 0.99);
        int height = (int) (SubCategoryActivity.height * 0.9);
        dialog.getWindow().setLayout(width, height);

        ImageView img_special=(ImageView)dialog.findViewById(R.id.img_special);
        LinearLayout lin_cross=(LinearLayout)dialog.findViewById(R.id.lin_cross);

        RelativeLayout rel_qnt=(RelativeLayout)dialog.findViewById(R.id.rel_qnt);

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


        rel_qnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(SubCategoryActivity.this);
                //callPopUpDrinkItems(txt_qnty, txt_qnty);
            }
        });

        txt_product_name.setText(listProductList.get(pos).get(SubCategoryActivity.Key_ProductName));

        if(available.equalsIgnoreCase("yes")){
            txt_old_price.setText(listProductList.get(pos).get(SubCategoryActivity.Key_ProductPrice));
            txt_old_price.setGravity(Gravity.CENTER);
            txt_real_price.setVisibility(View.GONE);
            img_special.setVisibility(View.GONE);
            lin_cross.setVisibility(View.GONE);
        }else{
            txt_real_price.setText(listProductList.get(pos).get(SubCategoryActivity.Key_ProductPrice));
        }

        txt_qnty.setText(listProductList.get(pos).get(SubCategoryActivity.Key_ProductQuantity));
        img_product.setImageResource(arrCatName.get(pos));

        RelativeLayout rel_add_to_shoping_list=(RelativeLayout)dialog.findViewById(R.id.rel_add_to_shoping_list);
        rel_add_to_shoping_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SubCategoryActivity.this, ShoppingListActivity.class);
                startActivity(intent);
            }
        });

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
                int i=Integer.parseInt(v.getTag().toString());

               /* if(available.equalsIgnoreCase("yes")){
                    *//*String totalPrice=txt_old_price.getText().toString();
                    totalPrice=totalPrice.substring(1, totalPrice.length());
                    Double a=Double.parseDouble(totalPrice);
                    double d=iii*a;
                     toEditAddToCart.putString(Constants.strShPrefPrice, "");*//*
                    mAddToCartTable.insert(listProductList.get(i).get(SubCategoryActivity.Key_ProductName),txt_old_price.getText().toString(),txt_count_cart_add.getText().toString(), arrCatName.get(i).toString());
                    utils.displayAlert("Successfully added to your cart");
                    i=mAddToCartTable.getCount();
                    btn_count.setText(""+i);
                }else{
                    mAddToCartTable.insert(listProductList.get(i).get(SubCategoryActivity.Key_ProductName),txt_real_price.getText().toString(),txt_count_cart_add.getText().toString(), arrCatName.get(i).toString());
                    utils.displayAlert("Successfully added to your cart");
                    i=mAddToCartTable.getCount();
                    btn_count.setText("" + i);
                }*/


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
                    Double b=Double.parseDouble(listProductList.get(pos).get(SubCategoryActivity.Key_ProductPrice).substring(1, listProductList.get(pos).get(SubCategoryActivity.Key_ProductPrice).length()));

                    Double newprice=a+b;
                    txt_old_price.setText("R"+newprice);
                }else{
                    String totalPrice=txt_real_price.getText().toString();
                    totalPrice=totalPrice.substring(1, totalPrice.length());
                    //strProductPrice=strProductPrice.substring(1, strProductPrice.length());
                    Double a=Double.parseDouble(totalPrice);
                    Double b=Double.parseDouble(listProductList.get(pos).get(SubCategoryActivity.Key_ProductPrice).substring(1, listProductList.get(pos).get(SubCategoryActivity.Key_ProductPrice).length()));

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
                if (intCount > 0) {
                    intCount--;
                    txt_count_cart_add.setText(String.valueOf(intCount));
                    /*if (available.equalsIgnoreCase("yes")) {
                        String totalPrice = txt_old_price.getText().toString();
                        totalPrice = totalPrice.substring(1, totalPrice.length());
                        //strProductPrice=strProductPrice.substring(1, strProductPrice.length());
                        Double a = Double.parseDouble(totalPrice);
                        Double b = Double.parseDouble(listProductList.get(pos).get(SubCategoryActivity.Key_ProductPrice).substring(1, listProductList.get(pos).get(SubCategoryActivity.Key_ProductPrice).length()));
                        Double newprice = a - b;
                        txt_old_price.setText("R" + newprice);
                    } else {
                        String totalPrice = txt_real_price.getText().toString();
                        totalPrice = totalPrice.substring(1, totalPrice.length());
                        //strProductPrice=strProductPrice.substring(1, strProductPrice.length());
                        Double a = Double.parseDouble(totalPrice);
                        Double b = Double.parseDouble(listProductList.get(pos).get(SubCategoryActivity.Key_ProductPrice).substring(1, listProductList.get(pos).get(SubCategoryActivity.Key_ProductPrice).length()));

                        Double newprice = a - b;
                        txt_real_price.setText("R" + newprice);
                    }*/
                }
            }
        });
       /* wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        wmlp.x = 200;   //x position
        wmlp.y = 200;   //y position*/
        dialog.show();
    }
    /////////////////////Hiding soft keyboard/////////////
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void openSOftKeyboard(Activity activity, EditText et_search){

        InputMethodManager inputMethodManager =
                (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
        et_search.setFocusable(true);
        et_search.requestFocus();
    }
    //////////////////Dropdown for Quantity/////////////////////////////////
   /* private void callPopUpDrinkItems(View anchorView, TextView txt_qnty) {

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
        final ArrayList<String> arrItem=new ArrayList<>();
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
        }
        DrinkCatSpinnerAdapter searchLangAdapter = new DrinkCatSpinnerAdapter(SubCategoryActivity.this, arrItem);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(Registration1Activity.this,android.R.layout.simple_list_item_1,android.R.id.text1,values);

        ListView listView = (ListView)view.findViewById(R.id.pop_up_menu_list);
        listView.setAdapter(searchLangAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                txt_qnt.setText(arrItem.get(position));
                pw.dismiss();
            }
        });

        return view;
    }
*/
    private void initGriview(){

        gridview = (ExpandableHeightGridView) findViewById(R.id.gridview);


    }
    /*private void initViewPager(){
        if(str_cat_type.equalsIgnoreCase("Drinks")){
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
        }
        featuredProductViewPagerAdapter = new FeaturedProductViewPagerAdapter(listProductList, arrCatName);
        viewPager.setAdapter(featuredProductViewPagerAdapter);
        *//*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int page, float scale, int offset) {
                if(scale != 0f && offset > 10) {
                    isSwiped=true;
                    if (currentOffset > offset && currentScale > scale && (page + 1) == count) {
                        if (scroll != -1) {
                            //newImageSet = true;
                            scroll = -1;
                            Log.e("scroll", "left");

                            *//**//*if (count==6) {
                                count--;
                                viewPager.setCurrentItem(count);
                            }

                            else if (count>0) {
                                if(count>1)
                                    count=count-2;

                                viewPager.setCurrentItem(count);
                                timer.cancel();
                                setTimePage();
                            }*//**//*
                            if (count == listProductList.size()) {
                                count = 0;
                            }
                            viewPager.setCurrentItem(count--, true);
                        }
                    } else if (currentOffset < offset && currentScale < scale && page == count) {
                        if (scroll != 1) {
                            //newImageSet = true;
                            scroll = 1;
                            Log.e("scroll", "right");
                            if (count == listProductList.size()) {
                                count = 0;
                            }

                            viewPager.setCurrentItem(count++, true);
                            *//**//*if (count < 6) {
                                viewPager.setCurrentItem(count);
                                count++;
                                timer.cancel();
                                setTimePage();
                            }*//**//*
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
        });*//*
    }*/
    public void setTimePage( final int i){

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (count == i) {
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
        }, 500, 3000);
    }

    public class FeaturedProductViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        List<FeaturedProductList> arrCatName;

        public FeaturedProductViewPagerAdapter(List<FeaturedProductList> arrCat) {

            this.arrCatName=arrCat;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // references to our images
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.addview_feature_products_cat_item, container,false);
            //view.setBackgroundColor(Color.GREEN);
            final ImageView img_main = (ImageView) view.findViewById(R.id.img_cat);

            TextView tvCatName = (TextView) view.findViewById(R.id.tv_cat_name);
            TextView tvCatPrice = (TextView) view.findViewById(R.id.tv_price);
            img_main.setId(position);
            img_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = v.getId();

                }
            });

            tvCatName.setText(arrCatName.get(position).getProductName());
            tvCatPrice.setText("R"+arrCatName.get(position).getProductPrice().toString());

            img_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = v.getId();
                    String url = arrCatName.get(i).getProductUrl();
                    /*Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);*/

                    Intent urlTandC = new Intent(SubCategoryActivity.this, KenCommerceWebViewActivity.class);
                    urlTandC.putExtra("header","Featured");
                    urlTandC.putExtra("url", url);
                    startActivity(urlTandC);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            });

            Glide.with(SubCategoryActivity.this).load(arrCatName.get(position).getProductImgUrl()).asBitmap().into(new BitmapImageViewTarget(img_main) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(SubCategoryActivity.this.getResources(), resource);
                    //circularBitmapDrawable.setCircular(true);
                    circularBitmapDrawable.setCornerRadius(15);
                    img_main.setImageDrawable(circularBitmapDrawable);

                    //img_main.setBackgroundDrawable(circularBitmapDrawable);

                }
            });

            ((ViewPager) container).addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return arrCatName.size();
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
            if(arrCatName.size()==0){
                return (super.getPageWidth(position));
            }else
            return (super.getPageWidth(position) / 2);
        }
    }

    public class SubProductViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        List<SubCategoryList> arrCatName;

        public SubProductViewPagerAdapter(List<SubCategoryList> arrCat) {

            this.arrCatName=arrCat;
        }

        @Override
        public Object instantiateItem( final ViewGroup container, int position) {

            // references to our images
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.addview_sub_cat_item, container,false);
            //view.setBackgroundColor(Color.GREEN);
            final TextView tvCatName = (TextView) view.findViewById(R.id.tv_cat_name);
            tvCatName.setText(arrCatName.get(position).getCatName());
            tvCatName.setTag(position);
            if(position==0){
                tvCatName.setTextColor(Color.BLACK);
            }

            tvCatName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i= Integer.parseInt(v.getTag().toString());
                    for(int j=0; j<arrCatName.size(); j++){

                        if(j==i){
                            //viewPager.setCurrentItem(viewPager.getAdapter().getCount()/2);
                            TextView txt_name=(TextView)container.getChildAt(j).findViewById(R.id.tv_cat_name);
                            txt_name.setTextColor(Color.BLACK);
                            customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
                            toEdit=shPrefUserSelection.edit();
                            toEdit.putString(Constants.strShUserProductId,arrCatName.get(i).getCatId().toString());
                            toEdit.commit();


                                getSubCat(arrCatName.get(i).getCatId().toString(), customer_id, "true");

                            timer.cancel();
                            //int scrollX = (v.getLeft() - (width / 2)) + (v.getWidth() / 2);
                            //Toast.makeText(SubCategoryActivity.this, "Horizontal Scrollview Child Count"+hs_tabs.getChildCount(), Toast.LENGTH_SHORT).show();
                            //container.scrollTo(scrollX, 0);
                            if(j==0 || j==arrCatName.size()-1){

                            }else{
                                viewPagerSubCat.setCurrentItem(j, true);
                            }
                        }else{
                            TextView txt_name=(TextView)container.getChildAt(j).findViewById(R.id.tv_cat_name);
                            txt_name.setTextColor(Color.WHITE);
                            txt_name.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

            ((ViewPager) container).addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return arrCatName.size();
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

            if(arrCatName.get(position).getCatName().length()>10){
                return (super.getPageWidth(position) / 2);
            }else
            return (super.getPageWidth(position) / 3);
        }
    }


    private void getDialogCoverage() {
        final Dialog dialog = new Dialog(SubCategoryActivity.this);
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


        shPrefUserSelection = SubCategoryActivity.this.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);
        store_id=shPrefUserSelection.getString(Constants.strShUserShopSelected, "");

        if(store_id.equalsIgnoreCase("drink")){
            Intent intent=new Intent(SubCategoryActivity.this, DrinkCategoriesActivity.class);
            finish();
            startActivity(intent);
        }else if(store_id.equalsIgnoreCase("food")){
            Intent intent=new Intent(SubCategoryActivity.this, FoodCategoriesActivity.class);
            finish();
            startActivity(intent);
        }
        else if(store_id.equalsIgnoreCase("day")){
            Intent intent=new Intent(SubCategoryActivity.this, Day2DayCategoriesActivity.class);
            finish();
            startActivity(intent);
        }
        else if(store_id.equalsIgnoreCase("gift")){
            Intent intent=new Intent(SubCategoryActivity.this, GiftCategoriesActivity.class);
            finish();
            startActivity(intent);
        }
        else if(store_id.equalsIgnoreCase("event")){
            Intent intent=new Intent(SubCategoryActivity.this, EventCategoriesActivity.class);
            finish();
            startActivity(intent);
        }else{
            Intent intent=new Intent(SubCategoryActivity.this, DrinkCategoriesActivity.class);
            finish();
            startActivity(intent);
        }
        SubCategoryActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }



}

