package com.ismail.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ismail.todolist.db.TodoItem
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.list_items.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>(){
    private var todoList: List<TodoItem> = ArrayList<TodoItem>()



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_items,
            parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return todoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      val currentItem = todoList[position]
        holder.itemView.tvTitle.text = currentItem.title
    }
 fun setList(todo : List<TodoItem>){
    this.todoList = todo
     notifyDataSetChanged()
 }
}