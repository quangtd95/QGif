package com.sgif.makegif.screen.export;

import com.sgif.makegif.common.base.BaseView;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public interface ExportGifVideoView extends BaseView {

    void setDelay(float duration);

    void setWidthGif(String s);

    void setHeightGif(String s);

    void onProgress(Float progress);

    void onPrepareExport();

    void onCompleteExport(String resultPath);

    void onCancelledExport(String error);

    void setStartTimeProgress(int progress, int position);

    void setEndTimeProgress(int progress, int position);
}
