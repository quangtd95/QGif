package com.sgif.makegif.view.export;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sgif.makegif.common.Constants;
import com.sgif.makegif.common.base.BasePresenter;
import com.sgif.makegif.domain.model.Photo;
import com.sgif.makegif.domain.task.ExportGifParams;
import com.sgif.makegif.domain.task.ExportPhotoGifTask;
import com.sgif.makegif.view.photo.PhotoChooseAdapter;

import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class ExportGifPhotoPresenter extends BasePresenter<ExportGifPhotoView> implements ExportPhotoGifTask.OnExportPhotoGifCallback {
    private PhotoChooseAdapter mPhotoAdapter;

    //always milliseconds
    private int mDelay = Constants.DEFAULT_DELAY;

    private int mWidth;

    private int mHeight;

    private int mDefaultWidth = 0;

    private int mDefaultHeight = 0;

    private float ratio = 1F;

    private boolean mKeepRatio = true;


    public void calculateDefaultDimens() {
        mDefaultWidth = 1;
        mDefaultHeight = 1;
        List<Photo> photoList = mPhotoAdapter.getItems();
        for (Photo photo : photoList) {
            String path = photo.getPath();
            if (!TextUtils.isEmpty(path)) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (bitmap != null) {
                    if (bitmap.getWidth() > mDefaultWidth) mDefaultWidth = bitmap.getWidth();
                    if (bitmap.getHeight() > mDefaultHeight) mDefaultHeight = bitmap.getHeight();
                }
            }
        }
        ratio = mDefaultWidth * 1.0f / mDefaultHeight;
        if (mDefaultWidth > Constants.DEFAULT_WIDTH) {
            mDefaultWidth = Constants.DEFAULT_WIDTH;
            mDefaultHeight = (int) (mDefaultWidth * 1.0f / ratio);
        }
        if (mDefaultHeight > Constants.DEFAULT_HEIGHT) {
            mDefaultHeight = Constants.DEFAULT_HEIGHT;
            mDefaultWidth = (int) (mDefaultHeight * 1.0f * ratio);
        }
        mWidth = mDefaultWidth;
        mHeight = mDefaultHeight;
    }

    public void setAdapter(PhotoChooseAdapter photoChooseAdapter) {
        this.mPhotoAdapter = photoChooseAdapter;
        calculateDefaultDimens();
    }

    public void setDelay(int delay) {
        float range = Constants.MAX_DELAY - Constants.MIN_DELAY;
        this.mDelay = (int) (delay * 1.0f / 100 * range + Constants.MIN_DELAY);
        getView().setDuration(mDelay);
    }

    public void setWidth(int width) {
        int height = mHeight;
        if (width > Constants.MAX_WIDTH) {
            width = Constants.MAX_WIDTH;
        }

        if (isKeepRatio()) {
            height = (int) (width * 1.0f / ratio);
            if (height > Constants.MAX_HEIGHT) {
                height = Constants.MAX_HEIGHT;
                width = (int) (height * 1.0f * ratio);
            }
        }

        this.mWidth = width;
        this.mHeight = height;
        getView().setWidthGif(String.valueOf(mWidth));
        getView().setHeightGif(String.valueOf(mHeight));
    }

    public boolean isKeepRatio() {
        return mKeepRatio;
    }

    public void setKeepRatio(boolean b) {
        mKeepRatio = b;
    }

    public void setHeight(int height) {
        int width = mWidth;
        if (height > Constants.MAX_HEIGHT) {
            height = Constants.MAX_HEIGHT;
        }

        if (isKeepRatio()) {
            width = (int) (height * 1.0f * ratio);
            if (width > Constants.MAX_WIDTH) {
                width = Constants.MAX_WIDTH;
                height = (int) (width * 1.0f / ratio);
            }
        }
        this.mWidth = width;
        this.mHeight = height;
        getView().setWidthGif(String.valueOf(mWidth));
        getView().setHeightGif(String.valueOf(mHeight));
    }

    public void setDefaultDimens() {
        mWidth = mDefaultWidth;
        mHeight = mDefaultHeight;
        getView().setWidthGif(String.valueOf(mWidth));
        getView().setHeightGif(String.valueOf(mHeight));
    }

    public void exportGif() {
        ExportGifParams params = new ExportGifParams();
        if (mDelay > Constants.MAX_DELAY || mDelay < Constants.MIN_DELAY) {
            getView().showNotifyDialog("delay time is not correct");
            return;
        } else {
            params.setDelay(mDelay);
        }
        if (mWidth <= 0 || mWidth > Constants.MAX_WIDTH) {
            getView().showNotifyDialog("width is not correct");
            return;
        } else {
            params.setWidth(mWidth);
        }
        if (mHeight <= 0 || mHeight > Constants.MAX_HEIGHT) {
            getView().showNotifyDialog("height is not correct");
            return;
        } else {
            params.setHeight(mHeight);
        }
        if (mPhotoAdapter.size() < Constants.MIN_PHOTO || mPhotoAdapter.size() > Constants.MAX_PHOTO) {
            getView().showNotifyDialog("list photos are not correct");
            return;
        } else {
            params.setPhotos(mPhotoAdapter.getItems());
        }
        ExportPhotoGifTask gifTask = new ExportPhotoGifTask(this);
        gifTask.execute(params);
    }

    @Override
    public void onProgressExportGif(Float progress) {
        getView().onProgress(progress);
    }

    @Override
    public void onCompleteExportGif(String s) {
        getView().onCompleteExport(s);
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancelledExportGif(String s) {
        getView().onCancelledExport(s);
        getView().showNotifyDialog(s);
    }

    @Override
    public void onPrepareExportGif() {
        getView().onPrepareExport();
    }
}
