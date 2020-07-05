package com.ismail.todolist.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TodoItem(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val id: Int,

    @ColumnInfo(name = "item_title")

    val title: String


) {
}