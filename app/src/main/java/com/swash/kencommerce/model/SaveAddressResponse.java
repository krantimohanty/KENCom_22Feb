package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kranti on 10/5/2016.
 */
public class SaveAddressResponse {
    @SerializedName("success_message")
    @Expose
    private String successMessage;

    /**
     *
     * @return
     * The successMessage
     */
    public String getSuccessMessage() {
        return successMessage;
    }

    /**
     *
     * @param successMessage
     * The success_message
     */
    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
