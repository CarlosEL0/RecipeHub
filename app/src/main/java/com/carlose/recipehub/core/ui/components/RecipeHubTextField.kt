package com.carlose.recipehub.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun RecipeHubTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector? = null,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color(0xFF6200EE), // Un morado tipo Android o tu color primario
            unfocusedLabelColor = Color.Gray,
            focusedBorderColor = Color(0xFF6200EE),
            unfocusedBorderColor = Color.Gray
        ),
        leadingIcon = if (icon != null) {
            { Icon(imageVector = icon, contentDescription = null, tint = Color.Gray) }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPassword) {
            {
                val image = if (passwordVisible)
                // Puedes usar iconos nativos o importar los de 'Icons.Filled.Visibility' si agregas la dependencia 'material-icons-extended'
                // Por ahora usaremos texto simple o nulo para no complicar dependencias
                    null
                else null

                // Un botón simple para alternar (podemos mejorar el icono luego)
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(text = if (passwordVisible) "Ocultar" else "Ver", color = Color.Gray)
                }
            }
        } else null,
        keyboardOptions = KeyboardOptions.Default
    )
}