package com.sgif.makegif.screen.gallery;

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
import com.sgif.makegif.domain.model.Media;
import com.sgif.makegif.util.Utils;

import java.io.File;

public class ChooseAdapter extends BaseAdapter<Media, ChooseAdapter.ItemViewHolder> {

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


    class ItemViewHolder extends BaseViewHolder<Media> {
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
        public void bindData(Media photo) {
            GlideApp.with(mContext).load(new File(photo.getPath()))
                    .centerCrop()
                    .override(mSizeImage, mSizeImage)
                    .into(imgChoose);
        }
    }
}
