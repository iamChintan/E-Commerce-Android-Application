package com.finalAssignment.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.finalAssignment.objects.Login;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.finalAssignment.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static void setPref(Context c, String pref, String val) {
        Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putString(pref, val);
        e.commit();
    }

    public static String getPref(Context c, String pref, String val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(pref,
                val);
    }

    public static void delPref(Context c, String pref) {
        Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.remove(pref);
        e.commit();
    }

    public static int getDeviceWidth(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.widthPixels;
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }

        return 480;
    }

    public static int getDeviceHeight(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.heightPixels;
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }

        return 800;
    }

    public static boolean isInternetConnected(Context mContext) {
        boolean outcome = false;

        try {
            if (mContext != null) {
                ConnectivityManager cm = (ConnectivityManager) mContext
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo[] networkInfos = cm.getAllNetworkInfo();

                for (NetworkInfo tempNetworkInfo : networkInfos) {
                    if (tempNetworkInfo.isConnected()) {
                        outcome = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outcome;
    }

    public static Typeface getBold(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(),
                    "RobotoCondensed-Bold.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Typeface getNormal(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(),
                    "RobotoCondensed-Regular.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static LayoutAnimationController getRowFadeSpeedAnimation(Context c) {
        AlphaAnimation anim = (AlphaAnimation) AnimationUtils.loadAnimation(c,
                R.anim.raw_fade);
        LayoutAnimationController controller = new LayoutAnimationController(
                anim, 0.3f);

        return controller;
    }

    public static void sendExceptionReport(Exception e) {
        e.printStackTrace();

        try {
            // Writer result = new StringWriter();
            // PrintWriter printWriter = new PrintWriter(result);
            // e.printStackTrace(printWriter);
            // String stacktrace = result.toString();
            // new CustomExceptionHandler(c, URLs.URL_STACKTRACE)
            // .sendToServer(stacktrace);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    public static Calendar getDatePart(Date date) {
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

        return cal;                                  // return the date part
    }

    public static String nullSafe(String content) {
        if (content == null) {
            return "";
        }

        if (content.equalsIgnoreCase("null")) {
            return "";
        }

        return content;
    }

    public static ImageLoader initImageLoader(Context mContext) {
        ImageLoader imageLoader = null;
        try {
            File cacheDir = StorageUtils.getOwnCacheDirectory(mContext,
                    Constant.CACHE_DIR);

            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true).cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                    mContext).defaultDisplayImageOptions(defaultOptions)
                    .diskCache(new UnlimitedDiskCache(cacheDir))
                    .memoryCache(new WeakMemoryCache());

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);
            L.writeLogs(false);

            return imageLoader;
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }

        try {
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                    mContext).defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(new WeakMemoryCache());

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);
            L.writeLogs(false);
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }

        return imageLoader;
    }

    public static boolean isSDcardMounted() {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)
                && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return true;
        }

        return false;
    }

    public static boolean isUserLoggedIn(Context c) {
        return (getSessionId(c).length() > 0) ? true : false;
    }

    public static String getSessionId(Context c) {
        return Utils.getPref(c, RequestParamsUtils.SESSION_ID, "");
    }

    public static Login getLoginDetails(Context c) {
        String response = Utils.getPref(c, Constant.LOGIN_INFO, "");

        if (response != null && response.length() > 0) {

            Login login = new Gson().fromJson(
                    response, new TypeToken<Login>() {
                    }.getType());

            if (login.status.equals("200")) {
                return login;
            }

        }

        return null;
    }


    public static void clearLoginCredetials(Activity c) {
        Utils.delPref(c, RequestParamsUtils.USER_ID);
        Utils.delPref(c, RequestParamsUtils.SESSION_ID);
        Utils.delPref(c, Constant.LOGIN_INFO);
    }

    public static void showDialog(final Activity c, String title, String message) {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(c)
                .title(title)
                .content(message)
                .positiveText(R.string.btn_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {

                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {

                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }
}
