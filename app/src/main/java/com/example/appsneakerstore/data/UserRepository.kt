package com.example.appsneakerstore.data

import com.example.appsneakerstore.model.User
import com.example.appsneakerstore.model.UserCredentials
import java.util.UUID

class UserRepository {
    // In a real app, this would be stored in a database (Room)
    private val registeredUsers = mutableMapOf<String, Pair<User, String>>() // email to (User, password)

    fun registerUser(name: String, email: String, password: String): Result<User> {
        if (registeredUsers.containsKey(email)) {
            return Result.failure(Exception("El usuario ya está registrado"))
        }

        val user = User(
            id = UUID.randomUUID().toString(),
            email = email,
            name = name,
            isGuest = false
        )
        registeredUsers[email] = Pair(user, password)
        return Result.success(user)
    }

    fun loginUser(credentials: UserCredentials): Result<User> {
        val userPair = registeredUsers[credentials.email]
            ?: return Result.failure(Exception("Usuario no encontrado"))

        if (userPair.second != credentials.password) {
            return Result.failure(Exception("Contraseña incorrecta"))
        }

        return Result.success(userPair.first)
    }

    fun createGuestUser(): User {
        return User(
            id = UUID.randomUUID().toString(),
            email = "guest@sneakerstore.com",
            name = "Invitado",
            isGuest = true
        )
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository().also { instance = it }
            }
        }
    }
}
