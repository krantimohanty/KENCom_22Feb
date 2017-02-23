package com.swash.kencommerce.retrofit_call;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.swash.kencommerce.model.AddItemToShoppingListRequest;
import com.swash.kencommerce.model.AddToWishListRequest;
import com.swash.kencommerce.model.AutoSearchKeyRequest;
import com.swash.kencommerce.model.CartTotalRequest;
import com.swash.kencommerce.model.CustomerDetailsRequest;
import com.swash.kencommerce.model.DeleteItemFromCartRequest;
import com.swash.kencommerce.model.DeleteShoppingListItemRequest;
import com.swash.kencommerce.model.DeleteWishListRequest;
import com.swash.kencommerce.model.EditProfileRequest;
import com.swash.kencommerce.model.EmptyCardRequest;
import com.swash.kencommerce.model.FbLoginRequest;
import com.swash.kencommerce.model.FbRegisterRequest;
import com.swash.kencommerce.model.ForgotPasswordRequest;
import com.swash.kencommerce.model.GetAddressRequest;
import com.swash.kencommerce.model.GetAllHubRequest;
import com.swash.kencommerce.model.GetCartListRequest;
import com.swash.kencommerce.model.GetDeliveryFeeRequest;
import com.swash.kencommerce.model.GetWishListRequest;
import com.swash.kencommerce.model.LogOutRequest;
import com.swash.kencommerce.model.LoginRequest;
import com.swash.kencommerce.model.MainCategoryRequest;
import com.swash.kencommerce.model.MoveShoppingListItemRequest;
import com.swash.kencommerce.model.MyShoppingListAllltemRequest;
import com.swash.kencommerce.model.MyShoppingListItemRequest;
import com.swash.kencommerce.model.PastOrderInfoRequest;
import com.swash.kencommerce.model.PastOrderRequest;
import com.swash.kencommerce.model.PastOrderSummaryRequest;
import com.swash.kencommerce.model.PaymentRequest;
import com.swash.kencommerce.model.ProductDetailsRequest;
import com.swash.kencommerce.model.RateReviewRequest;
import com.swash.kencommerce.model.ReOrderRequest;
import com.swash.kencommerce.model.RegistrationRequest;
import com.swash.kencommerce.model.RemoveShoppingListRequest;
import com.swash.kencommerce.model.SaveAddressRequest;
import com.swash.kencommerce.model.SearchResultRequest;
import com.swash.kencommerce.model.ServiceTaxRequest;
import com.swash.kencommerce.model.ShoppingListDetailRequest;
import com.swash.kencommerce.model.ShoppingListRequest;
import com.swash.kencommerce.model.SubCategoryListRequest;
import com.swash.kencommerce.model.SubscriptionRequest;
import com.swash.kencommerce.model.TrackOrder;
import com.swash.kencommerce.model.UpdateCartRequest;
import com.swash.kencommerce.model.ViewAllReviewRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by Kranti on 12/12/2016.
 */

public class RestService {

    private static RestInterface restInterface;
    private static final String TAG = "RestService";

    public RestService() {

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://stockup.co.za.dedi1025.jnb1.host-h.net/webservices/")
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        restInterface = restAdapter.create(RestInterface.class);
    }
    public static RestInterface get()
    {
        return restInterface;
    }

    public interface RestInterface {
        @GET("/order/orderLastInfo")
        void getDepartmentpID(Callback<TrackOrder> callback);
       // Synchronous declaration
        @GET("/tax/gettax")
        void getServiceTax(Callback<ServiceTaxRequest> callback);

        // GetAllHub
        @GET("/hub/getAllHub")
        void getAllHub(Callback<GetAllHubRequest> callback);



        /* @GET("/cities")
        void getCityID(Callback<CityListRequest> callback);

        */

        /*@FormUrlEncoded
        @POST("/vendors")
        void getRegistrationResponse(@Field("department_id") String department_id, @Field("city_id") String city_id,
                               @Field("zip_code") String zip_code, @Field("business_name") String business_name,
                               @Field("email_address") String email_address,@Field("password") String password, @Field("opening_time_from") String opening_time_from,
                               @Field("contact_no") String contact_no,
                               @Field("opening_time_end") String opening_time_end,
                               @Field("landmark") String landmark,@Field("address") String address,
                               Callback<RegistrationRequest> callback);*/



        @FormUrlEncoded
        @POST("/search/searchAuto")
        void getAutoSearchKey(@Field("search_query") String search_query, @Field("store_id") String store_id, @Field("customer_id") String customer_id,
                           Callback<AutoSearchKeyRequest> callback);

        @FormUrlEncoded
        @POST("/search/searchResult")
        void getAutoSearch(@Field("search_query") String search_query, @Field("store_id") String store_id,
                           Callback<SearchResultRequest> callback);

        @FormUrlEncoded
        @POST("/customer/loginPost")
        void getLoginResponse(@Field("email") String email_address, @Field("password") String password,
                              @Field("product_ids") String product_ids, @Field("device_id") String device_id,
                              Callback<LoginRequest> callback);

        @FormUrlEncoded
        @POST("/customer/accountCreate")
        void getFbRegistrationResponse(@Field("facebook_user_id") String facebook_user_id,
                                       @Field("device_id") String device_id,
                                       @Field("product_ids") String product_ids,
                                       @Field("firstname") String firstname, @Field("lastname") String lastname,
                                       @Field("phone") String phone, @Field("dob") String dob,
                                       @Field("email") String email, @Field("password") String password,
                                       Callback<FbRegisterRequest> callback);

        @FormUrlEncoded
        @POST("/customer/facebookLoginPost")
        void getFbLoginResponse(@Field("facebook_user_id") String facebook_user_id, @Field("device_id") String device_id,
                                @Field("product_ids") String product_ids,
                                Callback<FbLoginRequest> callback);

