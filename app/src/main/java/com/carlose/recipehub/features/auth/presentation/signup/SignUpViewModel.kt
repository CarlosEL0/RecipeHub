package com.carlose.recipehub.features.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlose.recipehub.features.auth.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _signUpSuccess = MutableStateFlow(false)
    val signUpSuccess = _signUpSuccess.asStateFlow()

    fun register(name: String, email: String, pass: String) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _error.value = "Por favor llena todos los campos"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // Llamamos al repositorio que sí conecta con la API
            val result = repository.register(name, email, pass)

            result.onSuccess {
                _signUpSuccess.value = true
            }.onFailure {
                _error.value = "Error: ${it.message}"
            }

            _isLoading.value = false
        }
    }
}