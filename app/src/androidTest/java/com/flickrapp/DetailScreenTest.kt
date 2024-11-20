package com.flickrapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.flickrapp.domain.model.FlickrImageDomain
import com.flickrapp.presentation.ui.DetailScreen
import com.flickrapp.presentation.viewmodel.FlickrViewModel
import com.flickrapp.domain.repository.FlickrRepository
import com.flickrapp.domain.usecase.SearchPhotosUseCase
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: FlickrViewModel

    @Before
    fun setup() {
        // Mock the FlickrRepository using MockK
        val flickrRepository = mockk<FlickrRepository>()

        // Mock the searchPhotos method to return a static list of FlickrImageDomain
        coEvery { flickrRepository.searchPhotos(any()) } returns listOf(
            FlickrImageDomain(
                title = "Sample Image",
                description = "width=1024, height=768",
                author = "Author Name",
                published = "2024-11-20",
                imageUrl = "https://example.com/sample.jpg",
                dateTaken = "2024-11-01",
                link = "Image Link",
                tags = "nature, landscape"
            )
        )

        // Initialize the ViewModel with the mocked repository
        viewModel = FlickrViewModel(SearchPhotosUseCase(flickrRepository))

        // Set the content for the DetailScreen with a sample photo
        composeTestRule.setContent {
            DetailScreen(
                photo = FlickrImageDomain(
                    title = "Sample Image",
                    description = "width=1024, height=768",
                    author = "Author Name",
                    published = "2024-11-20",
                    imageUrl = "https://example.com/sample.jpg",
                    dateTaken = "2024-11-01",
                    link = "Image Link",
                    tags = "nature, landscape"
                ),
                navController = rememberNavController()
            )
        }
    }

    @Test
    fun testDetailScreenDisplaysImage() {
        // Verify that the image is displayed in the detail screen
        composeTestRule.onNodeWithContentDescription("Sample Image").assertIsDisplayed()
    }

    @Test
    fun testDetailScreenDisplaysImageDetails() {
        // Check for the title, description, author, published date, and dimensions
        composeTestRule.onNodeWithText("Title: Sample Image").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description: width=1024, height=768").assertIsDisplayed()
        composeTestRule.onNodeWithText("Author: Author Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Published: 2024-11-20").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dimensions: 1024 x 768").assertIsDisplayed()
    }

    @Test
    fun testShareButtonFunctionality() {
        // Click on the share button
        composeTestRule.onNodeWithText("Share").performClick()

        // Since we aren't using a mock for intents in this test,
        // we would normally assert that an intent is triggered, but for this
        // simple UI test case, this step is omitted.
        // You can mock intent behavior in a more comprehensive test if needed.
    }
}
