package com.quangtd.qgifmaker.common.base;

import android.content.Context;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public abstract class BasePresenter<I extends BaseView> {

    private I view;

    public BasePresenter() {
    }

    public I getView() {
        return this.view;
    }

    public void setIFace(I iFace) {
        this.view = iFace;
    }

    protected Context getContext() {
        return this.view != null ? this.view.getContext() : null;
    }

    protected String getTag() {
        return this.getClass().getName();
    }

    public void onInit() {
    }
}
