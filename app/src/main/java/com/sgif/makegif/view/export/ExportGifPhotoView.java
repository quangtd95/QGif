package com.sgif.makegif.view.export;

import com.sgif.makegif.common.base.BaseView;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public interface ExportGifPhotoView extends BaseView {

    void setDuration(float duration);

    void setWidthGif(String s);

    void setHeightGif(String s);

    void onProgress(Float progress);

    void onPrepareExport();

    void onCompleteExport();
}
