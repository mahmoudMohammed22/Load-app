package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var URL : String

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var nameFile :String
    private lateinit var statusFile:String
    var finsh = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        lateinit var loadingButton: LoadingButton

        var complete = false


        val contentIntent =Intent(this,DetailActivity::class.java)

        pendingIntent =PendingIntent.getActivity(
            this,0,contentIntent,0
        )

        val radioButton :RadioGroup = findViewById(R.id.button_group)
        val radioRetrofit :RadioButton = findViewById(R.id.retrofit_button)
        val radioGlide :RadioButton = findViewById(R.id.Glide_button)
        val radioLoadApp :RadioButton = findViewById(R.id.LoadApp_button)
        fun add(){
            when(radioButton.checkedRadioButtonId){
                radioRetrofit.id ->{
                    URL = "https://github.com/square/retrofit"
                    nameFile = radioRetrofit.text.toString()
                }
                radioGlide.id ->{
                    URL =   "https://github.com/bumptech/glide"
                    nameFile = radioGlide.text.toString()

                }
                radioLoadApp.id-> {
                    URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
                    nameFile = radioLoadApp.text.toString()

                }

            }
        }


        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            add()
            complete = true
            cancelNotfication()
            if(radioGlide.isChecked || radioRetrofit.isChecked || radioLoadApp.isChecked ){
                download()

            }else{
                Toast.makeText(this, "Selected", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if(downloadID == id){
                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
                statusFile = "Success"
            }else {
                statusFile = "Fail"
            }
            createNotfication()

        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE) // Visibility of the download Notification
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// // enqueue puts the download request in the queue.


    }





    fun createNotfication(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,NotificationManager.IMPORTANCE_LOW)
            notificationChannel.lightColor = Color.BLACK
            notificationChannel.description = "Downloud File from internet"
            notificationManager =getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val contentIntent =Intent(baseContext,DetailActivity::class.java)
        contentIntent.putExtra("name",nameFile)
        contentIntent.putExtra("status",statusFile)


        pendingIntent =PendingIntent.getActivity(
            baseContext,0,contentIntent,PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(this.getString(R.string.notification_title))
            .setContentText(this.getString(R.string.notification_description))
            .addAction(R.drawable.ic_assistant_black_24dp,this.getString(R.string.notification_button),pendingIntent)
            .setAutoCancel(true)


        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID,builder.build())
    }

    fun cancelNotfication(){
        notificationManager =getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()

    }

    companion object {

        private const val CHANNEL_ID = "channelId"
        private const val CHANNEL_NAME = "Downloud"
        private const val NOTIFICATION_ID = 1
    }




}
