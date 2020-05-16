package com.android.friendycar.core.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import com.android.friendycar.model.common.ApplicationIntegration
import com.example.weanaklie.core.utils.LocaleManager


class WeakilnieApplication : Application(), LifecycleObserver {
    var appStatus = false
    var localeManager: LocaleManager? = null

    override fun onCreate() {
        super.onCreate()

        ApplicationIntegration.with(this)

    }


    companion object {

        private var instance: WeakilnieApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun appStatusFun(): Boolean {
            return instance!!.appStatus
        }
    }


    override fun attachBaseContext(base: Context?) {
        localeManager = LocaleManager(base)
        super.attachBaseContext(localeManager?.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager?.setLocale(this)

    }


}