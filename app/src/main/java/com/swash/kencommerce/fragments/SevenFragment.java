package com.swash.kencommerce.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swash.kencommerce.R;
import com.swash.kencommerce.MainDetailsActivity;


public class SevenFragment extends Fragment {
    ImageView img_tab;

    public SevenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seven, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initFields();
    }

    private void initFields() {
        img_tab = ((MainDetailsActivity) getActivity()).img_tab;
        img_tab.setBackgroundResource(R.drawable.bottom_image_day);
    }

}
