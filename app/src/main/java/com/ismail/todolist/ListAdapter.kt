package com.ismail.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ismail.todolist.db.TodoItem
import kotlinx.android.synthetic.main.item_list.view.*


class ListAdapter(
    private val callback: AdapterCallBack

) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var todoList: List<TodoItem> = ArrayList()

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
        holder.itemView.tvTitle.text = currentItem.title
        holder.itemView.dueDate.text = currentItem.dueDate
        holder.bind(currentItem)


    }

    fun setList(todo: List<TodoItem>) {
        this.todoList = todo
        notifyDataSetChanged()

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(todoItem: TodoItem) {
            val position = adapterPosition
            itemView.doneCheckBox.isChecked = false

            itemView.setOnClickListener() {
                callback.onItemClick(todoItem, position)
            }
            itemView.doneCheckBox.setOnClickListener() {
                callback.onCheckBoxClick(todoItem, position)
            }
        }
    }
}



