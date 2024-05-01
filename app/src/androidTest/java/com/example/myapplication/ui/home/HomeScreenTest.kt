package com.example.myapplication.ui.home

import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.myapplication.HiltTestActivity
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
class HomeScreenTest {
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
    fun loadHomeScreen_allItems() = runTest {
        // GIVEN:
        // THEN: show Home screen
        setContent()
        // THEN: The image card will be shown
        composeTestRule.onAllNodes(isToggleable()).onFirst().assertIsOff()
        // mark favourite
        composeTestRule.onAllNodes(isToggleable()).onFirst().performClick()

        composeTestRule.onAllNodes(isToggleable()).onFirst().assertIsOn()

    }
    /**
     * The helper function
     * for loading the screen composable
     */
    private fun setContent() {
        composeTestRule.setContent {
            MyApplicationTheme {
                Surface {
                    HomeScreen(
                        viewModel = HomeScreenViewModel(repository)
                    )
                }
            }
        }
    }
}