package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 9/30/2016.
 */
public class MainCategoryResponse {

    @SerializedName("main_category_list")
    @Expose
    private ArrayList<MainCategoryList> mMainCategoryList = new ArrayList<MainCategoryList>();

    /**
     *
     * @return
     * The subCategoryList
     */
    public List<MainCategoryList> getSubCategoryList() {
        return mMainCategoryList;
    }

    /**
     *
     * @param mMainCategoryList
     * The sub_category_list
     */
    public void setSubCategoryList(ArrayList<MainCategoryList> mMainCategoryList) {
        this.mMainCategoryList = mMainCategoryList;
    }

}
