package com.carlose.recipehub.features.profile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlose.recipehub.core.model.Recipe
import com.carlose.recipehub.core.model.User
import com.carlose.recipehub.features.feed.data.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: FeedRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()

    private val _myRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val myRecipes = _myRecipes.asStateFlow()

    private val _favoriteRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val favoriteRecipes = _favoriteRecipes.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        _user.value = User(
            id = 1,
            name = "CarlosElo",
            email = "carlos@test.com",
            profilePictureUrl = "https://i.pravatar.cc/300"
        )

        viewModelScope.launch {
            val allRecipes = repository.getRecipes()
            _myRecipes.value = allRecipes.filter { it.authorId == 101 }
            _favoriteRecipes.value = allRecipes.filter { it.isFavorite }
        }
    }

    fun onTabSelected(index: Int) {
        _selectedTabIndex.value = index
    }
}