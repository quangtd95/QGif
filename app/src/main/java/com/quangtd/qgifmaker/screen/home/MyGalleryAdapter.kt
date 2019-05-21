package com.quangtd.qgifmaker.screen.home

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.quangtd.qgifmaker.R
import com.quangtd.qgifmaker.common.base.BaseAdapter
import com.quangtd.qgifmaker.common.base.BaseViewHolder
import com.quangtd.qgifmaker.domain.model.Photo
import com.quangtd.qgifmaker.util.TimeUtils


/**
 * Created by QuangTD
 * on 3/14/2018.
 */
class MyGalleryAdapter(var context: Context) : BaseAdapter<Photo, MyGalleryAdapter.MyGalleryHolder>() {
    var onClickItemListener: OnClickItemMyGalleryListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGalleryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_gallery, parent, false)
        return MyGalleryHolder(view)
    }

    inner class MyGalleryHolder(itemView: View) : BaseViewHolder<Photo>(itemView) {
        private var rlRoot: RelativeLayout = itemView.findViewById(R.id.rlRoot)
        private var tvNameVideo: TextView = itemView.findViewById(R.id.tvNameVideo)
        private var tvDurationVideo: TextView = itemView.findViewById(R.id.tvDurationVideo)
        private var imgTrash: ImageView = itemView.findViewById(R.id.imgTrash)
        private var imgVideo: ImageView = itemView.findViewById(R.id.imgVideo)

        override fun bindData(t: Photo) {
            tvNameVideo.text = t.name
            tvDurationVideo.text = TimeUtils.parseMilliSecondsTimeToString(t.duration.toLong())
            context.contentResolver
            val options = BitmapFactory.Options()
            options.inSampleSize = 1
            Glide.with(context)
                    .load(t.path)
                    .apply(RequestOptions()
                            .dontAnimate()
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .error(R.drawable.placeholder))
                    .into(imgVideo)
            rlRoot.setOnClickListener {
                onClickItemListener?.onClickMyGallery(adapterPosition, t)
            }
            imgTrash.setOnClickListener {
                onClickItemListener?.onDeleteMyGallery(adapterPosition, t)
            }
        }
    }

    interface OnClickItemMyGalleryListener {
        fun onClickMyGallery(position: Int, video: Photo)
        fun onDeleteMyGallery(position: Int, video: Photo)
    }

}