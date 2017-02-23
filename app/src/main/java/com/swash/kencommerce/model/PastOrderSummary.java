package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kranti on 10/4/2016.
 */
public class PastOrderSummary {
    @SerializedName("delivery_address")
    @Expose
    private String deliveryAddress;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("order_total")
    @Expose
    private String orderTotal;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("content")
    @Expose
    private String content;

    /**
     *
     * @return
     * The deliveryAddress
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     *
     * @param deliveryAddress
     * The delivery_address
     */
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     *
     * @return
     * The telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     *
     * @param telephone
     * The telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     *
     * @return
     * The orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     *
     * @param orderId
     * The order_id
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The qty
     */
    public String getQty() {
        return qty;
    }

    /**
     *
     * @param qty
     * The qty
     */
    public void setQty(String qty) {
        this.qty = qty;
    }

    /**
     *
     * @return
     * The orderTotal
     */
    public String getOrderTotal() {
        return orderTotal;
    }

    /**
     *
     * @param orderTotal
     * The order_total
     */
    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
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

    /**
     *
     * @return
     * The paymentMethod
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     *
     * @param paymentMethod
     * The payment_method
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     *
     * @return
     * The clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     *
     * @param clientName
     * The client_name
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     *
     * @return
     * The deliveryDate
     */
    public String getDeliveryDate() {
        return deliveryDate;
    }

    /**
     *
     * @param deliveryDate
     * The delivery_date
     */
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     *
     * @return
     * The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     * The content
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @param content
     * The content
     */
    public void setContent(String content) {
        this.content = content;
    }
}
