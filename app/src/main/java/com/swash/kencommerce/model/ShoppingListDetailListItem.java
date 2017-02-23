package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/3/2016.
 */
public class ShoppingListDetailListItem {




    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_review_count")
    @Expose
    private Integer productReviewCount;
    @SerializedName("product_is_available")
    @Expose
    private Boolean productIsAvailable;
    @SerializedName("product_special_price")
    @Expose
    private String productSpecialPrice;
    @SerializedName("product_normal_price")
    @Expose
    private String productNormalPrice;
    @SerializedName("product_is_special")
    @Expose
    private Integer productIsSpecial;
    @SerializedName("product_review_summery")
    @Expose
    private Integer productReviewSummery;
    @SerializedName("product_size")
    @Expose
    private List<ProductSize> productSize = new ArrayList<ProductSize>();

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
     * The productImage
     */
    public String getProductImage() {
        return productImage;
    }

    /**
     *
     * @param productImage
     * The product_image
     */
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    /**
     *
     * @return
     * The productReviewCount
     */
    public Integer getProductReviewCount() {
        return productReviewCount;
    }

    /**
     *
     * @param productReviewCount
     * The product_review_count
     */
    public void setProductReviewCount(Integer productReviewCount) {
        this.productReviewCount = productReviewCount;
    }

    /**
     *
     * @return
     * The productIsAvailable
     */
    public Boolean getProductIsAvailable() {
        return productIsAvailable;
    }

    /**
     *
     * @param productIsAvailable
     * The product_is_available
     */
    public void setProductIsAvailable(Boolean productIsAvailable) {
        this.productIsAvailable = productIsAvailable;
    }

    /**
     *
     * @return
     * The productSpecialPrice
     */
    public String getProductSpecialPrice() {
        return productSpecialPrice;
    }

    /**
     *
     * @param productSpecialPrice
     * The product_special_price
     */
    public void setProductSpecialPrice(String productSpecialPrice) {
        this.productSpecialPrice = productSpecialPrice;
    }

    /**
     *
     * @return
     * The productNormalPrice
     */
    public String getProductNormalPrice() {
        return productNormalPrice;
    }

    /**
     *
     * @param productNormalPrice
     * The product_normal_price
     */
    public void setProductNormalPrice(String productNormalPrice) {
        this.productNormalPrice = productNormalPrice;
    }

    /**
     *
     * @return
     * The productIsSpecial
     */
    public Integer getProductIsSpecial() {
        return productIsSpecial;
    }

    /**
     *
     * @param productIsSpecial
     * The product_is_special
     */
    public void setProductIsSpecial(Integer productIsSpecial) {
        this.productIsSpecial = productIsSpecial;
    }

    /**
     *
     * @return
     * The productReviewSummery
     */
    public Integer getProductReviewSummery() {
        return productReviewSummery;
    }

    /**
     *
     * @param productReviewSummery
     * The product_review_summery
     */
    public void setProductReviewSummery(Integer productReviewSummery) {
        this.productReviewSummery = productReviewSummery;
    }
    /**
     *
     * @return
     * The productSize
     */
    public List<ProductSize> getProductSize() {
        return productSize;
    }

    /**
     *
     * @param productSize
     * The product_size
     */
    public void setProductSize(List<ProductSize> productSize) {
        this.productSize = productSize;
    }

}
