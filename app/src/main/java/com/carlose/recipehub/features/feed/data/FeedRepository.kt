package com.carlose.recipehub.features.feed.data

import com.carlose.recipehub.core.model.Recipe
import javax.inject.Inject
import kotlinx.coroutines.delay

class FeedRepository @Inject constructor() {

    suspend fun getRecipes(): List<Recipe> {
        delay(1000)
        return listOf(
            Recipe(
                id = 1,
                title = "Tacos al Pastor Caseros",
                description = "La receta clásica mexicana con piña y cilantro.",
                preparationTimeMinutes = 45,
                portions = 4,
                imageUrl = "https://images.unsplash.com/photo-1551504734-5ee1c4a1479b?q=80&w=1000&auto=format&fit=crop",
                publicationDate = "2025-11-24",
                authorName = "CarlosElo",
                authorId = 101,
                isFavorite = false
            ),
            Recipe(
                id = 2,
                title = "Pasta Carbonara Auténtica",
                description = "Sin crema, solo huevo, queso pecorino y guanciale.",
                preparationTimeMinutes = 20,
                portions = 2,
                imageUrl = "https://images.unsplash.com/photo-1612874742237-6526221588e3?q=80&w=1000&auto=format&fit=crop",
                publicationDate = "2025-11-23",
                authorName = "ChefLuigi",
                authorId = 102,
                isFavorite = true
            ),
            Recipe(
                id = 3,
                title = "Bowl de Acaí y Frutas",
                description = "Desayuno saludable y energético.",
                preparationTimeMinutes = 10,
                portions = 1,
                imageUrl = "https://images.unsplash.com/photo-1590301157890-4810ed352733?q=80&w=1000&auto=format&fit=crop",
                publicationDate = "2025-11-22",
                authorName = "HealthyAna",
                authorId = 103,
                isFavorite = false
            ),
            Recipe(
                id = 4,
                title = "Hamburguesa Gourmet",
                description = "Carne angus con queso brie y cebolla caramelizada.",
                preparationTimeMinutes = 35,
                portions = 1,
                imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=1000&auto=format&fit=crop",
                publicationDate = "2025-11-20",
                authorName = "CarlosElo",
                authorId = 101,
                isFavorite = false
            )
        )
    }
}
