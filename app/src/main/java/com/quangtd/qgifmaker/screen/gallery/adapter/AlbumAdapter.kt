package com.quangtd.qgifmaker.screen.gallery.adapter;
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.quangtd.qgifmaker.R
import com.quangtd.qgifmaker.common.base.BaseAdapter
import com.quangtd.qgifmaker.common.base.BaseViewHolder
import com.quangtd.qgifmaker.domain.model.FolderPhoto
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by QuangTD
 * on 1/18/2018.
 */

class AlbumAdapter(private var mContext: Context) : BaseAdapter<FolderPhoto, AlbumAdapter.AlbumPhotoVieWHolder>() {

    var mOnClickAlbumListener: OnClickAlbumListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumPhotoVieWHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_album, parent, false)
        return AlbumPhotoVieWHolder(view)
    }

    inner class AlbumPhotoVieWHolder(itemView: View) : BaseViewHolder<FolderPhoto>(itemView) {
        private val tvNumberOfPhotos: TextView = itemView.findViewById(R.id.tvNumberOfPhotos)
        private val imvThumbnail: CircleImageView = itemView.findViewById(R.id.imvThumbnail)
        private val tvNameFolder: TextView = itemView.findViewById(R.id.tvNameFolder)
        private val rootView: View = itemView.findViewById(R.id.rootView)

        override fun bindData(albumPhoto: FolderPhoto) {
            tvNumberOfPhotos.text = albumPhoto.photos.size.toString()
            tvNameFolder.text = albumPhoto.name
            Glide.with(mContext)
                    .load(albumPhoto.firstImage)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .error(R.drawable.placeholder))
                    .into(imvThumbnail)

            rootView.setOnClickListener {
                mOnClickAlbumListener?.onClickAlbum(albumPhoto)
            }
        }
    }

    interface OnClickAlbumListener {
        fun onClickAlbum(album: FolderPhoto)
    }
}
