package com.ismail.todolist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val alarmManagerHelper = AlarmManagerHelper(context)
        val builder = alarmManagerHelper.retrieveNotificationBuilder().build()
        alarmManagerHelper.getManager().notify(4, builder)
    }
}