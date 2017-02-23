package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/4/2016.
 */
public class PastOrderResponse {
    @SerializedName("order_data")
    @Expose
    private List<PastOrder> orderData = new ArrayList<PastOrder>();

    /**
     *
     * @return
     * The orderData
     */
    public List<PastOrder> getOrderData() {
        return orderData;
    }

    /**
     *
     * @param orderData
     * The order_data
     */
    public void setOrderData(List<PastOrder> orderData) {
        this.orderData = orderData;
    }
}
