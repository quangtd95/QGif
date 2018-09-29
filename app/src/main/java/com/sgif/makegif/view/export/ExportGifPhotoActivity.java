package com.sgif.makegif.view.export;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sgif.makegif.R;
import com.sgif.makegif.common.Constants;
import com.sgif.makegif.common.base.BaseActivity;
import com.sgif.makegif.common.listener.AfterTextChangedWatcher;
import com.sgif.makegif.common.listener.OnSeekBarChangeListener;
import com.sgif.makegif.domain.model.Photo;
import com.sgif.makegif.util.DialogUtils;
import com.sgif.makegif.util.RecyclerViewUtils;
import com.sgif.makegif.view.photo.PhotoActivity;
import com.sgif.makegif.view.photo.PhotoChooseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class ExportGifPhotoActivity extends BaseActivity<ExportGifPhotoPresenter> implements PhotoChooseAdapter.OnClickRemoveItemListener, ExportGifPhotoView {
    private ProgressBar mPgExportGif;
    private SeekBar mSbDuration;
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
    protected void initValues() {
        Bundle bundle = getIntent().getExtras();
        List<Photo> mPhotoList = null;
        if (bundle != null) {
            mPhotoList = bundle.getParcelableArrayList(Constants.BUNDLE_KEY_LIST_PHOTO);
            if (mPhotoList == null || mPhotoList.size() < 2) {
                DialogUtils.createAlertDialog(this, "Error", "please choose at least 2 photos", this::finish);
            }
        }
        mPhotoChooseAdapter = new PhotoChooseAdapter(this);
        mPhotoChooseAdapter.setItems(mPhotoList);
        getPresenter(this).setAdapter(mPhotoChooseAdapter);
    }

    @Override
    protected void initViews() {
        RecyclerView mRvPhotoChoose = findViewById(R.id.rvPhotoChoose);
        mPgExportGif = findViewById(R.id.pgExportGif);
        mSbDuration = findViewById(R.id.sbDuration);
        mTvDuration = findViewById(R.id.tvDuration);
        mEdtWidth = findViewById(R.id.edtWidth);
        mEdtHeight = findViewById(R.id.edtHeight);
        mBtnDefaultDimension = findViewById(R.id.btnDefaultDimension);
        mChkKeepRatio = findViewById(R.id.chkKeepRatio);
        mBtnExportGif = findViewById(R.id.btnExportGif);

        mPgExportGif.setVisibility(View.GONE);

        RecyclerViewUtils.Create().setUpGridHorizontal(this, mRvPhotoChoose, 1);
        mRvPhotoChoose.setAdapter(mPhotoChooseAdapter);

        mChkKeepRatio.setChecked(getPresenter(this).isKeepRatio());

        getPresenter(this).setDefaultDimens();

    }

    @Override
    protected void initActions() {
        mPhotoChooseAdapter.setOnClickRemoveItemListener(this);
        mSbDuration.setOnSeekBarChangeListener((OnSeekBarChangeListener) (seekBar, progress, fromUser) -> {
            getPresenter(this).setDuration(progress);
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
        mBtnExportGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}
