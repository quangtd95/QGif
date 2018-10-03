package com.sgif.makegif.screen.export;

import android.graphics.Bitmap;
import android.widget.Toast;

import com.sgif.makegif.common.Constants;
import com.sgif.makegif.common.base.BasePresenter;
import com.sgif.makegif.domain.model.Media;
import com.sgif.makegif.domain.model.MediaType;
import com.sgif.makegif.domain.task.ExportGifParams;
import com.sgif.makegif.domain.task.ExportGifTask;
import com.sgif.makegif.domain.task.OnExportGifCallback;
import com.sgif.makegif.util.Utils;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class ExportGifVideoPresenter extends BasePresenter<ExportGifVideoView> implements OnExportGifCallback {
    private Media mVideo;

    //always milliseconds
    private int mDelay = Constants.DEFAULT_DELAY;

    private int mStartTime;

    private int mEndTime;

    private int mWidth;

    private int mHeight;

    private int mDefaultWidth = 0;

    private int mDefaultHeight = 0;

    private float ratio = 1F;

    private boolean mKeepRatio = true;


    private void calculateDefaultValues() {
        Bitmap bitmap = Utils.getThumbnail(mVideo.getPath(), 0);
        mDefaultWidth = bitmap.getWidth();
        mDefaultHeight = bitmap.getHeight();

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
        mStartTime = 0;
        mEndTime = mVideo.getDuration();
    }

    public void setVideo(Media video) {
        this.mVideo = video;
        calculateDefaultValues();
    }

    public void setDelay(int delay) {
        float range = Constants.MAX_DELAY - Constants.MIN_DELAY;
        this.mDelay = (int) (delay * 1.0f / 100 * range + Constants.MIN_DELAY);
        getView().setDelay(mDelay);
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
        ExportGifParams params = new ExportGifParams(MediaType.VIDEO);
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
        if (mVideo != null) {
            if (mStartTime >= mEndTime || mStartTime < 0 || mStartTime > mVideo.getDuration() || mEndTime < 0 || mEndTime > mVideo.getDuration()) {
                getView().showNotifyDialog("time is not correct");
            } else {
                params.setVideo(mVideo);
                params.setStartTime(mStartTime);
                params.setEndTime(mEndTime != 0 ? mEndTime : mVideo.getDuration());
            }
        } else {
            getView().showNotifyDialog("video is not correct");
        }


        ExportGifTask gifTask = new ExportGifTask(this);
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


    public void setStartTime(int progress) {
        this.mStartTime = (int) (progress * 1.0f / 100 * mVideo.getDuration());
        if (mStartTime <= 0) mStartTime = 0;
        if (mStartTime >= mEndTime) mStartTime = mEndTime;
        getView().setStartTimeProgress((int) (mStartTime * 1.0f / mVideo.getDuration() * 100), mStartTime);
    }

    public void setEndTime(int progress) {
        this.mEndTime = (int) (progress * 1.0f / 100 * mVideo.getDuration());
        if (mEndTime >= mVideo.getDuration()) mEndTime = mVideo.getDuration();
        if (mEndTime <= mStartTime) mEndTime = mStartTime;
        getView().setEndTimeProgress((int) (mEndTime * 1.0f / mVideo.getDuration() * 100), mEndTime);
    }
}
