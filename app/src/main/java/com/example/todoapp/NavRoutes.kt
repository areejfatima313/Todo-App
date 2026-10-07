package com.example.todoapp

object Routes {
    // Splash & Onboarding
    const val SPLASH = "splash"
    const val ONBOARDING_1 = "onboarding_1"
    const val ONBOARDING_2 = "onboarding_2"
    const val ONBOARDING_3 = "onboarding_3"

    // Auth
    const val LOGIN = "login"
    const val SIGNUP = "signup"

    // Main app
    const val MY_TASKS = "my_tasks"
    const val ADD_TASK = "add_task"

    // Edit task with argument
    const val EDIT_TASK = "edit_task/{taskId}"
    fun editTask(taskId: Int) = "edit_task/$taskId"
}