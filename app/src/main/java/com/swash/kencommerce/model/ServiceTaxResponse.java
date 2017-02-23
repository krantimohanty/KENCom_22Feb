package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/6/2016.
 */
public class ServiceTaxResponse {


    @SerializedName("tax_data")
    @Expose
    private List<ServiceTaxData> taxData = new ArrayList<ServiceTaxData>();

    /**
     *
     * @return
     * The taxData
     */
    public List<ServiceTaxData> getTaxData() {
        return taxData;
    }

    /**
     *
     * @param taxData
     * The tax_data
     */
    public void setTaxData(List<ServiceTaxData> taxData) {
        this.taxData = taxData;
    }
}
