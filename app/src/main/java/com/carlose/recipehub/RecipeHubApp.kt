package com.carlose.recipehub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Esta anotación @HiltAndroidApp es OBLIGATORIA.
// Activa la generación de código de Dagger-Hilt para toda la app.
@HiltAndroidApp
class RecipeHubApp : Application()