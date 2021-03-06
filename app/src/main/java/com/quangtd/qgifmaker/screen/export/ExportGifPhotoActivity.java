package com.quangtd.qgifmaker.screen.export;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.quangtd.qgifmaker.R;
import com.quangtd.qgifmaker.common.Constants;
import com.quangtd.qgifmaker.common.base.BaseActivity;
import com.quangtd.qgifmaker.common.listener.AfterTextChangedWatcher;
import com.quangtd.qgifmaker.common.listener.OnSeekBarChangeListener;
import com.quangtd.qgifmaker.domain.model.Photo;
import com.quangtd.qgifmaker.screen.complete.CompleteActivity;
import com.quangtd.qgifmaker.screen.custom.GifView;
import com.quangtd.qgifmaker.screen.gallery.adapter.ChooseAdapter;
import com.quangtd.qgifmaker.util.DialogUtils;
import com.quangtd.qgifmaker.util.RecyclerViewUtils;
import com.quangtd.qgifmaker.util.SnackBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class ExportGifPhotoActivity extends BaseActivity<ExportGifPhotoPresenter> implements ChooseAdapter.OnClickRemoveItemListener, ExportGifPhotoView {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    private ProgressBar mPgExportGif;
    private SeekBar mSbDelay;
    private TextView mTvDuration;
    private EditText mEdtWidth;
    private EditText mEdtHeight;
    private Button mBtnDefaultDimension;
    private CheckBox mChkKeepRatio;
    private Button mBtnExportGif;
    private ImageView mImvBack;
    private GifView mGifView;

    private ChooseAdapter mChooseAdapter;
    private ExportGifPhotoPresenter mPresenter;

    @Override
    protected int getIdLayout() {
//        return R.layout.activity_gif_photo;
        return R.layout.activity_gif_photo;
    }

    @Override
    protected void bindData() {
        mPresenter = getPresenter(this);
        Bundle bundle = getIntent().getExtras();
        List<Photo> mPhotoList;
        if (bundle != null) {
            mPhotoList = bundle.getParcelableArrayList(Constants.BUNDLE_KEY_LIST_PHOTO);
            if (mPhotoList == null || mPhotoList.size() < Constants.MIN_PHOTO) {
                DialogUtils.createAlertDialog(this, "Error", "please choose at least 2 photos", this::finish);
            }
            mChooseAdapter.setItems(mPhotoList);
            mPresenter.setAdapter(mChooseAdapter);
            mPresenter.setDefaultDimens();
            mPresenter.setDelay(mSbDelay.getProgress());
            mChkKeepRatio.setChecked(getPresenter(this).isKeepRatio());
            mGifView.setAdapter(mChooseAdapter);
            mGifView.startPreview();
        } else {
            finish();
        }

    }

    @Override
    protected void initViews() {
        mGifView = findViewById(R.id.gifView);
        RecyclerView mRvPhotoChoose = findViewById(R.id.rvPhotoChoose);
        mPgExportGif = findViewById(R.id.pgExportGif);
        mSbDelay = findViewById(R.id.sbDelay);
        mTvDuration = findViewById(R.id.tvDuration);
        mEdtWidth = findViewById(R.id.edtWidth);
        mEdtHeight = findViewById(R.id.edtHeight);
        mBtnDefaultDimension = findViewById(R.id.btnDefaultDimension);
        mChkKeepRatio = findViewById(R.id.chkKeepRatio);
        mBtnExportGif = findViewById(R.id.btnExportGif);
        mImvBack = findViewById(R.id.imvBack);
        mPgExportGif.setVisibility(View.INVISIBLE);

        RecyclerViewUtils.getInstance().setUpGridHorizontal(this, mRvPhotoChoose, 1);
        mChooseAdapter = new ChooseAdapter(this);
        mRvPhotoChoose.setAdapter(mChooseAdapter);
    }

    @Override
    protected void initActions() {
        mChooseAdapter.setOnClickRemoveItemListener(this);
        mSbDelay.setOnSeekBarChangeListener((OnSeekBarChangeListener) (seekBar, progress, fromUser) -> getPresenter(this).setDelay(progress));
        mEdtWidth.addTextChangedListener((AfterTextChangedWatcher) s -> {
            // user nhập
            if (mEdtWidth.getTag() == null) {
                if (TextUtils.isEmpty(s)) {
                    getPresenter(ExportGifPhotoActivity.this).setWidth(0);
                } else {
                    getPresenter(ExportGifPhotoActivity.this).setWidth(Integer.parseInt(s.toString()));
                }
            }
        });
        mEdtHeight.addTextChangedListener((AfterTextChangedWatcher) s -> {
            // user nhập
            if (mEdtHeight.getTag() == null) {
                if (TextUtils.isEmpty(s)) {
                    getPresenter(ExportGifPhotoActivity.this).setHeight(0);
                } else {
                    getPresenter(ExportGifPhotoActivity.this).setHeight(Integer.parseInt(s.toString()));
                }
            }
        });
        mChkKeepRatio.setOnClickListener(v -> {
            if (mChkKeepRatio.isChecked()) {
                getPresenter(ExportGifPhotoActivity.this).setDefaultDimens();
            }
            getPresenter(ExportGifPhotoActivity.this).setKeepRatio(mChkKeepRatio.isChecked());
        });
        mBtnDefaultDimension.setOnClickListener(v -> getPresenter(ExportGifPhotoActivity.this).setDefaultDimens());
        mBtnExportGif.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    getPresenter(ExportGifPhotoActivity.this).exportGif();
                }
            } else {
                getPresenter(ExportGifPhotoActivity.this).exportGif();
            }
        });
        mImvBack.setOnClickListener(v -> finish());
    }

    public static void startActivity(Context context, ArrayList<Photo> photos) {
        Intent intent = new Intent(context, ExportGifPhotoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.BUNDLE_KEY_LIST_PHOTO, photos);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    public void onRemoveItem(int position) {
        if (mChooseAdapter.size() <= Constants.MIN_PHOTO) {
            DialogUtils.createAlertDialog(
                    this,
                    "error",
                    "please choose at least " + Constants.MIN_PHOTO + " photos",
                    () -> {
//                        PhotoGalleryActivity.Companion.startPhotoActivity(this, mChooseAdapter.getItems());
//                        finish();
                    });
        } else {
            mChooseAdapter.removeItem(position);
            mChooseAdapter.refreshRemove(position);
            mGifView.refreshRemove(position);
            getPresenter(this).calculateDefaultDimens();
            getPresenter(this).setDefaultDimens();
        }
    }

    @Override
    public void setDuration(float duration) {
        mTvDuration.setText(String.format(getString(R.string.time_format), duration / 1000));
        mGifView.setDuration(duration);
    }

    @Override
    public void setWidthGif(String s) {
        mEdtWidth.setTag(new Object());
        mEdtWidth.setText(s);
        mEdtWidth.setSelection(s.length());
        mEdtWidth.setTag(null);
    }

    @Override
    public void setHeightGif(String s) {
        mEdtHeight.setTag(new Object());
        mEdtHeight.setText(s);
        mEdtHeight.setSelection(s.length());
        mEdtHeight.setTag(null);
    }

    @Override
    public void onProgress(Float progress) {
        mPgExportGif.setProgress((int) (progress * (mPgExportGif.getMax())));
    }

    @Override
    public void onPrepareExport() {
        mPgExportGif.setVisibility(View.VISIBLE);
        showLoading();
    }

    @Override
    public void onCompleteExport(String resultPath) {
        mPgExportGif.setVisibility(View.INVISIBLE);
        hideLoading();
        CompleteActivity.startCompleteActivity(this, resultPath);
    }

    @Override
    public void onCancelledExport(String error) {
        runOnUiThread(() -> {
            mPgExportGif.setVisibility(View.INVISIBLE);
            hideLoading();
            SnackBarUtils.Companion.showErrorMessage(error, mPgExportGif);
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPresenter(this).exportGif();
            } else {
                DialogUtils.createAlertDialog(this, "", "need write to sdcard permission to process gif");
            }
        }
    }
}
