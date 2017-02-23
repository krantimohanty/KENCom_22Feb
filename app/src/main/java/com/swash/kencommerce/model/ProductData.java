package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/1/2016.
 */
public class ProductData {


    @SerializedName("product_id")
    @Expose
    private String id;
    @SerializedName("product_sku")
    @Expose
    private String sku;
    @SerializedName("product_name")
    @Expose
    private String name;
    @SerializedName("product_img_url")
    @Expose
    private String image;
    @SerializedName("product_short_description")
    @Expose
    private String shortDescription;
    @SerializedName("product_description")
    @Expose
    private String description;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("product_price")
    @Expose
    private String price;
    @SerializedName("product_is_special")
    @Expose
    private Integer isSpecial;
    @SerializedName("product_special_price")
    @Expose
    private String salePrice;
    @SerializedName("product_reviews_count")
    @Expose
    private String reviewsCount;
    @SerializedName("product_rating_summary")
    @Expose
    private Double ratings;
    @SerializedName("product_is_in_stock")
    @Expose
    private Integer isInStock;
    @SerializedName("product_is_salable")
    @Expose
    private Integer isSalable;
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
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The sku
     */
    public String getSku() {
        return sku;
    }

    /**
     *
     * @param sku
     * The sku
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The image
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image
     * The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     *
     * @return
     * The shortDescription
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     *
     * @param shortDescription
     * The short_description
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * The price
     */
    public String getPrice() {
        return price;
    }

    /**
     *
     * @param price
     * The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     *
     * @return
     * The isSpecial
     */
    public Integer getIsSpecial() {
        return isSpecial;
    }

    /**
     *
     * @param isSpecial
     * The is_special
     */
    public void setIsSpecial(Integer isSpecial) {
        this.isSpecial = isSpecial;
    }

    /**
     *
     * @return
     * The salePrice
     */
    public String getSalePrice() {
        return salePrice;
    }

    /**
     *
     * @param salePrice
     * The sale_price
     */
    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    /**
     *
     * @return
     * The reviewsCount
     */
    public String getReviewsCount() {
        return reviewsCount;
    }

    /**
     *
     * @param reviewsCount
     * The reviews_count
     */
    public void setReviewsCount(String reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    /**
     *
     * @return
     * The ratings
     */
    public Double getRatings() {
        return ratings;
    }

    /**
     *
     * @param ratings
     * The ratings
     */
    public void setRatings(Double ratings) {
        this.ratings = ratings;
    }

    /**
     *
     * @return
     * The isInStock
     */
    public Integer getIsInStock() {
        return isInStock;
    }

    /**
     *
     * @param isInStock
     * The is_in_stock
     */
    public void setIsInStock(Integer isInStock) {
        this.isInStock = isInStock;
    }

    /**
     *
     * @return
     * The isSalable
     */
    public Integer getIsSalable() {
        return isSalable;
    }

    /**
     *
     * @param isSalable
     * The is_salable
     */
    public void setIsSalable(Integer isSalable) {
        this.isSalable = isSalable;
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
