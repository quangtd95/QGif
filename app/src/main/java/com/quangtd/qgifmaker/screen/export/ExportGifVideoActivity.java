package com.quangtd.qgifmaker.screen.export;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.quangtd.qgifmaker.R;
import com.quangtd.qgifmaker.common.Constants;
import com.quangtd.qgifmaker.common.base.BaseActivity;
import com.quangtd.qgifmaker.common.listener.AfterTextChangedWatcher;
import com.quangtd.qgifmaker.common.listener.OnSeekBarChangeListener;
import com.quangtd.qgifmaker.domain.model.Media;
import com.quangtd.qgifmaker.screen.complete.CompleteActivity;
import com.quangtd.qgifmaker.util.DialogUtils;
import com.quangtd.qgifmaker.util.Utils;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class ExportGifVideoActivity extends BaseActivity<ExportGifVideoPresenter> implements ExportGifVideoView {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    private ProgressBar mPgExportGif;
    private SeekBar mSbDelay;
    private SeekBar mSbStartTime;
    private SeekBar mSbEndTime;
    private TextView mTvStartTime;
    private TextView mTvEndTime;
    private TextView mTvDelay;
    private EditText mEdtWidth;
    private EditText mEdtHeight;
    private Button mBtnDefaultDimension;
    private CheckBox mChkKeepRatio;
    private Button mBtnExportGif;
    private ImageView mImvBack;
    private VideoView mVideoView;
    private int mStartTime;
    private int mEndTime;
    private Handler mControlVideoHandler;

    @Override
    protected int getIdLayout() {
        return R.layout.activity_gif_video;
    }


    @Override
    protected void initViews() {
        mPgExportGif = findViewById(R.id.pgExportGif);
        mSbDelay = findViewById(R.id.sbDelay);
        mSbStartTime = findViewById(R.id.sbStartTime);
        mSbEndTime = findViewById(R.id.sbEndTime);
        mTvStartTime = findViewById(R.id.tvStartTime);
        mTvEndTime = findViewById(R.id.tvEndTime);
        mTvDelay = findViewById(R.id.tvDuration);
        mEdtWidth = findViewById(R.id.edtWidth);
        mEdtHeight = findViewById(R.id.edtHeight);
        mBtnDefaultDimension = findViewById(R.id.btnDefaultDimension);
        mChkKeepRatio = findViewById(R.id.chkKeepRatio);
        mBtnExportGif = findViewById(R.id.btnExportGif);
        mImvBack = findViewById(R.id.imvBack);
        mVideoView = findViewById(R.id.videoView);
        mPgExportGif.setVisibility(View.INVISIBLE);
        mVideoView.getLayoutParams().height = (int) (Utils.getScreenWidth(this) * 1.0f / 16 * 9);
    }

    @Override
    protected void bindData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Media mVideo = bundle.getParcelable(Constants.BUNDLE_KEY_VIDEO);
            if (mVideo == null) {
                DialogUtils.createAlertDialog(this, "Error", "video must not be null", this::finish);
            } else {
                getPresenter(this).setVideo(mVideo);
                getPresenter(this).setDefaultDimens();
                getPresenter(this).setDelay(mSbDelay.getProgress());
                mStartTime = 0;
                mEndTime = mVideo.getDuration();
                mTvStartTime.setText(String.format(getString(R.string.time_format), 0f));
                mTvEndTime.setText(String.format(getString(R.string.time_format), mEndTime * 1.0f / 1000));
                mChkKeepRatio.setChecked(getPresenter(this).isKeepRatio());
                MediaController mediaController = new MediaController(this);
                mVideoView.setMediaController(mediaController);
                mediaController.setAnchorView(mVideoView);
                mVideoView.setVideoPath(mVideo.getPath());
                mVideoView.requestFocus();
            }
        } else {
            finish();
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initActions() {

        mVideoView.setOnPreparedListener(mp -> {
            //chạy video
            mp.seekTo(0);
            mp.setVolume(0f, 0f);
            mp.start();
            setupVideoControl();
        });

        mSbStartTime.setOnSeekBarChangeListener((OnSeekBarChangeListener) (seekBar, progress, fromUser) -> {
            if (fromUser) {
                mVideoView.pause();
                getPresenter(this).setStartTime(progress);
            }
        });
        mSbEndTime.setOnSeekBarChangeListener((OnSeekBarChangeListener) (seekBar, progress, fromUser) -> {
            if (fromUser) {
                mVideoView.pause();
                getPresenter(this).setEndTime(progress);
            }
        });
        mSbDelay.setOnSeekBarChangeListener((OnSeekBarChangeListener) (seekBar, progress, fromUser) -> {
            getPresenter(this).setDelay(progress);
        });

        mEdtWidth.addTextChangedListener((AfterTextChangedWatcher) s -> {
            //tag = null khi user gõ vào edit text
            // chỉ xử lý khi user gõ
            if (mEdtWidth.getTag() == null) {
                if (TextUtils.isEmpty(s)) {
                    getPresenter(ExportGifVideoActivity.this).setWidth(0);
                } else {
                    getPresenter(ExportGifVideoActivity.this).setWidth(Integer.parseInt(s.toString()));
                }
            }
        });

        mEdtHeight.addTextChangedListener((AfterTextChangedWatcher) s -> {
            // user nhập
            if (mEdtHeight.getTag() == null) {
                if (TextUtils.isEmpty(s)) {
                    getPresenter(ExportGifVideoActivity.this).setHeight(0);
                } else {
                    getPresenter(ExportGifVideoActivity.this).setHeight(Integer.parseInt(s.toString()));
                }
            }
        });

        mChkKeepRatio.setOnClickListener(v -> {
            if (mChkKeepRatio.isChecked()) {
                getPresenter(ExportGifVideoActivity.this).setDefaultDimens();
            }
            getPresenter(ExportGifVideoActivity.this).setKeepRatio(mChkKeepRatio.isChecked());
        });

        // set lại giá trị mặc định cho dimension.
        mBtnDefaultDimension.setOnClickListener(v -> {
            getPresenter(ExportGifVideoActivity.this).setDefaultDimens();
        });

        mBtnExportGif.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    getPresenter(ExportGifVideoActivity.this).exportGif();
                }
            } else {
                getPresenter(ExportGifVideoActivity.this).exportGif();
            }
        });
        mImvBack.setOnClickListener(v -> finish());
    }

    public static void startActivity(Context context, Media video) {
        Intent intent = new Intent(context, ExportGifVideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BUNDLE_KEY_VIDEO, video);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void setDelay(float delayMs) {
        mTvDelay.setText(String.format(getString(R.string.time_format), delayMs / 1000));
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
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            mVideoView.stopPlayback();
        }
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
            Toast.makeText(ExportGifVideoActivity.this, error, Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void setStartTimeProgress(int progress, int startTime) {
        mSbStartTime.setProgress(progress);
        mVideoView.seekTo(startTime);
        mStartTime = startTime;
        mTvStartTime.setText(String.format(getString(R.string.time_format), startTime * 1.0f / 1000));
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
        }
    }

    @Override
    public void setEndTimeProgress(int progress, int endTime) {
        mSbEndTime.setProgress(progress);
        mVideoView.seekTo(endTime);
        mEndTime = endTime;
        mTvEndTime.setText(String.format(getString(R.string.time_format), endTime * 1.0f / 1000));
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPresenter(this).exportGif();
                } else {
                    DialogUtils.createAlertDialog(this, "", getString(R.string.need_permission));
                }
                break;
            }
        }
    }

    private void setupVideoControl() {
        mControlVideoHandler = new Handler();
        mControlVideoHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mVideoView.getCurrentPosition() > mEndTime) {
                    mVideoView.seekTo(mStartTime);
                    mVideoView.pause();
                }
                mControlVideoHandler.postDelayed(this, 100);
            }
        }, 100);
    }

    @Override
    protected void onDestroy() {
        if (mControlVideoHandler != null) {
            mControlVideoHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
