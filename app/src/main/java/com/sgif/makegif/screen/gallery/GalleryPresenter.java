package com.sgif.makegif.screen.gallery;

import com.sgif.makegif.common.base.BasePresenter;
import com.sgif.makegif.domain.model.FolderMedia;
import com.sgif.makegif.domain.model.MediaType;
import com.sgif.makegif.domain.repository.MediaRepository;
import com.sgif.makegif.domain.repository.PhotoRepositoryImpl;
import com.sgif.makegif.domain.repository.VideoRepositoryImpl;

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
