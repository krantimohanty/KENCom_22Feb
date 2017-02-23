package com.swash.kencommerce.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;


/**
 * Created by Kranti on 12/12/2016.
 */

public class KenCommerceTextView extends TextView {
    public KenCommerceTextView(Context context) {
        super(context);
    }

    /*public GlobalMileDatingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }*/

   /* private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.ViewStyle);
        String customFont = a.getString(R.styleable.ViewStyle_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }*/

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            return false;
        }
        setTypeface(tf);
        return true;
    }
}
