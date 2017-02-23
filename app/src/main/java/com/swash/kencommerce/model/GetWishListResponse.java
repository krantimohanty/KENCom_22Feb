package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/3/2016.
 */
public class GetWishListResponse {
    @SerializedName("wish_list")
    @Expose
    private List<GetWishList> wishList = new ArrayList<GetWishList>();

    /**
     *
     * @return
     * The wishList
     */
    public List<GetWishList> getWishList() {
        return wishList;
    }

    /**
     *
     * @param wishList
     * The wish_list
     */
    public void setWishList(List<GetWishList> wishList) {
        this.wishList = wishList;
    }
}
