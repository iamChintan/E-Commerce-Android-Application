package com.finalAssignment.objects;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public Message message;

    public class Message {
        @SerializedName("UserId")
        @Expose
        public String userId;
        @SerializedName("EmailAddress")
        @Expose
        public String emailAddress;
        @SerializedName("Username")
        @Expose
        public String username;
        @SerializedName("Token")
        @Expose
        public String token;

    }
}
