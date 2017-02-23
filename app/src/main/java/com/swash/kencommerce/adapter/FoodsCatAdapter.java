package com.swash.kencommerce.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.swash.kencommerce.R;

import java.util.ArrayList;

/**
 * Created by Kranti on 12/12/2016.
 */
public class FoodsCatAdapter extends BaseAdapter {
    private final Typeface typeFaceSegoeuiReg;
    private Context mContext;
    ArrayList<String> arrCatName;

    public FoodsCatAdapter(Context c, ArrayList<String> arrCatName) {
        this.mContext = c;
        this.arrCatName=arrCatName;
        typeFaceSegoeuiReg = Typeface.createFromAsset(mContext.getAssets(),
                "Roboto-Regular.ttf");
    }

    public int getCount() {
        return mThumbIds.length;
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
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvCatName.setTypeface(typeFaceSegoeuiReg);
        holder.tvCatName.setText(arrCatName.get(position).toString());
        holder.imgCat.setImageResource(mThumbIds[position]);

        return convertView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.food_cat_1, R.drawable.food_cat_2,
           /* R.drawable.foods, R.drawable.foods,
            R.drawable.foods, R.drawable.foods,
            R.drawable.foods, R.drawable.foods,
            R.drawable.foods,*/
    };
}
