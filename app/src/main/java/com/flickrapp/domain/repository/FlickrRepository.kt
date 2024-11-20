package com.flickrapp.domain.repository

import com.flickrapp.domain.model.FlickrImageDomain

interface FlickrRepository {
    suspend fun searchPhotos(tags: String): List<FlickrImageDomain>
}
