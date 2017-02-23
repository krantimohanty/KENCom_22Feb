package com.swash.kencommerce.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.swash.kencommerce.LoginActivity;
import com.swash.kencommerce.ProductDetailsActivity;
import com.swash.kencommerce.R;
import com.swash.kencommerce.ShoppingListActivity;
import com.swash.kencommerce.SubCategoryActivity;
import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;
import com.swash.kencommerce.dbhelper.AddToCartTable;
import com.swash.kencommerce.model.AddToWishListRequest;
import com.swash.kencommerce.model.CartTotalRequest;
import com.swash.kencommerce.model.ProductList;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by Kranti on 12/12/2016.
 */
public class SubCatProductListAdapter extends BaseAdapter {
    private static ArrayList<String> sKey;
    Context mContext;
    //ArrayList<String> arrCatName;
    ViewHolder holder = null;
    List<ProductList> data;
    String str_cat_type="";
    public static int size;
    ////////////DB for Add To Cart///////////////
    AddToCartTable mAddToCartTable;
    private PopupWindow pw;
    Button btn_count;
    private Utils utils;
    private RestService restService;
    private SharedPreferences sharedPreferenceUser;
    SharedPreferences.Editor toEdit;
    private ArrayList<HashMap<String, String>> arrItem;
    String str_old_price="", str_real_price="";
    private ProgressDialog pDialog;

    NumberFormat nf;
    private double tota1_price=0.0;
    private int qtycount;
    private String prevQty="";

    String optionId="", attId="", delType="";

    ArrayList<HashMap<String, String>> mData=new ArrayList<HashMap<String, String>>();

    public SubCatProductListAdapter(Context c, List<ProductList> d, String cat_type, Button btn_count) {
        this.mContext = c;
        this.data=d;
        this.str_cat_type=cat_type;
        this.btn_count=btn_count;
        restService=new RestService();
        utils=new Utils(mContext);
        mAddToCartTable=new AddToCartTable(mContext);

        mData=mAddToCartTable.getAll();
        sharedPreferenceUser=mContext.getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
        nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);



    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{

        ImageView imgCat, imgWish, imgPlus, imgSpecial, imgOnDemand;
        TextView tvCatName, tvCatPrice, tvCatQnty;
    }
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes

            convertView = inflater.inflate(R.layout.addview_drink_details_cat_item, null);
            holder = new ViewHolder();
            holder.tvCatName = (TextView) convertView.findViewById(R.id.tv_cat_name);
            holder.tvCatPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvCatQnty = (TextView) convertView.findViewById(R.id.tv_cat_qnt);
            holder.imgCat = (ImageView) convertView.findViewById(R.id.img_cat);
            holder.imgWish = (ImageView) convertView.findViewById(R.id.img_wish);
            holder.imgPlus = (ImageView) convertView.findViewById(R.id.img_plus);
            holder.imgSpecial = (ImageView) convertView.findViewById(R.id.img_special);
            holder.imgOnDemand = (ImageView) convertView.findViewById(R.id.img_on_demand);
            convertView.setTag(holder);

        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvCatName.setText(data.get(position).getProductName());
        Double total_price=0.0;
        if(data.get(position).getProductPrice().contains(",")&&data.get(position).getProductSize().get(0).getPricingValue().contains(",")){
            String price=data.get(position).getProductPrice().replace(",","");
            String price1=data.get(position).getProductSize().get(0).getPricingValue().replace(",","");
            total_price=Double.parseDouble(price)+Double.parseDouble(price1);
            holder.tvCatPrice.setText("R"+nf.format(total_price));

        }else if(data.get(position).getProductPrice().contains(",")){
            String price=data.get(position).getProductPrice().replace(",","");
            total_price=Double.parseDouble(price)+Double.parseDouble(data.get(position).getProductSize().get(0).getPricingValue());
            holder.tvCatPrice.setText("R"+nf.format(total_price));
        }
        /*else if(data.get(position).getProductSize().get(0).getPricingValue().contains(",")){
            String price1=data.get(position).getProductSize().get(0).getPricingValue().replace(",","");
            total_price=Double.parseDouble(data.get(position).getProductPrice())+Double.parseDouble(price1);
            holder.tvCatPrice.setText("R"+nf.format(total_price));
        }*/else{
            if(data.get(position).getProductSize().size()>0)
            total_price=Double.parseDouble(data.get(position).getProductPrice())+Double.parseDouble(data.get(position).getProductSize().get(0).getPricingValue());
            else{
                total_price=Double.parseDouble(data.get(position).getProductPrice());
            }
            holder.tvCatPrice.setText("R"+nf.format(total_price));
        }



