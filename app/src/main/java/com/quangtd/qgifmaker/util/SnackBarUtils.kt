package com.quangtd.qgifmaker.util

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.androidadvance.topsnackbar.TSnackbar

/**
 * Created by QuangTD
 * on 1/18/2018.
 */
class SnackBarUtils {
    companion object {
        fun showErrorMessage(message: String, view: View) {
            val snackBar = TSnackbar.make(view, message, TSnackbar.LENGTH_LONG)
            snackBar.setActionTextColor(Color.WHITE)
            val snackBarView = snackBar.view
            snackBarView.setBackgroundColor(Color.parseColor("#Ce0e15"))
            val textView = snackBarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.WHITE)
            textView.gravity = Gravity.CENTER
            snackBar.show()
        }

        fun showAlertMessage(message: String, view: View) {
            val snackBar = TSnackbar.make(view, message, TSnackbar.LENGTH_LONG)
            snackBar.setActionTextColor(Color.WHITE)
            val snackBarView = snackBar.view
            snackBarView.setBackgroundColor(Color.BLUE)
            val textView = snackBarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.WHITE)
            textView.gravity = Gravity.CENTER
            snackBar.show()
        }
    }
}