package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kranti on 10/4/2016.
 */
public class DeleteShoppingListItemRequest {



    @SerializedName("data")
    @Expose
    private DeleteShoppingListItemResponce data;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;

    /**
     *
     * @return
     * The data
     */
    public DeleteShoppingListItemResponce getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(DeleteShoppingListItemResponce data) {
        this.data = data;
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
}
