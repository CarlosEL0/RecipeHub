package com.carlose.recipehub.core.session

import com.carlose.recipehub.core.model.User

object SessionManager {
    // Aquí guardamos el usuario actual
    private var currentUser: User? = null

    fun saveUser(user: User) {
        currentUser = user
    }

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun getUserId(): Int {
        // Devuelve el ID del usuario real, o -1 si no hay nadie logueado
        return currentUser?.id ?: -1
    }

    fun clearSession() {
        currentUser = null
    }
}