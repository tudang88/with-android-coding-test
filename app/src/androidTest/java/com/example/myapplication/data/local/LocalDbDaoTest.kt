package com.example.myapplication.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.myapplication.common.SAMPLE_LOCAL_DATA
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class LocalDbDaoTest {
    // using an in-memory database because the information stored here disappears when the
    // process is killed
    private lateinit var database: LocalDatabase

    // Ensure that we use a new database for each test.
    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocalDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun insertUserAndGetUserById() = runTest {
        // GIVEN - insert a User
        val user = SAMPLE_LOCAL_DATA[0]
        // WHEN - insert
        database.localDbDao.upsert(user)
        // THEN - get user
        val loadUser = database.localDbDao.getById(user.id)
        assertNotNull(loadUser as LocalDbEntry)
        assertEquals(user, loadUser)
    }

    @Test
    fun insertMultipleUserAndGetAllUsers() = runTest {
        // GIVEN - empty database
        val emptyDb = database.localDbDao.observeAll().first()
        assertEquals(0, emptyDb.size)
        // WHEN - insert
        database.localDbDao.insertAll(SAMPLE_LOCAL_DATA)
        // THEN - get user
        val loadUsers = database.localDbDao.observeAll().first()
        assertNotNull(loadUsers)
        assertEquals(SAMPLE_LOCAL_DATA, loadUsers)
    }

    @Test
    fun insertMultipleUser_clearDb() = runTest {
        // GIVEN -
        // WHEN - insert all
        database.localDbDao.insertAll(SAMPLE_LOCAL_DATA)
        // THEN - get user
        val loadUsers = database.localDbDao.observeAll().first()
        assertNotNull(loadUsers)
        assertEquals(SAMPLE_LOCAL_DATA, loadUsers)
        // WHEN - clearDB
        database.localDbDao.clearDatabase()
        val loadUsers2 = database.localDbDao.observeAll().first()
        assertEquals(0, loadUsers2.size)
    }

    @Test
    fun addFavourite_getFavouriteUser_getFavouriteUserList() = runTest {
        // GIVEN -
        database.localDbDao.insertAll(SAMPLE_LOCAL_DATA)
        // WHEN - mark one to favourite
        val favUser = SAMPLE_LOCAL_DATA[0].copy(isFavorite = true)
        database.localDbDao.upsert(favUser)
        // THEN - get favourite user
        val loadUser = database.localDbDao.getById(favUser.id)
        assertNotNull(loadUser as LocalDbEntry)
        assertEquals(favUser, loadUser)
        // WHEN: add one more favourite user
        val favUser2 = SAMPLE_LOCAL_DATA[1].copy(isFavorite = true)
        database.localDbDao.upsert(favUser2)
        // THEN: get favourite user list
        val favList = database.localDbDao.getFavoriteItems().first()

        assertEquals(listOf(favUser, favUser2), favList)
    }
}