package com.flickrapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.flickrapp.data.network.FlickrApiService
import com.flickrapp.data.repository.FlickrRepositoryImpl
import com.flickrapp.domain.usecase.SearchPhotosUseCase
import com.flickrapp.presentation.ui.DetailScreen
import com.flickrapp.presentation.ui.FlickrSearchScreen
import com.flickrapp.presentation.viewmodel.FlickrViewModel

/**
 * Entry point for the app.
 */
class MainActivity : ComponentActivity() {
    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = FlickrViewModel(SearchPhotosUseCase(FlickrRepositoryImpl(FlickrApiService.create())))

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "search") {
                composable("search") {
                    FlickrSearchScreen(viewModel = viewModel, navController = navController)
                }
                composable("detail/{index}") { backStackEntry ->
                    val index = backStackEntry.arguments?.getString("index")?.toIntOrNull()
                    val photo = viewModel.photos.value[index ?: 0]
                    DetailScreen(photo = photo, navController = navController)
                }
            }
        }
    }
}