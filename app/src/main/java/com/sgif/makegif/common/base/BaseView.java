package com.sgif.makegif.common.base;

import android.content.Context;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public interface BaseView {

    void showNotifyDialog(String message);

    Context getContext();

    void showLoading();

    void hideLoading();
}
