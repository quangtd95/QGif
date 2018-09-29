package com.sgif.makegif.common.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.sgif.makegif.util.DialogUtils;

import java.lang.reflect.ParameterizedType;

/**
 * Created by quang.td95@gmail.com
 * on 9/28/2018.
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

    private P viewPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getIdLayout());
        initViews();
        bindData();
        initActions();
    }

    @LayoutRes
    protected abstract int getIdLayout();

    protected abstract void bindData();

    protected abstract void initViews();

    protected abstract void initActions();

    public P getPresenter(BaseView baseView) {
        try {
            if (this.viewPresenter == null) {
                String e = ((Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
                Class classDefinition = Class.forName(e);
                this.viewPresenter = (P) classDefinition.newInstance();
                this.viewPresenter.setIFace(baseView);
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

    public void updateStatusBarColor(String color) {// Color must be in hexadecimal format
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
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
}
