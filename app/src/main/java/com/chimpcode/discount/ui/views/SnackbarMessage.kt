package com.chimpcode.discount.ui.views

import android.app.Activity
import android.support.design.widget.Snackbar

/**
 * Created by anargu on 2/4/18.
 */
fun showSnackbarWithMessage(activity: Activity, message : String) {

    val view = activity.window.decorView

    val sb = Snackbar.make( view, message, Snackbar.LENGTH_LONG)
    sb.show()
}