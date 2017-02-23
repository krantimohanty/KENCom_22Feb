package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/1/2016.
 */
public class SearchResultResponse {
    @SerializedName("product_data")
    @Expose
    private List<SearchResultItem> productData = new ArrayList<SearchResultItem>();

    /**
     *
     * @return
     * The productData
     */
    public List<SearchResultItem> getProductData() {
        return productData;
    }

    /**
     *
     * @param productData
     * The product_data
     */
    public void setProductData(List<SearchResultItem> productData) {
        this.productData = productData;
    }
}
