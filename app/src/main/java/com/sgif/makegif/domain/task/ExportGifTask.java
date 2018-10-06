package com.sgif.makegif.domain.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;

import com.sgif.makegif.domain.model.Media;
import com.sgif.makegif.domain.model.MediaType;
import com.sgif.makegif.util.Utils;
import com.waynejo.androidndkgif.GifEncoder;

import java.io.IOException;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class ExportGifTask extends AsyncTask<ExportGifParams, Float, String> {
    private OnExportGifCallback mCallback;

    public ExportGifTask(OnExportGifCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected String doInBackground(ExportGifParams... exportGifParams) {
        try {
            encodeGif(exportGifParams[0]);
            return exportGifParams[0].getResultPath();
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

    private void encodeGif(ExportGifParams params) throws IOException {
        MediaType mediaType = params.getMediaType();
        int width = params.getWidth();
        int height = params.getHeight();
        int delayMs = params.getDelay() == 0 ? 10 : params.getDelay();

        GifEncoder gifEncoder = new GifEncoder();
        gifEncoder.init(width, height, params.getResultPath(), GifEncoder.EncodingType.ENCODING_TYPE_SIMPLE_FAST);
        gifEncoder.setDither(true);
        Bitmap destBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(destBitmap);
        Paint p = new Paint();
        switch (mediaType) {
            case PHOTO:
                final List<Media> photoList = params.getPhotos();
                for (int i = 0; i < photoList.size(); i++) {
                    canvas.drawColor(Color.BLACK);
                    String s = photoList.get(i).getPath();
                    Bitmap bitmap = BitmapFactory.decodeFile(s);
                    Matrix matrix = Utils.getMatrix(bitmap.getWidth(), bitmap.getHeight(), destBitmap.getWidth(), destBitmap.getHeight());
                    canvas.drawBitmap(bitmap, matrix, p);
                    gifEncoder.encodeFrame(destBitmap, delayMs);
                    publishProgress(i * 1.0f / photoList.size());
                }
                break;
            case VIDEO:
                String pathVideo = params.getVideo().getPath();
                Bitmap temp = Utils.getThumbnail(pathVideo, 0);
                Matrix matrix = Utils.getMatrix(temp.getWidth(), temp.getHeight(), destBitmap.getWidth(), destBitmap.getHeight());
                int startTime = params.getStartTime();
                int endTime = params.getEndTime();
                int duration = (endTime - startTime);
                for (int i = startTime; i < endTime; i += delayMs) {
                    canvas.drawColor(Color.BLACK);
                    Bitmap bitmap = Utils.getThumbnail(pathVideo, i);
                    if (bitmap != null) {
                        canvas.drawBitmap(bitmap, matrix, p);
                        gifEncoder.encodeFrame(destBitmap, delayMs);
                        publishProgress((i - startTime) * 1.0f / duration);
                    }
                }
                break;
        }

        gifEncoder.close();
    }
}
