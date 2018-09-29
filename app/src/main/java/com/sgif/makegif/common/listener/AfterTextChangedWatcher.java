package com.sgif.makegif.common.listener;

import android.text.Editable;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public interface AfterTextChangedWatcher extends android.text.TextWatcher {
    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    default void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    void afterTextChanged(Editable s);
}
