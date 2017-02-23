package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kranti on 9/30/2016.
 */
public class MainCategoryRequest {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("data")
    @Expose
    private MainCategoryResponse mMainCategoryResponse;

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

    /**
     *
     * @return
     * The data
     */
    public MainCategoryResponse getData() {
        return mMainCategoryResponse;
    }

    /**
     *
     * @param mMainCategoryResponse
     * The data
     */
    public void setData(MainCategoryResponse mMainCategoryResponse) {
        this.mMainCategoryResponse = mMainCategoryResponse;
    }
}
