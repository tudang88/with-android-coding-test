package com.example.myapplication.ui.master

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.myapplication.HiltTestActivity
import com.example.myapplication.R
import com.example.myapplication.data.MyDataSource
import com.example.myapplication.ui.navigation.AppNavigationGraph
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Tests for scenarios that requires navigating within the app.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class AppNavigationTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    private val activity get() = composeTestRule.activity

    @Inject
    lateinit var repository: MyDataSource

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun openHome_displayDetails_backHome_goFavourites() {
        // 1. open Home
        setContent()
        // 2. open details
        composeTestRule.onAllNodesWithContentDescription(activity.getString(R.string.user_photo))
            .onFirst().performClick()
        // confirm item display
        composeTestRule.onNode(isToggleable()).assertIsOff()
        // 3. click favourite
        composeTestRule.onNode(isToggleable()).performClick()
        composeTestRule.onNode(isToggleable()).assertIsOn()
        // 4. navigation back
        pressBack()
        // 5. navigate to favourite
        composeTestRule.onNodeWithText(activity.getString(R.string.fav_tab)).performClick()
        // confirm favourite badge
        composeTestRule.onNode(isToggleable()).assertIsOn()
        // 6. navigate to details
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.user_photo)).performClick()
        composeTestRule.onNode(isToggleable()).assertIsOn()
        // 7. click favourite on Detail to un-mark
        composeTestRule.onNode(isToggleable()).performClick()
        // 8. navigation back
        pressBack()
        // 9: no item on FavouritesScreen
        composeTestRule.onAllNodes(isToggleable()).assertCountEquals(0)
        // 10: back to home
        composeTestRule.onNodeWithText(activity.getString(R.string.home_tab)).performClick()
        composeTestRule.onAllNodes(isToggleable()).onFirst().assertIsOff()
    }

    /**
     * helper function
     * for involving compose
     */
    private fun setContent() {
        composeTestRule.setContent {
            MyApplicationTheme {
                MasterScreen(viewModel = MasterScreenViewModel(repository))
            }
        }
    }
}