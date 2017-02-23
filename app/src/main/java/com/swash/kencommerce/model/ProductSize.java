package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kranti on 10/4/2016.
 */
public class ProductSize {

    @SerializedName("attribute_id")
    @Expose
    private String attributeId;
    @SerializedName("option_id")
    @Expose
    private String optionId;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("pricing_value")
    @Expose
    private String pricingValue;

    /**
     *
     * @return
     * The attributeId
     */
    public String getAttributeId() {
        return attributeId;
    }

    /**
     *
     * @param attributeId
     * The attribute_id
     */
    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    /**
     *
     * @return
     * The optionId
     */
    public String getOptionId() {
        return optionId;
    }

    /**
     *
     * @param optionId
     * The option_id
     */
    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    /**
     *
     * @return
     * The label
     */
    public String getLabel() {
        return label;
    }

    /**
     *
     * @param label
     * The label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     *
     * @return
     * The pricingValue
     */
    public String getPricingValue() {
        return pricingValue;
    }

    /**
     *
     * @param pricingValue
     * The pricing_value
     */
    public void setPricingValue(String pricingValue) {
        this.pricingValue = pricingValue;
    }
}
