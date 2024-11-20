package com.flickrapp.data.repository

import com.flickrapp.data.model.PhotoItem
import com.flickrapp.data.network.FlickrApiService
import com.flickrapp.domain.model.FlickrImageDomain
import com.flickrapp.domain.repository.FlickrRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of FlickrRepository.
 * Fetches and maps data from the Flickr API.
 */
class FlickrRepositoryImpl(
    private val apiService: FlickrApiService
) : FlickrRepository {

    override suspend fun searchPhotos(tag: String): List<FlickrImageDomain> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPhotosByTag(tag)
                if (response.items.isNotEmpty()) {
                    response.items.map { item ->
                        // Mapping data from PhotoItem to FlickrImageDomain
                        FlickrImageDomain(
                            title = item.title,
                            link = item.link,
                            imageUrl = item.media.imageUrl,
                            dateTaken = item.date_taken,
                            description = item.description,
                            published = item.published,
                            author = item.author,
                            tags = item.tags
                        )
                    }
                } else {
                    emptyList()  // Return empty list if no photos found
                }
            } catch (e: Exception) {
                emptyList()  // Return empty list if an error occurs
            }
        }
    }
}
