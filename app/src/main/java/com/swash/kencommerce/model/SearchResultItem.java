package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/1/2016.
 */
public class SearchResultItem {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_img_url")
    @Expose
    private String productImgUrl;
    @SerializedName("product_sku")
    @Expose
    private String productSku;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("product_short_description")
    @Expose
    private String productShortDescription;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_is_special")
    @Expose
    private Integer productIsSpecial;
    @SerializedName("product_special_price")
    @Expose
    private String productSpecialPrice;
    @SerializedName("product_is_in_stock")
    @Expose
    private Integer productIsInStock;
    @SerializedName("product_is_salable")
    @Expose
    private Integer productIsSalable;
    @SerializedName("product_reviews_count")
    @Expose
    private Integer productReviewsCount;
    /*@SerializedName("product_rating_summary")
    @Expose
    private Integer productRatingSummary;*/
    @SerializedName("product_delivery_id")
    @Expose
    private String productDeliveryId;
    @SerializedName("product_delivery")
    @Expose
    private String productDelivery;
    @SerializedName("product_size")
    @Expose
    private List<ProductSize> productSize = new ArrayList<ProductSize>();
    @SerializedName("has_product_in_wishlist")
    @Expose
    private Integer hasProductInWishlist;
    @SerializedName("specific_review_rating")
    @Expose
    private SpecificReviewRating specificReviewRating;

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
     * The productImgUrl
     */
    public String getProductImgUrl() {
        return productImgUrl;
    }

    /**
     *
     * @param productImgUrl
     * The product_img_url
     */
    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    /**
     *
     * @return
     * The productSku
     */
    public String getProductSku() {
        return productSku;
    }

    /**
     *
     * @param productSku
     * The product_sku
     */
    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    /**
     *
     * @return
     * The productDescription
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     *
     * @param productDescription
     * The product_description
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /**
     *
     * @return
     * The productShortDescription
     */
    public String getProductShortDescription() {
        return productShortDescription;
    }

    /**
     *
     * @param productShortDescription
     * The product_short_description
     */
    public void setProductShortDescription(String productShortDescription) {
        this.productShortDescription = productShortDescription;
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
     * The productReviewsCount
     */
    public Integer getProductReviewsCount() {
        return productReviewsCount;
    }

    /**
     *
     * @param productReviewsCount
     * The product_reviews_count
     */
    public void setProductReviewsCount(Integer productReviewsCount) {
        this.productReviewsCount = productReviewsCount;
    }

    /**
     *
     * @return
     * The productRatingSummary
     */
    /*public Integer getProductRatingSummary() {
        return productRatingSummary;
    }*/

    /**
     *
     * @param productRatingSummary
     * The product_rating_summary
     */
    /*public void setProductRatingSummary(Integer productRatingSummary) {
        this.productRatingSummary = productRatingSummary;
    }*/

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

    /**
     *
     * @return
     * The hasProductInWishlist
     */
    public Integer getHasProductInWishlist() {
        return hasProductInWishlist;
    }

    /**
     *
     * @param hasProductInWishlist
     * The has_product_in_wishlist
     */
    public void setHasProductInWishlist(Integer hasProductInWishlist) {
        this.hasProductInWishlist = hasProductInWishlist;
    }

    /**
     *
     * @return
     * The specificReviewRating
     */
    public SpecificReviewRating getSpecificReviewRating() {
        return specificReviewRating;
    }

    /**
     *
     * @param specificReviewRating
     * The specific_review_rating
     */
    public void setSpecificReviewRating(SpecificReviewRating specificReviewRating) {
        this.specificReviewRating = specificReviewRating;
    }
}
