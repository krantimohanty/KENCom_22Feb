package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/5/2016.
 */
public class GetCartListResponse {

    @SerializedName("items")
    @Expose
    private List<CartData> items = new ArrayList<CartData>();

    /**
     *
     * @return
     * The items
     */
    public List<CartData> getItems() {
        return items;
    }

    /**
     *
     * @param items
     * The items
     */
    public void setItems(List<CartData> items) {
        this.items = items;
    }
}
