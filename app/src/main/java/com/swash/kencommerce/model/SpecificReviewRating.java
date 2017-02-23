package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kranti on 10/4/2016.
 */
public class SpecificReviewRating {

    @SerializedName("5_star")
    @Expose
    private String _5Star;
    @SerializedName("4_star")
    @Expose
    private String _4Star;
    @SerializedName("3_star")
    @Expose
    private String _3Star;
    @SerializedName("2_star")
    @Expose
    private String _2Star;
    @SerializedName("1_star")
    @Expose
    private String _1Star;

    /**
     *
     * @return
     * The _5Star
     */
    public String get5Star() {
        return _5Star;
    }

    /**
     *
     * @param _5Star
     * The 5_star
     */
    public void set5Star(String _5Star) {
        this._5Star = _5Star;
    }

    /**
     *
     * @return
     * The _4Star
     */
    public String get4Star() {
        return _4Star;
    }

    /**
     *
     * @param _4Star
     * The 4_star
     */
    public void set4Star(String _4Star) {
        this._4Star = _4Star;
    }

    /**
     *
     * @return
     * The _3Star
     */
    public String get3Star() {
        return _3Star;
    }

    /**
     *
     * @param _3Star
     * The 3_star
     */
    public void set3Star(String _3Star) {
        this._3Star = _3Star;
    }

    /**
     *
     * @return
     * The _2Star
     */
    public String get2Star() {
        return _2Star;
    }

    /**
     *
     * @param _2Star
     * The 2_star
     */
    public void set2Star(String _2Star) {
        this._2Star = _2Star;
    }

    /**
     *
     * @return
     * The _1Star
     */
    public String get1Star() {
        return _1Star;
    }

    /**
     *
     * @param _1Star
     * The 1_star
     */
    public void set1Star(String _1Star) {
        this._1Star = _1Star;
    }

}
