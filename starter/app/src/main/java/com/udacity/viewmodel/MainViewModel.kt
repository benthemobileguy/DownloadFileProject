package com.udacity.viewmodel

import android.app.Application
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.udacity.R
import com.udacity.notification.sendNotification

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var downloadID: Long = 0
    var checkedRadioId = MutableLiveData<Int>()
    var downloadCompleted = MutableLiveData<Boolean>()
    var checkedRadioText = ""
    private val context = getApplication<Application>().applicationContext


    fun sendNotification(message: String, status: String, fileName: String) {

        val notificationManager =
            ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.sendNotification(message, status, fileName, context)
    }


    fun download() {

        var path = ""

        when (checkedRadioId.value) {
            R.id.r1 -> {
                path = context.getString(R.string.radio_glide_repo)
                checkedRadioText = context.getString(R.string.radio_glide_text)
            }

            R.id.r2 -> {
                path = context.getString(R.string.radio_app_repo)
                checkedRadioText = context.getString(R.string.radio_app_text)
            }
            R.id.r3 -> {
                path = context.getString(R.string.radio_retrofit_repo)
                checkedRadioText = context.getString(R.string.radio_retrofit_text)
            }
            else -> Toast.makeText(
                context,
                context.getString(R.string.no_option_selected),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (path != "") {
            val request =
                DownloadManager.Request(Uri.parse(path))
                    .setTitle(context.getString(R.string.app_name))
                    .setDescription(context.getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)
                    // Required for api 29 and up
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        "/$context.getString(R.string.app_name)"
                    )

            val downloadManager =
                context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        } else {

            sendNotification(
                context!!.getString(R.string.notification_description2),
                context.getString(R.string.fail),
                checkedRadioText
            )
            downloadCompleted.value = true
        }

        val br = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

                if (id == downloadID) {
                    downloadCompleted.value = true
                    sendNotification(
                        context!!.getString(R.string.notification_description),
                        context.getString(R.string.success),
                        checkedRadioText
                    )
                } else {
                    sendNotification(
                        context!!.getString(R.string.notification_description2),
                        context.getString(R.string.fail),
                        checkedRadioText
                    )
                }
            }

        }
        context.registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

}
