package com.swash.kencommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranti on 10/1/2016.
 */
public class SubCategoryListResponse {

    @SerializedName("sub_category_list")
    @Expose
    private List<SubCategoryList> subCategoryList = new ArrayList<SubCategoryList>();

    @SerializedName("featured_product_list")
    @Expose
    private List<FeaturedProductList> featuredProductList = new ArrayList<FeaturedProductList>();

    @SerializedName("product_list")
    @Expose
    private List<ProductList> productList = new ArrayList<ProductList>();

    /**
     *
     * @return
     * The subCategoryList
     */
    public List<SubCategoryList> getSubCategoryList() {
        return subCategoryList;
    }

    /**
     *
     * @param subCategoryList
     * The sub_category_list
     */
    public void setSubCategoryList(List<SubCategoryList> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    /**
     *
     * @return
     * The featuredProductList
     */
    public List<FeaturedProductList> getFeaturedProductList() {
        return featuredProductList;
    }

    /**
     *
     * @param featuredProductList
     * The featured_product_list
     */
    public void setFeaturedProductList(List<FeaturedProductList> featuredProductList) {
        this.featuredProductList = featuredProductList;
    }

    /**
     *
     * @return
     * The productList
     */
    public List<ProductList> getProductList() {
        return productList;
    }

    /**
     *
     * @param productList
     * The product_list
     */
    public void setProductList(List<ProductList> productList) {
        this.productList = productList;
    }
}
