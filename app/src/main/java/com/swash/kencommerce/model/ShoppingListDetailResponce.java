package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/3/2016.
 */
public class ShoppingListDetailResponce {




    @SerializedName("product_list")
    @Expose
    private List<ShoppingListDetailListItem> ShoppingListDetailListItem = new ArrayList<ShoppingListDetailListItem>();

    /**
     *
     * @return
     * The productList
     */
    public List<ShoppingListDetailListItem> getShoppingListDetailList() {
        return ShoppingListDetailListItem;
    }

    /**
     *
     * @param productList
     * The product_list
     */
    public void setProductList(List<ProductList> productList) {
        this.ShoppingListDetailListItem = ShoppingListDetailListItem;
    }

}
