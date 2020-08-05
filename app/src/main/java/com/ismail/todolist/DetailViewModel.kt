package com.ismail.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val selectedDate: MutableLiveData<String> = MutableLiveData()
    private val selectedTime: MutableLiveData<String> = MutableLiveData()
    private val notificationStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val timeDate: MutableLiveData<Calendar> = MutableLiveData()

    val calenderTime: LiveData<Calendar>
        get() = timeDate

    fun displaySelectedTIme(calendarTime: Calendar) {
        timeDate.value = calendarTime
    }

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
