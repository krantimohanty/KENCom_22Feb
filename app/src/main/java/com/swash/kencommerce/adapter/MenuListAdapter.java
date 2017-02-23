package com.swash.kencommerce.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.swash.kencommerce.R;
import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.model.SlideMenuOption;

import java.util.ArrayList;


/**
 * Created by Kranti on 12/12/2016.
 */

public class MenuListAdapter extends BaseExpandableListAdapter {

    private SharedPreferences sharedPreferenceUser;
    private Context _context;
    private ArrayList<SlideMenuOption> _listDataHeader; // header titles
    String customer_id="", customer_name="";
    // child data in format of header title, child title

    public MenuListAdapter(Context context, ArrayList<SlideMenuOption> slideMenuOptions) {
        this._context = context;
        this._listDataHeader = slideMenuOptions;
        sharedPreferenceUser=_context.getSharedPreferences(Constants.strShPrefUserPrefName, Context.MODE_PRIVATE);
        customer_id=sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
        customer_name=sharedPreferenceUser.getString(Constants.strShPrefUserFname, "");
    }

    @Override
    public SlideMenuOption getChild(int groupPosition, int childPosititon) {
        return _listDataHeader.get(groupPosition).getSlideMenuOptionsChild().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adapter_menu_child, null);
        }

        TextView txtListChild = (TextView) convertView .findViewById(R.id.txt_name);

        if(_listDataHeader.get(groupPosition).getSlideMenuOptionsChild().get(childPosition).getName().equalsIgnoreCase(Constants.strCatName)){
            txtListChild.setTextColor(Color.parseColor("#048bcd"));
        }else
        {
            txtListChild.setTextColor(Color.BLACK);
        }

        ImageView imgList=(ImageView)convertView.findViewById(R.id.img_menu);
        imgList.setImageResource(_listDataHeader.get(groupPosition).getSlideMenuOptionsChild().get(childPosition).getImage());
        imgList.setImageResource(mThumbIds[_listDataHeader.get(groupPosition).getSlideMenuOptionsChild().get(childPosition).getId()-1]);
        txtListChild.setText(_listDataHeader.get(groupPosition).getSlideMenuOptionsChild().get(childPosition).getName());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (_listDataHeader.get(groupPosition).getSlideMenuOptionsChild())
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adapter_menu_group, null);
        }

        TextView txt_indicator = (TextView) convertView.findViewById(R.id.txt_indicator);
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.txt_name);
        ImageView imgList=(ImageView)convertView.findViewById(R.id.img_menu);

        imgList.setImageResource(mThumbIds[groupPosition]);
        lblListHeader.setText(_listDataHeader.get(groupPosition).getName());

        if (_listDataHeader.get(groupPosition).getSlideMenuOptionsChild() != null && _listDataHeader.get(groupPosition).getSlideMenuOptionsChild().size() > 0) {
            txt_indicator.setVisibility(View.VISIBLE);
        } else {
            txt_indicator.setVisibility(View.GONE);
        }

        if(!customer_id.equalsIgnoreCase("")&&_listDataHeader.get(groupPosition).getName().equalsIgnoreCase("Sign In")){
            lblListHeader.setText("Sign Out");
        }
        if(!customer_name.equalsIgnoreCase("")&&_listDataHeader.get(groupPosition).getName().equalsIgnoreCase("Profile")){
            lblListHeader.setText(customer_name);
        }
        if (isExpanded) {
           // txt_indicator.setText("-");
            txt_indicator.setBackgroundResource(R.drawable.minus_icon);
        } else {
            //txt_indicator.setText("+");
            txt_indicator.setBackgroundResource(R.drawable.plus_icon);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.mainstore_icon, R.drawable.profile_icon,
            R.drawable.cart_icon, R.drawable.signin_icon,

            R.drawable.support_icon, R.drawable.drinks_icon,
            R.drawable.foodnew_s_icon,R.drawable.day_2_day_icon,
            R.drawable.gift_icon,R.drawable.events_icon,
            R.drawable.profile_icon,
            R.drawable.payment_icon,R.drawable.address_icon,
            R.drawable.wishlist_icon,R.drawable.shopinglist_icon,
            R.drawable.pastorder_icon,
            R.drawable.serve_icon,
            R.drawable.faq_icon,R.drawable.privacy_policy_icon,
            R.drawable.terms_icon
    };

}
