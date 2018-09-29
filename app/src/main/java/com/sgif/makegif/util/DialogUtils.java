package com.sgif.makegif.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

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
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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


    public static void createMultiChoiceDialog(Context context, String title, String[] arrs, boolean[] bools, DialogAlertCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMultiChoiceItems(arrs, bools, (dialog, which, isChecked) -> bools[which] = isChecked);
        builder.setPositiveButton("yes", (dialog, which) -> callback.onClickPositive());
        builder.show();
    }

    public static void createSingleDialog(Context context, String title, String[] arrs, int positionChosen, DialogInterface.OnClickListener listener, DialogAlertCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setSingleChoiceItems(arrs, positionChosen, listener);
        builder.setPositiveButton("yes", (dialog, which) -> callback.onClickPositive());
        builder.show();
    }

    public static AlertDialog.Builder createSingleDialogBuilder(Context context, String title, String[] arrs, int positionChosen, DialogInterface.OnClickListener listener, DialogAlertCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setSingleChoiceItems(arrs, positionChosen, listener);
        builder.setTitle(title);
        builder.setPositiveButton("yes", (dialog, which) -> callback.onClickPositive());
        return builder;
    }

    public static AlertDialog.Builder createSingleDialogBuilder(Context context, String title, String[] arrs, int positionChosen, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setSingleChoiceItems(arrs, positionChosen, listener);
        builder.setTitle(title);
        return builder;
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
