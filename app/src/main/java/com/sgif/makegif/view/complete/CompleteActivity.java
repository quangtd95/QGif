package com.sgif.makegif.view.complete;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sgif.makegif.R;
import com.sgif.makegif.common.Constants;
import com.sgif.makegif.common.base.BaseActivity;

/**
 * Created by quang.td95@gmail.com
 * on 9/29/2018.
 */
public class CompleteActivity extends BaseActivity<CompletePresenter> implements CompleteView {
    private ImageView mImvGif;
    private TextView mTvResultPath;

    @Override
    protected int getIdLayout() {
        return R.layout.activity_complete;
    }

    @Override
    protected void bindData() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initActions() {

    }

    public static void startCompleteActivity(Context context, String resultPath) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_KEY_PATH_GIF, resultPath);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
