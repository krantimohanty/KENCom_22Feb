package com.swash.kencommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.swash.kencommerce.R;
import com.swash.kencommerce.SubCategoryActivity;
import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.model.MainCategoryList;

import java.util.List;

/**
 * Created by Kranti on 12/12/2016.
 */
public class EventCatAdapter extends BaseAdapter {


    SharedPreferences shPrefUserSelection;
    SharedPreferences.Editor toEdit;
    private Context mContext;
    List<MainCategoryList> arrCatName;
    private Typeface typeFaceSegoeuiReg;

    public EventCatAdapter(Context c, List<MainCategoryList> arrCatName) {
        this.mContext = c;
        this.arrCatName=arrCatName;
        typeFaceSegoeuiReg = Typeface.createFromAsset(mContext.getAssets(), "Roboto-Regular.ttf");
        shPrefUserSelection = mContext.getSharedPreferences(Constants.strShPrefUserSelection, Context.MODE_PRIVATE);


    }

    public int getCount() {
        return arrCatName.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{

        ImageView imgCat;
        TextView tvCatName;
    }
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        ViewHolder holder = null;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = inflater.inflate(R.layout.addview_drink_cat_item, null);
            holder = new ViewHolder();

            holder.tvCatName = (TextView) convertView.findViewById(R.id.tv_cat_name);
            holder.imgCat = (ImageView) convertView.findViewById(R.id.img_cat);

            //holder.imgCat.setLayoutParams(new GridView.LayoutParams(85, 85));
            holder.imgCat.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.imgCat.setPadding(8, 8, 8, 8);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvCatName.setTypeface(typeFaceSegoeuiReg);
        holder.tvCatName.setText(arrCatName.get(position).getCatName().toString().trim());
        holder.imgCat.setId(position);
        holder.imgCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=v.getId();
                toEdit = shPrefUserSelection.edit();
                toEdit.putString(Constants.strShUserProductId, arrCatName.get(i).getCatId().toString());
                toEdit.commit();
                Intent intent=new Intent(mContext, SubCategoryActivity.class);
                //intent.putExtra("product_id",arrCatName.get(i).getCatId().toString());
                mContext.startActivity(intent);

            }
        });
        /*Glide.with(mContext).load(arrCatName.get(position).getCatImgUrl().toString().trim()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imgCat) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(false);
                holder.imgCat.setImageBitmap(resource);
            }
        });*/
        Glide.with(mContext) //Context
                .load(arrCatName.get(position).getCatImgUrl().toString()).placeholder(R.drawable.wine_cat_drink) //URL/FILE
                .into(holder.imgCat);
        return convertView;
    }

    /*// references to our images
    private Integer[] mThumbIds = {
            R.drawable.champagne_cat_drink, R.drawable.wine_cat_drink,
            *//*R.drawable.champagne, R.drawable.champagne,
            R.drawable.champagne, R.drawable.champagne,
            R.drawable.champagne, R.drawable.champagne,
            R.drawable.champagne,*//*
    };*/
}
