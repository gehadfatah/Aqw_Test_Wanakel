package com.android.friendycar.presentation.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.friendycar.domain.common.InternalServerErrorException
import com.android.friendycar.domain.common.UnAuthorizedException
import com.android.friendycar.model.common.ApplicationIntegration
import com.android.friendycar.model.common.PREFERENCES_NAME
import com.example.weanaklie.R

import com.google.gson.Gson
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


fun Context.showLongToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Context.showLongToast(msgId: Int) {
    Toast.makeText(this, getString(msgId), Toast.LENGTH_SHORT).show()
}

fun Context.showShortToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
    val pref: SharedPreferences by lazy {
        ApplicationIntegration.getApplication()
            .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
fun Context.showShortToast(msgId: Int) {
    Toast.makeText(this, getString(msgId), Toast.LENGTH_SHORT).show()
}
fun Context.unwrap(): Activity {
    var context = this
    while (context !is Activity && context is ContextWrapper) {
        context = (this as ContextWrapper).baseContext
    }
    return context as Activity
}
fun Context.getDeviceMetrics(): DisplayMetrics {
    val metrics = DisplayMetrics()
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    display.getMetrics(metrics)
    return metrics
}

fun Context.hideKeyboard(view: View?) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
}

fun Fragment.showMessage(error: Throwable) {
    activity?.run {
        showMessage(error)
    }
}

fun Activity.showMessage(error: Throwable) {
    when (error) {
        is InternalServerErrorException -> showShortToast(R.string.error_occurred)
        is TimeoutException, is SocketTimeoutException -> showShortToast(R.string.time_out_message)
        is UnknownHostException, is ConnectException -> showShortToast(R.string.no_connection)
        is UnAuthorizedException -> {
            /* SessionManager(AppController.getContext()).clearLoginSession()
             startActivity(Intent(this, LoginActivity::class.java))
             showShortToast(R.string.session)*/
        }
    }
}


fun Context.getConnectionType(): Int {
    var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    result = 2
                } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    result = 1
                }
            }
        }
    } else {
        cm?.run {
            cm.activeNetworkInfo?.run {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    result = 2
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    result = 1
                }
            }
        }
    }
    return result
}
