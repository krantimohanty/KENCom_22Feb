package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kranti on 10/6/2016.
 */
public class GetDeliveryFee {
    @SerializedName("shipping_title")
    @Expose
    private String shippingTitle;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("currency")
    @Expose
    private String currency;

    /**
     *
     * @return
     * The shippingTitle
     */
    public String getShippingTitle() {
        return shippingTitle;
    }

    /**
     *
     * @param shippingTitle
     * The shipping_title
     */
    public void setShippingTitle(String shippingTitle) {
        this.shippingTitle = shippingTitle;
    }

    /**
     *
     * @return
     * The amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     *
     * @param amount
     * The amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
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
