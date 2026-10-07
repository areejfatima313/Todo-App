package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.AppDatabase
import com.example.todoapp.data.TaskRepository
import com.example.todoapp.data.entity.Task
import com.example.todoapp.session.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = TaskRepository(AppDatabase.getDatabase(app).taskDao())
    private val session = SessionManager(app)

    private val userId: Int = session.getUserId()

    val tasks: StateFlow<List<Task>> = repo.getTasks(userId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(title: String, date: String) {
        viewModelScope.launch {
            repo.addTask(Task(userId = userId, title = title, date = date))
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repo.updateTask(task)
        }
    }

    fun toggleDone(task: Task) {
        viewModelScope.launch {
            repo.updateTask(task.copy(isDone = !task.isDone))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repo.deleteTask(task)
        }
    }

    fun currentUserId(): Int = userId
    fun userName(): String = session.getUserName()
}