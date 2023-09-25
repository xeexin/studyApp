package com.example.team_16

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Build.*

class App :Application() {
    companion object{
        const val ALERT_CHANNEL_ID = "com.example.myalertnotification"
        const val ALARM_CHANNEL_ID = "com.example.myalarmservice"
    }

    override fun onCreate() {
        super.onCreate()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            getSystemService(NotificationManager::class.java).run{

                val alertChannel = NotificationChannel(
                    ALERT_CHANNEL_ID,
                    "Alert Test",
                    NotificationManager.IMPORTANCE_HIGH
                )
                createNotificationChannel(alertChannel)


                val alarmChannel = NotificationChannel(
                    ALARM_CHANNEL_ID,
                    "Tests",
                    NotificationManager.IMPORTANCE_LOW
                )
                createNotificationChannel(alarmChannel)


            }
            }
        }
    }

