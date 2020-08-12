package com.ismail.todolist

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ismail.todolist.databinding.FragmentDetailBinding
import com.ismail.todolist.db.TodoItem
import kotlinx.android.synthetic.main.fragment_detail.*
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {
    private val args by navArgs<DetailFragmentArgs>()
    private var _binding: FragmentDetailBinding? = null
    private val binding
        get() = _binding!!
    private val now: Calendar = Calendar.getInstance()
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.US)
    private var dateFormatter = SimpleDateFormat("EEE MMM dd, yyyy", Locale.US)
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var detailArgs: DetailFragmentArgs
    private lateinit var textViewCalender: TextView
    private lateinit var timePicker: TextView
    private lateinit var detailViewModel: DetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        observeCalenderPicker()
        observeTimePicker()
        textViewCalender = binding.tvCalender
        timePicker = binding.tvTimePicker
        binding.dateAndTimeSelector.setOnClickListener{
            showDialog()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_item, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val title = edtTaskName.text.toString()
        when (item.itemId) {
            R.id.add_item -> {
                createOrUpdateTodoItem()
                hideVirtualKeyboard()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createOrUpdateTodoItem() {
        // Update todoItem
        if (arguments != null) {
            val name = binding.edtTaskName.text.toString()
            val dueDate = textViewCalender.text.toString()
            val dueTime = timePicker.text.toString()
            val item =
                args.item?.id?.let { TodoItem(it, name, dueDate, dueTime) }
            if (item != null) {
                todoViewModel.updateItem(item)
                Log.i("updated_item", "$item")
                Toast.makeText(requireContext(), "Item is updated", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_detailFragment_to_mainFragment)
            } else {
                // create new todoItem
                val taskName = binding.edtTaskName.text.toString()
                val due = textViewCalender.text.toString()
                val time = timePicker.text.toString()

                if (name.isBlank()) {
                    Toast.makeText(requireContext(), "Empty task field", Toast.LENGTH_SHORT).show()
                    return
                }
                val toDOItem = TodoItem(0, taskName, due, time)
                todoViewModel.insertItem(toDOItem)
                Log.i("item_inserted", "Item is inserted $toDOItem ")
                findNavController().navigate(R.id.action_detailFragment_to_mainFragment)
                Toast.makeText(requireContext(), "Task added successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()

    }

    private fun getArgs() {
        if (arguments != null) {
            detailArgs = DetailFragmentArgs.fromBundle(requireArguments())
            binding.edtTaskName.setText(detailArgs.item?.title)
            textViewCalender.text = detailArgs.item?.dueDate
            timePicker.text = detailArgs.item?.dueTime


        }
    }

    private fun setAlarm(calendar: Calendar) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
//        val longBundle : Long = Bundle().getLong(longBundle)
        var timeInMill = calendar.timeInMillis
        var lonDate: Long = 0
        /// observes date changed(long) and updates it to chosen value.
        detailViewModel.dateInMill.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            lonDate = it
            Log.d("long_date", "$it")
        })
        timeInMill += lonDate
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 1, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMill, pendingIntent)
        Log.i("cal_alrm", "${calendar.timeInMillis}")
//        Toast.makeText(requireContext(), "Alarm: ${calendar.time}",Toast.LENGTH_SHORT).show()
    }

    private fun cancelAlarm() {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 1, intent, 0)
        alarmManager.cancel(pendingIntent)
        Log.d("alarm_cancel", "alarm canceled!")

    }


    private fun observeCalenderPicker() {
        detailViewModel.date.observe(viewLifecycleOwner, androidx.lifecycle.Observer { date ->
            textViewCalender.text = date
            Log.i("date_picker", "$date")
        })

    }

    private fun observeTimePicker() {
        detailViewModel.time.observe(viewLifecycleOwner, androidx.lifecycle.Observer { time ->
            timePicker.text = time
            Log.i("time_picker", "$time")

        })

    }

    private fun hideVirtualKeyboard() {
        try {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun showDialog() {
        val calender: Calendar = Calendar.getInstance()
        try {
            if (tvCalender.text != null) {
                val date: Date? = dateFormatter.parse(tvCalender.text.toString())
                Log.d("parse", "$date")
                calender.time = date ?: now.time

                Log.d("cal", "${calender.time}")

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //   val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        Log.d("month", "$month")
        Log.d("year", "$year")
        Log.d("day", "$day")


        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()


    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val date = dateFormatter.format(calendar.time)
        detailViewModel.setLongDateValue(calendar.timeInMillis)
        Log.d("longBundle", "${calendar.timeInMillis}")
        Log.d("calender", "${calendar.timeInMillis}")
        detailViewModel.setDateCalenderValue(date)

        showTimePicker()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        try {
            if (timePicker.text != null) {
                val time = timeFormat.parse(timePicker.text.toString())
                calendar.time = time ?: now.time
                Log.i("dialogCalender", "${calendar.time}")

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialogue = TimePickerDialog(
            requireContext(),
            this,
            hour,
            minute,
            android.text.format.DateFormat.is24HourFormat(requireContext())
        )
        timePickerDialogue.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        val time: String = timeFormat.format(calendar.time)
        detailViewModel.setTimePickerValue(time)
        setAlarm(calendar)
        Log.i("calender_alarm", "alarm is set: ${calendar.time}")
        Log.i("time", "$time")

    }

}

