package com.sgif.makegif.view.photo;

import com.sgif.makegif.common.base.BasePresenter;
import com.sgif.makegif.domain.model.FolderImage;
import com.sgif.makegif.domain.repository.ImageRepository;
import com.sgif.makegif.domain.repository.ImageRepositoryImpl;

import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public class PhotoPresenter extends BasePresenter<PhotoView> {
    private ImageRepository imageRepository;

    @Override
    public void onInit() {
        super.onInit();
        imageRepository = new ImageRepositoryImpl();

    }

    public void loadPhoto() {
        List<FolderImage> folderImages = imageRepository.getFolderImages(getContext());
        if (folderImages != null) {
            getView().onShowListImages(folderImages);
        }
    }

}
