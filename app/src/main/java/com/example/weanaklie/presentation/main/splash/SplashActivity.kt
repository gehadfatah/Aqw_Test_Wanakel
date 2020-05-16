package com.example.weanaklie.presentation.main.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.android.friendycar.model.common.ApplicationIntegration
import com.android.friendycar.model.common.PREFERENCES_NAME
import com.example.weanaklie.R
import com.example.weanaklie.presentation.main.wakilnieHome.WelcomeActivity


class SplashActivity : AppCompatActivity() {
    private val pref by lazy {
        ApplicationIntegration.getApplication()
            .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
    var timer: Handler? = null
    private var myRunnable: Runnable? = Runnable {
        this@SplashActivity.runOnUiThread {
      navigateActivity(WelcomeActivity::class.java)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hiding title bar of this activity
        window.requestFeature(Window.FEATURE_NO_TITLE)
        //making this activity full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        timer = Handler()
        timer!!.postDelayed(myRunnable, 2000)

    }

    override fun onDestroy() {
        super.onDestroy()
        timer!!.removeCallbacks(myRunnable)
        timer = null
        myRunnable = null
    }

    private fun navigateActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
        finish()

    }
}
