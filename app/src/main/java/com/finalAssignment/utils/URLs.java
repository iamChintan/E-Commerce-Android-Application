package com.finalAssignment.utils;

public class URLs {

    private static String MAIN_URL = "http://ec2-52-14-153-226.us-east-2.compute.amazonaws.com/API";

    public static String SIGN_UP() {
        return MAIN_URL + "/Users/doRegister.php";
    }

    public static String UPDATE_PROFILE() {
        return MAIN_URL + "/Users/updateProfile.php";
    }
    public static String GET_PROFILE() {
        return MAIN_URL + "/Users/getUserInfo.php";
    }
    public static String CHANGE_PASSWORD() {
        return MAIN_URL + "/Users/changePassword.php";
    }

    public static String GET_PRODUCTS() {
        return MAIN_URL + "/Product/getProducts.php";
    }

    public static String ADD_TO_CART() {
        return MAIN_URL + "/Cart/updateCart.php";
    }

    public static String MAKE_ORDER() {
        return MAIN_URL + "/Order/makeOrder.php";
    }

    public static String GET_CART() {
        return MAIN_URL + "/Cart/getCartItem.php";
    }
    public static String GET_ORDER() {
        return MAIN_URL + "/Order/getOrders.php";
    }

    public static String LOG_IN() {
        return MAIN_URL + "/Users/doLogin.php";
    }
}