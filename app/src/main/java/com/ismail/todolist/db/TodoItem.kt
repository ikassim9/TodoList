package com.ismail.todolist.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "todo_table")
data class TodoItem(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val id: Int,

    @ColumnInfo(name = "item_title")
    val title: String,

    @ColumnInfo(name = "item_dueDate")
    val dueDate  : String,

    @ColumnInfo(name = "item_dueTime")
    val dueTime : String








) : Parcelable