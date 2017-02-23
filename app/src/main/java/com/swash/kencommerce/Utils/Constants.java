package com.swash.kencommerce.Utils;

import java.util.ArrayList;

/**
 * Created by Kranti on 12/12/2016.
 */

public class Constants {
    public static String strShPrefUserSelection = "UserSelection";
    public static String strShUserShopSelected = "UserShopSelected";
    public static String strShUserStoreID = "UserStoreid";
    public static String strShUserShopFood = "food";
    public static String strShUserShopMainProduct = "welcome";
    public static String strShUserShopDay = "day";
    public static String strShUserShopGift = "gift";
    public static String strShUserShopEvent = "event";
    public static String strCatName="";
    public static String strAge ="age";
    public static String strAgeNumber ="18";
    public static String strTipPercent ="";
    public static Boolean strSeeAllProduct =false;
    // Shared Pref Details
    public static String strShPrefDelAddrName = "ShPrefDeliveryAddr";
    public static String strShPrefDelLat = "deliveryLat";
    public static String strShPrefDelLong = "deliveryLong";
    public static String strShPrefDelAddr = "deliveryAdd";
    public static String strShPrefDelCallFrom = "No";
    public static String strShPrefDeliver = "false";

    // Shared Pref Details Price and Qnty of add to cart/////////////
    public static String strShPrefAddToCart = "ShPrefAddToCart";
    public static String strShPrefPrice = "price";
    public static String strShPrefOnty = "qnty";
    public static String strShPrefIsInsert = "No";


    public static String deliverytime="";
    public static String deliveryDay="";
    // Shared Pref User Details
    public static String strShPrefUserPrefName = "ShPrefUser";
    public static String strShPrefUserId = "userId";
    public static String strShPrefUserFname = "fName";
    public static String strShPrefUserLname = "lName";
    public static String strShPrefUserName = "name";
    public static String strShPrefUserEmail = "email";
    public static String strShPrefUserPhone = "phone";

    public static String strShUserProductId = "UserProductId";
    public static String strShStoreID = "UserStoreID";
    public static Boolean flagMapPage = false;
    public static boolean flagLocation = false;
    public static String strCurLat = "22.4";
    public static String strCurLong = "88.6";
    public static String lastDeliveryTime40="";
    public static String lastDeliveryTime15="";
    public static String isFirstTimeDelivery="";
    public  static String deliveryInstruction="";
    public  static String fName="";
    public  static String lName="";
    public  static String emailAdd="";
    public  static String mobilenum="";
    public  static String fromOrderStatus="";
    public  static String address="";
    public  static String orderID="";
    public  static String latitude="";
    public  static String longitude="";
    public  static String productIDS="";
    public  static String paymenttype="";
    public  static String deliverytypeCart="";
    public  static String deliverytypeCheckout="";
    public  static String deliveryDate="";
    public  static String deliveryDateCheckout="";
    public  static double deliveryfee=0;
    public  static float serviceTax=0;
    public  static double driver_tip=0;
    public  static String cc_owner="";
    public  static String cc_number="";
    public  static String cc_type="";
    public  static String cc_cvv="";
    public  static String cc_expiry_month="";
    public  static String cc_expiry_year="";
    public  static String cc_card_type="";
    public static String urlMain = "http://cruiseaelb-538663152.ap-southeast-2.elb.amazonaws.com/api/v2";
    public static String urlGetLocation = "/get_latlong";

    public static String urlMainKenCommerce = "http://www.swashconvergence.com/contact.html";
    public static String urlGetDelData = "/order/orderLastInfo";
    public static ArrayList<String> arrMixed=new ArrayList<>();
}
