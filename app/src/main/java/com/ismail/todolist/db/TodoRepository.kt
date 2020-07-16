package com.ismail.todolist.db

import androidx.lifecycle.LiveData

class TodoRepository(private val dao: TodoDao) {

    val todoItems : LiveData<List<TodoItem>> = dao.getAllTodoItems()


    suspend fun insert(todoItem: TodoItem) : Long {
        return dao.insertItem(todoItem)
    }

    suspend fun update(todoItem: TodoItem) : Int {
       return dao.updateItem(todoItem)
    }

    suspend fun delete(todoItem: TodoItem) : Int {
       return dao.deleteItem(todoItem)
    }

    suspend fun deleteAll() : Int {
        return dao.deleteAll()
    }
}