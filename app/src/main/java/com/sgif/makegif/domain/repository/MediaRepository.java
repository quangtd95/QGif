package com.sgif.makegif.domain.repository;

import android.content.Context;

import com.sgif.makegif.domain.model.FolderMedia;

import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public interface MediaRepository {
    List<FolderMedia> getFolderMedia(Context context);
}
