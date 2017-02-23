package com.swash.kencommerce.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by Kranti on 12/12/2016.
 */
public class FVRSpinner extends Spinner {

    public FVRSpinner(Context context) {
        super(context);
    }

    public FVRSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FVRSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSelection(int position, boolean animate) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if (sameSelected) {
            /*TextView selectedView = (TextView) getSelectedView();
            selectedView.setTextSize(10);*/
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            if (getOnItemSelectedListener() != null) {
                getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
            }
        }
    }

    @Override
    public void setSelection(int position) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            if (getOnItemSelectedListener() != null) {
                getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
            }
        }
    }
}
