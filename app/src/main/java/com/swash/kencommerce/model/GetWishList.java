package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/3/2016.
 */
public class GetWishList {
    @SerializedName("wishlist_id")
    @Expose
    private String wishlistId;
    @SerializedName("item_qty")
    @Expose
    private String itemQty;
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
    @SerializedName("product_special_from_date")
    @Expose
    private String productSpecialFromDate;
    @SerializedName("product_special_to_date")
    @Expose
    private String productSpecialToDate;
    @SerializedName("product_news_from_date")
    @Expose
    private String productNewsFromDate;
    @SerializedName("product_news_to_date")
    @Expose
    private String productNewsToDate;
    @SerializedName("product_size")
    @Expose
    private List<ProductSize> productSize = new ArrayList<ProductSize>();
    @SerializedName("product_is_in_stock")
    @Expose
    private String productIsInStock;
    @SerializedName("product_is_salable")
    @Expose
    private String productIsSalable;
    @SerializedName("product_reviews_count")
    @Expose
    private Integer productReviewsCount;
    @SerializedName("product_rating_summary")
    @Expose
    private Integer productRatingSummary;

    /**
     *
     * @return
     * The wishlistId
     */
    public String getWishlistId() {
        return wishlistId;
    }

    /**
     *
     * @param wishlistId
     * The wishlist_id
     */
    public void setWishlistId(String wishlistId) {
        this.wishlistId = wishlistId;
    }

    /**
     *
     * @return
     * The itemQty
     */
    public String getItemQty() {
        return itemQty;
    }

    /**
     *
     * @param itemQty
     * The item_qty
     */
    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
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
     * The productSpecialFromDate
     */
    public String getProductSpecialFromDate() {
        return productSpecialFromDate;
    }

    /**
     *
     * @param productSpecialFromDate
     * The product_special_from_date
     */
    public void setProductSpecialFromDate(String productSpecialFromDate) {
        this.productSpecialFromDate = productSpecialFromDate;
    }

    /**
     *
     * @return
     * The productSpecialToDate
     */
    public String getProductSpecialToDate() {
        return productSpecialToDate;
    }

    /**
     *
     * @param productSpecialToDate
     * The product_special_to_date
     */
    public void setProductSpecialToDate(String productSpecialToDate) {
        this.productSpecialToDate = productSpecialToDate;
    }

    /**
     *
     * @return
     * The productNewsFromDate
     */
    public String getProductNewsFromDate() {
        return productNewsFromDate;
    }

    /**
     *
     * @param productNewsFromDate
     * The product_news_from_date
     */
    public void setProductNewsFromDate(String productNewsFromDate) {
        this.productNewsFromDate = productNewsFromDate;
    }

    /**
     *
     * @return
     * The productNewsToDate
     */
    public String getProductNewsToDate() {
        return productNewsToDate;
    }

    /**
     *
     * @param productNewsToDate
     * The product_news_to_date
     */
    public void setProductNewsToDate(String productNewsToDate) {
        this.productNewsToDate = productNewsToDate;
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
     * The productIsInStock
     */
    public String getProductIsInStock() {
        return productIsInStock;
    }

    /**
     *
     * @param productIsInStock
     * The product_is_in_stock
     */
    public void setProductIsInStock(String productIsInStock) {
        this.productIsInStock = productIsInStock;
    }

    /**
     *
     * @return
     * The productIsSalable
     */
    public String getProductIsSalable() {
        return productIsSalable;
    }

    /**
     *
     * @param productIsSalable
     * The product_is_salable
     */
    public void setProductIsSalable(String productIsSalable) {
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
    public Integer getProductRatingSummary() {
        return productRatingSummary;
    }

    /**
     *
     * @param productRatingSummary
     * The product_rating_summary
     */
    public void setProductRatingSummary(Integer productRatingSummary) {
        this.productRatingSummary = productRatingSummary;
    }
}
