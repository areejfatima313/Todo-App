# 📝 Todo App

A modern Android To-Do application built with **Kotlin** and **Jetpack Compose**, designed to help users organize daily tasks, track progress, and manage their productivity with a clean and simple interface.

## ✨ Features

* 🔐 **User Registration & Login**

  * Create a new account with name, email, and password
  * Login using registered credentials
  * Local user session management

* 🎯 **Task Management**

  * Add new tasks
  * Edit existing tasks
  * Delete tasks
  * Mark tasks as completed or pending
  * Tasks are associated with the logged-in user

* 🔎 **Task Filtering**

  * View all tasks
  * View pending tasks
  * View completed tasks

* 📅 **Date Selection**

  * Select a task date using a date picker
  * New tasks default to today's date

* 🚀 **Onboarding Experience**

  * Splash screen
  * Three onboarding screens
  * Simple introduction to the application before login

* 💾 **Local Data Storage**

  * Uses **Room Database** for persistent local storage
  * User and task data remain available locally

* 🎨 **Modern UI**

  * Built with Jetpack Compose
  * Material 3 components
  * Clean pink-themed interface
  * Responsive Compose-based screens

## 🛠️ Tech Stack

| Technology             | Usage                        |
| ---------------------- | ---------------------------- |
| **Kotlin**             | Primary programming language |
| **Jetpack Compose**    | UI development               |
| **Material 3**         | Modern Android UI components |
| **Room Database**      | Local data persistence       |
| **Navigation Compose** | Screen navigation            |
| **ViewModel**          | UI state and business logic  |
| **KSP**                | Room code generation         |
| **Lottie Compose**     | Splash screen animation      |
| **SharedPreferences**  | Local session management     |
| **Gradle Kotlin DSL**  | Project configuration        |

## 🏗️ Architecture

The application follows a layered architecture that separates the UI, business logic, and database operations.

```text
Jetpack Compose UI
       ↓
   ViewModel
       ↓
   Repository
       ↓
      DAO
       ↓
 Room Database
       ↓
 Local SQLite Storage
```

### Authentication Flow

```text
Login / Sign Up Screen
        ↓
   AuthViewModel
        ↓
   UserRepository
        ↓
      UserDao
        ↓
   Room Database
```

### Task Flow

```text
My Tasks Screen
       ↓
  TaskViewModel
       ↓
  TaskRepository
       ↓
    TaskDao
       ↓
 Room Database
```

## 📱 Application Flow

```text
Splash Screen
      ↓
Onboarding
      ↓
Login / Sign Up
      ↓
My Tasks
   ↙   ↓   ↘
Add   Edit  Delete
Task  Task  Task
      ↓
Completed / Pending
```

## 🗂️ Project Structure

```text
app/
└── src/
    └── main/
        ├── java/com/example/todoapp/
        │
        ├── AddTaskScreen.kt
        ├── EditTaskScreen.kt
        ├── HomeScreen.kt
        ├── LoginScreen.kt
        ├── SignUpScreen.kt
        ├── SplashScreen.kt
        │
        ├── OnboardingScreen1.kt
        ├── OnboardingScreen2.kt
        ├── OnboardingScreen3.kt
        │
        ├── MainActivity.kt
        ├── AppNavigation.kt
        └── NavRoutes.kt
        │
        ├── data/
        │   ├── AppDatabase.kt
        │   ├── TaskRepository.kt
        │   ├── UserRepository.kt
        │   │
        │   ├── dao/
        │   │   ├── TaskDao.kt
        │   │   └── UserDao.kt
        │   │
        │   └── entity/
        │       ├── Task.kt
        │       └── User.kt
        │
        ├── security/
        │   └── PasswordHasher.kt
        │
        ├── session/
        │   └── SessionManager.kt
        │
        ├── viewmodel/
        │   ├── AuthViewModel.kt
        │   └── TaskViewModel.kt
        │
        └── ui/theme/
            ├── Color.kt
            ├── Theme.kt
            └── Type.kt
```

## 🔑 Key Components

### Room Database

The application uses Room to store:

* User accounts
* Task information
* User-task relationships

Tasks are linked to their respective users, allowing each logged-in user to access their own task list.

### ViewModels

`AuthViewModel` manages:

* Registration
* Login
* Logout
* Authentication UI states

`TaskViewModel` manages:

* Adding tasks
* Updating tasks
* Deleting tasks
* Completing/pending tasks
* Loading the current user's tasks

### Session Management

The application uses `SharedPreferences` to maintain the local login session, including the current user's ID, name, and email.

## 🔐 Security

User passwords are not stored directly. The application generates a salt and stores a hashed password.

> **Note:** This project uses a simple SHA-256 based hashing implementation for learning/project purposes. For a production application, a password-hashing algorithm specifically designed for password storage, such as Argon2, bcrypt, or PBKDF2, would be preferable.

## 📋 Requirements

* Android Studio
* Android SDK
* JDK 11
* Android device or emulator
* Minimum SDK: **24**
* Target SDK: **36**

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/areejfatima313/Todo-App.git
```

### 2. Open the Project

Open the cloned project in **Android Studio**.

### 3. Sync Gradle

Allow Android Studio to sync the Gradle dependencies.

### 4. Run the Application

Connect an Android device or start an emulator, then run the application from Android Studio.

## 📸 Screens & User Experience

The application includes:

* Splash screen
* Three onboarding screens
* Login screen
* Sign-up screen
* My Tasks screen
* Add Task screen
* Edit Task screen
* Task filtering
* Logout confirmation

## 🎓 Project Purpose

This project was developed to practice modern Android application development using **Kotlin and Jetpack Compose**, with a focus on:

* Compose UI development
* Navigation
* MVVM-style architecture
* Room Database
* Repository and DAO patterns
* ViewModel state management
* Local authentication
* Session management
* CRUD operations

## 👩‍💻 Developer

**Areej Fatima**

Flutter & Android Developer

GitHub: [@areejfatima313](https://github.com/areejfatima313)

## 📄 License

This project is currently available without a specified open-source license.
