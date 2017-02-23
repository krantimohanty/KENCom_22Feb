package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kranti on 10/3/2016.
 */
public class ShoppingListDetailRequest {

    @SerializedName("data")
    @Expose
    private ShoppingListDetailResponce mShoppingListDetailResponce;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("success")
    @Expose
    private Integer success;

    /**
     *
     * @return
     * The data
     */
    public ShoppingListDetailResponce getData() {
        return mShoppingListDetailResponce;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(ShoppingListDetailResponce data) {
        this.mShoppingListDetailResponce = data;
    }

    /**
     *
     * @return
     * The errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     *
     * @param errorMsg
     * The error_msg
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     *
     * @return
     * The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The success
     */
    public Integer getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }


}
