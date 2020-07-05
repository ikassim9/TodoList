package com.ismail.todolist.db

import androidx.lifecycle.LiveData

class TodoRepository(private val dao: TodoDao) {

    val todoItems : LiveData<List<TodoItem>> = dao.getAllTodoItems()


    suspend fun insert(todoItem: TodoItem) {
        dao.insertItem(todoItem)
    }

    suspend fun update(todoItem: TodoItem) {
        dao.updateItem(todoItem)
    }

    suspend fun delete(todoItem: TodoItem) {
        dao.deleteItem(todoItem)
    }

    suspend fun deleteAll() {
        return dao.deleteAll()
    }
}