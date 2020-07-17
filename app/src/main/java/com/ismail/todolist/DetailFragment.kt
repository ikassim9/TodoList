package com.ismail.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.ismail.todolist.db.TodoItem
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.item_list.*
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {
    private val args by navArgs<DetailFragmentArgs>()
    private var notificationOnOrOff = false
    private var c: Calendar = Calendar.getInstance()
    private lateinit var toggle_notifcation: TextView
    private lateinit var spinner: Spinner
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var detailArgs: DetailFragmentArgs
    private lateinit var now: Calendar
    private lateinit var calender: TextView
    private lateinit var timePicker: TextView
    private val dateFormat = SimpleDateFormat("EEEE MMM dd", Locale.US)
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.US)
    private var hasArgument: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        now = Calendar.getInstance()
        calender = view.findViewById(R.id.tvCalender)
        timePicker = view.findViewById(R.id.tv_time_picker)
        calender.setOnClickListener {
            datePickerDialogue()
        }
        timePicker.setOnClickListener {
            timePickerDialogue()
        }

        return view
    }

    private fun datePickerDialogue() {
        val calender = Calendar.getInstance()
        var datePickerDialog = DatePickerDialog(
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
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()

    }

    private fun timePickerDialogue() {
        val timepicker = TimePickerDialog(
            requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                now.set(Calendar.HOUR_OF_DAY, hourOfDay)
                now.set(Calendar.MINUTE, minute)
                val time = timeFormat.format(now.time)
                time
                if (now.timeInMillis > c.timeInMillis) {
                    tv_time_picker.text = time

                }

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


            } else {
                val taskName = edtTaskName.text.toString()
                val due = tvCalender.text.toString()
                val time = tv_time_picker.text.toString()

                if (taskName.isBlank()) {
                    Toast.makeText(requireContext(), "Empty task field", Toast.LENGTH_SHORT).show()
                    return
                }
                val toDOItem = TodoItem(0, taskName, dueDate, time)
                todoViewModel.insertItem(toDOItem)
                Log.i("item_inserted", "Item is inserted -> $toDOItem ")
                findNavController().navigate(R.id.action_detailFragment_to_mainFragment)
                Toast.makeText(requireContext(), "Item inserted successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
//        view.edtTaskName.setText(args.item?.title)
//        view.tvCalender.text = args.item?.dueDate
        spinner = view.findViewById(R.id.notification_toggle) as Spinner
        toggle_notifcation = view.findViewById(R.id.tv_notification_status)
        val notification_type = arrayOf("Off", "On")
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
                var selectedItem: String = notification_type[position].toString()
                var item = selectedItem.toUpperCase()
                notificationOnOrOff = selectedItem != "OFF"
            }
        }

    }

    private fun getArgs() {
        if (arguments != null) {
            detailArgs = DetailFragmentArgs.fromBundle(requireArguments())
            view?.edtTaskName?.setText(detailArgs.item?.title)
            view?.tvCalender?.text = detailArgs.item?.dueDate
            view?.tv_time_picker?.text = detailArgs.item?.dueTime
            hasArgument = true


        }
    }
}
