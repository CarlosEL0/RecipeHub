package com.carlose.recipehub.features.auth.data

import com.carlose.recipehub.core.model.User
import com.carlose.recipehub.core.network.LoginRequest
import com.carlose.recipehub.core.network.RecipeHubApiService
import com.carlose.recipehub.core.network.RegisterRequest // Asegúrate de importar esto
import com.carlose.recipehub.core.session.SessionManager // <--- Importante
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: RecipeHubApiService
) {
    suspend fun login(email: String, pass: String): Result<User> {
        return try {
            val response = api.login(LoginRequest(email, pass))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val user = User(
                    id = body.id,
                    name = body.name,
                    email = body.email,
                    profilePictureUrl = null,
                    token = body.token
                )

                // --- GUARDAMOS LA SESIÓN AQUÍ ---
                SessionManager.saveUser(user)
                // --------------------------------

                Result.success(user)
            } else {
                Result.failure(Exception("Credenciales incorrectas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Agregamos el método de registro para que también guarde la sesión automáticamente
    suspend fun register(name: String, email: String, pass: String): Result<User> {
        return try {
            val response = api.register(RegisterRequest(name, email, pass))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                // El endpoint de registro devuelve el usuario creado
                val user = User(
                    id = body.id,
                    name = body.name,
                    email = body.email,
                    profilePictureUrl = null,
                    token = null // En registro a veces no viene token, pero ya tenemos el ID
                )

                // --- GUARDAMOS LA SESIÓN AQUÍ TAMBIÉN ---
                SessionManager.saveUser(user)
                // ----------------------------------------

                Result.success(user)
            } else {
                Result.failure(Exception("Error al registrar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}