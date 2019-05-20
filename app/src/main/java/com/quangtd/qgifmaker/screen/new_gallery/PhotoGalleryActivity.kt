package com.quangtd.qgifmaker.screen.new_gallery;

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.quangtd.qgifmaker.R
import com.quangtd.qgifmaker.common.Constants
import com.quangtd.qgifmaker.common.GDef
import com.quangtd.qgifmaker.common.base.BaseActivity
import com.quangtd.qgifmaker.common.base.IAdapterView
import com.quangtd.qgifmaker.common.listener.SimpleItemTouchHelperCallback
import com.quangtd.qgifmaker.domain.model.FolderPhoto
import com.quangtd.qgifmaker.domain.model.MediaType
import com.quangtd.qgifmaker.domain.model.Photo
import com.quangtd.qgifmaker.screen.new_gallery.adapter.AlbumAdapter
import com.quangtd.qgifmaker.screen.new_gallery.adapter.PhotoAdapter
import com.quangtd.qgifmaker.screen.new_gallery.adapter.PhotoChosenAdapter
import com.quangtd.qgifmaker.util.AnimationUtils
import com.quangtd.qgifmaker.util.RecyclerViewUtils
import com.quangtd.qgifmaker.util.SnackBarUtils
import kotlinx.android.synthetic.main.activity_photo_gallery.*
import java.util.*


/**
 * Created by QuangTD
 * on 1/16/2018.
 */

class PhotoGalleryActivity : BaseActivity<PhotoGalleryPresenter>(), IPhotoGalleryActivity, AlbumAdapter.OnClickAlbumListener, PhotoAdapter.OnClickPhotoListener, PhotoChosenAdapter.OnClickRemovePhotoListener, View.OnClickListener {
    override fun getIdLayout(): Int {
        return R.layout.activity_photo_gallery
    }

    private var mAlbumAdapter: IAdapterView? = null
    private var mPhotoAdapter: IAdapterView? = null
    private var mPhotoChosenAdapter: IAdapterView? = null
    private var mIsShowPhoto = false
    private var mNumPhotoChosen = 0
    private var mMediaType: MediaType? = null
    private var mRvUtils = RecyclerViewUtils.getInstance()

