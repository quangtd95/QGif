package com.quangtd.qgifmaker.screen.new_gallery.adapter;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.quangtd.qgifmaker.R
import com.quangtd.qgifmaker.common.base.BaseAdapter
import com.quangtd.qgifmaker.common.base.BaseViewHolder
import com.quangtd.qgifmaker.domain.model.Photo
import com.quangtd.qgifmaker.util.ScreenUtils
import kotlinx.android.synthetic.main.item_photo.view.*


/**
 * Created by QuangTD
 * on 1/18/2018.
 */
class PhotoAdapter(private val mContext: Context) : BaseAdapter<Photo, PhotoAdapter.PhotoViewHolder>() {

    private var mPhotoDimen: Int = (ScreenUtils.getWidthScreen(mContext) - 2 * ScreenUtils.convertDpToPixel(mContext, 5)) / 2
    var onClickPhotoListener: OnClickPhotoListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false))
    }

    inner class PhotoViewHolder(itemView: View) : BaseViewHolder<Photo>(itemView) {
        private val imvThumbnail: ImageView = itemView.imvThumbnail

        init {
            imvThumbnail.layoutParams.height = mPhotoDimen
            imvThumbnail.layoutParams.width = mPhotoDimen
        }

        override fun bindData(t: Photo) {
            Glide.with(mContext)
                    .load(if (t.uri != null) t.uri else t.path)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .centerCrop()
                            .error(R.drawable.placeholder))
                    .into(imvThumbnail)
            imvThumbnail.setOnClickListener {
                onClickPhotoListener?.onClickPhoto(t)
            }
        }
    }

    interface OnClickPhotoListener {
        fun onClickPhoto(photo: Photo)
    }
}