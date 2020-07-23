package com.ismail.todolist

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_detail.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class DatePickerFragment : DialogFragment() {
    private lateinit var calender: Calendar
    private val dateFormat = SimpleDateFormat("EEEE MMM dd", Locale.US)


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calender = Calendar.getInstance()
        // val calender = DetailFragment.now
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        val datepickerDiolog = DatePickerDialog(
            requireActivity(),
            parentFragment as OnDateSetListener?,
            year,
            month,
            day
        )
        datepickerDiolog.datePicker.minDate = System.currentTimeMillis() - 1000
        return datepickerDiolog
    }

}
