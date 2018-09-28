package com.sgif.makegif.common.base;

import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public interface BaseAdapterData<M extends BaseModel> {

    void addItem(M m);

    void addItems(List<M> ms);

    void setItems(List<M> ms);

    void clearData();

    void removeItem(int position);

    M getItem(int position);

    int size();

    List<M> getItems();


}
