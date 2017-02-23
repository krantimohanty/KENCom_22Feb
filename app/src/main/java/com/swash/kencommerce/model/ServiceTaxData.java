package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kranti on 10/6/2016.
 */
public class ServiceTaxData {

    @SerializedName("tax")
    @Expose
    private String tax;

    /**
     *
     * @return
     * The tax
     */
    public String getTax() {
        return tax;
    }

    /**
     *
     * @param tax
     * The tax
     */
    public void setTax(String tax) {
        this.tax = tax;
    }
}
