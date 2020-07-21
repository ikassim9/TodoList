package com.ismail.todolist

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_detail.*
import java.util.*


class DatePickerFragment : DialogFragment() {
    val dateFormat = DetailFragment.dateFormat
    val now = DetailFragment.now
    private lateinit var calendar: Calendar
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val calender = DetailFragment.now
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



