package com.quangtd.qgifmaker.common.base;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public interface IAdapterView {
    void refresh();

    void refreshInsert(int position);

    void refreshRemove(int position);

    void refreshChanged(int position);

    void refreshAdd();

}