        @FormUrlEncoded
        @POST("/customer/logout")
        void logOut(@Field("customer_id") String customer_id, MyCallback<LogOutRequest, LogOutRequest> callback);

        @FormUrlEncoded
        @POST("/customer/forgotpasswordPost")
        void getPassword(@Field("email") String email, MyCallback<ForgotPasswordRequest, ForgotPasswordRequest> callback);

        @FormUrlEncoded
        @POST("/wishlist/addtolist")
        void addToWish(@Field("customer_id") String customer_id, @Field("product_id") String product_id,
                       MyCallback<AddToWishListRequest, AddToWishListRequest> callback);

        @FormUrlEncoded
        @POST("/wishlist/getList")
        void getWishList(@Field("customer_id") String customer_id, MyCallback<GetWishListRequest, GetWishListRequest> callback);

        @FormUrlEncoded
        @POST("/wishlist/removeItem")
        void deleteWish(@Field("customer_id") String customer_id, @Field("wishlist_id") String wishlist_id,
                       MyCallback<DeleteWishListRequest, DeleteWishListRequest> callback);

        @FormUrlEncoded
        @POST("/order/orderHistory")
        void getPastOrder(@Field("customer_id") String customer_id, MyCallback<PastOrderRequest, PastOrderRequest> callback);

        @FormUrlEncoded
        @POST("/order/orderInfo")
        void getPastOrderInfo(@Field("order_id") String order_id, MyCallback<PastOrderInfoRequest, PastOrderInfoRequest> callback);

        @FormUrlEncoded
        @POST("/order/orderSummary")
        void getPastOrderorderSummary(@Field("order_id") String order_id, MyCallback<PastOrderSummaryRequest, PastOrderSummaryRequest> callback);

        @FormUrlEncoded
        @POST("/customer/viewCustomerDetails")
        void getCustomerDetails(@Field("customer_id") String customer_id, MyCallback<CustomerDetailsRequest, CustomerDetailsRequest> callback);

        @FormUrlEncoded
        @POST("/customer/accountCreate")
        void getRegistrationResponse(@Field("firstname") String firstname, @Field("lastname") String lastname,
                                     @Field("phone") String phone, @Field("dob") String dob,
                                     @Field("email") String email, @Field("password") String password,
                                     Callback<RegistrationRequest> callback);

        @FormUrlEncoded
        @POST("/customer/editCustomerDetails")
        void getEditProfileResponse(@Field("customer_id") String customer_id, @Field("fname") String fname, @Field("lname") String lname,
                                     @Field("phone") String phone, @Field("email") String email, @Field("password") String password,
                                        @Field("old_password") String old_password,
                                        Callback<EditProfileRequest> callback);

        @FormUrlEncoded
        @POST("/order/placeOrder")
        void getPayment(@Field("firstname") String firstname, @Field("lastname") String lastname,
                        @Field("email") String email, @Field("telephone") String telephone,
                        @Field("address") String street1,@Field("latitude") String street2,
                        @Field("longitude") String street3,@Field("product_ids") String product_ids,
                        @Field("payment_method") String payment_type,@Field("cc_number") String cc_number,
                        @Field("cc_owner") String cc_owner,@Field("cc_type") String cc_type,
                        @Field("cc_cid") String cc_cid,@Field("cc_exp_month") String cc_exp_month,
                        @Field("cc_exp_year") String cc_exp_year,@Field("delivery_method") String delivery_method,
                        @Field("shipping_arrival_date_display") String shipping_arrival_date_display,@Field("shipping_arrival_comments") String shipping_arrival_comments,
                        Callback<PaymentRequest> callback);

        @FormUrlEncoded
        @POST("/catalog/catagoryInfo")
        void subCategoryList(@Field("cat_id") String cat_id,@Field("customer_id") String customer_id, Callback<SubCategoryListRequest> callback);

        @FormUrlEncoded
        @POST("/catalog/storeInfo")
        void subCategorySeeAllList(@Field("store_id") String store_id,@Field("customer_id") String customer_id, Callback<SubCategoryListRequest> callback);

        @FormUrlEncoded
        @POST("/product/getProduct")
        void productDetails(@Field("product_id") String product_id,@Field("customer_id") String customer_id, Callback<ProductDetailsRequest> callback);

        @FormUrlEncoded
        @POST("/catalog/mainCatagoryList")
        void mainCategoryList(@Field("address") String address, @Field("store_id") String store_id,Callback<MainCategoryRequest> callback);

        @FormUrlEncoded
        @POST("/rating/getRating")
        void viewAllReview(@Field("product_id") String product_id, Callback<ViewAllReviewRequest> callback);

        @FormUrlEncoded
        @POST("/rating/setReview ")
        void setRateReview(@Field("product_id") String product_id,@Field("review_title") String review_title,
                           @Field("review_detail") String review_detail, @Field("customer_id") String customer_id ,
                           @Field("rating") String rating, Callback<RateReviewRequest> callback);



        //====== Shopping List =========
        @FormUrlEncoded
        @POST("/shopingList/showShoppingList")
        void viewShoppingList(@Field("customer") String customar_id, Callback<ShoppingListRequest> callback);

        @FormUrlEncoded
        @POST("/shopingList/showShoppingListDetails")
        void viewShoppingDetailList(@Field("id") String id,@Field("customer") String customar, Callback<ShoppingListDetailRequest> callback);

        @FormUrlEncoded
        @POST("/shopingList/removeShoppingListItem")
        void deleteShoppingListItem(@Field("item_id") String item_id, @Field("customer") String customar, MyCallback<DeleteShoppingListItemRequest, DeleteShoppingListItemRequest> callback);


        @FormUrlEncoded
        @POST("/shopingList/moveItem")
        void moveShoppingListItem(@Field("item_id") String item_id, @Field("list_id") String list_id,@Field("customer") String customar, MyCallback<MoveShoppingListItemRequest, MoveShoppingListItemRequest> callback);


