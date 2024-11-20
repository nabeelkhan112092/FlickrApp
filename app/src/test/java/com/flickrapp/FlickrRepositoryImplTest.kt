package com.flickrapp

import com.flickrapp.data.model.FlickrItem
import com.flickrapp.data.model.FlickrResponse
import com.flickrapp.data.model.Media
import com.flickrapp.data.network.FlickrApiService
import com.flickrapp.data.repository.FlickrRepositoryImpl
import com.flickrapp.domain.model.FlickrImageDomain
import com.flickrapp.domain.repository.FlickrRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FlickrRepositoryImplTest {

    private val apiService: FlickrApiService = mock()
    private val flickrRepository: FlickrRepository = FlickrRepositoryImpl(apiService)

    @Test
    fun `test searchPhotos maps API response to domain model`() = runTest {
        // Given
        val mockFlickrResponse = FlickrResponse(
            title = "Flickr Feed",
            link = "Link",
            description = null,
            modified = "2024-11-01",
            generator = "Flickr",
            items = listOf(
                FlickrItem(
                    title = "Image Title",
                    link = "Image Link",
                    media = Media("imageUrl"),
                    date_taken = "2024-11-01",
                    description = "width=100, height=200",
                    published = "2024-11-01",
                    author = "Author",
                    author_id = "123",
                    tags = "nature, landscape"
                )
            )
        )

        whenever(apiService.getPhotosByTag("nature")).thenReturn(mockFlickrResponse)

        // When
        val result = flickrRepository.searchPhotos("nature")

        // Then
        val expected = listOf(
                FlickrImageDomain(
                    title = "Image Title",
                    link = "Image Link",
                    imageUrl = "imageUrl",
                    dateTaken = "2024-11-01",
                    description = "width=100, height=200",
                    published = "2024-11-01",
                    author = "Author",
                    tags = "nature, landscape"
                )
        )
        assertEquals(expected, result)
    }
}
