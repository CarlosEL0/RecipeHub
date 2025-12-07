package com.carlose.recipehub.features.profile.presentation

import com.carlose.recipehub.features.profile.presentation.profile.ProfileViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carlose.recipehub.core.ui.components.RecipeGridItem

@Composable
fun ProfileScreen(
    onRecipeClick: (Int) -> Unit,
    onLogout: () -> Unit, // Callback para salir
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val myRecipes by viewModel.myRecipes.collectAsState()
    val favoriteRecipes by viewModel.favoriteRecipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val contentList = if (selectedTab == 0) myRecipes else favoriteRecipes

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Scaffold(
        containerColor = Color(0xFF121212)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = currentUser?.name ?: "Cargando...",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = currentUser?.email ?: "",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.logout()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar Sesión", color = Color.Red)
                }
            }

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF6200EE)
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { viewModel.onTabSelected(0) },
                    text = { Text("Mis Recetas") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { viewModel.onTabSelected(1) },
                    text = { Text("Favoritos") }
                )
            }

            // --- Grid de Contenido ---
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                if (contentList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (selectedTab == 0) "Aún no has creado recetas" else "Aún no tienes favoritos",
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(contentList) { recipe ->
                            RecipeGridItem(
                                recipe = recipe,
                                onClick = { onRecipeClick(recipe.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}