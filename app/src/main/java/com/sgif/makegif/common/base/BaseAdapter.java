package com.sgif.makegif.common.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public abstract class BaseAdapter<M extends BaseModel, VH extends BaseViewHolder<M>> extends RecyclerView.Adapter<VH> implements BaseAdapterData<M>, BaseAdapterView {

    private List<M> data;
    protected Context mContext;

    public BaseAdapter(Context context) {
        this.data = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        vh.bindData(data.get(i));
    }

    @Override
    public int getItemCount() {
        return size();
    }

    @Override
    public void addItem(M m) {
        if (data != null && m != null) {
            data.add(m);
        }
    }

    @Override
    public void addItems(List<M> ms) {
        if (data != null && ms != null) {
            data.addAll(ms);
        }
    }

    @Override
    public void setItems(List<M> ms) {
        if (ms != null) {
            data.clear();
            data.addAll(ms);
        }
    }

    @Override
    public void clearData() {
        data.clear();
    }

    @Override
    public void removeItem(int position) {
        if (data != null && position >= 0 && position < data.size()) {
            data.remove(position);
        }
    }

    @Override
    public M getItem(int position) {
        if (data != null && position >= 0 && position < data.size()) {
            return data.get(position);
        }
        return null;
    }

    @Override
    public int size() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public List<M> getItems() {
        return data;
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public void refreshInsert(int position) {
        notifyItemInserted(position);
    }

    @Override
    public void refreshRemove(int position) {
        notifyItemRemoved(position);
    }

    @Override
    public void refreshChanged(int position) {
        notifyItemChanged(position);
    }

    @Override
    public void refreshAdd() {
        notifyItemInserted(data.size());
    }
}
