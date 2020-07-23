package com.ismail.todolist

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.core.graphics.translationMatrix
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_detail.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment : DialogFragment() {
     var timeFormat = SimpleDateFormat("h:mm a", Locale.US)
     val now = Calendar.getInstance()
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
}
