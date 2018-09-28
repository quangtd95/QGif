package com.sgif.makegif.view.photo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sgif.makegif.R;
import com.sgif.makegif.common.GlideApp;
import com.sgif.makegif.common.base.BaseAdapter;
import com.sgif.makegif.common.base.BaseViewHolder;
import com.sgif.makegif.domain.model.FolderImage;

import java.io.File;


public class FolderAdapter extends BaseAdapter<FolderImage, FolderAdapter.ItemViewHolder> {
    public interface OnClickItemFolderListener {
        void onClickFolder(FolderImage folderImage);
    }

    private OnClickItemFolderListener mOnClickItemFolderListener;

    public void setOnClickItemFolderListener(OnClickItemFolderListener onClickItemFolderListener) {
        this.mOnClickItemFolderListener = onClickItemFolderListener;
    }

    FolderAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        return new ItemViewHolder(view);
    }


    public class ItemViewHolder extends BaseViewHolder<FolderImage> {
        private TextView tvNameFolder;
        private TextView tvPathFolder;
        private ImageView imgFolder;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvNameFolder = itemView.findViewById(R.id.tvNameFolder);
            tvPathFolder = itemView.findViewById(R.id.tvPathFolder);
            imgFolder = itemView.findViewById(R.id.imgFolder);

            itemView.setOnClickListener(v -> {
                if (mOnClickItemFolderListener != null) {
                    mOnClickItemFolderListener.onClickFolder(getItem(getLayoutPosition()));
                }
            });
        }

        @Override
        public void bindData(FolderImage folderImage) {
            tvNameFolder.setText(folderImage.getName());
            tvPathFolder.setText(folderImage.getPath());
            GlideApp.with(mContext).load(new File(folderImage.getFirstImage())).centerCrop().override(150, 150).into(imgFolder);
        }
    }
}
