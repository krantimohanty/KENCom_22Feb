package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kranti on 10/4/2016.
 */
public class PastOrderInfo {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_qty")
    @Expose
    private String productQty;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("currency")
    @Expose
    private String currency;

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
     * The productImage
     */
    public String getProductImage() {
        return productImage;
    }

    /**
     *
     * @param productImage
     * The product_image
     */
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    /**
     *
     * @return
     * The productQty
     */
    public String getProductQty() {
        return productQty;
    }

    /**
     *
     * @param productQty
     * The product_qty
     */
    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    /**
     *
     * @return
     * The totalPrice
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     *
     * @param totalPrice
     * The total_price
     */
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     *
     * @return
     * The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     *
     * @param currency
     * The currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
