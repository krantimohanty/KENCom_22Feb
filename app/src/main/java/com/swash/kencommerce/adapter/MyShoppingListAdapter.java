package com.swash.kencommerce.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swash.kencommerce.MyShoppingListActivity;
import com.swash.kencommerce.MyShoppingListAllItemsActivity;
import com.swash.kencommerce.R;
import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.model.RemoveShoppingListRequest;
import com.swash.kencommerce.retrofit_call.RestCallback;
import com.swash.kencommerce.retrofit_call.RestService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kranti on 12/12/2016.
 */
public class MyShoppingListAdapter extends BaseAdapter {
    private Activity activity;
    //private ArrayList<ShoppingListItem> data;
    private ArrayList<HashMap<String, String>> data;
    HashMap<String, String> mapShopSearchList = new HashMap<String, String>();

    private static LayoutInflater inflater = null;
    ViewHolder holder = null;
    public static String customar_list_id, shopping_list_name, shopping_list_item_count;
    String userId;
    SharedPreferences sharedPreferenceUser;
    RestService restService;


    public MyShoppingListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        restService = new RestService();
        sharedPreferenceUser = activity.getSharedPreferences(Constants.strShPrefUserPrefName, Activity.MODE_PRIVATE);
        userId = sharedPreferenceUser.getString(Constants.strShPrefUserId, "");
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

    public class ViewHolder {
        TextView txt_shoping_list, tv_item_count, txt_customar_list_id;
        ImageView img_arrow;
        RelativeLayout rel_parent;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_my_shopping_list, null);
            holder = new ViewHolder();
            holder.rel_parent = (RelativeLayout) convertView.findViewById(R.id.rel_parent);
            holder.txt_shoping_list = (TextView) convertView.findViewById(R.id.txt_shoping_list);
            holder.tv_item_count = (TextView) convertView.findViewById(R.id.txt_shoping_list_count);
            holder.txt_customar_list_id = (TextView) convertView.findViewById(R.id.txt_customar_list_id);
            holder.img_arrow = (ImageView) convertView.findViewById(R.id.chk_unique_id);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        mapShopSearchList = new HashMap<String, String>();
        mapShopSearchList = data.get(position);

        //holder.chk.setTag(position);
        holder.rel_parent.setTag(position);
        holder.rel_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int i = Integer.parseInt(v.getTag().toString());
                //customar_list_id = data.get(i).get(MyShoppingListActivity.Key_shoppingListId);
                mapShopSearchList = new HashMap<String, String>();
                mapShopSearchList = data.get(i);
                customar_list_id = mapShopSearchList.get(MyShoppingListActivity.Key_shoppingListId);
                getDialog(customar_list_id);
                return true;
            }
        });
        holder.rel_parent.setTag(position);
        holder.rel_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position= (int) v.getTag();
                int i = Integer.parseInt(v.getTag().toString());
                mapShopSearchList = new HashMap<String, String>();
                mapShopSearchList = data.get(i);

                Intent intent = new Intent(activity, MyShoppingListAllItemsActivity.class);
                customar_list_id = mapShopSearchList.get(MyShoppingListActivity.Key_shoppingListId);
                shopping_list_name = mapShopSearchList.get(MyShoppingListActivity.Key_shoppingListName);
                shopping_list_item_count = mapShopSearchList.get(MyShoppingListActivity.Key_shoppingList_Count);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                activity.finish();

            }
        });

        String item_count = data.get(position).get(MyShoppingListActivity.Key_shoppingList_Count);
        holder.txt_shoping_list.setText(data.get(position).get(MyShoppingListActivity.Key_shoppingListName));
        if (Integer.parseInt(item_count) <= 1) {
            holder.tv_item_count.setText(item_count + " " + "item");
        } else if (Integer.parseInt(item_count) > 1) {
            holder.tv_item_count.setText(item_count + " " + "item(s)");
        }
        holder.txt_customar_list_id.setText(data.get(position).get(MyShoppingListActivity.Key_shoppingListId));

        /*holder.txt_shoping_list.setText(data.get(position).get(MyShoppingListActivity.Key_shoppingListName));
        holder.tv_item_count.setText(data.get(position).get(MyShoppingListActivity.Key_shoppingList_Count));
        holder.txt_customar_list_id.setText(data.get(position).get(MyShoppingListActivity.Key_shoppingListId));
*/
        return convertView;
    }


    private void getDialog(final String list_id) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_remove_list);
        activity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvHeader = (TextView) dialog.findViewById(R.id.header);
        tvHeader.setText("KenCommerce");
        TextView tvMsg = (TextView) dialog.findViewById(R.id.textView_remove_list);
        tvMsg.setText("Are you sure you wish to delete this shopping list?");
        TextView btn_no = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView btn_yes = (TextView) dialog.findViewById(R.id.btn_ok);


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
                /*data.remove(i);
                notifyDataSetChanged();
                MyShoppingListActivity.tv_shopping_list_count.setText("Shopping list (" + data.size()+")");*/
                getListItemDataDelete(userId, list_id);
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void getListItemDataDelete(String customerid, String list_id) {
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.show();
        pDialog.setMessage("Please wait..");

        restService.getRemoveShoppingList(list_id, customerid, new RestCallback<RemoveShoppingListRequest>() {
            @Override
            public void success(RemoveShoppingListRequest response) {
                //Log.d("Result", response.getSuccess_msg());
                if (pDialog != null)
                    pDialog.dismiss();
                String reqSuccess = String.valueOf(response.getSuccess());
                Log.d("reqSuccess", reqSuccess);

                if (reqSuccess.equalsIgnoreCase(String.valueOf(response.getSuccess()))) {
                    //Toast.makeText(activity, "The shopping list was successfully deleted.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(activity, MyShoppingListActivity.class);
                    activity.finish();
                    activity.startActivity(i);
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
}
