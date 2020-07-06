package com.ismail.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ismail.todolist.db.TodoItem
import kotlinx.android.synthetic.main.fragment_detail.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class DetailFragment : Fragment() {
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var now: Calendar
    private lateinit var calender: TextView
    private lateinit var timePicker: TextView
    private val dateFormat = SimpleDateFormat("EEEE MMM dd", Locale.US)
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.US)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        now = Calendar.getInstance()
        calender = view.findViewById(R.id.tvCalender)
        timePicker = view.findViewById(R.id.tv_time_picker)
        calender.setOnClickListener() {
            datePickerDialogue()
        }
        timePicker.setOnClickListener() {
            timePickerDialogue()
        }
        return view
    }

    private fun datePickerDialogue() {
        val calender = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                now.set(Calendar.YEAR, year)
                now.set(Calendar.MONTH, month)
                now.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date = dateFormat.format(now.time)
                tvCalender.text = date

                Toast.makeText(requireContext(), "Click", Toast.LENGTH_SHORT).show()
            },
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()

    }

    private fun timePickerDialogue() {
        val timepicker = TimePickerDialog(
            requireActivity(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                now.set(Calendar.HOUR_OF_DAY, hourOfDay)
                now.set(Calendar.MINUTE, minute)
                val time = timeFormat.format(now.time)
                tv_time_picker.text = time

            },

            now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false
        )

        timepicker.show()

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_item, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val title = edtTaskName.text.toString()
        when (item.itemId) {
            R.id.add_item -> {
                saveTodoItem()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveTodoItem() {
        val taskName = edtTaskName.text.toString()
        if (taskName.isEmpty()) {
            Toast.makeText(requireContext(), "Empty task field", Toast.LENGTH_SHORT).show()
            return
        }
        val toDOItem = TodoItem(0, taskName)
        todoViewModel.insertItem(toDOItem)
        Log.i("item_inserted", "Item is inserted -> $toDOItem ")
        Toast.makeText(requireContext(), "Item inserted successfully", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_detailFragment_to_mainFragment)

    }
}
