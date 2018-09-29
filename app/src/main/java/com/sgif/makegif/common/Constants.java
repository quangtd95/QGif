package com.sgif.makegif.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public class Constants {
    public static final String TYPE_IMAGE = "type_image";
    public static final String TYPE_VIDEO = "type_video";

    public static final String BUNDLE_KEY_LIST_PHOTO = "bundle_key_list_photo";
    public static final String BUNDLE_KEY_PATH_GIF = "bundle_key_path_gif";

    public static final int MAX_PHOTO = 100;
    public static final int MIN_PHOTO = 2;

    public static final int MAX_DELAY = 2000;
    public static final int MIN_DELAY = 500;

    public static final int MAX_WIDTH = 1000;
    public static final int MAX_HEIGHT = 1000;

    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 500;
    public static final int DEFAULT_DELAY = 500;

    //%s : thời gian xuất gif
    public static String RESULT_PATH = Environment.getExternalStorageDirectory() + File.separator + "SGif_%s.gif";
}
