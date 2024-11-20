package com.flickrapp.domain.usecase

import com.flickrapp.domain.repository.FlickrRepository

class SearchPhotosUseCase(private val repository: FlickrRepository) {
    suspend fun execute(tags: String) = repository.searchPhotos(tags)
}
