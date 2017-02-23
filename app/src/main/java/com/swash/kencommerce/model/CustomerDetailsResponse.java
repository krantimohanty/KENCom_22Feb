package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 10/1/2016.
 */
public class CustomerDetailsResponse {
    @SerializedName("customer_data")
    @Expose
    private CustomerDetails customerData;

    /**
     *
     * @return
     * The customerData
     */
    public CustomerDetails getCustomerData() {
        return customerData;
    }

    /**
     *
     * @param customerData
     * The customer_data
     */
    public void setCustomerData(CustomerDetails customerData) {
        this.customerData = customerData;
    }
}
