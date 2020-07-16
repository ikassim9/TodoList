package com.ismail.todolist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ismail.todolist.db.TodoItem
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.android.synthetic.main.list_items.view.dueDate
import kotlinx.android.synthetic.main.list_items.view.tvTitle

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var todoList: List<TodoItem> = ArrayList()
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list,
                parent, false
            )
        )


    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = todoList[position]
        holder.itemView.tvTitle.text = currentItem.title
        holder.itemView.dueDate.text = currentItem.dueDate
        holder.itemView.rowLayout.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
            Log.i("cardview", "Cardview is pressed")

        }
    }

    fun setList(todo: List<TodoItem>) {
        this.todoList = todo
        notifyDataSetChanged()
    }




        }


//    fun bind(
//        noteItem: NoteItem,
//        longClickListener: (NoteItem, Int) -> Boolean,
//        clickListener: (NoteItem, Int) -> Unit
//    ) {
//        val position = adapterPosition
//        itemView.title_text_view.text = noteItem.title
//        itemView.setOnLongClickListener() {
//            longClickListener(noteItem, position)
//        }
//        itemView.setOnClickListener() {
//            clickListener(noteItem, position)
