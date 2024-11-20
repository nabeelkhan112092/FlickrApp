package com.flickrapp.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.flickrapp.domain.model.FlickrImageDomain

/**
 * Detail screen to display image details.
 */
@Composable
fun DetailScreen(
    photo: FlickrImageDomain,
    navController: NavController
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(photo.imageUrl),
                contentDescription = photo.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(bottom = 16.dp)
            )
            Text(text = "Title: ${photo.title}", fontWeight = FontWeight.Bold)
            val descriptionText = Html.fromHtml(photo.description, Html.FROM_HTML_MODE_LEGACY).toString()
            Text(text = "Description: $descriptionText")
            Text(text = "Author: ${photo.author}")
            Text(text = "Published: ${photo.published}")
            Text(
                text = "Dimensions: ${
                    parseDimensions(photo.description)
                }", // Extract width and height from description
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { shareImage(photo, navController.context) }) {
                Text(text = "Share")
            }
        }
    }
}

/**
 * Parse dimensions from the description string.
 */
fun parseDimensions(description: String): String {
    val regex = Regex("width=(\\d+), height=(\\d+)")
    val match = regex.find(description)
    return match?.destructured?.let { (width, height) ->
        "$width x $height"
    } ?: "Unknown"
}

/**
 * Share the image and metadata using an intent.
 */
fun shareImage(photo: FlickrImageDomain, context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "Check out this image!\n\nTitle: ${photo.title}\nAuthor: ${photo.author}\nPublished: ${photo.published}\nURL: ${photo.imageUrl}"
        )
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
}
