package com.quangtd.qgifmaker.domain.repository;

import android.content.Context;

import com.quangtd.qgifmaker.domain.model.FolderPhoto;

import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public interface PhotoRepository {
    List<FolderPhoto> getFolderMedia(Context context);
}
