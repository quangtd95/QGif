package com.quangtd.qgifmaker.screen.mygallery;

import com.quangtd.qgifmaker.common.Constants
import com.quangtd.qgifmaker.common.base.BasePresenter
import com.quangtd.qgifmaker.common.base.IAdapterData
import com.quangtd.qgifmaker.domain.model.Photo
import com.quangtd.qgifmaker.domain.repository.VideoMyGalleryRepository
import com.quangtd.qgifmaker.domain.repository.VideoMyGalleryRepositoryImpl
import java.io.File


/**
 * Created by quang.td95@gmail.com
 * on 5/11/2018.
 */
class MyGalleryPresenter : BasePresenter<IMyGalleryActivity>() {
    private var mView: IMyGalleryActivity? = null
    private var mVideoMyGalleryRepo: VideoMyGalleryRepository? = null
    var adapter: IAdapterData<Photo>? = null
    override fun onInit() {
        mView = iView
        mVideoMyGalleryRepo = VideoMyGalleryRepositoryImpl(context)
    }

    fun loadMyGallery() {
        try {
            val output = File(Constants.RESULT_FOLDER)
            if (!output.exists()) {
                output.mkdirs()
            }
            val photos = mVideoMyGalleryRepo?.loadList()
            adapter?.setItems(photos!!)
            iView.onLoadMyGallerySuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            iView.onLoadMyGalleryFailure(e.localizedMessage)
        }

    }
}