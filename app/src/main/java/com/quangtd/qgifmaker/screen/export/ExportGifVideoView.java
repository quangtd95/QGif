package com.quangtd.qgifmaker.screen.export;

import com.quangtd.qgifmaker.common.base.IBaseView;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public interface ExportGifVideoView extends IBaseView {

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
