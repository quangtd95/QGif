package com.quangtd.qgifmaker.screen.custom

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

/**
 * Created by QuangTD
 * on 1/15/2018.
 */
class SquareHeightImageView(context: Context?, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(heightMeasureSpec,heightMeasureSpec)
    }
}