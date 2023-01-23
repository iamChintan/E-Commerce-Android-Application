package com.finalAssignment.utils;

import android.content.Context;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RequestParamsUtils {

    public static final String USER_ID = "user_id";
    public static final String SESSION_ID = "session_id";
    public static final String AUTHORIZATION = "Authorization";

    public static FormBody.Builder newRequestFormBody(Context c) {
        FormBody.Builder builder = new FormBody.Builder();

        return builder;
    }
}
