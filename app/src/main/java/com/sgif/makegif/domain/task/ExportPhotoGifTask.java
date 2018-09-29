package com.sgif.makegif.domain.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Toast;

import com.sgif.makegif.common.Constants;
import com.sgif.makegif.domain.model.Photo;
import com.sgif.makegif.util.Utils;
import com.waynejo.androidndkgif.GifEncoder;

import java.io.IOException;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class ExportPhotoGifTask extends AsyncTask<ExportGifParams, Float, String> {
    private OnExportPhotoGifCallback mCallback;
    private String mResultPath;

    public ExportPhotoGifTask(OnExportPhotoGifCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected String doInBackground(ExportGifParams... exportGifParams) {
        try {
            encodeListImage(exportGifParams[0]);
            return mResultPath;
        } catch (IOException e) {
            e.printStackTrace();
            onCancelled(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mCallback.onCompleteExportGif(s);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCallback.onPrepareExportGif();
        mResultPath = String.format(Constants.RESULT_PATH, Utils.parseTimeStampToString(System.currentTimeMillis()));
    }

    @Override
    protected void onProgressUpdate(Float... values) {
        super.onProgressUpdate(values);
        mCallback.onProgressExportGif(values.length > 0 ? values[0] : 0);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
        mCallback.onCancelledExportGif(s);
    }

    private void encodeListImage(ExportGifParams params) throws IOException {
        final List<Photo> photoList = params.getPhotos();
        final String filePath = mResultPath;

        int width = params.getWidth();
        int height = params.getHeight();
        int delayMs = params.getDelay();

        GifEncoder gifEncoder = new GifEncoder();
        gifEncoder.init(width, height, filePath, GifEncoder.EncodingType.ENCODING_TYPE_SIMPLE_FAST);
        gifEncoder.setDither(true);
        Bitmap destBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(destBitmap);
        Paint p = new Paint();

        for (int i = 0; i < photoList.size(); i++) {
            canvas.drawColor(Color.BLACK);
            String s = photoList.get(i).getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(s);
            Matrix matrix = Utils.getMatrix(bitmap.getWidth(), bitmap.getHeight(), destBitmap.getWidth(), destBitmap.getHeight());
            canvas.drawBitmap(bitmap, matrix, p);
            gifEncoder.encodeFrame(destBitmap, delayMs);
            publishProgress(i * 1.0f / photoList.size());
        }
        gifEncoder.close();
    }


    public interface OnExportPhotoGifCallback {
        void onProgressExportGif(Float progress);

        void onCompleteExportGif(String s);

        void onCancelledExportGif(String s);

        void onPrepareExportGif();
    }
}
