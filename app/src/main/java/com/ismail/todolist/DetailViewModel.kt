package com.ismail.todolist

import android.app.Application
import android.text.format.DateUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val selectedDate: MutableLiveData<String> = MutableLiveData()
    private val selectedTime: MutableLiveData<String> = MutableLiveData()
    private val longDateValue: MutableLiveData<Long> = MutableLiveData()

    val dateInMill: LiveData<Long>
        get() = longDateValue



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
    fun setLongDateValue(date: Long) {
        if (DateUtils.isToday(date)) {
            longDateValue.value = 0
        } else {
            longDateValue.value = date
        }

    }

    }