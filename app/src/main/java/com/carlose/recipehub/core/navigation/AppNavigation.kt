package com.carlose.recipehub.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.carlose.recipehub.features.creation.presentation.CreateRecipeScreen
import com.carlose.recipehub.features.feed.presentation.HomeScreen
import com.carlose.recipehub.features.feed.presentation.SearchScreen
import com.carlose.recipehub.features.planner.presentation.PlannerScreen
import com.carlose.recipehub.features.profile.presentation.ProfileScreen

// Definimos los items de la barra de navegación
data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Lista de pantallas para la barra inferior
    val items = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("Buscar", Screen.Search.route, Icons.Filled.Search, Icons.Outlined.Search),
        BottomNavItem("Crear", Screen.CreateRecipe.route, Icons.Filled.AddCircle, Icons.Outlined.AddCircle),
        BottomNavItem("Plan", Screen.Planner.route, Icons.Filled.DateRange, Icons.Outlined.DateRange),
        BottomNavItem("Perfil", Screen.Profile.route, Icons.Filled.Person, Icons.Outlined.Person)
    )

    Scaffold(
        bottomBar = {
            // Lógica para ocultar la barra en Login/Registro si estuviéramos ahí
            NavigationBar(
                containerColor = Color.Black, // Barra negra
                contentColor = Color.White
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {
                                // Esto evita que se acumulen pantallas al volver atrás
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(item.title) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Aquí es donde sucede la magia de cambiar pantallas
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Search.route) { SearchScreen() }
            composable(Screen.CreateRecipe.route) { CreateRecipeScreen() }
            composable(Screen.Planner.route) { PlannerScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}