        if(data.get(position).getProductIsSpecial()==1)
        {
            holder.imgSpecial.setVisibility(View.VISIBLE);
            if(!data.get(position).getProductSpecialPrice().toString().equalsIgnoreCase("")){

               //price=Double.parseDouble(data.get(position).getProductSpecialPrice().toString())+Double.parseDouble(data.get(position).getProductSize().get(0).getPricingValue());


                if(data.get(position).getProductSpecialPrice().contains(",")&&data.get(position).getProductSize().get(0).getPricingValue().contains(",")){
                    String price=data.get(position).getProductSpecialPrice().replace(",","");
                    String price1=data.get(position).getProductSize().get(0).getPricingValue().replace(",","");
                    total_price=Double.parseDouble(price)+Double.parseDouble(price1);
                    holder.tvCatPrice.setText("R"+nf.format(total_price));

                }else if(data.get(position).getProductSpecialPrice().contains(",")){
                    String price=data.get(position).getProductSpecialPrice().replace(",","");
                    total_price=Double.parseDouble(price)+Double.parseDouble(data.get(position).getProductSize().get(0).getPricingValue());
                    holder.tvCatPrice.setText("R"+nf.format(total_price));
                }
                else if(data.get(position).getProductSize().get(0).getPricingValue().contains(",")){
                    String price1=data.get(position).getProductSize().get(0).getPricingValue().replace(",","");
                    total_price=Double.parseDouble(data.get(position).getProductSpecialPrice())+Double.parseDouble(price1);
                    holder.tvCatPrice.setText("R"+nf.format(total_price));
                }else{
                    total_price=Double.parseDouble(data.get(position).getProductSpecialPrice())+Double.parseDouble(data.get(position).getProductSize().get(0).getPricingValue());
                    holder.tvCatPrice.setText("R"+nf.format(total_price));
                }

            }

            holder.tvCatPrice.setText("R"+nf.format(total_price));
        }
        else
        {
            holder.imgSpecial.setVisibility(View.INVISIBLE);
        }

        if(data.get(position).getProductIsSalable()==0){
            holder.imgOnDemand.setVisibility(View.VISIBLE);
            holder.imgOnDemand.setImageResource(R.drawable.outstock);
        }

        if(data.get(position).getProductDelivery().equalsIgnoreCase("asap")){
            holder.imgOnDemand.setVisibility(View.VISIBLE);
            holder.imgOnDemand.setImageResource(R.drawable.subcategory_ondmnd_icon);
        }

        holder.tvCatQnty.setText(data.get(position).getProductSize().get(0).getLabel());

        holder.imgCat.setId(position);
        holder.imgPlus.setTag(position);
        holder.imgWish.setTag(position);

