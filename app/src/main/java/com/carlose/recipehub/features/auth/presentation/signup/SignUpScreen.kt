package com.carlose.recipehub.features.auth.presentation.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carlose.recipehub.core.ui.components.RecipeHubTextField

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel() // Inyectamos el ViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val signUpSuccess by viewModel.signUpSuccess.collectAsState()

    // Solo navegamos si la API responde con éxito
    LaunchedEffect(signUpSuccess) {
        if (signUpSuccess) {
            onSignUpSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(48.dp))

        RecipeHubTextField(
            value = name,
            onValueChange = { name = it },
            label = "Nombre Completo",
            icon = Icons.Default.Person
        )
        Spacer(modifier = Modifier.height(16.dp))
        RecipeHubTextField(
            value = email,
            onValueChange = { email = it },
            label = "Correo Electrónico",
            icon = Icons.Default.Email
        )
        Spacer(modifier = Modifier.height(16.dp))
        RecipeHubTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            isPassword = true,
            icon = Icons.Default.Lock
        )

        if (error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = error!!, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            // Conectamos el botón al ViewModel
            onClick = { viewModel.register(name, email, password) },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(text = "Registrarse")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿Ya tienes cuenta? Inicia Sesión",
            color = Color.White,
            modifier = Modifier.clickable { onNavigateToLogin() }
        )
    }
}