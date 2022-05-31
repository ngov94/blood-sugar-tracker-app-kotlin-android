package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {

    val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar!!.title = ""
        supportActionBar!!.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        timer.schedule(3000){
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