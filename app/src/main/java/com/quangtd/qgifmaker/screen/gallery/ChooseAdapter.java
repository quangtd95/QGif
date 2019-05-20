package com.quangtd.qgifmaker.screen.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quangtd.qgifmaker.R;
import com.quangtd.qgifmaker.common.GlideApp;
import com.quangtd.qgifmaker.common.base.IAdapter;
import com.quangtd.qgifmaker.common.base.BaseViewHolder;
import com.quangtd.qgifmaker.domain.model.Photo;
import com.quangtd.qgifmaker.util.Utils;

import java.io.File;

public class ChooseAdapter extends IAdapter<Photo, ChooseAdapter.ItemViewHolder> {

    public interface OnClickRemoveItemListener {
        void onRemoveItem(int position);
    }

    private OnClickRemoveItemListener mOnClickRemoveItemListener;
    private int mSizeImage;

    public ChooseAdapter(Context context) {
        super(context);
        mSizeImage = Utils.convertDpToPixel(mContext, 70);
    }

    public void setOnClickRemoveItemListener(OnClickRemoveItemListener mOnClickRemoveItemListener) {
        this.mOnClickRemoveItemListener = mOnClickRemoveItemListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo_choose, parent, false);
        return new ItemViewHolder(view);
    }


    class ItemViewHolder extends BaseViewHolder<Photo> {
        private ImageView imgRemove;
        private ImageView imgChoose;

        ItemViewHolder(View itemView) {
            super(itemView);

            imgRemove = itemView.findViewById(R.id.imgRemove);
            imgChoose = itemView.findViewById(R.id.imgChoose);

            imgChoose.getLayoutParams().width = mSizeImage;
            imgChoose.getLayoutParams().height = mSizeImage;

            imgRemove.setOnClickListener(v -> {
                if (mOnClickRemoveItemListener != null) {
                    mOnClickRemoveItemListener.onRemoveItem(getLayoutPosition());
                }
            });
        }

        @Override
        public void bindData(Photo photo) {
            GlideApp.with(mContext).load(new File(photo.getPath()))
                    .centerCrop()
                    .override(mSizeImage, mSizeImage)
                    .into(imgChoose);
        }
    }
}