        if(data.get(position).getHasProductInWishlist()==1){

            holder.imgWish.setImageResource(R.drawable.wishlisticon_active);
        }
        holder.imgCat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int i = v.getId();
                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                //intent.putExtra("isAvailable", data.get(i).getProductIsInStock());
                //intent.putExtra("product_id", data.get(i).getProductId());
                toEdit=sharedPreferenceUser.edit();
                toEdit.putString("ProductId", data.get(i).getProductId());
                toEdit.commit();
                mContext.startActivity(intent);
                Activity activity = (Activity) mContext;
                activity.finish();
                activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);


            }
        });
        holder.imgWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = Integer.parseInt(v.getTag().toString());
                String strCustId = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");

                if (data.get(pos).getHasProductInWishlist()==1){
                    utils.displayAlert("Product is already added in your WishList");
                }

                else if (!strCustId.equalsIgnoreCase("")){
                    addToWishList(strCustId, data.get(pos).getProductId().toString());
                    ImageView imgWish=(ImageView)parent.getChildAt(pos).findViewById(R.id.img_wish);
                    imgWish.setImageResource(R.drawable.wishlisticon_active);

                }else
                {
                    displayAlert("To proceed, sign into your account.");
                }
            }
        });
        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = Integer.parseInt(v.getTag().toString());
                str_old_price=data.get(pos).getProductPrice().toString();
                if(str_old_price.contains(","))
                    str_old_price=str_old_price.replace(",","");
                str_real_price=data.get(pos).getProductSpecialPrice().toString();
                if(str_real_price.contains(","))
                    str_real_price=str_old_price.replace(",","");

                optionId=data.get(pos).getProductSize().get(0).getOptionId();
                attId=data.get(pos).getProductSize().get(0).getAttributeId();
                delType=data.get(pos).getProductDelivery();

                getDialogDetails(pos);
            }
        });

        final ProgressBar progress_grid = (ProgressBar) convertView.findViewById(R.id.progress_grid);
        Glide.with(mContext) //Context
                .load(data.get(position).getProductImgUrl())  //URL/FILE
                //.placeholder(R.drawable.champagne)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progress_grid.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imgCat);

        return convertView;
    }

 public void displayAlert(String message)
    {
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        //alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("KenCommerce") ;
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent=new Intent(mContext, LoginActivity.class);
                intent.putExtra("context_act1", "webskitters.com.stockup.SubCategoryActivity");
                mContext.startActivity(intent);
                Activity activity = (Activity) mContext;
                activity.finish();
                activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        TextView myMsg = new TextView(mContext);
        myMsg.setText(message);
        myMsg.setPadding(20, 20, 20, 20);
        myMsg.setTextSize(16);
        //myMsg.setTypeface(typeFaceSegoeuiReg);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL);
        alertDialogBuilder.setView(myMsg);

        TextView title = new TextView(mContext);
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


    private void getDialogDetails(final int pos) {

        final Dialog dialog = new Dialog(mContext);
        mAddToCartTable=new AddToCartTable(mContext);
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
        TextView txt_description=(TextView)dialog.findViewById(R.id.txt_description);

        final TextView txt_real_price=(TextView)dialog.findViewById(R.id.txt_real_price);
        final TextView txt_old_price=(TextView)dialog.findViewById(R.id.txt_old_price);
        final TextView txt_qnty=(TextView)dialog.findViewById(R.id.txt_qnt);
        ImageView img_product=(ImageView)dialog.findViewById(R.id.img_product);

        txt_product_name.setText(data.get(pos).getProductName());
        Double price=Double.parseDouble(str_old_price)+Double.parseDouble(data.get(pos).getProductSize().get(0).getPricingValue());
        txt_old_price.setText("R"+nf.format(price));

        if(!str_real_price.equalsIgnoreCase("")){
            Double realprice=Double.parseDouble(str_real_price)+Double.parseDouble(data.get(pos).getProductSize().get(0).getPricingValue());
            txt_real_price.setText("R"+nf.format(realprice));

        }

        txt_description.setText(data.get(pos).getProductDescription());
        txt_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEdit=sharedPreferenceUser.edit();
                toEdit.putString("ProductId", data.get(pos).getProductId());
                toEdit.commit();
                Intent intent=new Intent(mContext, ProductDetailsActivity.class);
                mContext.startActivity(intent);
                Activity activity = (Activity) mContext;
                activity.finish();
                activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        txt_qnty.setText(data.get(pos).getProductSize().get(0).getLabel().toString());

            if(data.get(pos).getProductSize().get(0).getLabel().toString().equalsIgnoreCase("")){
                rel_qnt.setVisibility(View.GONE);
            }else{
                rel_qnt.setVisibility(View.VISIBLE);
            }


        if(data.get(pos).getProductIsSalable()==1){
            //txt_old_price.setText(data.get(pos).getProductPrice());
            txt_old_price.setGravity(Gravity.CENTER);
            txt_real_price.setVisibility(View.GONE);
            lin_cross.setVisibility(View.GONE);
        }else
        {
           // txt_real_price.setText(data.get(pos).getProductPrice());

        }

        if(data.get(pos).getProductIsSpecial()==0){
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
        Glide.with(mContext)
                .load(data.get(pos).getProductImgUrl())
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
                    Intent intent = new Intent(mContext, ShoppingListActivity.class);
                    intent.putExtra("productid", data.get(pos).getProductId());
                    intent.putExtra("qnty", txt_qnty.getText().toString());
                    mContext.startActivity(intent);
                    Activity activity = (Activity) mContext;
                    activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                else{
                    displayAlert("To proceed, kindly sign into your account.");
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
                    addToCart(data.get(pos).getProductId().toString(), txt_count_cart_add.getText().toString(), optionId, strCustId, attId);
                }else{
                        pDialog=new ProgressDialog(mContext);
                        pDialog.setMessage("Loading...");
                        pDialog.show();
                        ArrayList<String> arrProductIDS=new ArrayList<String>();
                        for(int a=0; a<mData.size(); a++){

                            arrProductIDS.add(mData.get(a).get("productid").toString());
                            if(mData.get(a).get("productid").toString().equalsIgnoreCase(data.get(pos).getProductId())){
                                prevQty=mData.get(a).get("qty").toString();
                            }
                        }
                    if(data.get(pos).getProductIsSpecial()==1){
                        Double price=Double.parseDouble(data.get(pos).getProductSpecialPrice().toString())+Double.parseDouble(data.get(pos).getProductSize().get(0).getPricingValue().toString());
                        if(arrProductIDS.contains(data.get(pos).getProductId())) {
                            mAddToCartTable.deleteitem(data.get(pos).getProductId());

                            int qnt = Integer.parseInt(prevQty) + Integer.parseInt(txt_count_cart_add.getText().toString());

                            mAddToCartTable.insert(data.get(pos).getProductId(), data.get(pos).getProductName(), ""+qnt,optionId, attId, price.toString(), delType);
                        }else{
                            mAddToCartTable.insert(data.get(pos).getProductId(), data.get(pos).getProductName(),  txt_count_cart_add.getText().toString(),optionId, attId, price.toString(), delType);
                        }
                        //mAddToCartTable.insert(data.get(pos).getProductId(), data.get(pos).getProductName(), txt_count_cart_add.getText().toString(), data.get(pos).getProductSize().get(0).getOptionId(), data.get(pos).getProductSize().get(0).getAttributeId(), data.get(pos).getProductSpecialPrice().toString());
                    }else {
                        Double price=Double.parseDouble(data.get(pos).getProductPrice().toString())+Double.parseDouble(data.get(pos).getProductSize().get(0).getPricingValue().toString());
                        if(arrProductIDS.contains(data.get(pos).getProductId())){
                            mAddToCartTable.deleteitem(data.get(pos).getProductId());

                            int qnt=Integer.parseInt(prevQty)+Integer.parseInt(txt_count_cart_add.getText().toString());
                            mAddToCartTable.insert(data.get(pos).getProductId(), data.get(pos).getProductName(), ""+qnt, optionId, attId, price.toString(), delType);
                        }else{
                            mAddToCartTable.insert(data.get(pos).getProductId(), data.get(pos).getProductName(),  txt_count_cart_add.getText().toString(),optionId,attId, price.toString(), delType);
                        }
                        //mAddToCartTable.insert(data.get(pos).getProductId(), data.get(pos).getProductName(), txt_count_cart_add.getText().toString(), data.get(pos).getProductSize().get(0).getOptionId(), data.get(pos).getProductSize().get(0).getAttributeId(), data.get(pos).getProductPrice());
                    }
                    pDialog.dismiss();
                    utils.displayAlert("Item has been added to cart.");
                    tota1_price=0.0;
                    mData=mAddToCartTable.getAll();
                    qtycount=0;
                    for(int i=0; i<mData.size(); i++){
                        String single_price=mData.get(i).get("price").toString();
                        if(single_price.startsWith("R")){
                            single_price= single_price.substring(1, single_price.length());
                        }
                        if(single_price!=null ||!single_price.equalsIgnoreCase("")){
                            double a=Double.parseDouble(single_price);
                            a=a*Integer.parseInt(mData.get(i).get("qty"));
                            tota1_price=tota1_price+a;
                            qtycount=qtycount+Integer.parseInt(mData.get(i).get("qty"));
                        }
                        btn_count.setText("" + qtycount);
                        SubCategoryActivity.txt_total_price.setText("R" + nf.format(tota1_price));
                    }

                }

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
                if (intCount>1) {
                    intCount--;
                    txt_count_cart_add.setText(String.valueOf(intCount));

                }
            }
        });
        dialog.show();
    }
    private void getCartTotal(String customer_id) {

        pDialog=new ProgressDialog(mContext);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait");
        restService.getCartTotal(customer_id, new RestCallback<CartTotalRequest>() {
            @Override
            public void success(CartTotalRequest object) {

                if(object.getStatus()==200&&object.getSuccess()==1){

                    btn_count.setText(object.getData().getTotalQty().toString());
                    SubCategoryActivity.txt_total_price.setText(object.getData().getCurrency()+object.getData().getTotalPrice());
                }
                if (pDialog != null)
                    pDialog.dismiss();
            }

            @Override
            public void invalid() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(mContext, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(mContext, "Error parsing tracking list", Toast.LENGTH_LONG).show();

            }
        });

    }


    private void addToCart(String product_id, String qnt, String option_id, final String customer_id, String Att_id) {
        pDialog = new ProgressDialog(mContext);
        pDialog.show();
        pDialog.setMessage("Loading... Please wait.");
        restService.addToCart(product_id, qnt, Att_id, customer_id, option_id, new RestCallback<AddToWishListRequest>() {
            @Override
            public void success(AddToWishListRequest object) {

                if (pDialog != null)
                    pDialog.dismiss();
                if(object.getStatus()==200&&object.getSuccess()==1){
                    getCartTotal(customer_id);
                    utils.displayAlert(object.getData().getSuccessMsg());

                }else{
                    utils.displayAlert(object.getErrorMsg());
                }
            }

            @Override
            public void invalid() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(mContext, "Problem while fetching tracking list", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure() {

                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(mContext, "Error parsing tracking list", Toast.LENGTH_LONG).show();
            }

        });
    }

    //////////////////Dropdown for Quantity/////////////////////////////////
    private void callPopUpDrinkItems(View anchorView, TextView txt_qnty, int pos, TextView txt_old_price, TextView txt_real_price) {

        pw = new PopupWindow(dropDownMenuDrinkItems(R.layout.pop_up_menu, new Vector(), txt_qnty, pos, txt_old_price,txt_real_price),anchorView.getWidth(), SubCategoryActivity.height/3, true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchorView);
        pw.update();
    }


    private View dropDownMenuDrinkItems(int layout, Vector menuItem, final TextView txt_qnt, final int pos, final TextView txt_old_price, final TextView txt_real_price)
    {
        View view = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null, false);

        if(data.get(pos).getProductSize().size()>0){
            arrItem = new ArrayList<HashMap<String, String>>();
            for(int i=0; i<data.get(pos).getProductSize().size(); i++){

                /*if(i==0)
                {
                    txt_qnt.setText(data.get(pos).getProductSize().get(0).getLabel());

                    Double old_price=Double.parseDouble(data.get(pos).getProductPrice().toString())+Double.parseDouble(data.get(pos).getProductSize().get(0).getPricingValue());
                    Double real_price=Double.parseDouble(data.get(pos).getProductSpecialPrice().toString())+Double.parseDouble(data.get(pos).getProductSize().get(0).getPricingValue());
                    txt_old_price.setText("R"+data.get(pos).getProductSize().get(0).getPricingValue());
                    txt_real_price.setText("R"+data.get(pos).getProductSize().get(0).getPricingValue());
                }*/

                HashMap<String, String> mapShopList = new HashMap<String, String>();
                mapShopList.put("ProductQnty", data.get(pos).getProductSize().get(i).getLabel());
                mapShopList.put("ProductPrice", data.get(pos).getProductSize().get(i).getPricingValue());
                mapShopList.put("ProductId", data.get(pos).getProductSize().get(i).getOptionId());
                arrItem.add(mapShopList);
            }
        }


        DrinkCatSpinnerAdapter searchLangAdapter = new DrinkCatSpinnerAdapter(mContext, arrItem);

        ListView listView = (ListView)view.findViewById(R.id.pop_up_menu_list);
        listView.setAdapter(searchLangAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                txt_qnt.setText(arrItem.get(position).get("ProductQnty").toString());
                Double old_price=Double.parseDouble(str_old_price.toString())+Double.parseDouble(arrItem.get(position).get("ProductPrice").toString());
                Double real_price=Double.parseDouble(str_real_price)+Double.parseDouble(arrItem.get(position).get("ProductPrice").toString());
                txt_old_price.setText("R"+nf.format(old_price));
                txt_real_price.setText("R"+nf.format(real_price));
                optionId=data.get(pos).getProductSize().get(position).getOptionId();
                attId=data.get(pos).getProductSize().get(position).getAttributeId();
                pw.dismiss();
            }
        });

        return view;
    }


    private void addToWishList(String strCustId, String product_id ) {
        final ProgressDialog pDialog=new ProgressDialog(mContext);
        pDialog.show();
        pDialog.setMessage("Adding to wish list...");
        restService.addToWish(strCustId, product_id, new RestCallback<AddToWishListRequest>() {

            @Override
            public void success(AddToWishListRequest responce) {
                pDialog.dismiss();
                int reqStatus = responce.getStatus();
                int reqSuccess = responce.getSuccess();
                if (reqStatus == 200 && reqSuccess == 1) {
                    utils.displayAlert(responce.getData().getSuccessMsg());
                } else {
                    utils.displayAlert(responce.getErrorMsg());
                }
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

    private void getDialogOK(String strMsg) {

        final Dialog dialog = new Dialog(mContext);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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

}
