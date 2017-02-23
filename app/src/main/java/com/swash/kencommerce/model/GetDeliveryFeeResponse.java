package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/6/2016.
 */
public class GetDeliveryFeeResponse {
    @SerializedName("delivery_fee_data")
    @Expose
    private List<GetDeliveryFee> deliveryFeeData = new ArrayList<GetDeliveryFee>();

    /**
     *
     * @return
     * The deliveryFeeData
     */
    public List<GetDeliveryFee> getDeliveryFeeData() {
        return deliveryFeeData;
    }

    /**
     *
     * @param deliveryFeeData
     * The delivery_fee_data
     */
    public void setDeliveryFeeData(List<GetDeliveryFee> deliveryFeeData) {
        this.deliveryFeeData = deliveryFeeData;
    }
}
