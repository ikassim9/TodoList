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
import com.ismail.todolist.databinding.FragmentMainBinding
import com.ismail.todolist.db.TodoItem
import kotlinx.android.synthetic.main.item_list.*

class MainFragment : Fragment() , AdapterCallBack {
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

    override fun checkBoxListener(view: View, ischeck: Boolean, todoItem: TodoItem, position: Int) {
       if(ischeck) {
           todoViewModel.deleteItem(todoItem)
           Toast.makeText(requireContext(), "Task name ${todoItem.title} is completed", Toast.LENGTH_SHORT).show()
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
        dialogBuilder.setMessage("Do you want to delete all your item?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                todoViewModel.deleteItem(todoItem)
                Toast.makeText(
                    requireContext(),
                    "Item has been deleted at position $position",
                    Toast.LENGTH_SHORT
                ).show()
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
            adapter.setList(it as ArrayList<TodoItem>)
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

}
