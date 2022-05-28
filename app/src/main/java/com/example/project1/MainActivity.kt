package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.title = "Blood Sugar Tracker"
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

//        var selectedOption = ""
//
//        when(item.itemId)
//        {
//            R.id.menu_settings -> selectedOption = "Settings"
//        }
//        Toast.makeText(this,"Option : $selectedOption", Toast.LENGTH_SHORT).show()

        var settingsIntent = Intent(this,Settings::class.java)
        startActivity(settingsIntent)

        return super.onOptionsItemSelected(item)
    }
}

