package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kranti on 10/1/2016.
 */
public class FeaturedProductList {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_img_url")
    @Expose
    private String productImgUrl;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("product_price")
    @Expose
    private String productPrice;

    /**
     *
     * @return
     * The productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     *
     * @param productId
     * The product_id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     *
     * @return
     * The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     *
     * @param productName
     * The product_name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     *
     * @return
     * The productImgUrl
     */
    public String getProductImgUrl() {
        return productImgUrl;
    }

    /**
     *
     * @param productImgUrl
     * The product_img_url
     */
    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    /**
     *
     * @return
     * The productUrl
     */
    public String getProductUrl() {
        return productUrl;
    }

    /**
     *
     * @param productUrl
     * The product_url
     */
    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    /**
     *
     * @return
     * The productPrice
     */
    public String getProductPrice() {
        return productPrice;
    }

    /**
     *
     * @param productPrice
     * The product_price
     */
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
