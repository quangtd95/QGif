package com.quangtd.qgifmaker.common.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public abstract class BaseViewHolder<T extends BaseModel> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData(T t);
}
