package com.ismail.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ismail.todolist.db.TodoDatabase
import com.ismail.todolist.db.TodoItem
import com.ismail.todolist.db.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    val getAllTodoItems: LiveData<List<TodoItem>>
    private val repository: TodoRepository
    private val selectedDate: MutableLiveData<String> = MutableLiveData()
    private val selectedTime: MutableLiveData<String> = MutableLiveData()
    val date: LiveData<String>
        get() = selectedDate
    val time: LiveData<String>
        get() = selectedTime

    init {
        val todoDao = TodoDatabase.getDatabase(application).todoDao
        repository = TodoRepository(dao = todoDao)
        getAllTodoItems = repository.todoItems
    }

    fun insertItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(todoItem)
        }
    }
    fun updateItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(todoItem)
        }

    }

    fun deleteItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(todoItem)
        }
    }

    fun deleteAllItems() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()

        }
    }

    fun setDateCalenderValue(date: String) {
        selectedDate.value = date
    }

    fun setTimePickerValue(time: String) {
        selectedTime.value = time
    }
}