        @FormUrlEncoded
        @POST("/shopingList/createNewList")
        void getViewMyShoppingListItem(@Field("customer") String customar_id, @Field("title") String title, Callback<MyShoppingListItemRequest> callback);
        //void getViewMyShoppingListItem(@Field("customer") String customar_id, @Field("title") String title, MyCallback<MyShoppingListItemRequest, MyShoppingListItemRequest> callback);

        @FormUrlEncoded
        @POST("/shopingList/renameShoppingList")
        void getViewMyAllltemList(@Field("customer") String customer_id, @Field("title") String title, @Field("list_id") String list_id, Callback<MyShoppingListAllltemRequest> callback);

        @FormUrlEncoded
        @POST("/shopingList/removeShoppingList")
        void getRemoveShoppingList(@Field("id") String remove_id, @Field("customer") String customer_id, Callback<RemoveShoppingListRequest> callback);

        //====== Shopping List =========
        // Add To Cart
        @FormUrlEncoded
        @POST("/cart/addToCart")
        void addToCart(@Field("product_id") String product_id,@Field("qty") String qty,
                       @Field("attribute_id") String attribute_id, @Field("customer_id") String customer_id ,
                       @Field("option_id") String option_id, Callback<AddToWishListRequest> callback);
        @FormUrlEncoded
        @POST("/cart/getCartDetails")
        void getCartDetails(@Field("customer_id") String customer_id ,Callback<GetCartListRequest> callback);

        @FormUrlEncoded
        @POST("/cart/deleteItemFromCart")
        void deleteItemCart(@Field("customer_id") String customer_id, @Field("cart_item_id") String cart_item_id,Callback<DeleteItemFromCartRequest> callback);

        @FormUrlEncoded
        @POST("/shopingList/addToShoppingList")
        void insertItemToShoppinglist(@Field("qty") String quantity, @Field("id") String listid,
                                      @Field("list_title") String list_title, @Field("product") String productId,
                                      @Field("customer") String customer, Callback<AddItemToShoppingListRequest> callback);

        //Address
        @FormUrlEncoded
        @POST("/customer/saveAddress")
        void saveAddress(@Field("customer_id") String customer_id, @Field("address") String address,
                                      @Field("latitude") String latitude, @Field("longitude") String longitude,
                                      Callback<SaveAddressRequest> callback);

        @FormUrlEncoded
        @POST("/customer/getAddress")
        void getAddress(@Field("customer_id") String customer_id, Callback<GetAddressRequest> callback);

        @FormUrlEncoded
        @POST("/delivery/getDeliveryFee")
        void getDeleveryFee(@Field("product_ids") String product_ids, @Field("customer_id") String customer_id, Callback<GetDeliveryFeeRequest> callback);

        @FormUrlEncoded
        @POST("/shopingList/removeShoppingListItem")
        void deleteShoppingListItem(@Field("item_id") String item_id, @Field("customer") String customar, @Field("list_id") String list_id, MyCallback<DeleteShoppingListItemRequest, DeleteShoppingListItemRequest> callback);

        @FormUrlEncoded
        @POST("/shopingList/moveItem")
        void moveShoppingListItem(@Field("item_id") String item_id, @Field("list_id") String list_id,@Field("from_list_id") String from_list_id,@Field("customer") String customar,@Field("list_title") String list_title, MyCallback<MoveShoppingListItemRequest, MoveShoppingListItemRequest> callback);


        @FormUrlEncoded
        @POST("/cart/getCartTotal")
        void getCartTotal(@Field("customer_id") String customer_id, Callback<CartTotalRequest> callback);

        @FormUrlEncoded
        @POST("/order/reOrder")
        void reOrder(@Field("order_id") String order_id, Callback<ReOrderRequest> callback);

        @FormUrlEncoded
        @POST("/cart/emptyCart")
        void emptyCart(@Field("customer_id") String customer_id, Callback<EmptyCardRequest> callback);


        @FormUrlEncoded
        @POST("/cart/updateCart")
        void updateCart(@Field("customer_id") String customer_id,
                        @Field("items") String items, Callback<UpdateCartRequest> callback);

        @FormUrlEncoded
        @POST("/stocknotification/notify")
        void notifyUser(@Field("subscription_email") String subscription_email, @Field("product_id") String product_id,
                        Callback<SubscriptionRequest> callback);

    }

    public void getFbRegistrationResponse(String facebook_user_id, String device_id,
                                          String product_ids, String firstname, String lastname, String phone, String dob,
                                          String email, String password,
                                          RestCallback<FbRegisterRequest> callback) {

        restInterface.getFbRegistrationResponse(facebook_user_id, device_id, product_ids,
                firstname, lastname, phone, dob, email, password,
                new MyCallback<FbRegisterRequest, FbRegisterRequest>(callback) {
                    @Override
                    protected FbRegisterRequest processResponse(FbRegisterRequest regResponse) {
                        return regResponse;
                    }
                });
    }
    public void getFbLoginResponse(String facebook_user_id, String device_id, String product_ids,
                                   RestCallback<FbLoginRequest> callback) {

        restInterface.getFbLoginResponse(facebook_user_id, device_id, product_ids,
                new MyCallback<FbLoginRequest, FbLoginRequest>(callback) {
                    @Override
                    protected FbLoginRequest processResponse(FbLoginRequest loginResponse) {
                        return loginResponse;
                    }
                });
    }

