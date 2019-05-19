package com.quangtd.qgifmaker.screen;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.quangtd.qgifmaker.R;
import com.quangtd.qgifmaker.common.GDef;
import com.quangtd.qgifmaker.util.FontUtils;

public class FontTextView  extends AppCompatTextView {

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
        mFontName = a.getString(R.styleable.FontTextView_font_name);
        if (mFontName == null){
            mFontName = GDef.DEFAULT_FONT;
        }
        Typeface typeface = FontUtils.Companion.getTypeface(mFontName,context);
        setTypeface(typeface);
        a.recycle();
    }

    public void setFont(String fontName){
        mFontName = fontName;
        Typeface typeface = FontUtils.Companion.getTypeface(mFontName,getContext());
        setTypeface(typeface);

    }

    private String mFontName;


}

//class FontTextView(context: Context?, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
//    private var mFontName: String?
//
//    init {
//        val a: TypedArray? = context?.obtainStyledAttributes(attrs, R.styleable.FontTextView)
//        mFontName = a?.getString(R.styleable.FontTextView_font_name)
//        if (mFontName == null) {
//            mFontName = GDef.DEFAULT_FONT
//        }
//        val typeface = FontUtils.getTypeface(mFontName!!, context)
//        setTypeface(typeface)
//        a?.recycle()
//    }
//
//    fun setFont(fontName: String) {
//        mFontName = fontName
//        val typeface = FontUtils.getTypeface(mFontName!!, context)
//        setTypeface(typeface)
//    }
//}
