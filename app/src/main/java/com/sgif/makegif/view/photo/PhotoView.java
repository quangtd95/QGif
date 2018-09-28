package com.sgif.makegif.view.photo;

import com.sgif.makegif.common.base.BaseView;
import com.sgif.makegif.domain.model.FolderImage;

import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public interface PhotoView extends BaseView {
    void onShowListImages(List<FolderImage> folderImages);
}
