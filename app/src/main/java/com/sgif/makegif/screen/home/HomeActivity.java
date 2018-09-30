package com.sgif.makegif.screen.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import com.sgif.makegif.R;
import com.sgif.makegif.common.base.BaseActivity;
import com.sgif.makegif.domain.model.MediaType;
import com.sgif.makegif.screen.gallery.GalleryActivity;
import com.sgif.makegif.util.DialogUtils;


/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeView {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;

    private Button mBtnChooseImage;

    private Button mBtnChooseVideo;

    private MediaType mMediaType;

    @Override
    @LayoutRes
    protected int getIdLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void bindData() {

    }

    @Override
    protected void initViews() {
        mBtnChooseImage = findViewById(R.id.btnChooseImage);
        mBtnChooseVideo = findViewById(R.id.btnChooseVideo);
    }

    @Override
    protected void initActions() {
        mBtnChooseVideo.setOnClickListener(v -> openGallery(MediaType.VIDEO));

        mBtnChooseImage.setOnClickListener(v -> openGallery(MediaType.PHOTO));
    }

    private void openGallery(MediaType mediaType) {
        this.mMediaType = mediaType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                GalleryActivity.startPhotoActivity(this, mediaType);
            }
        } else {
            GalleryActivity.startPhotoActivity(this, mediaType);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GalleryActivity.startPhotoActivity(this, mMediaType);
                } else {
                    DialogUtils.createAlertDialog(this, "", "need permission to process gif");
                }
                break;
            }
        }
    }
}
