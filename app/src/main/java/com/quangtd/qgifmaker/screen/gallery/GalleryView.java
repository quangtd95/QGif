package com.quangtd.qgifmaker.screen.gallery;

import com.quangtd.qgifmaker.common.base.BaseView;
import com.quangtd.qgifmaker.domain.model.FolderMedia;

import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public interface GalleryView extends BaseView {
    void onShowListImages(List<FolderMedia> folderPhotos);
}