    public void notifyUser(String subscription_email, String product_id, RestCallback<SubscriptionRequest> callback) {

        restInterface.notifyUser(subscription_email, product_id, new MyCallback<SubscriptionRequest, SubscriptionRequest>(callback) {
            @Override
            protected SubscriptionRequest processResponse(SubscriptionRequest mSubscription) {
                return mSubscription;
            }
        });
    }
    public void updateCart(String customer_id,String items, RestCallback<UpdateCartRequest> callback) {

        restInterface.updateCart(customer_id, items, new MyCallback<UpdateCartRequest, UpdateCartRequest>(callback) {
            @Override
            protected UpdateCartRequest processResponse(UpdateCartRequest mUpdateCartRequest) {
                return mUpdateCartRequest;
            }
        });
    }
    public void emptyCart(String customar_id, RestCallback<EmptyCardRequest> callback) {

        restInterface.emptyCart(customar_id, new MyCallback<EmptyCardRequest, EmptyCardRequest>(callback) {
            @Override
            protected EmptyCardRequest processResponse(EmptyCardRequest mEmptyCart) {
                return mEmptyCart;
            }
        });
    }

    public void getCartTotal(String customer_id, RestCallback<CartTotalRequest> callback) {

        restInterface.getCartTotal(customer_id, new MyCallback<CartTotalRequest, CartTotalRequest>(callback) {
            @Override
            protected CartTotalRequest processResponse(CartTotalRequest cartTotalRequest) {
                return cartTotalRequest;
            }
        });
    }

    public void reorder(String order_id, RestCallback<ReOrderRequest> callback) {

        restInterface.reOrder(order_id, new MyCallback<ReOrderRequest, ReOrderRequest>(callback) {
            @Override
            protected ReOrderRequest processResponse(ReOrderRequest mReOrderRequest) {
                return mReOrderRequest;
            }
        });
    }
   /* public void getLocation(String city_id, RestCallback<getLocationRequest> callback){

        restInterface.getLocation(city_id, new MyCallback<getLocationRequest, getLocationRequest>(callback) {
            @Override
            protected getLocationRequest processResponse(getLocationRequest signInResponse) {
                return signInResponse;
            }
        });
    }

    public void getOffer(String str,RestCallback<OfferRequest> callback) {

        restInterface.getOffer(str, new MyCallback<OfferRequest, OfferRequest>(callback) {
            @Override
            protected OfferRequest processResponse(OfferRequest signInResponse) {
                return signInResponse;
            }
        });
    }

    public void postOfferIncreaseCount(String vendor_id,String offer_id,
                                      RestCallback<PostIncreaseCountRequest> callback) {

        restInterface.postOfferIncreaseCount(vendor_id, offer_id,
                new MyCallback<PostIncreaseCountRequest, PostIncreaseCountRequest>(callback) {
                    @Override
                    protected PostIncreaseCountRequest processResponse(PostIncreaseCountRequest postIncreaseCountRequest) {
                        return postIncreaseCountRequest;
                    }
                });
    }
    public void getAutoSearchResponse(String location,
                                      RestCallback<AutoSearchRequest> callback) {

        restInterface.getAutoSearchResponse(location,
                new MyCallback<AutoSearchRequest, AutoSearchRequest>(callback) {
                    @Override
                    protected AutoSearchRequest processResponse(AutoSearchRequest autoSearchResponse) {
                        return autoSearchResponse;
                    }
                });
    }
    public void getOfferConfirm(String offer_id, String name, String email, String phone_no,
                                RestCallback<GetOfferVisitorRequest> callback) {

        restInterface.getOfferConfirm(offer_id, name, email, phone_no,
                new MyCallback<GetOfferVisitorRequest, GetOfferVisitorRequest>(callback) {
                    @Override
                    protected GetOfferVisitorRequest processResponse(GetOfferVisitorRequest getOfferConfirmResponse) {
                        return getOfferConfirmResponse;
                    }
                });
    }
    public void referFriend(String str_offer_id, String str_name, String str_phone, String str_unique_code,
                                RestCallback<ReferFriendRequest> callback) {

        restInterface.referFriend(str_offer_id, str_name, str_phone, str_unique_code,
                new MyCallback<ReferFriendRequest, ReferFriendRequest>(callback) {
                    @Override
                    protected ReferFriendRequest processResponse(ReferFriendRequest referFriendResponse) {
                        return referFriendResponse;
                    }
                });
    }

    public void getVisitorResponse(String location,
                                        RestCallback<VisitorDashBoardRequest> callback) {

        restInterface.getVisitorResponse(location,
                new MyCallback<VisitorDashBoardRequest, VisitorDashBoardRequest>(callback) {
                    @Override
                    protected VisitorDashBoardRequest processResponse(VisitorDashBoardRequest signInResponse) {
                        return signInResponse;
                    }
                });
    }*/

    public void getAutoSearchKey(String search_query, String store_id, String customer_id,
                                  RestCallback<AutoSearchKeyRequest> callback) {

        restInterface.getAutoSearchKey(search_query, store_id, customer_id,
                new MyCallback<AutoSearchKeyRequest, AutoSearchKeyRequest>(callback) {
                    @Override
                    protected AutoSearchKeyRequest processResponse(AutoSearchKeyRequest searchKeyResponse) {
                        return searchKeyResponse;
                    }
                });
    }

    public void getAutoSearch(String search_query, String store_id,
                              RestCallback<SearchResultRequest> callback) {

        restInterface.getAutoSearch(search_query, store_id,
                new MyCallback<SearchResultRequest, SearchResultRequest>(callback) {
                    @Override
                    protected SearchResultRequest processResponse(SearchResultRequest searchResponse) {
                        return searchResponse;
                    }
                });
    }

    public void getLoginResponse(String email, String password,String product_ids, String device_id,
                                 RestCallback<LoginRequest> callback) {

        restInterface.getLoginResponse(email, password, product_ids, device_id,
                new MyCallback<LoginRequest, LoginRequest>(callback) {
                    @Override
                    protected LoginRequest processResponse(LoginRequest loginResponse) {
                        return loginResponse;
                    }
                });
    }

