package com.ismail.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val selectedDate: MutableLiveData<String> = MutableLiveData()
    private val selectedTime: MutableLiveData<String> = MutableLiveData()
    private val notificationStatus: MutableLiveData<Boolean> = MutableLiveData()

     val status: LiveData<Boolean>
        get() = notificationStatus

    private var notificationOn = false

    val date: LiveData<String>
        get() = selectedDate

    val time: LiveData<String>
        get() = selectedTime

    fun setDateCalenderValue(date: String) {
        selectedDate.value = date
    }

    fun setTimePickerValue(time: String) {
        selectedTime.value = time
    }

    fun setNotificationStatus(status : Boolean) {
        notificationStatus.value = status

    }
}
