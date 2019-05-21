package com.quangtd.qgifmaker.screen.custom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import com.quangtd.qgifmaker.domain.model.Photo;
import com.quangtd.qgifmaker.screen.gallery.adapter.ChooseAdapter;
import com.quangtd.qgifmaker.util.Utils;

import java.io.IOException;
import java.util.List;

public class PreviewGifPresenter {
    private List<Photo> mPhotos;
    private SparseArray<Bitmap> hmBitmap;
    private long mStartTime = 0;
    //always milliseconds
    private int mDelay = 2000;

    private int mWidth;

    private int mHeight;

    private int mDefaultWidth = 0;

    private int mDefaultHeight = 0;

    private float ratio = 1F;

    private boolean mKeepRatio = true;

    private boolean mIsStartFlg = false;

    private Handler mHandlerPreview;

    private GifView mView;

    private Paint mPaint;

    public PreviewGifPresenter(GifView gifView) {
        this.mView = gifView;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (hmBitmap == null) {
            hmBitmap = new SparseArray<>();
        }
        mHandlerPreview = new Handler(Looper.getMainLooper());
    }

    public void setAdapter(ChooseAdapter adapter) {
        this.mPhotos = adapter.getItems();
        this.mStartTime = 0;
        hmBitmap.clear();
    }


    void drawPreview(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        if (mIsStartFlg) {
            int index = getIndexByTime();
            if (hmBitmap.get(index) == null) {
                String s = mPhotos.get(index).getPath();
                Bitmap bitmap = decodeScaledBitmapFromSdCard(s, mView.getWidth() / 4, mView.getHeight() / 4);
                hmBitmap.put(index, bitmap);
            }
            Bitmap bitmap = hmBitmap.get(index);
            Matrix matrix = Utils.getMatrix(bitmap.getWidth(), bitmap.getHeight(), mView.getWidth(), mView.getHeight());
            canvas.drawBitmap(bitmap, matrix, mPaint);
        }
    }

    private Bitmap decodeScaledBitmapFromSdCard(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        ExifInterface exif;
        Matrix matrix = new Matrix();
        int rotate = 0;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270: {
                    rotate = 270;
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_180: {
                    rotate = 180;
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_90: {
                    rotate = 90;
                    break;
                }
            }
            matrix.setRotate(rotate);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return (rotate == 0) ? bitmap : Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round(height * 1.0f / reqHeight);
            int widthRatio = Math.round(width * 1.0f / reqWidth);
            inSampleSize = (heightRatio < widthRatio) ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private long getTotalTime() {
        return mPhotos.size() * mDelay;
    }

    private long elapsedTime() {
        return System.currentTimeMillis() - this.mStartTime;
    }

    public void startPreview() {
        this.mStartTime = System.currentTimeMillis();
        this.mIsStartFlg = true;
        mHandlerPreview.post(new Runnable() {
            @Override
            public void run() {
                mView.invalidate();
                mHandlerPreview.postDelayed(this, 1000 / 24);
            }
        });
    }

    public int getIndexByTime() {
        long elapsedTime = elapsedTime();
        if (mDelay != 0) {
            return (int) (elapsedTime / mDelay % mPhotos.size());
        } else {
            return 0;
        }

    }


    public void setDuration(float duration) {
        mDelay = (int) duration;
    }

    public void remove(int position) {
        mIsStartFlg = false;
        hmBitmap.clear();
        mIsStartFlg = true;
    }
}