    public void logOut(String customer_id, RestCallback<LogOutRequest> callback) {
        restInterface.logOut(customer_id,
                new MyCallback<LogOutRequest, LogOutRequest>(callback) {
                    @Override
                    protected LogOutRequest processResponse(LogOutRequest fpResponse) {
                        return fpResponse;
                    }
                });
    }

    public void getPassword(String email, RestCallback<ForgotPasswordRequest> callback) {
        restInterface.getPassword(email,
                new MyCallback<ForgotPasswordRequest, ForgotPasswordRequest>(callback) {
                    @Override
                    protected ForgotPasswordRequest processResponse(ForgotPasswordRequest fpResponse) {
                        return fpResponse;
                    }
                });
    }

    public void deleteWish(String customer_id, String product_id, RestCallback<DeleteWishListRequest> callback) {
        restInterface.deleteWish(customer_id, product_id,
                new MyCallback<DeleteWishListRequest, DeleteWishListRequest>(callback) {
                    @Override
                    protected DeleteWishListRequest processResponse(DeleteWishListRequest delWlResponse) {
                        return delWlResponse;
                    }
                });
    }

    public void getWishList(String customer_id, RestCallback<GetWishListRequest> callback) {
        restInterface.getWishList(customer_id,
                new MyCallback<GetWishListRequest, GetWishListRequest>(callback) {
                    @Override
                    protected GetWishListRequest processResponse(GetWishListRequest getWlResponse) {
                        return getWlResponse;
                    }
                });
    }

    public void addToWish(String customer_id, String product_id, RestCallback<AddToWishListRequest> callback) {
        restInterface.addToWish(customer_id, product_id,
                new MyCallback<AddToWishListRequest, AddToWishListRequest>(callback) {
                    @Override
                    protected AddToWishListRequest processResponse(AddToWishListRequest addWlResponse) {
                        return addWlResponse;
                    }
                });
    }

    public void getPastOrder(String customer_id, RestCallback<PastOrderRequest> callback) {
        restInterface.getPastOrder(customer_id,
                new MyCallback<PastOrderRequest, PastOrderRequest>(callback) {
                    @Override
                    protected PastOrderRequest processResponse(PastOrderRequest poResponse) {
                        return poResponse;
                    }
                });
    }

    public void getPastOrderInfo(String order_id, RestCallback<PastOrderInfoRequest> callback) {
        restInterface.getPastOrderInfo(order_id,
                new MyCallback<PastOrderInfoRequest, PastOrderInfoRequest>(callback) {
                    @Override
                    protected PastOrderInfoRequest processResponse(PastOrderInfoRequest poiResponse) {
                        return poiResponse;
                    }
                });
    }

    public void getPastOrderorderSummary(String order_id, RestCallback<PastOrderSummaryRequest> callback) {
        restInterface.getPastOrderorderSummary(order_id,
                new MyCallback<PastOrderSummaryRequest, PastOrderSummaryRequest>(callback) {
                    @Override
                    protected PastOrderSummaryRequest processResponse(PastOrderSummaryRequest posResponse) {
                        return posResponse;
                    }
                });
    }

    public void getCustomerDetails(String customer_id, RestCallback<CustomerDetailsRequest> callback) {
        restInterface.getCustomerDetails(customer_id,
                new MyCallback<CustomerDetailsRequest, CustomerDetailsRequest>(callback) {
                    @Override
                    protected CustomerDetailsRequest processResponse(CustomerDetailsRequest customerResponse) {
                        return customerResponse;
                    }
                });
    }

    public void getRegistrationResponse(String firstname, String lastname, String phone, String dob,
                                        String email, String password,
                                        RestCallback<RegistrationRequest> callback) {

        restInterface.getRegistrationResponse(firstname, lastname, phone, dob, email, password,
                new MyCallback<RegistrationRequest, RegistrationRequest>(callback) {
                    @Override
                    protected RegistrationRequest processResponse(RegistrationRequest regResponse) {
                        return regResponse;
                    }
                });
    }


    public void getEditProfileResponse(@Field("customer_id") String customer_id, @Field("fname") String fname,
                                       @Field("lname") String lname, @Field("phone") String phone,
                                       @Field("email") String email, @Field("password") String password,
                                       @Field("old_password") String old_password,
                                       RestCallback<EditProfileRequest> callback) {

        restInterface.getEditProfileResponse(customer_id, fname, lname, phone, email, password, old_password,
                new MyCallback<EditProfileRequest, EditProfileRequest>(callback) {
                    @Override
                    protected EditProfileRequest processResponse(EditProfileRequest regResponse) {
                        return regResponse;
                    }
                });
    }

    public void getPayment(String firstname, String lastname,String emailaddress,String telephone, String address,String latitude,String longitude ,String productids,String paymentmethod, String cc_number, String cc_owner, String cc_type,String cc_cid , String cc_exp_month, String cc_exp_year,
                           String delivery_method,String shipping_arrival_date_display,String shipping_arrival_comments, RestCallback<PaymentRequest> callback) {

        restInterface.getPayment(firstname, lastname, emailaddress, telephone, address, latitude, longitude, productids, paymentmethod, cc_number, cc_owner, cc_type, cc_cid, cc_exp_month, cc_exp_year,
                delivery_method, shipping_arrival_date_display, shipping_arrival_comments, new MyCallback<PaymentRequest, PaymentRequest>(callback) {
                    @Override
                    protected PaymentRequest processResponse(PaymentRequest paymentRequest) {
                        return paymentRequest;
                    }
                });
    }

    public void subCategoryList(String product_id, String customer_id, RestCallback<SubCategoryListRequest> callback) {

        restInterface.subCategoryList(product_id, customer_id, new MyCallback<SubCategoryListRequest, SubCategoryListRequest>(callback) {
            @Override
            protected SubCategoryListRequest processResponse(SubCategoryListRequest mSubCategoryListRequest) {
                return mSubCategoryListRequest;
            }
        });
    }

