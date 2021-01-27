package com.core.extensions

import android.Manifest
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import com.core.R
import timber.log.Timber
import java.util.*

fun Context.isNetworkActive(): Boolean {
    val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun Context.isNetworkActiveWithMessage(): Boolean {
    val isNetworkActive = isNetworkActive()
    if (!isNetworkActive) {
        shortToast(R.string.connection_error_message)
    }
    return isNetworkActive
}

fun View.isNetworkActiveWithMessage(): Boolean {
    val isNetworkActive = context.isNetworkActive()
    if (!isNetworkActive) {
        longSnackbar(R.string.connection_error_message)
    }
    return isNetworkActive
}

fun Context.clearNotifications() {
    (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancelAll()
}

fun Context.shortToast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.shortToast(@StringRes resId: Int) {
    shortToast(getString(resId))
}

fun Context.longToast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Context.longToast(@StringRes resId: Int) {
    longToast(getString(resId))
}

fun Context.dpToPx(dp: Int): Int {
    return (dp.toFloat() * resources.displayMetrics.density).toInt()
}

fun Context.spToPx(sp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
}

fun Context.pxToDp(px: Int): Int {
    return (px / resources.displayMetrics.density).toInt()
}

fun Context.getScreenWidth(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

fun Context.dialPhone(countryCode: String?, phoneNumber: String?) {
    if (!countryCode.isNullOrBlank() && !phoneNumber.isNullOrBlank()) {
        dialPhone(String.format(Locale.US, "+%s%s", countryCode, phoneNumber))
    } else {
        Timber.w("Country code or phone number is null +$countryCode-$phoneNumber")
    }
}

fun Context.dialPhone(phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
        startActivity(intent)
    } catch (exception: Exception) {
        Timber.w(exception)
    }
}

fun Context.openUrl(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    } catch (exception: Exception) {
        Timber.w(exception)
    }
}

fun Context.shareText(text: String) {
    try {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    } catch (exception: Exception) {
        Timber.w(exception)
    }
}

fun Context.copyText(label: String, text: String) {
    try {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        // todo clipboard.primaryClip = clip
    } catch (exception: Exception) {
        Timber.w(exception)
    }
}

fun Context.locationPermissionAndGpsEnabled(): Boolean {
    return locationPermissionGranted() && isGpsEnabled()
}

fun Context.locationPermissionGranted(): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.isGpsEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}