    override fun bindData() {

        val bundle = intent.extras
        if (bundle != null) {
            mMediaType = MediaType.getByCode(bundle.getInt(Constants.BUNDLE_KEY_MEDIA_TYPE, Constants.TYPE_PHOTO))
            when (mMediaType) {
                MediaType.PHOTO -> {
////                    mTvNameFolder.setText(R.string.choose_photo)
//                    val mPhotoChosenList = bundle.getParcelableArrayList<Photo>(Constants.BUNDLE_KEY_LIST_PHOTO)
//                    if (mPhotoChosenList != null) {
//                        mAdapterPhotoChoose.setItems(mPhotoChosenList)
//                        mAdapterPhotoChoose.refresh()
//                        mTvPlease.setVisibility(View.GONE)
//                        mRlContainerPlease.setVisibility(View.VISIBLE)
//                        mTvNumber.setText(String.format(getString(R.string.text_number), mAdapterPhotoChoose.size()))
                }
//                }
                MediaType.VIDEO -> {
//                    mTvNameFolder.setText(R.string.choose_video)
//                    mTvPlease.setVisibility(View.GONE)
//                    mRlContainerPlease.setVisibility(View.GONE)
                    imvToggleTrayOpen.visibility = View.GONE
                    trayPhotoChosen.visibility = View.GONE
                }
//            getPresenter(this).loadMedia(mMediaType)
            }

            mNumPhotoChosen = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        GDef.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
            } else {
                getPresenter(this).loadAlbums(mMediaType)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GDef.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPresenter(this).loadAlbums(mMediaType)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition()
                    } else {
                        finish()
                    }
                }
            }
        }
    }

    override fun initActions() {
        imvBack.setOnClickListener(this)
        imvNext.setOnClickListener(this)

        imvToggleTrayOpen.setOnClickListener {
            trayPhotoChosen.expand()
        }
        imvToggleTrayClose.setOnClickListener {
            trayPhotoChosen.collapse()
        }

    }


    override fun initViews() {
        val albumAdapter = AlbumAdapter(this)
        val photoAdapter = PhotoAdapter(this)
        val photoChosenAdapter = PhotoChosenAdapter(this)

        albumAdapter.mOnClickAlbumListener = this
        photoAdapter.onClickPhotoListener = this
        photoChosenAdapter.onClickRemoveItemListener = this

        mRvUtils.setUpGridVertical(this, rvAlbum, 3)
        mRvUtils.setUpGridVertical(this, rvPhoto, 2)
        mRvUtils.setUpHorizontal(this, rvPhotoChosen)
        val callback = SimpleItemTouchHelperCallback(photoChosenAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(rvPhotoChosen)

        mAlbumAdapter = albumAdapter
        mPhotoAdapter = photoAdapter
        mPhotoChosenAdapter = photoChosenAdapter

        rvAlbum.adapter = albumAdapter
        rvPhoto.adapter = photoAdapter
        rvPhotoChosen.adapter = photoChosenAdapter

        getPresenter(this).setAlbumAdapter(albumAdapter)
        getPresenter(this).setPhotoAdapter(photoAdapter)
        getPresenter(this).setPhotoChosenAdapter(photoChosenAdapter)

        rvPhoto.visibility = View.GONE
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imvBack ->
                when (mIsShowPhoto) {
                    true -> {
                        if (trayPhotoChosen.isExpanded) trayPhotoChosen.collapse(true)
                        hidePhotos()
                    }
                    false -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition()
                        } else {
                            finish()
                        }
                    }
                }
            R.id.imvNext ->
                getPresenter(this).moveToExportScreen(mMediaType)
        }
    }

    override fun showPhotos() {
        mIsShowPhoto = true
        AnimationUtils.showViewUp(rvPhoto)
        rvAlbum.visibility = View.GONE
        mPhotoAdapter?.refresh()
    }

    private fun hidePhotos() {
        mIsShowPhoto = false
        rvAlbum.visibility = View.VISIBLE
        rvPhoto.visibility = View.GONE
    }

    override fun refreshChoosePhoto(position: Int) {
        if (trayPhotoChosen.visibility != View.GONE) {
            if (!trayPhotoChosen.isExpanded) trayPhotoChosen.expand(true)
            rvPhotoChosen.scrollToPosition(position)
        }
        mNumPhotoChosen++
        mPhotoChosenAdapter?.refreshAdd()
        tvNumberOfPhotoChosen.text = String.format("(%d)", mNumPhotoChosen)
    }

    override fun refreshRemovePhoto(position: Int) {
        mNumPhotoChosen--
        mPhotoChosenAdapter?.refreshRemove(position)
        tvNumberOfPhotoChosen.text = String.format("(%d)", mNumPhotoChosen)
    }


    override fun onClickAlbum(album: FolderPhoto) {
        getPresenter(this).openAlbum(album)
    }

    override fun onClickPhoto(photo: Photo) {
        getPresenter(this).choosePhoto(photo)
        if (mMediaType == MediaType.VIDEO) {
            getPresenter(this).moveToExportScreen(mMediaType)
        }

    }

    override fun onClickRemovePhoto(position: Int) {
        getPresenter(this).removePhoto(position)
    }


    override fun onLoadAlbumFailure(message: String) {
        SnackBarUtils.showErrorMessage(message, rootView)
    }

    override fun onLoadAlbumSuccess() {
        mAlbumAdapter?.refresh()
    }

    override fun moveToExportScreen(intent: Intent?) {
        intent?.let {
            startActivity(intent)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
        }
    }

    override fun showDialogError(message: String) {
        SnackBarUtils.showErrorMessage(message, rootView)
    }

    override fun onBackPressed() {
        onClick(imvBack)
    }

    companion object {
        fun startPhotoActivity(context: Context, photos: List<Photo>?) {
            val intent = Intent(context, PhotoGalleryActivity::class.java)
            if (photos != null) {
                val bundle = Bundle()
                bundle.putParcelableArrayList(Constants.BUNDLE_KEY_LIST_PHOTO, photos as ArrayList<out Parcelable>?)
                bundle.putInt(Constants.BUNDLE_KEY_MEDIA_TYPE, Constants.TYPE_PHOTO)
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }

        fun startPhotoActivity(context: Context, mediaType: MediaType) {
            val intent = Intent(context, PhotoGalleryActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(Constants.BUNDLE_KEY_MEDIA_TYPE, mediaType.code)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }


}
