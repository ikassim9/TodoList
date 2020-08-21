package com.ismail.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ismail.todolist.db.TodoItem
import kotlinx.android.synthetic.main.item_list.view.*
import java.util.*
import kotlin.collections.ArrayList


class ListAdapter(
    private val callback: AdapterCallBack

) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>(), Filterable {

    private var todoList: MutableList<TodoItem> = ArrayList()

    // contain user searches
    lateinit var searchTodoList: List<TodoItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_list, parent, false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = todoList[position]
        holder.bind(currentItem)
    }

    fun setList(todoItem: List<TodoItem>) {
        val oldList = todoList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            TodoItemDiffCal(oldList, todoItem)
        )
        todoList = todoItem as MutableList<TodoItem>
        diffResult.dispatchUpdatesTo(this)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            searchTodoList = ArrayList(todoList)
        }

        fun bind(todoItem: TodoItem) {
            val position = adapterPosition
            itemView.doneCheckBox.isChecked = false
            itemView.tvTitle.text = todoItem.title
            itemView.dueDate.text = todoItem.dueDate
            itemView.doneCheckBox.isChecked = false
            itemView.setOnClickListener() {
                callback.onItemClick(todoItem, position)
            }
            itemView.doneCheckBox.setOnCheckedChangeListener { view, isCheck ->
                callback.checkBoxListener(
                    view,
                    isCheck,
                    todoItem,
                    position
                )
            }
            itemView.setOnLongClickListener {
                callback.onItemLongClick(todoItem, position)
            }
        }
    }
// Compares two list and returns optimize result
    class TodoItemDiffCal(
        private var oldList: List<TodoItem>,
        private var newList: List<TodoItem>
    ) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    // Filter search results
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterList: MutableList<TodoItem> = ArrayList()
                if (constraint == null || constraint.isEmpty()) {
                    filterList.addAll(searchTodoList)
                } else {
                    val filterPattern: String = constraint.toString().toLowerCase(Locale.ROOT)
                        .trim()
                    for (item: TodoItem in searchTodoList) {
                        if (item.title.toLowerCase(Locale.ROOT).startsWith(filterPattern)) {
                            filterList.add(item)
                        }
                    }
                }
                val searchResult = FilterResults()
                searchResult.values = filterList
                return searchResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                todoList.clear()
                todoList.addAll((results?.values) as List<TodoItem>)
                notifyDataSetChanged()

            }
        }
    }
}