    public void subCategorySeeAllList(String product_id, String customer_id, RestCallback<SubCategoryListRequest> callback) {

        restInterface.subCategorySeeAllList(product_id, customer_id, new MyCallback<SubCategoryListRequest, SubCategoryListRequest>(callback) {
            @Override
            protected SubCategoryListRequest processResponse(SubCategoryListRequest mSubCategoryListRequest) {
                return mSubCategoryListRequest;
            }
        });
    }

    public void getDepartmentpID(RestCallback<TrackOrder> callback) {

        restInterface.getDepartmentpID(new MyCallback<TrackOrder, TrackOrder>(callback) {
            @Override
            protected TrackOrder processResponse(TrackOrder signInResponse) {
                return signInResponse;
            }
        });
    }

    public void productDetails(String product_id,String customer_id, RestCallback<ProductDetailsRequest> callback) {

        restInterface.productDetails(product_id, customer_id, new MyCallback<ProductDetailsRequest, ProductDetailsRequest>(callback) {
            @Override
            protected ProductDetailsRequest processResponse(ProductDetailsRequest mViewAllReviewRequest) {
                return mViewAllReviewRequest;
            }
        });
    }

    public void mainCategoryList(String address, String store_id, RestCallback<MainCategoryRequest> callback) {

        restInterface.mainCategoryList(address, store_id, new MyCallback<MainCategoryRequest, MainCategoryRequest>(callback) {
            @Override
            protected MainCategoryRequest processResponse(MainCategoryRequest mSubCategoryRequest) {
                return mSubCategoryRequest;
            }
        });
    }

    public void viewAllReviews(String product_id,  RestCallback<ViewAllReviewRequest> callback){

        restInterface.viewAllReview(product_id, new MyCallback<ViewAllReviewRequest, ViewAllReviewRequest>(callback) {
            @Override
            protected ViewAllReviewRequest processResponse(ViewAllReviewRequest mViewAllReviewRequest) {
                return mViewAllReviewRequest;
            }
        });
    }

    public void setRateReview(String product_id, String review_title, String review_detail, String customer_id,
                              String rating,RestCallback<RateReviewRequest> callback){

        restInterface.setRateReview(product_id, review_title, review_detail, customer_id, rating, new MyCallback<RateReviewRequest, RateReviewRequest>(callback) {
            @Override
            protected RateReviewRequest processResponse(RateReviewRequest mRateReviewRequest) {
                return mRateReviewRequest;
            }
        });
    }


    // Shopping List
    public void deleteShoppingListItem(String item_id, String customar, RestCallback<DeleteShoppingListItemRequest> callback) {
        restInterface.deleteShoppingListItem(item_id, customar,
                new MyCallback<DeleteShoppingListItemRequest, DeleteShoppingListItemRequest>(callback) {
                    @Override
                    protected DeleteShoppingListItemRequest processResponse(DeleteShoppingListItemRequest delShoppngListItemResResponse) {
                        return delShoppngListItemResResponse;
                    }
                });
    }

    public void moveShoppingListItem(String item_id, String listid, String customar, RestCallback<MoveShoppingListItemRequest> callback) {
        restInterface.moveShoppingListItem(item_id, listid, customar,
                new MyCallback<MoveShoppingListItemRequest, MoveShoppingListItemRequest>(callback) {
                    @Override
                    protected MoveShoppingListItemRequest processResponse(MoveShoppingListItemRequest moveShoppngListItemResResponse) {
                        return moveShoppngListItemResResponse;
                    }
                });
    }


    public void viewShoppingList(String customar_id,  RestCallback<ShoppingListRequest> callback){

        restInterface.viewShoppingList(customar_id, new MyCallback<ShoppingListRequest, ShoppingListRequest>(callback) {
            @Override
            protected ShoppingListRequest processResponse(ShoppingListRequest mShoppingListRequest) {
                return mShoppingListRequest;
            }
        });
    }

    public void viewShoppingDetailList(String id, String customar, RestCallback<ShoppingListDetailRequest> callback) {

        restInterface.viewShoppingDetailList(id, customar, new MyCallback<ShoppingListDetailRequest, ShoppingListDetailRequest>(callback) {
            @Override
            protected ShoppingListDetailRequest processResponse(ShoppingListDetailRequest mShoppingListDetailRequest) {
                return mShoppingListDetailRequest;
            }
        });
    }


    public void getViewMyShoppingListItem(String customer_id, String title,
                                          RestCallback<MyShoppingListItemRequest> callback) {

        restInterface.getViewMyShoppingListItem(customer_id, title,
                new MyCallback<MyShoppingListItemRequest, MyShoppingListItemRequest>(callback) {
                    @Override
                    protected MyShoppingListItemRequest processResponse(MyShoppingListItemRequest myShoppingListItemRequest) {
                        return myShoppingListItemRequest;
                    }
                });
    }


    public void getViewMyAllltemList(String customer_id, String title, String list_id,
                                     RestCallback<MyShoppingListAllltemRequest> callback) {
        restInterface.getViewMyAllltemList(customer_id, title, list_id,
                new MyCallback<MyShoppingListAllltemRequest, MyShoppingListAllltemRequest>(callback) {

                    @Override
                    protected MyShoppingListAllltemRequest processResponse(MyShoppingListAllltemRequest myShoppingListAllltemRequest) {
                        return myShoppingListAllltemRequest;
                    }
                });
    }

    public void getRemoveShoppingList(String remove_id, String customer_id,
                                      RestCallback<RemoveShoppingListRequest> callback) {
        restInterface.getRemoveShoppingList(remove_id, customer_id,
                new MyCallback<RemoveShoppingListRequest, RemoveShoppingListRequest>(callback) {

                    @Override
                    protected RemoveShoppingListRequest processResponse(RemoveShoppingListRequest mRemoveShoppingListRequest) {
                        return mRemoveShoppingListRequest;
                    }
                });
    }

