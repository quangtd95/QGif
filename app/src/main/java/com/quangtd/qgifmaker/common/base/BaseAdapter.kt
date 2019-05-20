package com.quangtd.qgifmaker.common.base

import android.support.v7.widget.RecyclerView

/**
 * Created by QuangTD
 * on 3/7/2018.
 */
abstract class BaseAdapter<M : BaseModel, VH : BaseViewHolder<M>> : RecyclerView.Adapter<VH>(), IAdapterData<M>, IAdapterView {
    protected var mList = ArrayList<M>()

    override fun refresh() {
        notifyDataSetChanged()
    }

    override fun refreshChanged(position: Int) {
        notifyItemChanged(position)
    }

    override fun refreshInsert(position: Int) {
        notifyItemInserted(position)
    }

    override fun refreshRemove(position: Int) {
        notifyItemRemoved(position)
    }

    override fun refreshAdd() {
        notifyItemInserted(mList.size)
    }

    override fun removeAll() {
        mList.clear()
    }

    override fun setItems(ts: List<M>) {
        mList.clear()
        mList.addAll(ts)
    }

    override fun addItem(t: M) {
        mList.add(t)
    }

    override fun addItems(t: List<M>) {
        super.addItems(t)
        mList.addAll(t)
    }

    override fun removeItem(position: Int) {
        super.removeItem(position)
        mList.removeAt(position)
    }

    override fun getItem(position: Int): M = mList[position]

    override fun getSize(): Int = itemCount

    override fun getItems(): ArrayList<M>? = mList

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindData(getItem(position))
    }

}