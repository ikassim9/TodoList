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
    suspend fun insertItem(toDoItem: TodoItem)  : Long

    @Update
    suspend fun updateItem(toDoItem: TodoItem) : Int


    @Delete
    suspend fun deleteItem(toDoItem: TodoItem) : Int

    @Query("Delete from todo_table")
    suspend fun deleteAll() : Int

    @Query("Select * from todo_table")
    fun getAllTodoItems(): LiveData<List<TodoItem>>


}