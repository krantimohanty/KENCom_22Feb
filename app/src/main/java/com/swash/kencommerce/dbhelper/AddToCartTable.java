package com.swash.kencommerce.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class AddToCartTable extends SQLiteOpenHelper {

    public static ArrayList<Integer> arrProductImage= new ArrayList<Integer>();

    public static final String DATABASE_NAME = "kencommercedb";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "AddToCartTable";
    public static final String COL_ID = "_id";
    public static final String STRING_COL_ID = "strid";
    public static final String PRODUCTID = "product_id";
    public static final String PRODUCTNAME = "product_name";
    public static final String PRODUCTPRICE = "product_price";
    public static final String PRODUCTOPTIONID = "productoptionid";
    public static final String PRODUCTQNTY = "productqnty";
    public static final String PRODUCTATTID = "productattid";
    public static final String PRODUCTDELTYPE = "productdeltype";
    private ArrayList<HashMap<String, String>> listAddToCartList;

    public AddToCartTable(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, product_id TEXT, product_name TEXT, productoptionid TEXT," +
                "productqnty TEXT, productattid TEXT, product_price TEXT, productdeltype TEXT);");
        //db.execSQL("CREATE TABLE IF NOT EXISTS "+ "abc" +" (_id INTEGER PRIMARY KEY, area TEXT, sales_person_name TEXT, month TEXT, target_rp TEXT,target_qty TEXT,standard_visit TEXT, standard_wh TEXT);");
        Log.d("ContactTable:", "Table Created");

    }

    public void insert(String ID,String Name, String qnty, String optionid, String attid, String price, String deltype)
    {
        ContentValues cv = new ContentValues();
        cv.put(PRODUCTID, ID);
        cv.put(PRODUCTNAME, Name);
        cv.put(PRODUCTQNTY, qnty);
        cv.put(PRODUCTOPTIONID, optionid);
        cv.put(PRODUCTATTID, attid);
        cv.put(PRODUCTPRICE, price);
        cv.put(PRODUCTPRICE, price);
        cv.put(PRODUCTDELTYPE, deltype);
        getWritableDatabase().insert(TABLE_NAME, null, cv);
        Log.d("ContactTable:", "Data Inserted");
    }
    public void deleteAll(){
        getWritableDatabase().delete(TABLE_NAME, null , null);
    }

    public int getCount(){
        Cursor c = getWritableDatabase().rawQuery("select * from "+ TABLE_NAME,null);
        int count = c.getCount();
        return  count;
    }

    public void delete()
    {
        getWritableDatabase().execSQL("DELETE FROM " + TABLE_NAME + ";");
    }
    /*public void update(String id,String socialMediaName, String profileName, String profileURLLink, String profileImageLink)
    {
        ContentValues cv = new ContentValues();
        String[] args = { id };

        cv.put(SOCIALMEDIANAME, socialMediaName);
        cv.put(PROFILENAME, profileName);
        cv.put(PROFILEIMAGE, profileImageLink);
        cv.put(PROFILEURL, profileURLLink);
        if(socialMediaName.equalsIgnoreCase("FACEBOOK")){
            cv.put(FACEBOOKTAG, profileURLLink);

        }else if(socialMediaName.equalsIgnoreCase("TWITTER")){

            cv.put(TWITTERTAG, profileURLLink);

        }
        else if(socialMediaName.equalsIgnoreCase("LINKEDIN")){

            cv.put(LINKEDINTAG, profileURLLink);

        }
        else if(socialMediaName.equalsIgnoreCase("INSTAGRAM")){

            cv.put(INSTAGRAMTAG, profileURLLink);

        }
        else if(socialMediaName.equalsIgnoreCase("GOOGLE PLUS")){

            cv.put(GOOGLEPLUSTAG, profileURLLink);

        }
        else if(socialMediaName.equalsIgnoreCase("FLICKR")){

            cv.put(FLICKRTAG, profileURLLink);

        }
        else if(socialMediaName.equalsIgnoreCase("SKYPE")){

            cv.put(SKYPETAG, profileURLLink);
        }
        getWritableDatabase().update(TABLE_NAME , cv, "_id=?", args);
        getWritableDatabase().close();
        Log.d("praxairsheet:", "Data Inserted");
    }*/
    public ArrayList<HashMap<String, String>> getAll()
    {

        listAddToCartList = new ArrayList<HashMap<String, String>>();

        Cursor cur=null;
        SQLiteDatabase db= this.getReadableDatabase();

        cur=(db.rawQuery("SELECT  * FROM "+ TABLE_NAME +" ", null));

        if(cur.moveToFirst()){
            do{
                HashMap<String, String> mapShopList = new HashMap<String, String>();
                mapShopList.put("productid", cur.getString(1));
                mapShopList.put("productname", cur.getString(2));
                mapShopList.put("qty", cur.getString(4));
                mapShopList.put("productoptionid", cur.getString(3));
                mapShopList.put("productattid", cur.getString(5));
                mapShopList.put("price", cur.getString(6));
                mapShopList.put("deltype", cur.getString(7));
                //arrProductImage.add(cur.getInt(3));
                listAddToCartList.add(mapShopList);
            }while(cur.moveToNext());
        }
        cur.close();
        db.close();

        return listAddToCartList;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
    public void deleteitem(String strid){
        getWritableDatabase().delete(TABLE_NAME, PRODUCTID + " = ?", new String[]{strid});

    }
    public boolean favoriteDelete(String id) {
        SQLiteDatabase db= this.getReadableDatabase();
        return db.delete(TABLE_NAME, COL_ID +  "=" + id, null) > 0;
    }
    /*public Cursor getByInspectionName(String name)
    {
        Constants.facilityID="";
        String[] args = {name};
        Cursor cur=null;
        SQLiteDatabase db= this.getReadableDatabase();
        ///getReadableDatabase();

        cur=(db.rawQuery("SELECT _id FROM "+ TABLE_NAME +" WHERE profilename=?", args));

        if(cur.moveToFirst()){
            do{

                Constants.facilityID=cur.getString(0);

            }while(cur.moveToNext());
        }

        cur.close();
        db.close();
        return cur;
    }*/


}
