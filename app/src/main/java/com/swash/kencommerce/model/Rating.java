package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kranti on 10/1/2016.
 */
public class Rating {

    @SerializedName("review_heading")
    @Expose
    private String reviewHeading;
    @SerializedName("review_rating")
    @Expose
    private Integer reviewRating;
    @SerializedName("review_details")
    @Expose
    private String reviewDetails;

    /**
     *
     * @return
     * The reviewHeading
     */
    public String getReviewHeading() {
        return reviewHeading;
    }

    /**
     *
     * @param reviewHeading
     * The review_heading
     */
    public void setReviewHeading(String reviewHeading) {
        this.reviewHeading = reviewHeading;
    }

    /**
     *
     * @return
     * The reviewRating
     */
    public Integer getReviewRating() {
        return reviewRating;
    }

    /**
     *
     * @param reviewRating
     * The review_rating
     */
    public void setReviewRating(Integer reviewRating) {
        this.reviewRating = reviewRating;
    }

    /**
     *
     * @return
     * The reviewDetails
     */
    public String getReviewDetails() {
        return reviewDetails;
    }

    /**
     *
     * @param reviewDetails
     * The review_details
     */
    public void setReviewDetails(String reviewDetails) {
        this.reviewDetails = reviewDetails;
    }

}
