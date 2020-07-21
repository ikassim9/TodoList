package com.ismail.todolist

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_detail.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment : DialogFragment() {
    private lateinit var now: Calendar
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = now.get(Calendar.HOUR_OF_DAY)
        val minute = now.get(Calendar.MINUTE)
        return TimePickerDialog(
            requireActivity(),
            parentFragment as TimePickerDialog.OnTimeSetListener?,
            hour,
            minute, android.text.format.DateFormat.is24HourFormat(requireContext())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        now = Calendar.getInstance()
    }
}
