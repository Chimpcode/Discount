package com.chimpcode.discount.ui.views

import android.app.Activity
import android.app.ProgressDialog

/**
 * Created by anargu on 2/6/18.
 */
class LoadingDialog {

    companion object {
        fun showLoadingDialog(activity: Activity) {

//            DEPRECATED
            val dialog = ProgressDialog.show(activity.applicationContext, "", "Espere unos segundos...", true)
        }
    }
}