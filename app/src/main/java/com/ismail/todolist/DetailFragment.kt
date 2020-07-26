package com.ismail.todolist

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ismail.todolist.db.TodoItem
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.view.*
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {
    private val args by navArgs<DetailFragmentArgs>()
    private var notificationOnOrOff = false
    private var c: Calendar = Calendar.getInstance()
    private val reminderchannelID = "reminder_channel_id"
    val reminderChannel: String = "reminderChannel"
    val request_ID = 2
    private lateinit var toggle_notifcation: TextView
    private lateinit var spinner: Spinner
    private val now: Calendar = Calendar.getInstance()
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.US)
    private val dateFormat = SimpleDateFormat("EEEE MMM dd", Locale.US)
    private lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel

    // private val dateFormat = SimpleDateFormat("EEEE MMM dd", Locale.US)
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var detailArgs: DetailFragmentArgs
    private lateinit var calender: TextView
    private lateinit var timePicker: TextView
    private lateinit var detailViewModel: DetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        observeCalenderPicker()
        observeTimePicker()
        calender = view.findViewById(R.id.tvCalender)
        timePicker = view.findViewById(R.id.tv_time_picker)
        calender.setOnClickListener {
            DatePickerFragment().show(childFragmentManager, "Date Picker")
        }
        timePicker.setOnClickListener {
            TimePickerFragment().show(childFragmentManager, "Time Picker")
        }

        return view
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
                saveTodoItem()
                // cancelAlarm()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveTodoItem() {
        if (arguments != null) {
            val name = edtTaskName.text.toString()
            val dueDate = tvCalender.text.toString()
            val dueTime = tv_time_picker.text.toString()
            val item = args.item?.id?.let { TodoItem(it, name, dueDate, dueTime) }
            if (item != null) {
                todoViewModel.updateItem(item)
                Log.i("updated_item", "$item")
                Toast.makeText(requireContext(), "Item is updated", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_detailFragment_to_mainFragment)
            } else {
                val taskName = edtTaskName.text.toString()
                val due = tvCalender.text.toString()
                val time = tv_time_picker.text.toString()

                if (taskName.isBlank()) {
                    Toast.makeText(requireContext(), "Empty task field", Toast.LENGTH_SHORT).show()
                    return
                }
                val toDOItem = TodoItem(0, taskName, due, time)
                todoViewModel.insertItem(toDOItem)
                Log.i("item_inserted", "Item is inserted -> $toDOItem ")
                findNavController().navigate(R.id.action_detailFragment_to_mainFragment)
                Toast.makeText(requireContext(), "Task added successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        toggle_notifcation = view.findViewById(R.id.tv_notification_status)
        btnNotificationStatus.setOnClickListener() {
          updateNotificationStatus()
        }
    }

    private fun getArgs() {
        if (arguments != null) {
            detailArgs = DetailFragmentArgs.fromBundle(requireArguments())
            view?.edtTaskName?.setText(detailArgs.item?.title)
            view?.tvCalender?.text = detailArgs.item?.dueDate
            view?.tv_time_picker?.text = detailArgs.item?.dueTime
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calender = Calendar.getInstance()
        calender.set(Calendar.YEAR, year)
        calender.set(Calendar.MONTH, month)
        calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val date: String = dateFormat.format(calender.time)
        Log.i("date", "${calender.time}")
        // val selectedDate = dateFormat.parse(tvCalender.text.toString())
        detailViewModel.setDateCalenderValue(date)
        Toast.makeText(requireContext(), "Date has been chosen", Toast.LENGTH_SHORT).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //val calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        val time = timeFormat.format(cal.time)
        detailViewModel.setTimePickerValue(time)
        setAlarm(cal)
        Log.i("time", "${cal.time}")
    }

    companion object {
        val cal: Calendar = Calendar.getInstance()
    }

    private fun setAlarm(calendar: Calendar) {
        //val time = DetailFragment.cal.time
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), request_ID, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Log.i("alarm", "Alarm is set")
    }

    private fun cancelAlarm() {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), request_ID, intent, 0)
        alarmManager.cancel(pendingIntent)
        Log.i("alarm_cancel", "Alarm is cancel")
        Toast.makeText(requireContext(), "Alarm is cancel", Toast.LENGTH_SHORT).show()
    }
    
    private fun observeCalenderPicker() {
        detailViewModel.date.observe(viewLifecycleOwner, androidx.lifecycle.Observer { date ->
            tvCalender.text = date
            Log.i("date_picker", "$date")
        })

    }

    private fun observeTimePicker() {
        detailViewModel.time.observe(viewLifecycleOwner, androidx.lifecycle.Observer { time ->
            tv_time_picker.text = time
            Log.i("time_picker", "$time")


        })
    }

    private fun observeNotificationStatus() {
        detailViewModel.status.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            btnNotificationStatus.isChecked = it
        })
    }

    private fun updateNotificationStatus() {
        detailViewModel.setNotificationStatus(btnNotificationStatus.isChecked)


    }
}