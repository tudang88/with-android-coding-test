package com.example.myapplication.ui.favourites

import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.myapplication.HiltTestActivity
import com.example.myapplication.common.SAMPLE_USERS
import com.example.myapplication.data.MyDataSource
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@MediumTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@ExperimentalCoroutinesApi
class FavouriteScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var repository: MyDataSource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun loadFavouritesScreen_noFavourites() = runTest {
        // GIVEN:
        // THEN: show Favourites screen
        setContent()
        // THEN: The empty screen is display
        composeTestRule.onAllNodes(isToggleable()).assertCountEquals(0)
    }

    @Test
    fun loadFavouritesScreen_SomeFavourites() = runTest {
        // GIVEN:
        for (i in 0..2) {
            repository.markFavourite(SAMPLE_USERS[i].id, true)
        }
        // THEN: show Favourites screen
        setContent()
        // THEN: The screen is displayed with 3 items inside
        composeTestRule.onAllNodes(isToggleable()).assertCountEquals(3)
        // remove one favourite item
        composeTestRule.onAllNodes(isToggleable()).onFirst().performClick()
        // THEN: the favourites list will be deducted by one
        composeTestRule.onAllNodes(isToggleable()).assertCountEquals(2)

    }
    /**
     * The helper function
     * for loading the screen composable
     */
    private fun setContent() {
        composeTestRule.setContent {
            MyApplicationTheme {
                Surface {
                    FavouritesScreen(
                        viewModel = FavouritesScreenViewModel(repository)
                    )
                }
            }
        }
    }
}