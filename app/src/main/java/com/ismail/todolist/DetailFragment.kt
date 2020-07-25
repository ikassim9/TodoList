package com.ismail.todolist

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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

    // private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var toggle_notifcation: TextView
    private lateinit var spinner: Spinner
    private val now: Calendar = Calendar.getInstance()
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.US)
    private val dateFormat = SimpleDateFormat("EEEE MMM dd", Locale.US)
    private lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    //  private lateinit var builder: Notification


    // private val dateFormat = SimpleDateFormat("EEEE MMM dd", Locale.US)
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var detailArgs: DetailFragmentArgs
    private lateinit var calender: TextView
    private lateinit var timePicker: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoViewModel.date.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            tvCalender.text = it
            Log.i("date_picker", "$it")

        })

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
                // sendNotificationReminder()

                //  secondNotification()

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
                // sendNotificationReminder()

                //sendNotificationReminder(toDOItem)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        //notificationManager = NotificationManagerCompat.from(requireContext())
        spinner = view.findViewById(R.id.notification_toggle) as Spinner
        toggle_notifcation = view.findViewById(R.id.tv_notification_status)
        val notification_type = arrayOf("Yes", "No")
        spinner.adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            notification_type
        )
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                toggle_notifcation.text = notification_type[position]
                val selectedItem: String = notification_type[position].toString()
                // var item = selectedItem.toUpperCase()
                notificationOnOrOff = selectedItem != "OFF"
            }
        }
        todoViewModel.time.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            tv_time_picker.text = it
            Log.i("time_picker", "$it")
        })
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
        todoViewModel.setDateCalenderValue(date)

        Toast.makeText(requireContext(), "Date has been chosen", Toast.LENGTH_SHORT).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //val calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        val time = timeFormat.format(cal.time)
        todoViewModel.setTimePickerValue(time)
        setAlarm(cal)
        Log.i("time", "${cal.time}")
    }
    companion object {
        val cal: Calendar = Calendar.getInstance()
    }
//    private fun sendNotificationReminder(todoItem: TodoItem) {
//        val notificationIntent = Intent(activity, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        notificationIntent.putExtra("key", todoItem.id)
//        val remoteView =
//            RemoteViews(requireContext().packageName, R.layout.notification_custom_layout)
//        val calender = Calendar.getInstance()
//        val time: String = timeFormat.format(calender.time).toString()
//        remoteView.setTextViewText(R.id.tv_notification_title, "Reminder for ${todoItem.title}")
//        remoteView.setTextViewText(R.id.tv_content, time)
//
//        val pendingIntent: PendingIntent =
//            PendingIntent.getActivity(
//                requireContext(),
//                0,
//                notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//        val builder =
//            NotificationCompat.Builder(requireContext(), NotificationHelper.reminderchannelID)
//                .setSmallIcon(R.drawable.ic_notify)
////                .setContent(remoteView)
//                .setCustomContentView(remoteView)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .setCategory(NotificationCompat.CATEGORY_REMINDER)
//                .build()
//
//
//        with(NotificationManagerCompat.from(requireContext())) {
//            notify(1, builder)
//            Log.i("notify", "Notification is send")
//
//        }
//    }


    private fun setAlarm(calendar: Calendar) {
        //val time = DetailFragment.cal.time
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Log.i("alarm", "Alarm is set")


    }

    private fun cancelAlarm() {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(requireContext(), "Alarm is cancel", Toast.LENGTH_SHORT).show()
    }
}

//  private fun secondNotification() {
//        val builder =
//            NotificationCompat.Builder(requireContext(), NotificationHelper.reminderChannel2)
//                .setSmallIcon(R.drawable.ic_notify)
//                .setContentTitle("Second Reminder")
//                .setContentText("Second time, not gonna remind you!")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true)
//                .setGroup(NotificationHelper.notificationGroupID)
//                //.setCategory(NotificationCompat.CATEGORY_ALARM)
//                .build()
//
//        with(NotificationManagerCompat.from(requireContext())) {
//            notify(1, builder)
//
//}