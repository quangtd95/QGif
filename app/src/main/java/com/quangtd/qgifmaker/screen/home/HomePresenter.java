package com.quangtd.qgifmaker.screen.home;

import com.quangtd.qgifmaker.common.Constants;
import com.quangtd.qgifmaker.common.GDef;
import com.quangtd.qgifmaker.common.base.BasePresenter;
import com.quangtd.qgifmaker.domain.model.Photo;
import com.quangtd.qgifmaker.domain.repository.VideoMyGalleryRepository;
import com.quangtd.qgifmaker.domain.repository.VideoMyGalleryRepositoryImpl;

import java.io.File;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public class HomePresenter extends BasePresenter<HomeView> {
    private MyGalleryAdapter myGalleryAdapter;
    private VideoMyGalleryRepository repository;

    @Override
    public void onInit() {
        super.onInit();
        repository = new VideoMyGalleryRepositoryImpl(getContext());
    }

    public void setAdapter(MyGalleryAdapter adapter) {
        myGalleryAdapter = adapter;
    }

    public void loadMyGallery() {
        try {
            File output = new File(Constants.RESULT_FOLDER);
            if (!output.exists()){
                output.mkdirs();
            }
            List<Photo> photos = repository.loadList();
            myGalleryAdapter.setItems(photos.size() <= 4 ? photos : photos.subList(photos.size() - 4, photos.size() - 1));
            getIView().onLoadMyGallerySuccess();
        } catch (Exception e) {
            e.printStackTrace();
            getIView().onLoadMyGalleryFailure(e.getLocalizedMessage());
        }
    }
}
