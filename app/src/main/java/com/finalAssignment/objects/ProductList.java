package com.finalAssignment.objects;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductList {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<Datum> data = new ArrayList<Datum>();

    public class Datum {

        @SerializedName("productId")
        @Expose
        public String productId;
        @SerializedName("productName")
        @Expose
        public String productName;
        @SerializedName("productDescription")
        @Expose
        public String productDescription;
        @SerializedName("productImage")
        @Expose
        public String productImage;
        @SerializedName("productPrice")
        @Expose
        public String productPrice;
        @SerializedName("shippingCost")
        @Expose
        public String shippingCost;

    }
}
