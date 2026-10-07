package com.example.todoapp.data

import com.example.todoapp.data.dao.TaskDao
import com.example.todoapp.data.entity.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getTasks(userId: Int): Flow<List<Task>> = taskDao.getTasksForUser(userId)

    suspend fun addTask(task: Task) = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    suspend fun getTask(id: Int): Task? = taskDao.getTaskById(id)
}