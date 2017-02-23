package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/4/2016.
 */
public class PastOrderInfoResponse {
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_data")
    @Expose
    private List<PastOrderInfo> orderData = new ArrayList<PastOrderInfo>();
    @SerializedName("order_total")
    @Expose
    private String orderTotal;

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
     * The orderData
     */
    public List<PastOrderInfo> getOrderData() {
        return orderData;
    }

    /**
     *
     * @param orderData
     * The order_data
     */
    public void setOrderData(List<PastOrderInfo> orderData) {
        this.orderData = orderData;
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
}
