package com.quangtd.qgifmaker.util

import android.content.Context
import android.graphics.Typeface

/**
 * Created by QuangTD
 * on 1/14/2018.
 */
class FontUtils {
    companion object {
        private var fontCache: HashMap<String, Typeface> = HashMap()

        fun getTypeface(fontName: String, context: Context?): Typeface? {
            var typeface = fontCache[fontName]
            try {
                if (typeface == null) {
                    typeface = Typeface.createFromAsset(context?.assets, "font/$fontName")
                    fontCache[fontName] = typeface
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                typeface = Typeface.DEFAULT
            }
            return typeface
        }
    }
}