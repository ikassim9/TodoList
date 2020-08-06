package com.ismail.todolist

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ismail.todolist.databinding.FragmentMainBinding
import com.ismail.todolist.db.TodoItem

class MainFragment : Fragment(), AdapterCallBack {
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var adapter: ListAdapter
    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        setUpViewModel()
        setUpRecyclerView(binding)
        setUpFab(binding)
        return binding.root
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_all, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_items -> {
                deleteAllItemDiolag()
                cancelAlarm()
                Toast.makeText(requireContext(), "All tasks have beeng deleted", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun checkBoxListener(view: View, ischeck: Boolean, todoItem: TodoItem, position: Int) {
        if (ischeck) {
            todoViewModel.deleteItem(todoItem)
            undoCompletedTaskSnackbar(todoItem)
            Log.i("delete_check", "$todoItem is check at position $position")

        }
    }

    override fun onItemClick(todoItem: TodoItem, position: Int) {
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(todoItem)
        findNavController().navigate(action)
        Log.i("cardview_click", "Card view  item $todoItem is click at position $position")
    }

    override fun onItemLongClick(todoItem: TodoItem, position: Int): Boolean {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Delete item?")
        dialogBuilder.setMessage("Do you want to delete this task (${todoItem.title})?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                todoViewModel.deleteItem(todoItem)
                undoDeletedTaskSnackbar(todoItem)
                Log.i("item_deleted", "Deleted at position $position")
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.cancel()
            }.show()
        return true
    }

    private fun setUpViewModel() {
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoViewModel.getAllTodoItems.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
            Log.i("list", "$it")
        })
    }

    private fun setUpRecyclerView(binding: FragmentMainBinding) {
        val recyclerView = binding.recyclerview
        adapter = ListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUpFab(binding: FragmentMainBinding) {
        binding.fabBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_detailFragment)
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Create"
            Log.i("fab_btn", "Fab button is pressed")
        }
    }

    private fun undoCompletedTaskSnackbar(todoItem: TodoItem) {
        val snackbar: Snackbar =
            Snackbar.make(binding.rootLayout, "Task completed", Snackbar.LENGTH_SHORT)
        snackbar.apply {
            setActionTextColor(Color.RED)
            snackbar.setAction("Undo") {
                undoDelete(todoItem)
            }
        }
        snackbar.show()
    }

    private fun undoDelete(todoItem: TodoItem) {
        todoViewModel.insertItem(todoItem)
        Log.i("pos", "$todoItem")


    }

    private fun undoDeletedTaskSnackbar(todoItem: TodoItem) {
        val snackbar: Snackbar =
            Snackbar.make(binding.rootLayout, "Task has been deleted", Snackbar.LENGTH_SHORT)
        snackbar.apply {
            setActionTextColor(Color.RED)
            snackbar.setAction("Undo") {
                undoDelete(todoItem)
            }
        }
        snackbar.show()
    }

    private fun cancelAlarm() {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            1, intent, 0
        )
        alarmManager.cancel(pendingIntent)
        Log.d("alarm_cancel", "alarm canceled!")
    }

    private fun deleteAllItemDiolag() : Boolean {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Delete all tasks?")
        dialogBuilder.setMessage("Do you want to delete all tasks?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                todoViewModel.deleteAllItems()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.cancel()
            }.show()
    return true

}
}