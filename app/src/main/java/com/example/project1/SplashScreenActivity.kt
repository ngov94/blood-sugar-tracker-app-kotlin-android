package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {

    val timer = Timer()
    val SPLASH_TIME_OUT = 1500L

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar!!.title = ""
        supportActionBar!!.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        timer.schedule(SPLASH_TIME_OUT){
            var intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)

            finish()
        }

    }

    override fun onPause() {
        timer.cancel()
        super.onPause()
    }
}