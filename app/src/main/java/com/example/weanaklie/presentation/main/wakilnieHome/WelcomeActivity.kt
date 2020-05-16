package com.example.weanaklie.presentation.main.wakilnieHome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weanaklie.R
import com.example.weanaklie.presentation.common.setWindowFlag

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        setWindowFlag()

    }
}
