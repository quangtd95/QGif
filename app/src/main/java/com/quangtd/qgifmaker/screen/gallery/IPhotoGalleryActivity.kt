package com.quangtd.qgifmaker.screen.gallery;

import android.content.Intent
import com.quangtd.qgifmaker.common.base.IBaseView

/**
 * Created by QuangTD
 * on 1/17/2018.
 */
interface IPhotoGalleryActivity : IBaseView {
    fun onLoadAlbumSuccess()
    fun onLoadAlbumFailure(message: String)
    fun showPhotos()
    fun refreshChoosePhoto(position: Int)
    fun refreshRemovePhoto(position: Int)
    fun moveToExportScreen(intent: Intent?)
    fun showDialogError(message: String)
}