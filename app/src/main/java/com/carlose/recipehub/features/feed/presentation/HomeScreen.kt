package com.carlose.recipehub.features.feed.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)), // Fondo Negro
        contentAlignment = Alignment.Center
    ) {
        Text(text = "🏠 Home Feed", color = Color.White, style = MaterialTheme.typography.headlineMedium)
    }
}