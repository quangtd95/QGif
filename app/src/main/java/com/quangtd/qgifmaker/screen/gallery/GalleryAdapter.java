package com.quangtd.qgifmaker.screen.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quangtd.qgifmaker.R;
import com.quangtd.qgifmaker.common.GlideApp;
import com.quangtd.qgifmaker.common.base.BaseAdapter;
import com.quangtd.qgifmaker.common.base.BaseViewHolder;
import com.quangtd.qgifmaker.domain.model.Media;
import com.quangtd.qgifmaker.util.Utils;

import java.io.File;


public class GalleryAdapter extends BaseAdapter<Media, GalleryAdapter.ItemViewHolder> {
    public interface OnClickItemPhotoListener {
        void onClickItemMedia(Media photo);
    }

    private final int mSizeImage;
    private OnClickItemPhotoListener mOnClickItemHomeListener;

    public void setOnClickItemPhotoListener(OnClickItemPhotoListener onClickItemHomeListener) {
        this.mOnClickItemHomeListener = onClickItemHomeListener;
    }

    GalleryAdapter(Context context) {
        super(context);
        mSizeImage = (Utils.getScreenWidth(mContext) - 2 * Utils.convertDpToPixel(mContext, 5)) / 3;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return size();
    }

    public class ItemViewHolder extends BaseViewHolder<Media> implements View.OnClickListener {

        private ImageView mImgPhoto;

        ItemViewHolder(View itemView) {
            super(itemView);
            mImgPhoto = itemView.findViewById(R.id.imgPhoto);

            mImgPhoto.getLayoutParams().height = mSizeImage;
            mImgPhoto.getLayoutParams().width = mSizeImage;

            itemView.setOnClickListener(this);
        }

        @Override
        public void bindData(Media photo) {
            String link = photo.getPath();
            if (link != null) {
                GlideApp
                        .with(mContext)
                        .load(new File(link))
                        .centerCrop()
                        .override(mSizeImage, mSizeImage)
                        .into(mImgPhoto);
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnClickItemHomeListener != null) {
                mOnClickItemHomeListener.onClickItemMedia(getItem(getLayoutPosition()));
            }
        }
    }
}
