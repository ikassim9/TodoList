package com.ismail.todolist

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_detail.*
import org.w3c.dom.Text
import java.text.DateFormat
import java.util.*

class DetailFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var calender : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
         calender = view.findViewById(R.id.tvCalender)
        calender.setOnClickListener() {
            datePickerDialogue()
        }
        return view
    }

    private fun datePickerDialogue() {
        val datePickerDialog = DatePickerDialog(
            requireContext(), this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val currentTime = Calendar.getInstance().time
        val calender = Calendar.getInstance()
        calender.set(Calendar.YEAR, year)
        calender.set(Calendar.MONTH, month)
        calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val currentData = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime)

        tvCalender.text = currentData

    }
}
