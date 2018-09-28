package com.sgif.makegif.view.home;

import android.support.annotation.LayoutRes;
import android.widget.Button;

import com.sgif.makegif.R;
import com.sgif.makegif.common.base.BaseActivity;
import com.sgif.makegif.view.photo.PhotoActivity;


/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeView {
    private Button mBtnChooseImage;

    private Button mBtnChooseVideo;

    @Override
    @LayoutRes
    protected int getIdLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initViews() {
        mBtnChooseImage = findViewById(R.id.btnChooseImage);
        mBtnChooseVideo = findViewById(R.id.btnChooseVideo);
    }

    @Override
    protected void initActions() {
        mBtnChooseVideo.setOnClickListener(v -> {

        });

        mBtnChooseImage.setOnClickListener(v -> {
            PhotoActivity.startPhotoActivity(this);
        });
    }
}
