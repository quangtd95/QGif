package com.sgif.makegif.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public class Utils {

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
    private static DisplayMetrics getDisplayMetrics(final Context context) {
        final WindowManager
                windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public static int convertDpToPixel(Context context, int dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / 160f));
    }

    /**
     * @param timeStamp thời gian cần đổi
     * @return định dạng MM-dd-yyyy_hh-mm-ss
     */
    public static String parseTimeStampToString(long timeStamp) {
        try {
            DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy_hh-mm-ss", Locale.US);
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }


    /**
     * @param videoPath  : đường dẫn của video
     * @param msDuration : đơn vị millisecond
     * @return thumbnail của video tại thời gian msDuration
     */
    public static Bitmap getThumbnail(String videoPath, float msDuration) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        Bitmap bitmap = retriever.getFrameAtTime((long) (msDuration * 1000), MediaMetadataRetriever.OPTION_CLOSEST);
        retriever.release();
        return bitmap;
    }

    /**
     * <p>Hàm tính toán matrix khi draw bitmap source lên bitmap đích</p>
     * <br/>
     * <br/>
     *
     * @param srcWidth  : width của bitmap source
     * @param srcHeight : height của bitmap source
     * @param desWidth  : width của bitmap đích
     * @param desHeight : height của bitmap dích
     * @return <p>kết quả sẽ là matrix để bitmap source nằm ở trung tâm bitmap đích</p>
     */
    public static Matrix getMatrix(int srcWidth, int srcHeight, int desWidth, int desHeight) {
        Matrix matrix = new Matrix();
        float scaleX;
        float scaleY;
        float newWidth;
        float newHeight;

        scaleX = desWidth * 1.0f / srcWidth;
        newWidth = desWidth;
        newHeight = srcHeight * scaleX;
        if (newHeight > desHeight) {
            scaleY = desHeight * 1.0f / newHeight;
            newHeight = desHeight;
            newWidth = newWidth * scaleY;
        }
        matrix.setScale(newWidth * 1.0f / srcWidth, newHeight * 1.0f / srcHeight);
        matrix.postTranslate((desWidth - newWidth) / 2, (desHeight - newHeight) / 2);
        return matrix;
    }

}
