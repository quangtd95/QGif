package com.quangtd.qgifmaker.common.base;

import android.content.Context;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public interface IBaseView {

    void showNotifyDialog(String message);

    Context getContext();

    void showLoading();

    void hideLoading();
}
