package com.sgif.makegif.view.export;

import android.Manifest;
import android.app.ExpandableListActivity;
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
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sgif.makegif.R;
import com.sgif.makegif.common.Constants;
import com.sgif.makegif.common.base.BaseActivity;
import com.sgif.makegif.common.listener.AfterTextChangedWatcher;
import com.sgif.makegif.common.listener.OnSeekBarChangeListener;
import com.sgif.makegif.domain.model.Photo;
import com.sgif.makegif.util.DialogUtils;
import com.sgif.makegif.util.RecyclerViewUtils;
import com.sgif.makegif.view.complete.CompleteActivity;
import com.sgif.makegif.view.photo.PhotoActivity;
import com.sgif.makegif.view.photo.PhotoChooseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class ExportGifPhotoActivity extends BaseActivity<ExportGifPhotoPresenter> implements PhotoChooseAdapter.OnClickRemoveItemListener, ExportGifPhotoView {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    private ProgressBar mPgExportGif;
    private SeekBar mSbDelay;
    private TextView mTvDuration;
    private EditText mEdtWidth;
    private EditText mEdtHeight;
    private Button mBtnDefaultDimension;
    private CheckBox mChkKeepRatio;
    private Button mBtnExportGif;

    private PhotoChooseAdapter mPhotoChooseAdapter;

    @Override
    protected int getIdLayout() {
        return R.layout.activity_gif_photo;
    }

    @Override
    protected void bindData() {
        Bundle bundle = getIntent().getExtras();
        List<Photo> mPhotoList;
        if (bundle != null) {
            mPhotoList = bundle.getParcelableArrayList(Constants.BUNDLE_KEY_LIST_PHOTO);
            if (mPhotoList == null || mPhotoList.size() < 2) {
                DialogUtils.createAlertDialog(this, "Error", "please choose at least 2 photos", this::finish);
            }
            mPhotoChooseAdapter.setItems(mPhotoList);
            getPresenter(this).setAdapter(mPhotoChooseAdapter);
            getPresenter(this).setDefaultDimens();
            mChkKeepRatio.setChecked(getPresenter(this).isKeepRatio());
        } else {
            finish();
        }

    }

    @Override
    protected void initViews() {
        RecyclerView mRvPhotoChoose = findViewById(R.id.rvPhotoChoose);
        mPgExportGif = findViewById(R.id.pgExportGif);
        mSbDelay = findViewById(R.id.sbDelay);
        mTvDuration = findViewById(R.id.tvDuration);
        mEdtWidth = findViewById(R.id.edtWidth);
        mEdtHeight = findViewById(R.id.edtHeight);
        mBtnDefaultDimension = findViewById(R.id.btnDefaultDimension);
        mChkKeepRatio = findViewById(R.id.chkKeepRatio);
        mBtnExportGif = findViewById(R.id.btnExportGif);

        mPgExportGif.setVisibility(View.INVISIBLE);

        RecyclerViewUtils.Create().setUpGridHorizontal(this, mRvPhotoChoose, 1);
        mPhotoChooseAdapter = new PhotoChooseAdapter(this);
        mRvPhotoChoose.setAdapter(mPhotoChooseAdapter);
    }

    @Override
    protected void initActions() {
        mPhotoChooseAdapter.setOnClickRemoveItemListener(this);
        mSbDelay.setOnSeekBarChangeListener((OnSeekBarChangeListener) (seekBar, progress, fromUser) -> {
            getPresenter(this).setDelay(progress);
        });
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
            getPresenter(ExportGifPhotoActivity.this).setKeepRatio(mChkKeepRatio.isChecked());
        });
        mBtnDefaultDimension.setOnClickListener(v -> {
            getPresenter(ExportGifPhotoActivity.this).setDefaultDimens();
        });
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
        mPhotoChooseAdapter.removeItem(position);
        mPhotoChooseAdapter.refreshRemove(position);
        if (mPhotoChooseAdapter.size() < 2) {
            DialogUtils.createAlertDialog(
                    this,
                    "error",
                    "please choose at least 2 photos to continue",
                    () -> {
                        PhotoActivity.startPhotoActivity(this, mPhotoChooseAdapter.getItems());
                        finish();
                    });
        } else {
            getPresenter(this).calculateDefaultDimens();
            getPresenter(this).setDefaultDimens();
        }
    }

    @Override
    public void setDuration(float duration) {
        mTvDuration.setText(String.format(getString(R.string.duration_number), duration));
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
            Toast.makeText(ExportGifPhotoActivity.this, error, Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPresenter(this).exportGif();
                } else {
                    //TODO: nêu lí do cần cấp quyền
                }
                break;
            }
        }
    }
}
