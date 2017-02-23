package com.swash.kencommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swash.kencommerce.R;
import com.swash.kencommerce.model.Rating;

import java.util.List;

/**
 * Created by Kranti on 12/12/2016.
 */
public class ViewAllRatesReviewListAdapter extends BaseAdapter {
    private Context mContext;
    List<Rating> arrCatName;

    public ViewAllRatesReviewListAdapter(Context c, List<Rating> arrCatName) {
        this.mContext = c;
        this.arrCatName=arrCatName;
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

        TextView tv_count, txtRating_title, txt_rating_details;
    }
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;

        ViewHolder holder = null;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = inflater.inflate(R.layout.addview_rating_list_item, null);
            holder = new ViewHolder();

            holder.txtRating_title = (TextView) convertView.findViewById(R.id.txt_rating_title);
            holder.txt_rating_details = (TextView) convertView.findViewById(R.id.txt_rating_details_msg);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_rating_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtRating_title.setText(arrCatName.get(position).getReviewHeading());
        holder.txt_rating_details.setText(arrCatName.get(position).getReviewDetails());
        holder.tv_count.setText(arrCatName.get(position).getReviewRating().toString());

        return convertView;

    }


}
