package com.finalAssignment.utils;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Debug {

    public static final boolean DEBUG = true;

    public static void e(@NonNull String tag, @Nullable String msg) {
        if (DEBUG) {
            if (TextUtils.isEmpty(tag))
                tag = "unknown";

//            Log.e(tag, msg);
            final String finalTag = tag;
            log(msg, new LogCB() {
                @Override
                public void log(@NonNull String message) {
                    Log.e(finalTag, message);
                }
            });
        }
    }

    private static final int MAX_LOG_LENGTH = 4000;

    private static void log(@Nullable String message, @NonNull LogCB callback) {
        if (message == null) {
            callback.log("null");
            return;
        }
        // Split by line, then ensure each line can fit into Log's maximum length.
        for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
                int end = Math.min(newline, i + MAX_LOG_LENGTH);
                callback.log(message.substring(i, end));
                i = end;
            } while (i < newline);
        }
    }

    private interface LogCB {
        void log(@NonNull String message);
    }

}