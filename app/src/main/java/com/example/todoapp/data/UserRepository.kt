package com.example.todoapp.data

import com.example.todoapp.data.dao.UserDao
import com.example.todoapp.data.entity.User
import com.example.todoapp.security.PasswordHasher

class UserRepository(private val userDao: UserDao) {

    sealed class AuthResult {
        data class Success(val user: User) : AuthResult()
        data class Error(val message: String) : AuthResult()
    }

    suspend fun register(name: String, email: String, password: String): AuthResult {
        if (name.isBlank() || email.isBlank() || password.length < 6) {
            return AuthResult.Error("Please fill all fields (password min 6 chars)")
        }
        if (userDao.emailExists(email) > 0) {
            return AuthResult.Error("Email already registered")
        }
        val salt = PasswordHasher.generateSalt()
        val hash = PasswordHasher.hash(password, salt)
        val user = User(name = name, email = email, passwordHash = hash, salt = salt)
        val id = userDao.insertUser(user)
        return AuthResult.Success(user.copy(id = id.toInt()))
    }

    suspend fun login(email: String, password: String): AuthResult {
        val user = userDao.findByEmail(email)
            ?: return AuthResult.Error("User not found")
        val ok = PasswordHasher.verify(password, user.salt, user.passwordHash)
        return if (ok) AuthResult.Success(user)
        else AuthResult.Error("Incorrect password")
    }
}