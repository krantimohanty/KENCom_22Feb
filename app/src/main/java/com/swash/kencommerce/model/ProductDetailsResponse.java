package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/1/2016.
 */
public class ProductDetailsResponse {

    @SerializedName("related_data")
    @Expose
    private List<RelatedData> relatedData = new ArrayList<RelatedData>();
    @SerializedName("product_data")
    @Expose
    private ProductData productData;

    /**
     *
     * @return
     * The relatedData
     */
    public List<RelatedData> getRelatedData() {
        return relatedData;
    }

    /**
     *
     * @param relatedData
     * The related_data
     */
    public void setRelatedData(List<RelatedData> relatedData) {
        this.relatedData = relatedData;
    }

    /**
     *
     * @return
     * The productData
     */
    public ProductData getProductData() {
        return productData;
    }

    /**
     *
     * @param productData
     * The product_data
     */
    public void setProductData(ProductData productData) {
        this.productData = productData;
    }
}
