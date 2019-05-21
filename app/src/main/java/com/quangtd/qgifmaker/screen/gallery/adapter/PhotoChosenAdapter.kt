package com.quangtd.qgifmaker.screen.gallery.adapter;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.quangtd.qgifmaker.R
import com.quangtd.qgifmaker.common.base.BaseAdapter
import com.quangtd.qgifmaker.common.base.BaseViewHolder
import com.quangtd.qgifmaker.common.listener.ItemTouchHelperAdapter
import com.quangtd.qgifmaker.domain.model.Photo
import kotlinx.android.synthetic.main.item_photo_chosen.view.*
import java.util.*


/**
 * Created by QuangTD
 * on 1/19/2018.
 */
class PhotoChosenAdapter(val mContext: Context) : BaseAdapter<Photo, PhotoChosenAdapter.PhotoChosenHolder>(), ItemTouchHelperAdapter {
    var onClickRemoveItemListener: OnClickRemovePhotoListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoChosenHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.item_photo_chosen, parent, false)
        return PhotoChosenHolder(view)
    }


    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(mList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(mList, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        removeItem(position)
        notifyItemRemoved(position)
    }


    inner class PhotoChosenHolder(itemView: View?) : BaseViewHolder<Photo>(itemView) {

        override fun bindData(t: Photo) {
            Glide.with(mContext)
                    .load(t.uri)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .centerCrop()
                            .error(R.drawable.placeholder))
                    .into(itemView.imvPhotoChosen)
            itemView.imvRemove.setOnClickListener {
                onClickRemoveItemListener?.onClickRemovePhoto(adapterPosition)
            }
        }
    }

    interface OnClickRemovePhotoListener {
        fun onClickRemovePhoto(position: Int)
    }
}