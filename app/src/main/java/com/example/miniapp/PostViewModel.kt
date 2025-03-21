package com.example.miniapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val repository = Repository()

    private val _postsState = MutableStateFlow<UiState<List<Post>>>(UiState.Loading)
    val postsState: StateFlow<UiState<List<Post>>> = _postsState.asStateFlow()

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            _postsState.value = UiState.Loading
            try {
                val posts = repository.getPosts()
                _postsState.value = UiState.Success(posts)
            } catch (e: Exception) {
                _postsState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}