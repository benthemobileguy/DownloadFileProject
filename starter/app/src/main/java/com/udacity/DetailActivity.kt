package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var activityDetailBinding: ActivityDetailBinding

    companion object {
        const val EXTRA_DETAIL_STATUS = "detailStatus"
        const val EXTRA_DETAIL_FILENAME = "fileNameDetails"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        setSupportActionBar(activityDetailBinding.toolbar)


        // initialize the notification manager
        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelAll()

        activityDetailBinding.contentDetail.fileName.text = intent.getStringExtra(EXTRA_DETAIL_FILENAME)
        activityDetailBinding.contentDetail.status.text = intent.getStringExtra(EXTRA_DETAIL_STATUS)


        activityDetailBinding.contentDetail.okButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }



}

