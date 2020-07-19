package com.ismail.todolist

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*


class DatePickerFragment : DialogFragment(){
   // private lateinit var calendar: Calendar
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calender = Calendar.getInstance()
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