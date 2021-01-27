package com.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.core.R
import permissions.dispatcher.PermissionRequest

object PermissionUtils {
    fun showRationalDialog(
        context: Context, @StringRes messageResId: Int,
        request: PermissionRequest
    ) {
        AlertDialog.Builder(context)
            .setPositiveButton(R.string.permission_btn_allow) { _, _ -> request.proceed() }
            .setNegativeButton(R.string.permission_btn_deny) { _, _ -> request.cancel() }
            .setCancelable(false)
            .setMessage(messageResId)
            .show()
    }

    fun showAppSettingsDialog(
        activity: AppCompatActivity, @StringRes messageResId: Int,
        requestCode: Int
    ) {
        AlertDialog.Builder(activity)
            .setPositiveButton(R.string.permission_label_settings) { _, _ ->
                activity.startActivityForResult(
                    getAppSettingsIntent(
                        activity
                    ), requestCode
                )
            }
            .setNegativeButton(R.string.permission_btn_cancel) { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .setMessage(messageResId)
            .show()
    }

    fun showAppSettingsDialog(fragment: Fragment, @StringRes messageResId: Int, requestCode: Int) {
        val context = fragment.activity
        context ?: return

        AlertDialog.Builder(context)
            .setPositiveButton(R.string.permission_label_settings) { _, _ ->
                fragment.startActivityForResult(
                    getAppSettingsIntent(
                        context
                    ), requestCode
                )
            }
            .setNegativeButton(R.string.permission_btn_cancel) { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .setMessage(messageResId)
            .show()
    }

    private fun getAppSettingsIntent(context: Context): Intent {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", context.packageName, null)
        return intent
    }
}