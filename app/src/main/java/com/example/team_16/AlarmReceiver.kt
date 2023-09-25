package com.example.team_16

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

const val ALARM_NOTIFICATION_ID = 3

class AlarmReceiver: BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        context.let {

            val contentIntent = Intent(context, StopwatchFragment::class.java)
            val pendingIntent = PendingIntent.getActivity(context,0,contentIntent, PendingIntent.FLAG_IMMUTABLE)

            val notification = NotificationCompat.Builder(context, App.ALERT_CHANNEL_ID)
                .setContentTitle("Alarm Alert")
                .setContentText("공부를 시작한지 1시간이 지났습니다!")
                .setSmallIcon((R.drawable.ic_baseline_notifications_active_24))
                .setContentIntent(pendingIntent)
                .build()

            context.getSystemService(NotificationManager::class.java)
                .notify(ALARM_NOTIFICATION_ID, notification)
        }
    }
}