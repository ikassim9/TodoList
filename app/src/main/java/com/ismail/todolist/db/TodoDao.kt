package com.ismail.todolist.db

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Dao

@Dao
interface TodoDao {

    @Insert
    suspend fun insertItem(toDoItem: TodoItem)

    @Update
    suspend fun updateItem(toDoItem: TodoItem)


    @Delete
    suspend fun deleteItem(toDoItem: TodoItem)

    @Query("Delete from todo_table")
    suspend fun deleteAll()

    @Query("Select * from todo_table")
    fun getAllTodoItems(): LiveData<List<TodoItem>>


}