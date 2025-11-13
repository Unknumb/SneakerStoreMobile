package com.example.appsneakerstore.data.repository

import android.app.Application
import com.example.appsneakerstore.data.local.AppDatabase
import com.example.appsneakerstore.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(application: Application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()

    fun getUserByUsername(username: String): Flow<User?> = userDao.getUserByUsername(username)

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
}