    public void insertItemToShoppinglist(String quantity, String list_id, String list_title, String productid, String customer,
                                         RestCallback<AddItemToShoppingListRequest> callback) {
        restInterface.insertItemToShoppinglist(quantity, list_id, list_title, productid, customer,
                new MyCallback<AddItemToShoppingListRequest, AddItemToShoppingListRequest>(callback) {

                    @Override
                    protected AddItemToShoppingListRequest processResponse(AddItemToShoppingListRequest mAddItemToShoppingListRequest) {
                        return mAddItemToShoppingListRequest;
                    }
                });
    }

    public void deleteShoppingListItem(String item_id, String customar,String list_id, RestCallback<DeleteShoppingListItemRequest> callback) {
        restInterface.deleteShoppingListItem(item_id, customar, list_id,
                new MyCallback<DeleteShoppingListItemRequest, DeleteShoppingListItemRequest>(callback) {
                    @Override
                    protected DeleteShoppingListItemRequest processResponse(DeleteShoppingListItemRequest delShoppngListItemResResponse) {
                        return delShoppngListItemResResponse;
                    }
                });
    }

    public void moveShoppingListItem(String item_id, String listid,String from_list_id,String customar,String list_title, RestCallback<MoveShoppingListItemRequest> callback) {
        restInterface.moveShoppingListItem(item_id, listid, from_list_id, customar, list_title,
                new MyCallback<MoveShoppingListItemRequest, MoveShoppingListItemRequest>(callback) {
                    @Override
                    protected MoveShoppingListItemRequest processResponse(MoveShoppingListItemRequest moveShoppngListItemResResponse) {
                        return moveShoppngListItemResResponse;
                    }
                });
    }




    // Add To Cart
    public void addToCart(String product_id,String qty ,String attribute_id ,String customer_id ,
                          String option_id,RestCallback<AddToWishListRequest> callback){

        restInterface.addToCart(product_id, qty, attribute_id, customer_id, option_id, new MyCallback<AddToWishListRequest, AddToWishListRequest>(callback) {
            @Override
            protected AddToWishListRequest processResponse(AddToWishListRequest mAddToWishListRequest) {
                return mAddToWishListRequest;
            }
        });
    }
    public void getCartDetails(String customer_id, RestCallback<GetCartListRequest> callback){

        restInterface.getCartDetails(customer_id, new MyCallback<GetCartListRequest, GetCartListRequest>(callback) {
            @Override
            protected GetCartListRequest processResponse(GetCartListRequest mGetCartListRequest) {
                return mGetCartListRequest;
            }
        });
    }

    public void deleteItemCart(String customer_id,String cart_item_id, RestCallback<DeleteItemFromCartRequest> callback){

        restInterface.deleteItemCart(customer_id, cart_item_id, new MyCallback<DeleteItemFromCartRequest, DeleteItemFromCartRequest>(callback) {
            @Override
            protected DeleteItemFromCartRequest processResponse(DeleteItemFromCartRequest mDeleteItemFromCartRequest) {
                return mDeleteItemFromCartRequest;
            }
        });
    }

    // Address
    public void saveAddress(String customer_id,String address,
                            String latitude, String longitude, RestCallback<SaveAddressRequest> callback){

        restInterface.saveAddress(customer_id, address, latitude, longitude, new MyCallback<SaveAddressRequest, SaveAddressRequest>(callback) {
            @Override
            protected SaveAddressRequest processResponse(SaveAddressRequest saveAddressRequest) {
                return saveAddressRequest;
            }
        });
    }
    public void getAddress(String customer_id, RestCallback<GetAddressRequest> callback){

        restInterface.getAddress(customer_id, new MyCallback<GetAddressRequest, GetAddressRequest>(callback) {
            @Override
            protected GetAddressRequest processResponse(GetAddressRequest getAddressRequest) {
                return getAddressRequest;
            }
        });
    }

    public void getDeleveryFee(String product_ids,String customer_id, RestCallback<GetDeliveryFeeRequest> callback) {

        restInterface.getDeleveryFee(product_ids,customer_id, new MyCallback<GetDeliveryFeeRequest, GetDeliveryFeeRequest>(callback) {
            @Override
            protected GetDeliveryFeeRequest processResponse(GetDeliveryFeeRequest getDelFeeRequest) {
                return getDelFeeRequest;
            }
        });
    }
    public void getServiceTax(RestCallback<ServiceTaxRequest> callback){

        restInterface.getServiceTax(new MyCallback<ServiceTaxRequest, ServiceTaxRequest>(callback) {
            @Override
            protected ServiceTaxRequest processResponse(ServiceTaxRequest signInResponse) {
                return signInResponse;
            }
        });
    }

    public void getAllHub(RestCallback<GetAllHubRequest> callback){

        restInterface.getAllHub(new MyCallback<GetAllHubRequest, GetAllHubRequest>(callback) {
            @Override
            protected GetAllHubRequest processResponse(GetAllHubRequest hubResponse) {
                return hubResponse;
            }
        });
    }



