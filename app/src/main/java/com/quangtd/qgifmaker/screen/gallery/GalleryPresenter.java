package com.quangtd.qgifmaker.screen.gallery;

import com.quangtd.qgifmaker.common.base.BasePresenter;
import com.quangtd.qgifmaker.domain.model.FolderMedia;
import com.quangtd.qgifmaker.domain.model.MediaType;
import com.quangtd.qgifmaker.domain.repository.MediaRepository;
import com.quangtd.qgifmaker.domain.repository.PhotoRepositoryImpl;
import com.quangtd.qgifmaker.domain.repository.VideoRepositoryImpl;

import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public class GalleryPresenter extends BasePresenter<GalleryView> {
    private MediaRepository mediaRepository;

    @Override
    public void onInit() {
        super.onInit();
    }

    public void loadMedia(MediaType mediaType) {
        if (mediaRepository == null) {
            switch (mediaType) {
                case PHOTO:
                    mediaRepository = new PhotoRepositoryImpl();
                    break;
                case VIDEO:
                    mediaRepository = new VideoRepositoryImpl();
                    break;
            }
        }
        List<FolderMedia> folderPhotos = mediaRepository.getFolderMedia(getContext());
        if (folderPhotos != null) {
            getView().onShowListImages(folderPhotos);
        }
    }
}
