package com.quangtd.qgifmaker.common.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.quangtd.qgifmaker.R;
import com.quangtd.qgifmaker.util.DialogUtils;

import java.lang.reflect.ParameterizedType;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IBaseView {

    private P viewPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LightTheme);
        setContentView(getIdLayout());
        initViews();
        bindData();
        initActions();
        overridePendingTransition(R.anim.view_enter_from_right, R.anim.view_exit_to_left);
    }

    @LayoutRes
    protected abstract int getIdLayout();

    protected abstract void bindData();

    protected abstract void initViews();

    protected abstract void initActions();

    public P getPresenter(IBaseView IBaseView) {
        try {
            if (this.viewPresenter == null) {
                String e = ((Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
                Class classDefinition = Class.forName(e);
                this.viewPresenter = (P) classDefinition.newInstance();
                this.viewPresenter.setIFace(IBaseView);
                this.viewPresenter.onInit();
                return this.viewPresenter;
            }
        } catch (InstantiationException var4) {
            var4.printStackTrace();
        } catch (IllegalAccessException var5) {
            var5.printStackTrace();
        } catch (ClassNotFoundException var6) {
            var6.printStackTrace();
        }

        return this.viewPresenter;
    }

    @Override
    public void showNotifyDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notify");
        builder.setMessage(message);
        builder.setNegativeButton("OK", null);
        builder.show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showLoading() {
        DialogUtils.showLoadingDialog(this);
    }

    @Override
    public void hideLoading() {
        DialogUtils.hideLoadingDialog();
    }

    private void hideStatusAndNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar

                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar

                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar

                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideStatusAndNavigationBar();
        }
    }

    public void uiChangeListener() {
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                hideStatusAndNavigationBar();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.view_enter_from_left, R.anim.view_exit_to_right);
    }
}
