package com.ismail.todolist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(
            "NotificationReceiver",
            "Broadcast received."
        )
        val uniqueID = (System.currentTimeMillis().toInt())
        val alarmManagerHelper = AlarmManagerHelper(context)
        val builder = alarmManagerHelper.retrieveNotificationBuilder().build()
        alarmManagerHelper.getManager().notify(uniqueID, builder)
    }
}