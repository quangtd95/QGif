package com.sgif.makegif.view.photo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sgif.makegif.R;
import com.sgif.makegif.common.Constants;
import com.sgif.makegif.common.base.BaseActivity;
import com.sgif.makegif.domain.model.FolderImage;
import com.sgif.makegif.domain.model.Photo;
import com.sgif.makegif.util.DialogUtils;
import com.sgif.makegif.util.GridSpacingItemDecoration;
import com.sgif.makegif.util.RecyclerViewUtils;
import com.sgif.makegif.view.export.ExportGifPhotoActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public class PhotoActivity extends BaseActivity<PhotoPresenter> implements PhotoView, FolderAdapter.OnClickItemFolderListener, PhotoAdapter.OnClickItemPhotoListener, PhotoChooseAdapter.OnClickRemoveItemListener, View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;

    private ImageView mImgBack;
    private TextView mTvNameFolder;
    private TextView mTvPlease;
    private TextView mTvNumber;
    private RelativeLayout mRlContainerPlease;
    private LinearLayout mLlNumber;
    private RecyclerView mRecyclerFolder;
    private RecyclerView mRecyclerPhoto;
    private LinearLayoutManager mLayoutManagerPhotoChoose;
    private FolderAdapter mAdapterFolder;
    private PhotoAdapter mAdapterPhoto;
    private PhotoChooseAdapter mAdapterPhotoChoose;
    private List<Photo> mPhotoChosenList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                getPresenter(this).loadPhoto();
            }
        } else {
            getPresenter(this).loadPhoto();
        }
    }


    @Override
    protected int getIdLayout() {
        return R.layout.activity_photo;
    }

    @Override
    protected void initValues() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPhotoChosenList = bundle.getParcelableArrayList(Constants.BUNDLE_KEY_LIST_PHOTO);
        }
    }

    @Override
    protected void initViews() {
        mImgBack = findViewById(R.id.imgBack);
        mTvNameFolder = findViewById(R.id.tvNameFolder);
        mTvPlease = findViewById(R.id.tvPlease);
        mTvNumber = findViewById(R.id.tvNumber);
        mRlContainerPlease = findViewById(R.id.rlContainerPlease);
        mLlNumber = findViewById(R.id.llNumber);

        mRecyclerFolder = findViewById(R.id.recyclerFolder);
        LinearLayoutManager mLayoutManagerFolder = new LinearLayoutManager(this);
        mAdapterFolder = new FolderAdapter(this);
        mRecyclerFolder.setLayoutManager(mLayoutManagerFolder);
        mRecyclerFolder.setAdapter(mAdapterFolder);

        mRecyclerPhoto = findViewById(R.id.recyclerPhoto);
        GridLayoutManager mLayoutManagerPhoto = new GridLayoutManager(this, 3);
        mAdapterPhoto = new PhotoAdapter(this);
        mRecyclerPhoto.setLayoutManager(mLayoutManagerPhoto);
        mRecyclerPhoto.setAdapter(mAdapterPhoto);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(3, 10, false);
        mRecyclerPhoto.addItemDecoration(itemDecoration);


        RecyclerView mRecyclerPhotoChoose = findViewById(R.id.recyclerPhotoChoose);
        RecyclerViewUtils.Create().setUpHorizontal(this, mRecyclerPhotoChoose);
        mLayoutManagerPhotoChoose = (LinearLayoutManager) mRecyclerPhotoChoose.getLayoutManager();
        mAdapterPhotoChoose = new PhotoChooseAdapter(this);

        mRecyclerFolder.setVisibility(View.VISIBLE);
        mRecyclerPhoto.setVisibility(View.GONE);
        mRlContainerPlease.setVisibility(View.GONE);
        mTvPlease.setVisibility(View.VISIBLE);

        mRecyclerPhotoChoose.setAdapter(mAdapterPhotoChoose);
        if (mPhotoChosenList != null) {
            mAdapterPhotoChoose.setItems(mPhotoChosenList);
            mAdapterPhotoChoose.refresh();
            mTvPlease.setVisibility(View.GONE);
            mRlContainerPlease.setVisibility(View.VISIBLE);
            mTvNumber.setText(String.format(getString(R.string.text_number), mAdapterPhotoChoose.size()));
        }


    }

    @Override
    protected void initActions() {
        mAdapterFolder.setOnClickItemFolderListener(this);
        mAdapterPhoto.setOnClickItemPhotoListener(this);
        mAdapterPhotoChoose.setOnClickRemoveItemListener(this);
        mImgBack.setOnClickListener(this);
        mLlNumber.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                if (mRecyclerFolder.getVisibility() == View.VISIBLE) {
                    finish();
                } else {
                    mRecyclerFolder.setVisibility(View.VISIBLE);
                    mRecyclerPhoto.setVisibility(View.GONE);
                    mTvNameFolder.setText(getString(R.string.choose_photo));
                }
                break;
            case R.id.llNumber:
                if (mAdapterPhotoChoose.size() < Constants.MIN_PHOTO) {
                    DialogUtils.createAlertDialog(
                            this,
                            "error",
                            "please choose at least 2 photos to continue");
                } else {
                    ExportGifPhotoActivity.startActivity(this, (ArrayList<Photo>) mAdapterPhotoChoose.getItems());
                    finish();
                }

                break;
        }
    }

    @Override
    public void onClickFolder(FolderImage folderImage) {
        mRecyclerPhoto.setVisibility(View.VISIBLE);
        mRecyclerFolder.setVisibility(View.GONE);
        mTvNameFolder.setText(folderImage.getName());
        mAdapterPhoto.setItems(folderImage.getPhotos());
        mAdapterPhoto.refresh();
    }

    @Override
    public void onClickItemPhoto(Photo photo) {
        if (mAdapterPhotoChoose.size() < Constants.MAX_PHOTO) {
            mAdapterPhotoChoose.addItem(photo);
            mLayoutManagerPhotoChoose.scrollToPosition(mAdapterPhotoChoose.size() - 1);
            mAdapterPhotoChoose.refreshAdd();

            if (mAdapterPhotoChoose.size() == 0) {
                mTvPlease.setVisibility(View.VISIBLE);
                mRlContainerPlease.setVisibility(View.GONE);
            } else {
                mTvPlease.setVisibility(View.GONE);
                mRlContainerPlease.setVisibility(View.VISIBLE);
                mTvNumber.setText(String.format(getString(R.string.text_number), mAdapterPhotoChoose.size()));
            }
        }
    }

    @Override
    public void onRemoveItem(int position) {
        mAdapterPhotoChoose.removeItem(position);
        mAdapterPhotoChoose.refreshRemove(position);

        if (mAdapterPhotoChoose.size() == 0) {
            mTvPlease.setVisibility(View.VISIBLE);
            mRlContainerPlease.setVisibility(View.GONE);
        } else {
            mTvPlease.setVisibility(View.GONE);
            mRlContainerPlease.setVisibility(View.VISIBLE);
            mTvNumber.setText(String.format(getString(R.string.text_number), mAdapterPhotoChoose.size()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPresenter(this).loadPhoto();
                } else {
                    finish();
                }
                break;
            }
        }
    }

    @Override
    public void onShowListImages(List<FolderImage> folderImages) {
        mAdapterFolder.setItems(folderImages);
        mAdapterFolder.refresh();
    }

    public static void startPhotoActivity(Context context, List<Photo> photos) {
        Intent intent = new Intent(context, PhotoActivity.class);
        if (photos != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Constants.BUNDLE_KEY_LIST_PHOTO, (ArrayList<? extends Parcelable>) photos);
            intent.putExtras(bundle);
        }

        context.startActivity(intent);
    }


    public static void startPhotoActivity(Context context) {
        startPhotoActivity(context, null);
    }
}

