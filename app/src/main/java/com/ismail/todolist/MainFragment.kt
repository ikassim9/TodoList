package com.ismail.todolist

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismail.todolist.db.TodoItem
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.item_list.*


class MainFragment : Fragment(), AdapterCallBack {
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var checkDone: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val recyclerView = view.recyclerview
        //    checkBoxListener()
        val adapter =
            ListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoViewModel.getAllTodoItems.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
            Log.i("list", "$it")
        })

        view.fabBtn.setOnClickListener() {
            findNavController().navigate(R.id.action_mainFragment_to_detailFragment)
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Create"
            Log.i("fab_btn", "Fab button is pressed")
        }
        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_all, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_items -> {
                todoViewModel.deleteAllItems()
                Toast.makeText(requireContext(), "All items deleted", Toast.LENGTH_SHORT).show()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCheckBoxClick(todoItem: TodoItem, position: Int) {
        if (doneCheckBox.isChecked) {
            todoViewModel.deleteItem(todoItem = todoItem)
            Log.i("check_click", "Check box is clicked")
        }
    }

    override fun onItemClick(todoItem: TodoItem, position: Int) {
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(todoItem)
        findNavController().navigate(action)
        Toast.makeText(requireContext(), "Navigation successful;", Toast.LENGTH_SHORT).show()
        Log.i("cardview_click", "Card view is click")

    }

    override fun onItemLongClick(todoItem: TodoItem, position: Int): Boolean {
      return deleteItem(todoItem, position)

    }


    private fun deleteItem(todoItem: TodoItem , position: Int): Boolean {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Delete Note")
        dialogBuilder.setMessage("Do you want to permanently delete this note?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                todoViewModel.deleteItem(todoItem)
                Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.cancel()
            }.show()
        return true

    }
    }

