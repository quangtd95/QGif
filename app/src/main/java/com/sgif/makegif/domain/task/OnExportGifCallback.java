package com.sgif.makegif.domain.task;

public interface OnExportGifCallback {
        void onProgressExportGif(Float progress);

        void onCompleteExportGif(String s);

        void onCancelledExportGif(String s);

        void onPrepareExportGif();
    }