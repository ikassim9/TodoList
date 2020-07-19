package com.ismail.todolist

import com.ismail.todolist.db.TodoItem


interface AdapterCallBack {

    fun onCheckBoxClick(todoItem: TodoItem, position: Int)

    fun onItemClick(todoItem: TodoItem, position: Int)

}
