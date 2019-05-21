package com.quangtd.qgifmaker.screen.mygallery;

import com.quangtd.qgifmaker.common.base.IBaseView

/**
 * Created by quang.td95@gmail.com
 * on 5/11/2018.
 */
interface IMyGalleryActivity : IBaseView {
    fun onLoadMyGallerySuccess()
    fun onLoadMyGalleryFailure(localizedMessage: String)
}