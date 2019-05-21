package com.quangtd.qgifmaker.screen.complete;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.quangtd.qgifmaker.R;
import com.quangtd.qgifmaker.common.Constants;
import com.quangtd.qgifmaker.common.GlideApp;
import com.quangtd.qgifmaker.common.base.BaseActivity;

import java.io.File;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class CompleteActivity extends BaseActivity<CompletePresenter> implements CompleteView {
    private ImageView mImvGif;
    private ImageView imvBack;
    private ImageView imvShare;
    private TextView tvShare;
    private TextView mTvResultPath;
    private String mResultPath;

    @Override
    protected int getIdLayout() {
        return R.layout.activity_complete;
    }

    @Override
    protected void bindData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String resultPath = bundle.getString(Constants.BUNDLE_KEY_PATH_GIF);
            if (TextUtils.isEmpty(resultPath)) {
                //TODO:
                finish();
            } else {
                mResultPath = resultPath;
                mTvResultPath.setText(resultPath);
                GlideApp.with(this).load(resultPath).into(mImvGif);
            }
        } else {
            //TODO:
            finish();
        }
    }

    @Override
    protected void initViews() {
        mImvGif = findViewById(R.id.imvGif);
        imvBack = findViewById(R.id.imvBack);
        imvShare = findViewById(R.id.imvShare);
        tvShare = findViewById(R.id.tvShare);
        mTvResultPath = findViewById(R.id.tvResultPath);
    }

    @Override
    protected void initActions() {
        imvBack.setOnClickListener(v -> finish());
        imvShare.setOnClickListener(v -> shareGif(mResultPath));
        tvShare.setOnClickListener(v -> shareGif(mResultPath));
    }

    private void shareGif(String resourceName) {
        Uri apkURI = FileProvider.getUriForFile(
                this,
                this.getApplicationContext()
                        .getPackageName() + ".provider", new File(resourceName));
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/gif");
        shareIntent.putExtra(Intent.EXTRA_STREAM, apkURI);
        startActivity(Intent.createChooser(shareIntent, "Share Gif"));
    }

    public static void startCompleteActivity(Context context, String resultPath) {
        Intent intent = new Intent(context, CompleteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_KEY_PATH_GIF, resultPath);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
