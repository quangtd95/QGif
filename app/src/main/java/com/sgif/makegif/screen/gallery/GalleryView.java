package com.sgif.makegif.screen.gallery;

import com.sgif.makegif.common.base.BaseView;
import com.sgif.makegif.domain.model.FolderMedia;

import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public interface GalleryView extends BaseView {
    void onShowListImages(List<FolderMedia> folderPhotos);
}