   /* public void getMembership(String vendor_id, String membership_id,
                           RestCallback<MembershipRequest> callback) {

        restInterface.getmembershipassign(vendor_id, membership_id,
                new MyCallback<MembershipRequest, MembershipRequest>(callback) {
                    @Override
                    protected MembershipRequest processResponse(MembershipRequest paymentRequest) {
                        return paymentRequest;
                    }
                });
    }
    public void gerForgetPassword(String email_address,
                                  RestCallback<Forgetpassword> callback) {

        restInterface.getForgetPassword(email_address,
                new MyCallback<Forgetpassword, Forgetpassword>(callback) {
                    @Override
                    protected Forgetpassword processResponse(Forgetpassword signInResponse) {
                        return signInResponse;
                    }
                });
    }
    public void getPostOfferResponse(String vendor_id, String offer_details, String userLimit, String userCOunt, String day, String expiryDate,
                                  RestCallback<PostOfferRequest> callback) {

        restInterface.getPostOfferResponse(vendor_id, offer_details, userLimit, userCOunt, day, expiryDate,
                new MyCallback<PostOfferRequest, PostOfferRequest>(callback) {
                    @Override
                    protected PostOfferRequest processResponse(PostOfferRequest signInResponse) {
                        return signInResponse;
                    }
                });
    }
    public void getDepartmentpID(RestCallback<DepartmentListRequest> callback){

        restInterface.getDepartmentpID(new MyCallback<DepartmentListRequest, DepartmentListRequest>(callback) {
            @Override
            protected DepartmentListRequest processResponse(DepartmentListRequest signInResponse) {
                return signInResponse;
            }
        });
    }

    public void getCityID(RestCallback<CityListRequest> callback){

        restInterface.getCityID(new MyCallback<CityListRequest, CityListRequest>(callback) {
            @Override
            protected CityListRequest processResponse(CityListRequest signInResponse) {
                return signInResponse;
            }
        });
    }

    public void getHotOffers(RestCallback<HotOffersRequest> callback) {

        restInterface.getHotOffers(new MyCallback<HotOffersRequest, HotOffersRequest>(callback) {
            @Override
            protected HotOffersRequest processResponse(HotOffersRequest hotOffersResponse) {
                return hotOffersResponse;
            }
        });
    }

    public void getCloseOffer(String offer_id,
                              RestCallback<CloseOfferRequest> callback) {

        restInterface.getCloseOffer(offer_id,
                new MyCallback<CloseOfferRequest, CloseOfferRequest>(callback) {
                    @Override
                    protected CloseOfferRequest processResponse(CloseOfferRequest closeOfferRequest) {
                        return closeOfferRequest;
                    }
                });
    }
    public void CloseGrabOffer(String grab_id,
                               RestCallback<CloseGrabOfferRequest> callback) {

        restInterface.CloseGrabedOffer(grab_id,
                new MyCallback<CloseGrabOfferRequest, CloseGrabOfferRequest>(callback) {
                    @Override
                    protected CloseGrabOfferRequest processResponse(CloseGrabOfferRequest closeGrabOfferRequest) {
                        return closeGrabOfferRequest;
                    }
                });
    }
    public void DeleteOffer(String grab_id,
                               RestCallback<DeleteOfferRequest> callback) {

        restInterface.DeleteOffer(grab_id,
                new MyCallback<DeleteOfferRequest, DeleteOfferRequest>(callback) {
                    @Override
                    protected DeleteOfferRequest processResponse(DeleteOfferRequest closeGrabOfferRequest) {
                        return closeGrabOfferRequest;
                    }
                });
    }*/


    private List<MyCallback> callbacks = new ArrayList<MyCallback>();

    private abstract class MyCallback<M, N> implements Callback<N> {
        private RestCallback<M> restCallback;
        private boolean isCanceled;

        protected MyCallback(RestCallback<M> restCallback) {
            this.restCallback = restCallback;
            callbacks.add(this);
        }

        final void cancel() {
            isCanceled = true;
        }

        @Override
        public final void success(N n, Response response) {
            if (!isCanceled && restCallback != null) {
                try {
                    M m = processResponse(n);
                    if (m != null) {
                        Log.d(TAG, "success");
                        restCallback.success(m);
                    } else {
                        Log.d(TAG, "invalid");
                        restCallback.invalid();
                    }
                } catch (Exception e) {
                    Log.d(TAG, "parse failure");
                    String errorMessage = e.getMessage();
                    if (errorMessage != null && errorMessage.length() > 0) {
                        Log.e(TAG, e.getMessage());
                    } else {
                        e.printStackTrace();
                    }
                    restCallback.invalid();
                }
            }
        }

        @Override
        public final void failure(RetrofitError error) {
            if (!isCanceled && restCallback != null) {
                Log.d(TAG, "failure");
                String errorMessage = error.getMessage();
                if (errorMessage != null && errorMessage.length() > 0) {
                    Log.d(TAG, errorMessage);
                }
                restCallback.failure();
            }
        }

        protected abstract M processResponse(N n);
    }
    private List<MyCallback1> callbacks1 = new ArrayList<MyCallback1>();

    private abstract class MyCallback1<M, N> implements Callback<N> {
        private RestCallback1<M> restCallback1;
        private boolean isCanceled;

        protected MyCallback1(RestCallback1<M> restCallback) {
            this.restCallback1 = restCallback;
            callbacks1.add(this);
        }

        final void cancel() {
            isCanceled = true;
        }

        @Override
        public final void success(N n, Response response) {
            if (!isCanceled && restCallback1 != null) {
                M m = null;
                try {
                    m = processResponse(n);
                    if (m != null) {
                        Log.d(TAG, "success");
                        restCallback1.success(m);
                    } else {
                        Log.d(TAG, "invalid");
                        restCallback1.invalid(m);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "parse failure");
                    String errorMessage = e.getMessage();
                    if (errorMessage != null && errorMessage.length() > 0) {
                        Log.e(TAG, e.getMessage());
                    } else {
                        e.printStackTrace();
                    }
                    restCallback1.invalid(m);
                }
            }
        }

        @Override
        public final void failure(RetrofitError error) {
            if (!isCanceled && restCallback1 != null) {
                Log.d(TAG, "failure");
                String errorMessage = error.getMessage();
                if (errorMessage != null && errorMessage.length() > 0) {
                    Log.d(TAG, errorMessage);
                }
                restCallback1.failure();
            }
        }

        protected abstract M processResponse(N n);
    }
}


