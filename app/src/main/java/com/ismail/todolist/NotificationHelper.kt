package com.ismail.todolist

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationManagerCompat

class NotificationHelper : Application() {
    private lateinit var notificationManager: NotificationManagerCompat

    companion object {
        const val reminderchannelID = "reminder_channel_id"
        const val reminderChannel2: String = "reminderChannel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "reminder"
            val descriptionText = "This is a reminder"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel =
                NotificationChannel(reminderchannelID, name, importance).apply {
                    description = descriptionText
                }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            //  val notificationManager : NotificationManager = getSystemService(NotificationManager::class.java)


            val notificationChannel2 = NotificationChannel(
                reminderChannel2,
                "Hey!",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminder ok!"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel2)

            notificationManager.createNotificationChannel(notificationChannel)


        }

    }

}