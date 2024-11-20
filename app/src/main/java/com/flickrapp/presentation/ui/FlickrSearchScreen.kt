package com.flickrapp.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.flickrapp.domain.model.FlickrImageDomain
import com.flickrapp.presentation.viewmodel.FlickrViewModel

@Composable
fun FlickrSearchScreen(
    viewModel: FlickrViewModel,
    navController: NavController // To navigate to the detail screen
) {
    val photos by viewModel.photos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.fillMaxSize().background(color = Color.Black)) {
        // Accessible search bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it; viewModel.searchPhotos(it.text) }
        )

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Accessible grid of images
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(photos.size) { index ->
                    FlickrImageItem(
                        photo = photos[index],
                        onClick = { navController.navigate("detail/${index}") }
                    )
                }
            }
        }
    }
}

/**
 * Search bar component with label, placeholder, and clear button.
 */
@Composable
fun SearchBar(query: TextFieldValue, onQueryChange: (TextFieldValue) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search TextField
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp)
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    onQueryChange(query)
                })
            )

            // Placeholder text when the query is empty
            if (query.text.isEmpty()) {
                Text(
                    text = "Search images...",
                    style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        }

        // Clear button for the search query
        if (query.text.isNotEmpty()) {
            IconButton(onClick = { onQueryChange(TextFieldValue("")) }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Search"
                )
            }
        }
    }
}


/**
 * Single grid item displaying a thumbnail image.
 */
@Composable
fun FlickrImageItem(photo: FlickrImageDomain, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
            .semantics {
                contentDescription = photo.title // Accessibility: Speakable description
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(photo.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .semantics {
                    contentDescription = photo.title // Accessibility: Image description
                },
            contentScale = ContentScale.Crop
        )
    }
}

