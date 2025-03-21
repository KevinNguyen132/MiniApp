package com.example.miniapp

import android.util.Log

class Repository {
    private val apiService = Client.apiService

    suspend fun getPosts(): List<Post> {
        try {
            val response = apiService.getPosts()
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                // Debug log
                Log.d("PostRepository", "Fetched ${posts.size} posts")
                posts.forEach { post ->
                    Log.d("PostRepository", "Post #${post.id}: ${post.title}")
                }
                return posts
            } else {
                Log.e("PostRepository", "Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "Exception: ${e.message}")
        }
        return emptyList()
    }
}