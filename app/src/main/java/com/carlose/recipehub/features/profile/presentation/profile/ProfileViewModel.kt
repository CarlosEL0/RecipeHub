package com.carlose.recipehub.features.profile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlose.recipehub.core.model.Recipe
import com.carlose.recipehub.core.model.User
import com.carlose.recipehub.core.session.SessionManager
import com.carlose.recipehub.features.feed.data.FeedRepository
import com.carlose.recipehub.features.profile.data.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val feedRepository: FeedRepository
) : ViewModel() {

    private val _myRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val myRecipes = _myRecipes.asStateFlow()

    private val _favoriteRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val favoriteRecipes = _favoriteRecipes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab = _selectedTab.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    init {
        loadData()
    }

    fun onTabSelected(index: Int) {
        _selectedTab.value = index
    }

    fun loadData() {
        _currentUser.value = SessionManager.getCurrentUser()

        viewModelScope.launch {
            _isLoading.value = true

            val myResult = profileRepository.getMyRecipes()
            myResult.onSuccess { _myRecipes.value = it }

            val feedRecipes = feedRepository.getRecipes()
            _favoriteRecipes.value = feedRecipes.filter { it.isFavorite }

            _isLoading.value = false
        }
    }

    fun logout() {
        SessionManager.clearSession()
    }
}