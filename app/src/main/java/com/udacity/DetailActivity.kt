package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    companion object {
        const val LETTER = "letter"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val fileName :TextView = findViewById(R.id.file_dowloud)
        val status :TextView = findViewById(R.id.status_dowloud)

        val letterId = intent?.extras?.getString("name").toString()
        val statusFile = intent?.extras?.getString("status").toString()
        Log.d("data",letterId)
        fileName.text = letterId
        status.text = statusFile


        val backButton :FloatingActionButton = findViewById(R.id.fab)

        backButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            this.startActivity(intent)

        }





        val notificationManager =getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()


    }

}
