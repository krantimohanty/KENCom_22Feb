package com.swash.kencommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swash.kencommerce.MyShoppingListActivity;
import com.swash.kencommerce.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kranti on 12/12/2016.
 */
public class ShoppingListAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    ViewHolder holder = null;
    public static String list_ids ="",list_ids_all="";
    public static ArrayList<String> arrIDs;
    public ShoppingListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        arrIDs=new ArrayList<>();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        TextView txt_shoping_list;
        CheckBox chk;
        RelativeLayout rel_parent;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_shopping_list, null);
            holder = new ViewHolder();
            holder.rel_parent=(RelativeLayout)convertView.findViewById(R.id.rel_parent);
            holder.txt_shoping_list = (TextView) convertView.findViewById(R.id.txt_shoping_list);
            holder.chk = (CheckBox) convertView.findViewById(R.id.chk_unique_id);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        //holder.chk.setTag(position);
        holder.rel_parent.setTag(position);
        holder.rel_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=Integer.parseInt(v.getTag().toString());
                View pv = (View)v.getParent();
                holder.chk=(CheckBox)pv.findViewById(R.id.chk_unique_id);
                //data.get(i).getId();
                String id= data.get(i).get(MyShoppingListActivity.Key_shoppingListId);

                if( holder.chk.isChecked()){
                    arrIDs.remove(id);
                    holder.chk.setChecked(false);
                    holder.chk.setButtonDrawable(R.drawable.radio_uncheck);

                }else{
                        arrIDs.add(id);
                        list_ids_all = arrIDs.toString();
                        //list_ids = (list_ids_all.substring(1, list_ids_all.length()-1));
                        holder.chk.setChecked(true);
                        holder.chk.setButtonDrawable(R.drawable.radio_check);

                }


            }
        });


       /* JSONArray jsonArray=new JSONArray();
        JSONObject jObjImages;
        for(int i=0; i< arrIDs.size(); i++){
            jObjImages=new JSONObject();
            try {
                jObjImages.put("attribute_id", arrIDs.get(i).get("id"));

                jsonArray.put(i, jObjImages);
               // Toast.makeText(activity, "JSONDATA: " + jsonArray.toString(), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //listAddToCartList.add(mapShopList);
        }*/

         /*holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(buttonView.isChecked()){
                    buttonView.setChecked(true);
                    buttonView.setButtonDrawable(R.drawable.close_offer_checkbox_sel);
                }else{

                    buttonView.setChecked(false);
                    buttonView.setButtonDrawable(R.drawable.close_offer_checkbox);
                }
            }
        });*/
       /* HashMap<String, String> mapShopList = new HashMap<String, String>();
        mapShopList = data.get(position);*/


        if (MyShoppingListAdapter.customar_list_id.length() > 0){

            if (MyShoppingListAdapter.customar_list_id.equalsIgnoreCase(data.get(position).get(MyShoppingListActivity.Key_shoppingListId))) {
                holder.rel_parent.setVisibility(View.GONE);
            } else {
                holder.rel_parent.setVisibility(View.VISIBLE);
                holder.txt_shoping_list.setText(data.get(position).get(MyShoppingListActivity.Key_shoppingListName));
            }



        }else {
            holder.rel_parent.setVisibility(View.VISIBLE);
            holder.txt_shoping_list.setText(data.get(position).get(MyShoppingListActivity.Key_shoppingListName));
        }
        return convertView;
    }

    //recursive blind checks removal for everything inside a View
    private void removeAllChecks(ViewGroup vg) {
        View v = null;
        for(int i = 0; i < vg.getChildCount(); i++){
            try {
                v = vg.getChildAt(i);
                ((CheckBox)v).setChecked(false);
                ((CheckBox)v).setButtonDrawable(R.drawable.radio_uncheck);
            }
            catch(Exception e1){ //if not checkBox, null View, etc
                try {
                    removeAllChecks((ViewGroup)v);
                }
                catch(Exception e2){ //v is not a view group
                    continue;
                }
            }
        }

    }
}
