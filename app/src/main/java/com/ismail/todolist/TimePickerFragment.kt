package com.ismail.todolist

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.text.DateFormat
import java.util.*

class TimePickerFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calender = Calendar.getInstance()
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireActivity(),
            parentFragment as TimePickerDialog.OnTimeSetListener?,
            hour,
            minute, android.text.format.DateFormat.is24HourFormat(requireContext())
        )    }

    }
