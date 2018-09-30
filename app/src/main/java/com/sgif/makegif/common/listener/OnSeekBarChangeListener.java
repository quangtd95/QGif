package com.sgif.makegif.common.listener;

import android.widget.SeekBar;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public interface OnSeekBarChangeListener extends SeekBar.OnSeekBarChangeListener {

    @Override
    default void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    default void onStopTrackingTouch(SeekBar seekBar) {

    }
}
