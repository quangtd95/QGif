package com.sgif.makegif.util;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Window;

import com.sgif.makegif.R;


public class DialogUtils {
    private static Dialog dialog;

    private DialogUtils() {
        //no-op
    }

    public static void showLoadingDialog(Context context) {
        if (null == context) return;
        if (dialog != null) {
            if (dialog.isShowing()) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dialog = null;
        }
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_progress_dialog);
        dialog.show();
    }

    /*
     *
     * dismiss loading dialog when call API done
     */


    public static void hideLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }


    public static void createAlertDialog(Context context, String title, String message) {
        createAlertDialog(context, title, message, null);
    }

    public static void createAlertDialog(Context context, String title, String message, DialogAlertCallback callback) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setCancelable(false);
        alert.setPositiveButton("Ok", (dialog, which) -> {
            if (null != callback) {
                callback.onClickPositive();
            }
            dialog.dismiss();
        });
        alert.show();
    }


    public interface DialogAlertCallback {
        void onClickPositive();
    }


}
