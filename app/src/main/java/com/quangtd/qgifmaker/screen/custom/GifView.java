package com.quangtd.qgifmaker.screen.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.quangtd.qgifmaker.screen.gallery.adapter.ChooseAdapter;

import java.util.Random;

public class GifView extends View {
    private Paint mPaint;
    private Random mRandom;
    private ChooseAdapter mChooseAdapter;
    private PreviewGifPresenter mPresenter;

    public GifView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mRandom = new Random();
        mPresenter = new PreviewGifPresenter(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPresenter.drawPreview(canvas);
    }

    public void setAdapter(ChooseAdapter adapter) {
        mPresenter.setAdapter(adapter);
    }

    public void refreshRemove(int position){
        mPresenter.remove(position);
    }


    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public void startPreview() {
        mPresenter.startPreview();
    }

    public void setDuration(float duration) {
        mPresenter.setDuration(duration);
    }
}
