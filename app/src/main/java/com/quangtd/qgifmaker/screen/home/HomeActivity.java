package com.quangtd.qgifmaker.screen.home;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quangtd.qgifmaker.R;
import com.quangtd.qgifmaker.common.base.BaseActivity;
import com.quangtd.qgifmaker.domain.model.MediaType;
import com.quangtd.qgifmaker.screen.new_gallery.PhotoGalleryActivity;
import com.quangtd.qgifmaker.util.DialogUtils;


/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeView {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;

    private View mBtnChooseImage;

    private View mBtnChooseVideo;

    private View mBtnChooseCenter;

    private TextView mTvPolicy;

    private TextView mTvBottomText;

    private ImageView mImvHome;

    private MediaType mMediaType;

    private AnimatorSet mScaleUp;

    private AnimatorSet mScaleDown;


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
        mBtnChooseImage = findViewById(R.id.rlPhoto2Gif);
        mBtnChooseVideo = findViewById(R.id.rlVideo2Gif);
        mTvPolicy = findViewById(R.id.tvPolicy);
        mBtnChooseCenter = findViewById(R.id.rlOpenQStudioCenter);
        mTvBottomText = findViewById(R.id.tvBottomText);
        mImvHome = findViewById(R.id.mImvHome);
        mScaleUp = zoomInAnimation(mImvHome);
        mScaleDown = zoomOutAnimation(mImvHome);
        mScaleUp.start();
        mTvBottomText.setSelected(true);
    }

    @Override
    protected void initActions() {
        mBtnChooseVideo.setOnClickListener(v -> openGallery(MediaType.VIDEO));

        mBtnChooseImage.setOnClickListener(v -> openGallery(MediaType.PHOTO));

        mBtnChooseCenter.setOnClickListener(v -> openCenter(getString(R.string.center_id)));

        mTvPolicy.setOnClickListener(v -> openUrl(getString(R.string.policy_url)));
    }

    private void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
    }

    private void openGallery(MediaType mediaType) {
//        Intent intent = new Intent(this, PhotoGalleryActivity.class);
//        startActivity(intent);
        this.mMediaType = mediaType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                PhotoGalleryActivity.Companion.startPhotoActivity(this, mediaType);
            }
        } else {
            PhotoGalleryActivity.Companion.startPhotoActivity(this, mediaType);
        }
    }

    private void openCenter(String id) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(id);
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
            finish();
        } else {
            Uri uri = Uri.parse("market://details?id=$id");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            } else {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            }
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=$id")));
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoGalleryActivity.Companion.startPhotoActivity(this, mMediaType);
                } else {
                    DialogUtils.createAlertDialog(this, "", "need permission to process gif");
                }
                break;
            }
        }
    }

    private AnimatorSet zoomInAnimation(View view) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.2f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1.2f);
        scaleDownX.setDuration(10000);
        scaleDownY.setDuration(10000);
        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.play(scaleDownX).with(scaleDownY);
        scaleUp.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationRepeat(Animator p0) {
            }

            @Override
            public void onAnimationEnd(Animator p0) {
                mScaleDown.start();
            }

            @Override
            public void onAnimationCancel(Animator p0) {
            }

            @Override
            public void onAnimationStart(Animator p0) {
            }

        });
        return scaleUp;
    }

    private AnimatorSet zoomOutAnimation(View view) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f);
        scaleDownX.setDuration(10000);
        scaleDownY.setDuration(10000);
        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);
        scaleDown.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationRepeat(Animator p0) {
            }

            @Override
            public void onAnimationEnd(Animator p0) {
                mScaleUp.start();
            }

            @Override
            public void onAnimationCancel(Animator p0) {
            }

            @Override
            public void onAnimationStart(Animator p0) {
            }

        });
        return scaleDown;
    }
}
