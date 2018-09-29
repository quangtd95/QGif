package com.sgif.makegif.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import com.sgif.makegif.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public class Utils {

    private static Dialog dialog;
    private static int screenWidth = -1;


    // size to the
    // screen width
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Activity activity) {

        if (screenWidth != -1)
            return screenWidth;
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();

        int width;
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            width = size.x;
        } else {
            width = display.getWidth();

        }
        screenWidth = width;

        return width;
    }

    public static int getScreenWidth(final Context context) {
        if (context == null) {
            return 0;
        }
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * Returns a valid DisplayMetrics object
     *
     * @param context valid context
     * @return DisplayMetrics object
     */
    static DisplayMetrics getDisplayMetrics(final Context context) {
        final WindowManager
                windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public static void resizeView(View view, int width, int height) {
        ViewGroup.LayoutParams layout = view.getLayoutParams();
        layout.width = width;
        layout.height = height;
        view.setLayoutParams(layout);
    }

    public static int convertDpToPixel(Context context, int dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / 160f));
    }

    /**
     * @param duration second
     * @return 00:00
     */
    public static String convertDurationToString(int duration) {
        if (duration < 60) return ":" + duration;
        int minute = duration / 60;
        int second = duration % 60;
        return minute + ":" + (second >= 10 ? second : "0" + second);
    }

    /**
     * @param time: milisecond
     * @return 00:00:00.000
     */
    public static String convertLongDurationToString(long time) {
        if (time < 0) return "00:00:00.000";
        int seconds = (int) (time / 1000);
        int miliseconds = (int) (time - seconds * 1000);
        int minutes = seconds / 60;
        seconds = seconds - minutes * 60;
        int hours = minutes / 60;
        minutes = minutes - hours * 60;
        String s_miliseconds = "";
        if (miliseconds < 10) s_miliseconds = String.format(Locale.US, "00%d", miliseconds);
        else if (miliseconds < 100) s_miliseconds = String.format(Locale.US, "0%d", miliseconds);
        else if (miliseconds < 1000) s_miliseconds = String.format(Locale.US, "%d", miliseconds);
        return String.format(Locale.US, "%s:%s:%s.%s",
                ((hours < 10) ? "0" : "") + hours,
                ((minutes < 10) ? "0" : "") + minutes,
                ((seconds < 10) ? "0" : "") + seconds,
                s_miliseconds);
    }


    public static String parseTimeStampToString(long timeStamp) {
        try {
            DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy_hh-mm-ss", Locale.US);
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }


    public static List<Bitmap> getThumbnails(String videoPath, float msDuration, int number, int width, int height) {
        List<Bitmap> bitmaps = new ArrayList<>();
        try {
            float step = msDuration / number;
            for (int i = 0; i < number; i++) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(videoPath);
                Bitmap bitmapOrigin = retriever.getFrameAtTime((long) (i * step * 1000));
                Bitmap bitmap = Bitmap.createScaledBitmap(cropBitmap(bitmapOrigin), width, height, true);
                bitmaps.add(bitmap);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return bitmaps;
    }

    public static Bitmap getThumbnail(String videoPath, float msDuration, int width, int height) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        Bitmap bitmapOrigin = retriever.getFrameAtTime((long) (msDuration * 1000));
        return Bitmap.createScaledBitmap(cropBitmap(bitmapOrigin), width, height, true);
    }

    private static Bitmap cropBitmap(Bitmap srcBmp) {
        Bitmap dstBmp;
        int w = srcBmp.getWidth();
        int h = srcBmp.getHeight();
        dstBmp = Bitmap.createBitmap(srcBmp, w / 2 - h / 4, 0, h / 2, h);
        return dstBmp;
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view.requestFocus()) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * @param videoPath
     * @param msDuration : miliseconds
     * @return
     */
    public static Bitmap getThumbnail(String videoPath, float msDuration) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        Bitmap bitmap = retriever.getFrameAtTime((long) (msDuration * 1000), MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        retriever.release();
        return bitmap;
    }
}
