package com.kdn.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kdn.presentation.R
import com.kdn.presentation.widget.extension.startActivityWithFinish
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            startApp()
        }
    }

    private suspend fun startApp() = this.startActivityWithFinish(this, MainActivity::class.java)

}