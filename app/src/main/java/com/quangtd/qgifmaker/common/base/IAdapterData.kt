package com.quangtd.qgifmaker.common.base

/**
 * Created by QuangTD
 * on 1/18/2018.
 */
interface IAdapterData<T : BaseModel> {
    fun addItem(t: T) {}

    fun addItems(t: List<T>) {}

    fun setItems(ts: List<T>)

    fun removeItem(position: Int) {}

    fun removeAll() {}

    fun getItem(position: Int): T

    fun getSize(): Int

    fun getItems(): ArrayList<T>?
}