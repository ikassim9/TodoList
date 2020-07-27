package com.ismail.todolist

import android.view.View
import com.ismail.todolist.db.TodoItem


interface AdapterCallBack {

  //  fun onCheckBoxClick(position: Int)

    fun checkBoxListener(view : View, ischeck : Boolean, todoItem: TodoItem, position: Int)

    fun onItemClick(todoItem: TodoItem, position: Int)

    fun onItemLongClick(todoItem: TodoItem, position: Int) : Boolean

}
