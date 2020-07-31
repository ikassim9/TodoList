package com.ismail.todolist

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
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
    private var notificationOnOrOff = false
    private var c: Calendar = Calendar.getInstance()
    private var _binding: FragmentDetailBinding? = null
    private val binding
        get() = _binding!!
    private val reminderchannelID = "reminder_channel_id"
    val reminderChannel: String = "reminderChannel"
    private val request_ID = 2
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
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        observeCalenderPicker()
        observeTimePicker()
        calender = binding.tvCalender
        timePicker = binding.tvTimePicker
        calender.setOnClickListener {
            DatePickerFragment().show(childFragmentManager, "Date Picker")
        }
        timePicker.setOnClickListener {
            TimePickerFragment().show(childFragmentManager, "Time Picker")
        }
        binding.btnNotificationStatus.setOnCheckedChangeListener { view, ischeck ->
            updateNotificationStatus(ischeck)
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
                saveTodoItem()
                hideVirtualKeyboard()
                // cancelAlarm()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveTodoItem() {
        if (arguments != null) {
            val name = binding.edtTaskName.text.toString()
            val dueDate = calender.text.toString()
            val dueTime = timePicker.text.toString()
            val notificationStatus = btnNotificationStatus.isChecked
            val item =
                args.item?.id?.let { TodoItem(it, name, dueDate, dueTime, notificationStatus) }
            if (item != null) {
                todoViewModel.updateItem(item)
                Log.i("updated_item", "$item")
                Toast.makeText(requireContext(), "Item is updated", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_detailFragment_to_mainFragment)
            } else {
                val taskName = binding.edtTaskName.text.toString()
                val due = calender.text.toString()
                val time = timePicker.text.toString()
                val statusOfNotification = binding.btnNotificationStatus.isChecked

                if (name.isBlank()) {
                    Toast.makeText(requireContext(), "Empty task field", Toast.LENGTH_SHORT).show()
                    return
                }
                val toDOItem = TodoItem(0, taskName, due, time, statusOfNotification)
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
        observeNotificationStatus()

    }

    private fun getArgs() {
        if (arguments != null) {
            detailArgs = DetailFragmentArgs.fromBundle(requireArguments())
            binding.edtTaskName.setText(detailArgs.item?.title)
            calender.text = detailArgs.item?.dueDate
            timePicker.text = detailArgs.item?.dueTime
            binding.btnNotificationStatus.isChecked = detailArgs.item?.notificationOnOrOff ?: false

            Log.i("binding", "${binding.btnNotificationStatus.isChecked}")
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
        val time = timeFormat.format(cal.timeInMillis)
        detailViewModel.setTimePickerValue(time)

        //  updateNotificationStatus(cal)
    }

    companion object {
        val cal: Calendar = Calendar.getInstance()
    }

    private fun setAlarm(calendar: Calendar) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), request_ID, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
    }

    private fun cancelAlarm() {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), request_ID, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    private fun observeCalenderPicker() {
        detailViewModel.date.observe(viewLifecycleOwner, androidx.lifecycle.Observer { date ->
            calender.text = date
            Log.i("date_picker", "$date")
        })

    }

    private fun observeTimePicker() {
        detailViewModel.time.observe(viewLifecycleOwner, androidx.lifecycle.Observer { time ->
            timePicker.text = time
            Log.i("time_picker", "$time")

        })
    }
    fun observeCalenderText(){
        detailViewModel.time.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

        })
    }

    private fun observeNotificationStatus() {
        detailViewModel.status.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { status: Boolean ->
                binding.btnNotificationStatus.isChecked = status
                Log.i("log_check", "$status")

            })
    }

    private fun updateNotificationStatus(status: Boolean) {
        detailViewModel.setNotificationStatus(status)
        if (status) {
            setAlarm(cal)
            Toast.makeText(
                requireContext(),
                "Alarm is set for ${timeFormat.format(cal.time)}",
                Toast.LENGTH_SHORT
            ).show()
            Log.i("alarm_set", "${timeFormat.format(cal.time)}")
        } else {
            cancelAlarm()
            Toast.makeText(requireContext(), "Alarm is cancel ", Toast.LENGTH_SHORT).show()
            Log.i("alarm_cancel", "$status")

        }

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

    private fun update() {
        val status = binding.btnNotificationStatus.isChecked
        detailViewModel.setNotificationStatus(status)
        if (status) {
            setAlarm(cal)
            Toast.makeText(requireContext(), "Alarm is set for ${timeFormat.format(cal.time)}", Toast.LENGTH_SHORT).show()
            Log.i("alarm_set", "${timeFormat.format(cal.time)}")
        } else {
            cancelAlarm()
            Toast.makeText(requireContext(), "Alarm is cancel ", Toast.LENGTH_SHORT).show()
            Log.i("alarm_cancel", "$status")

        }
    }
}