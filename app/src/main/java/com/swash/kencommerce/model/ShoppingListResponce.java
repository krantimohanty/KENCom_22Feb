package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/3/2016.
 */
public class ShoppingListResponce {



    @SerializedName("shopping_list")
    @Expose
    private List<ShoppingListItem> ShoppingListItem = new ArrayList<ShoppingListItem>();

    /**
     *
     * @return
     * The shoppingList
     */
    public List<ShoppingListItem> getShoppingList() {
        return ShoppingListItem;
    }

    /**
     *
     * @param ShoppingListItem
     * The shopping_list
     */
    public void setShoppingList(List<ShoppingListItem> ShoppingListItem) {
        this.ShoppingListItem = ShoppingListItem;
    }
}
