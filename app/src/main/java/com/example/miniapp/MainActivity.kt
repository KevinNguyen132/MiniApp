package com.example.miniapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.miniapp.ui.theme.MiniAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PostScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PostScreen(modifier: Modifier = Modifier) {
    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = true) {
        try {
            withContext(Dispatchers.IO) {
                val repository = Repository()
                posts = repository.getPosts()
            }
            isLoading = false
        } catch (e: Exception) {
            Log.e("PostScreen", "Error fetching posts", e)
            error = e.message
            isLoading = false
        }
    }

    Column(modifier = modifier) {
        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text("Error: $error")
            posts.isEmpty() -> Text("No posts found")
            else -> {
                Text("Posts (${posts.size}):")
                LazyColumn {
                    items(posts) { post ->
                        Text(
                            text = "${post.id}: ${post.title}",
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}