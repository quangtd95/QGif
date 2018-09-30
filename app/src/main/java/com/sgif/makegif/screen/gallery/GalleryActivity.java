package com.sgif.makegif.screen.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.sgif.makegif.domain.model.FolderMedia;
import com.sgif.makegif.domain.model.Media;
import com.sgif.makegif.domain.model.MediaType;
import com.sgif.makegif.screen.export.ExportGifPhotoActivity;
import com.sgif.makegif.screen.export.ExportGifVideoActivity;
import com.sgif.makegif.util.DialogUtils;
import com.sgif.makegif.util.GridSpacingItemDecoration;
import com.sgif.makegif.util.RecyclerViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public class GalleryActivity extends BaseActivity<GalleryPresenter> implements GalleryView, FolderAdapter.OnClickItemFolderListener, GalleryAdapter.OnClickItemPhotoListener, ChooseAdapter.OnClickRemoveItemListener, View.OnClickListener {

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
    private GalleryAdapter mAdapterPhoto;
    private ChooseAdapter mAdapterPhotoChoose;
    private MediaType mMediaType;

    @Override
    protected int getIdLayout() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void bindData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mMediaType = MediaType.getByCode(bundle.getInt(Constants.BUNDLE_KEY_MEDIA_TYPE, Constants.TYPE_PHOTO));
            switch (mMediaType) {
                case PHOTO:
                    mTvNameFolder.setText(R.string.choose_photo);
                    List<Media> mPhotoChosenList = bundle.getParcelableArrayList(Constants.BUNDLE_KEY_LIST_PHOTO);
                    if (mPhotoChosenList != null) {
                        mAdapterPhotoChoose.setItems(mPhotoChosenList);
                        mAdapterPhotoChoose.refresh();
                        mTvPlease.setVisibility(View.GONE);
                        mRlContainerPlease.setVisibility(View.VISIBLE);
                        mTvNumber.setText(String.format(getString(R.string.text_number), mAdapterPhotoChoose.size()));
                    }
                    break;
                case VIDEO:
                    mTvNameFolder.setText(R.string.choose_video);
                    mTvPlease.setVisibility(View.GONE);
                    mRlContainerPlease.setVisibility(View.GONE);
                    break;
            }
            getPresenter(this).loadMedia(mMediaType);
        }

    }

    @Override
    protected void initViews() {
        mImgBack = findViewById(R.id.imvBack);
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
        mAdapterPhoto = new GalleryAdapter(this);
        mRecyclerPhoto.setLayoutManager(mLayoutManagerPhoto);
        mRecyclerPhoto.setAdapter(mAdapterPhoto);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(3, 10, false);
        mRecyclerPhoto.addItemDecoration(itemDecoration);


        RecyclerView mRecyclerPhotoChoose = findViewById(R.id.recyclerPhotoChoose);
        RecyclerViewUtils.Create().setUpHorizontal(this, mRecyclerPhotoChoose);
        mLayoutManagerPhotoChoose = (LinearLayoutManager) mRecyclerPhotoChoose.getLayoutManager();
        mAdapterPhotoChoose = new ChooseAdapter(this);
        mRecyclerPhotoChoose.setAdapter(mAdapterPhotoChoose);

        mRecyclerFolder.setVisibility(View.VISIBLE);
        mRecyclerPhoto.setVisibility(View.GONE);
        mRlContainerPlease.setVisibility(View.GONE);
        if (mMediaType == MediaType.PHOTO) {
            mTvPlease.setVisibility(View.VISIBLE);
        } else {
            mTvPlease.setVisibility(View.GONE);
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
            case R.id.imvBack:
                if (mRecyclerFolder.getVisibility() == View.VISIBLE) {
                    finish();
                } else {
                    mRecyclerFolder.setVisibility(View.VISIBLE);
                    mRecyclerPhoto.setVisibility(View.GONE);
                    switch (mMediaType) {
                        case PHOTO:
                            mTvNameFolder.setText(getString(R.string.choose_photo));
                            break;
                        case VIDEO:
                            mTvNameFolder.setText(getString(R.string.choose_video));
                            break;
                    }

                }
                break;
            case R.id.llNumber:
                if (mAdapterPhotoChoose.size() < Constants.MIN_PHOTO) {
                    DialogUtils.createAlertDialog(
                            this,
                            "error",
                            "please choose at least 2 photos to continue");
                } else {
                    ExportGifPhotoActivity.startActivity(this, (ArrayList<Media>) mAdapterPhotoChoose.getItems());
                }

                break;
        }
    }

    @Override
    public void onClickFolder(FolderMedia folderPhoto) {
        mRecyclerPhoto.setVisibility(View.VISIBLE);
        mRecyclerFolder.setVisibility(View.GONE);
        mTvNameFolder.setText(folderPhoto.getName());
        mAdapterPhoto.setItems(folderPhoto.getListMedia());
        mAdapterPhoto.refresh();
    }

    @Override
    public void onClickItemMedia(Media media) {
        switch (mMediaType) {
            case PHOTO:
                if (mAdapterPhotoChoose.size() < Constants.MAX_PHOTO) {
                    mAdapterPhotoChoose.addItem(media);
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
                break;
            case VIDEO:
                ExportGifVideoActivity.startActivity(this, media);
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
    public void onShowListImages(List<FolderMedia> folderPhotos) {
        mAdapterFolder.setItems(folderPhotos);
        mAdapterFolder.refresh();
    }

    public static void startPhotoActivity(Context context, List<Media> photos) {
        Intent intent = new Intent(context, GalleryActivity.class);
        if (photos != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Constants.BUNDLE_KEY_LIST_PHOTO, (ArrayList<? extends Parcelable>) photos);
            bundle.putInt(Constants.BUNDLE_KEY_MEDIA_TYPE, Constants.TYPE_PHOTO);
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startPhotoActivity(Context context, MediaType mediaType) {
        Intent intent = new Intent(context, GalleryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_KEY_MEDIA_TYPE, mediaType.getCode());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}

