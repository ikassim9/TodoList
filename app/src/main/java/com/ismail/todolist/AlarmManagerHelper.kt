package com.ismail.todolist

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ismail.todolist.db.TodoItem
import java.text.SimpleDateFormat
import java.util.*

class AlarmManagerHelper(base: Context) : ContextWrapper(base) {
    private lateinit var notificationManager: NotificationManagerCompat
    private var manager: NotificationManager? = null
    val name = "reminder"
    private val descriptionText = "This is a reminder"
    private val importance = NotificationManager.IMPORTANCE_DEFAULT
    private val reminderchannelID = "reminder_channel_id"
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.US)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationChannel =
            NotificationChannel(reminderchannelID, name, importance).apply {
                description = descriptionText
            }
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        getManager().createNotificationChannel(notificationChannel)
    }

     fun getManager(): NotificationManager {
        if (manager == null) {
            manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return manager as NotificationManager
    }
    fun retrieveNotificationBuilder(): NotificationCompat.Builder {
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val remoteView =
            RemoteViews(packageName, R.layout.notification_custom_layout)
        val calender = Calendar.getInstance()
        val time: String = timeFormat.format(calender.time).toString()
        remoteView.setTextViewText(R.id.tv_notification_title, "Reminder for something")
        remoteView.setTextViewText(R.id.tv_content, time)
        val pendingIntent = PendingIntent.getActivity(this, 1, notificationIntent,  0)
        return NotificationCompat.Builder(applicationContext, reminderchannelID)
            .setCustomContentView(remoteView)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

               }
}