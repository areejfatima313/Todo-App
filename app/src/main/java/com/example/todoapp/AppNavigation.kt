package com.example.todoapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.viewmodel.AuthViewModel
import com.example.todoapp.viewmodel.TaskViewModel

@Composable
fun AppNavigation() {

    // 🧭 NavController
    val navController = rememberNavController()

    // 🔐 AuthViewModel
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        // ─────────────────────────────────────
        // 0️⃣ SPLASH
        // ─────────────────────────────────────
        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashFinished = {

                    val destination = if (authViewModel.isLoggedIn()) {
                        Routes.MY_TASKS                  //  Logged in → Home
                    } else {
                        Routes.ONBOARDING_1              //  Logged out → Onboarding
                    }
                    navController.navigate(destination) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // ─────────────────────────────────────
        // 0️⃣.1 ONBOARDING 1
        // ─────────────────────────────────────
        composable(Routes.ONBOARDING_1) {
            OnboardingScreen1(
                onNextClick = { navController.navigate(Routes.ONBOARDING_2) }
            )
        }

        // ─────────────────────────────────────
        // 0️⃣.2 ONBOARDING 2
        // ─────────────────────────────────────
        composable(Routes.ONBOARDING_2) {
            OnboardingScreen2(
                onNextClick = { navController.navigate(Routes.ONBOARDING_3) }
            )
        }

        // ─────────────────────────────────────
        // 0️⃣.3 ONBOARDING 3
        // ─────────────────────────────────────
        composable(Routes.ONBOARDING_3) {
            OnboardingScreen3(
                onGetStartedClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ONBOARDING_1) { inclusive = true }
                    }
                }
            )
        }

        // ─────────────────────────────────────
        // 1️⃣ LOGIN
        // ─────────────────────────────────────
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.MY_TASKS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate(Routes.SIGNUP) }
            )
        }

        // ─────────────────────────────────────
        // 2️⃣ SIGNUP
        // ─────────────────────────────────────
        composable(Routes.SIGNUP) {
            SignupScreen(
                viewModel = authViewModel,
                onSignupSuccess = {
                    navController.navigate(Routes.MY_TASKS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onLoginClick = { navController.popBackStack() }
            )
        }

        // ─────────────────────────────────────
        // 3️⃣ MY TASKS
        // ─────────────────────────────────────
        composable(Routes.MY_TASKS) {
            val taskViewModel: TaskViewModel = viewModel()

            MyTasksScreen(
                viewModel = taskViewModel,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MY_TASKS) { inclusive = true }
                    }
                },
                onAddTaskClick = { navController.navigate(Routes.ADD_TASK) },
                onEditTaskClick = { taskId ->
                    navController.navigate(Routes.editTask(taskId))
                }
            )
        }

        // ─────────────────────────────────────
        // 4️⃣ ADD TASK
        // ─────────────────────────────────────
        composable(Routes.ADD_TASK) {
            val taskViewModel: TaskViewModel = viewModel()

            AddTaskScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = { title, date ->
                    taskViewModel.addTask(title, date)
                    navController.popBackStack()
                }
            )
        }

        // ─────────────────────────────────────
        // 5️⃣ EDIT TASK
        // ─────────────────────────────────────
        composable(Routes.EDIT_TASK) { backStackEntry ->
            val taskViewModel: TaskViewModel = viewModel()
            val tasks by taskViewModel.tasks.collectAsStateWithLifecycle()

            val taskId = backStackEntry.arguments
                ?.getString("taskId")
                ?.toIntOrNull() ?: -1

            val task = tasks.find { it.id == taskId }

            if (task != null) {
                EditTaskScreen(
                    taskId = task.id,
                    initialTitle = task.title,
                    initialDate = task.date,
                    onBackClick = { navController.popBackStack() },
                    onUpdateClick = { _, title, date ->
                        taskViewModel.updateTask(task.copy(title = title, date = date))
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}