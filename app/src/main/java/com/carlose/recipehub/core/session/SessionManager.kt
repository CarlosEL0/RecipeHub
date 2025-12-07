package com.carlose.recipehub.core.session

import com.carlose.recipehub.core.model.User

object SessionManager {
    private var currentUser: User? = null

    fun saveUser(user: User) {
        currentUser = user
    }

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun getUserId(): Int {
        return currentUser?.id ?: -1
    }

    fun clearSession() {
        currentUser = null
    }
}