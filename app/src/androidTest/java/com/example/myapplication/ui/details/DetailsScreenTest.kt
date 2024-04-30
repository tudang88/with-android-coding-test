package com.example.myapplication.ui.details

import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
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

/**
 * Integration test for the Details screen.
 */
//@MediumTest
//@RunWith(AndroidJUnit4::class)
//@HiltAndroidTest
//@ExperimentalCoroutinesApi
class DetailsScreenTest {
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

//    @Test
    fun loadDetailScreen() = runTest {
        // GIVEN: pick a user
        val user = SAMPLE_USERS[0]
        // THEN: show screen
        setContent(user.id)
        // THEN: The DetailsScreen is display
        composeTestRule.onNodeWithText(user.nickname).assertIsDisplayed()
        composeTestRule.onNode(isToggleable()).assertIsOff()
    }

    /**
     * The helper function
     * for loading the screen composable
     */
    private fun setContent(userId: Int) {
        composeTestRule.setContent {
            MyApplicationTheme {
                Surface {
                    DetailsScreen(
                        id = userId,
                        viewModel = DetailsScreenViewModel(repository)
                    )
                }
            }
        }
    }
}