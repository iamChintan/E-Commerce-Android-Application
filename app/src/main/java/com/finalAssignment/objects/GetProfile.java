package com.finalAssignment.objects;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProfile {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;


}
