package com.quangtd.qgifmaker.screen.mygallery;

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.quangtd.qgifmaker.R
import com.quangtd.qgifmaker.common.GDef
import com.quangtd.qgifmaker.common.base.BaseActivity
import com.quangtd.qgifmaker.domain.model.Photo
import com.quangtd.qgifmaker.screen.complete.CompleteActivity
import com.quangtd.qgifmaker.util.SnackBarUtils
import kotlinx.android.synthetic.main.activity_home.rvMyGallery
import kotlinx.android.synthetic.main.activity_my_gallery.*
import java.io.File


class MyGalleryActivity : BaseActivity<MyGalleryPresenter>(), IMyGalleryActivity, View.OnClickListener, MyGalleryAdapter.OnClickItemMyGalleryListener {
    override fun getIdLayout(): Int {
        return R.layout.activity_my_gallery
    }

    override fun bindData() {
    }

    override fun initViews() {
        rvMyGallery.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvMyGallery.isNestedScrollingEnabled = false
        rvMyGallery.adapter = MyGalleryAdapter(this).apply {
            onClickItemListener = this@MyGalleryActivity
        }
        imvBack.setOnClickListener {
            super.finish()
        }
        myGalleryAdapter = rvMyGallery.adapter as MyGalleryAdapter
        getPresenter(this).adapter = rvMyGallery.adapter as MyGalleryAdapter
    }

    override fun initActions() {
    }

    private var myGalleryAdapter: MyGalleryAdapter? = null
    private val mVideos = ArrayList<Photo>()

    override fun onStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    GDef.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        } else {
            getPresenter(this).loadMyGallery()
        }
        super.onStart()
    }

    override fun onClick(v: View) {
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GDef.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPresenter(this).loadMyGallery()
                } else {
                    finish()
                }
            }
        }
    }

    private fun reloadMedia(path: String, isDelete: Boolean) {
        MediaScannerConnection.scanFile(this,
                arrayOf(path), null
        ) { path1, uri ->
            if (isDelete) {
                if (uri != null) {
                    contentResolver.delete(uri, null, null)
                }
            }
        }
    }

    override fun onLoadMyGallerySuccess() {
        myGalleryAdapter?.refresh()
    }

    override fun onLoadMyGalleryFailure(message: String) {
        SnackBarUtils.showErrorMessage(message, rvMyGallery)
    }

    override fun onClickMyGallery(position: Int, video: Photo) {
        CompleteActivity.startCompleteActivity(this, video.path)
    }

    override fun onDeleteMyGallery(position: Int, video: Photo) {
        val path = mVideos[position].path
        val file = File(path)
        val deleted = file.delete()
        mVideos.removeAt(position)
        myGalleryAdapter!!.notifyItemRemoved(position)
        Toast.makeText(this, "Xoa video " + if (deleted) "thành công" else "thất bại", Toast.LENGTH_SHORT).show()
        reloadMedia(path, deleted)
    }

    companion object{
       public  fun startActivity(context: Context) {
            val intent = Intent(context, MyGalleryActivity::class.java)
            context.startActivity(intent)
        }
    }


}
