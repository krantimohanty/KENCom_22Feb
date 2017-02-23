package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kranti on 12/12/2016.
 */

public class AddToWishListResponse {
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("success_msg")
    @Expose
    private String successMsg;

    /**
     *
     * @return
     * The customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     *
     * @param customerId
     * The customer_id
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     *
     * @return
     * The productId
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     *
     * @param productId
     * The product_id
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     *
     * @return
     * The successMsg
     */
    public String getSuccessMsg() {
        return successMsg;
    }

    /**
     *
     * @param successMsg
     * The success_msg
     */
    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }
}
