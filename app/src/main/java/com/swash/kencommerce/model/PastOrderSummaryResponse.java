package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kranti on 10/4/2016.
 */
public class PastOrderSummaryResponse {
    @SerializedName("order_summary")
    @Expose
    private PastOrderSummary orderSummary;

    /**
     *
     * @return
     * The orderSummary
     */
    public PastOrderSummary getOrderSummary() {
        return orderSummary;
    }

    /**
     *
     * @param orderSummary
     * The order_summary
     */
    public void setOrderSummary(PastOrderSummary orderSummary) {
        this.orderSummary = orderSummary;
    }
}
