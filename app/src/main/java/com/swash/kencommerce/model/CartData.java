package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kranti on 12/12/2016.
 */

public class CartData {

    @SerializedName("cart_item_id")
    @Expose
    private String cartItemId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_qty")
    @Expose
    private Integer productQty;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_is_in_stock")
    @Expose
    private Integer productIsInStock;
    @SerializedName("product_is_salable")
    @Expose
    private Integer productIsSalable;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_delivery_id")
    @Expose
    private String productDeliveryId;
    @SerializedName("product_delivery")
    @Expose
    private String productDelivery;
    @SerializedName("attribute_id")
    @Expose
    private String attributeId;
    @SerializedName("option_id")
    @Expose
    private String optionId;

    /**
     *
     * @return
     * The cartItemId
     */
    public String getCartItemId() {
        return cartItemId;
    }

    /**
     *
     * @param cartItemId
     * The cart_item_id
     */
    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    /**
     *
     * @return
     * The productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     *
     * @param productId
     * The product_id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     *
     * @return
     * The productQty
     */
    public Integer getProductQty() {
        return productQty;
    }

    /**
     *
     * @param productQty
     * The product_qty
     */
    public void setProductQty(Integer productQty) {
        this.productQty = productQty;
    }

    /**
     *
     * @return
     * The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     *
     * @param productName
     * The product_name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     *
     * @return
     * The productIsInStock
     */
    public Integer getProductIsInStock() {
        return productIsInStock;
    }

    /**
     *
     * @param productIsInStock
     * The product_is_in_stock
     */
    public void setProductIsInStock(Integer productIsInStock) {
        this.productIsInStock = productIsInStock;
    }

    /**
     *
     * @return
     * The productIsSalable
     */
    public Integer getProductIsSalable() {
        return productIsSalable;
    }

    /**
     *
     * @param productIsSalable
     * The product_is_salable
     */
    public void setProductIsSalable(Integer productIsSalable) {
        this.productIsSalable = productIsSalable;
    }

    /**
     *
     * @return
     * The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     *
     * @param currency
     * The currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     *
     * @return
     * The productPrice
     */
    public String getProductPrice() {
        return productPrice;
    }

    /**
     *
     * @param productPrice
     * The product_price
     */
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    /**
     *
     * @return
     * The productDeliveryId
     */
    public String getProductDeliveryId() {
        return productDeliveryId;
    }

    /**
     *
     * @param productDeliveryId
     * The product_delivery_id
     */
    public void setProductDeliveryId(String productDeliveryId) {
        this.productDeliveryId = productDeliveryId;
    }

    /**
     *
     * @return
     * The productDelivery
     */
    public String getProductDelivery() {
        return productDelivery;
    }

    /**
     *
     * @param productDelivery
     * The product_delivery
     */
    public void setProductDelivery(String productDelivery) {
        this.productDelivery = productDelivery;
    }

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


}
