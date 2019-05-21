package com.quangtd.qgifmaker.screen.gallery;

import com.quangtd.qgifmaker.R
import com.quangtd.qgifmaker.common.base.BasePresenter
import com.quangtd.qgifmaker.common.base.IAdapterData
import com.quangtd.qgifmaker.domain.model.FolderPhoto
import com.quangtd.qgifmaker.domain.model.MediaType
import com.quangtd.qgifmaker.domain.model.Photo
import com.quangtd.qgifmaker.domain.repository.PhotoRepository
import com.quangtd.qgifmaker.domain.repository.PhotoRepositoryImpl
import com.quangtd.qgifmaker.domain.repository.VideoRepositoryImpl
import com.quangtd.qgifmaker.screen.export.ExportGifPhotoActivity
import com.quangtd.qgifmaker.screen.export.ExportGifVideoActivity

/**
 * Created by QuangTD
 * on 1/17/2018.
 */
class PhotoGalleryPresenter : BasePresenter<IPhotoGalleryActivity>() {

    private lateinit var mPhotoRepo: PhotoRepository
    private var mAlbumAdapterData: IAdapterData<FolderPhoto>? = null
    private var mPhotoAdapterData: IAdapterData<Photo>? = null
    private var mPhotoChosenAdapterData: IAdapterData<Photo>? = null
    private var mMediaType: MediaType? = null

    override fun onInit() {
    }

    fun setAlbumAdapter(albumAdapterData: IAdapterData<FolderPhoto>) {
        this.mAlbumAdapterData = albumAdapterData
    }

    fun setPhotoAdapter(photoAdapter: IAdapterData<Photo>) {
        this.mPhotoAdapterData = photoAdapter
    }

    fun setPhotoChosenAdapter(photoChosenAdapter: IAdapterData<Photo>) {
        this.mPhotoChosenAdapterData = photoChosenAdapter
    }

    fun loadAlbums(mediaType: MediaType?) {
        mMediaType = mediaType
        if (mediaType == MediaType.PHOTO) {
            mPhotoRepo = PhotoRepositoryImpl()
        } else if (mediaType == MediaType.VIDEO) {
            mPhotoRepo = VideoRepositoryImpl()
        }
        iView?.showLoading()
        val photoList = mPhotoRepo.getFolderMedia(context)
        if (photoList != null) {
            mAlbumAdapterData?.setItems(photoList)
            iView?.onLoadAlbumSuccess()
        } else {
            iView?.onLoadAlbumFailure("error")
        }
        iView?.hideLoading()
    }

    fun openAlbum(album: FolderPhoto) {
        mPhotoAdapterData?.setItems(album.photos)
        iView?.showPhotos()
    }

    fun choosePhoto(photo: Photo) {
        if (mMediaType == MediaType.VIDEO) {
            if (mPhotoChosenAdapterData?.getSize() == 1) {
                return
            }
        }
        mPhotoChosenAdapterData?.addItem(photo)
        iView?.refreshChoosePhoto(mPhotoChosenAdapterData?.getSize()!!.minus(1))
    }

    fun removePhoto(position: Int) {
        mPhotoChosenAdapterData?.removeItem(position)
        iView?.refreshRemovePhoto(position)
    }

    fun moveToExportScreen(mediaType: MediaType?) {
        mPhotoChosenAdapterData?.let {
            if (it.getSize() > 0) {
                if (mediaType == MediaType.PHOTO) {
                    ExportGifPhotoActivity.startActivity(context, it.getItems())
                } else {
                    ExportGifVideoActivity.startActivity(context, it.getItem(0))
                }
            } else {
                iView?.showDialogError(context?.getString(R.string.gallery_select_more_photo)!!)
            }
        }
        iView.moveToExportScreen(null)
    }
}
