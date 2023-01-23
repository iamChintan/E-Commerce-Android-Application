package com.finalAssignment.utils;

import android.os.Environment;

import java.io.File;

public class Constant {
    public static final String FOLDER_NAME = ".diamfair";
    public static final String CACHE_DIR = ".diamfair/Cache";

    public static final String TMP_DIR = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + FOLDER_NAME + "/tmp";

    public static final String PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "" + FOLDER_NAME;

    public static final String FOLDER_RIDEINN_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + "DiamFair";

    public static final String LOGIN_INFO = "login_info";


    public static final String FINISH_ACTIVITY = "finish_activity";

    public static final int REQ_CODE_SETTING = 555;

}
