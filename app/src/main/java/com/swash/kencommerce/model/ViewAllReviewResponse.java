package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/1/2016.
 */
public class ViewAllReviewResponse {

    @SerializedName("rating")
    @Expose
    private List<Rating> rating = new ArrayList<Rating>();
    @SerializedName("msg")
    @Expose
    private String msg;

    /**
     *
     * @return
     * The rating
     */
    public List<Rating> getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(List<Rating> rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     * The msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     *
     * @param msg
     * The msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
