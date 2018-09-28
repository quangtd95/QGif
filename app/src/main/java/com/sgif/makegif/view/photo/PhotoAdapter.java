package com.sgif.makegif.view.photo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sgif.makegif.R;
import com.sgif.makegif.common.GlideApp;
import com.sgif.makegif.common.base.BaseAdapter;
import com.sgif.makegif.common.base.BaseViewHolder;
import com.sgif.makegif.domain.model.Photo;
import com.sgif.makegif.util.Utils;

import java.io.File;


public class PhotoAdapter extends BaseAdapter<Photo, PhotoAdapter.ItemViewHolder> {
    public interface OnClickItemPhotoListener {
        void onClickItemPhoto(Photo photo);
    }

    private final int mSizeImage;
    private OnClickItemPhotoListener mOnClickItemHomeListener;

    public void setOnClickItemPhotoListener(OnClickItemPhotoListener onClickItemHomeListener) {
        this.mOnClickItemHomeListener = onClickItemHomeListener;
    }

    PhotoAdapter(Context context) {
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

    public class ItemViewHolder extends BaseViewHolder<Photo> implements View.OnClickListener {

        private ImageView mImgPhoto;

        ItemViewHolder(View itemView) {
            super(itemView);
            mImgPhoto = itemView.findViewById(R.id.imgPhoto);

            mImgPhoto.getLayoutParams().height = mSizeImage;
            mImgPhoto.getLayoutParams().width = mSizeImage;

            itemView.setOnClickListener(this);
        }

        @Override
        public void bindData(Photo photo) {
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
                mOnClickItemHomeListener.onClickItemPhoto(getItem(getLayoutPosition()));
            }
        }
    }
}