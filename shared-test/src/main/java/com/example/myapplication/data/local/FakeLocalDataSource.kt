package com.example.myapplication.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource(initialEntries: List<LocalDbEntry>? = emptyList()) : LocalDbDao {
    // fake local storage
    private var _userEntries: MutableMap<Int, LocalDbEntry>? = null

    // only public for test
    private var allEntries: List<LocalDbEntry>?
        get() = _userEntries?.values?.toList()
        set(newEntriesSet) {
            _userEntries = newEntriesSet?.associateBy { it.id }?.toMutableMap()
        }

    init {
        allEntries = initialEntries
    }

    override suspend fun upsert(favProfile: LocalDbEntry) {
       val entry = _userEntries?.get(favProfile.id)?.copy(isFavorite = true)
        if (entry != null) {
            _userEntries?.put(entry.id, entry)
        }
    }

    override suspend fun insertAll(items: List<LocalDbEntry>) {
        _userEntries?.putAll(items.associateBy { it.id })
    }

    override fun observeAll(): Flow<List<LocalDbEntry>> {
        return flow {
            _userEntries?.map { entry -> entry.value }?.let { emit(it.toList()) }
        }
    }

    override suspend fun getById(id: Int): LocalDbEntry? {
        return _userEntries?.get(id)
    }

    override fun getFavoriteItems(): Flow<List<LocalDbEntry>> {
       return flow {
           _userEntries?.filter { entry -> entry.value.isFavorite }?.map { entry -> entry.value  }
               ?.let { emit(it.toList()) }
       }
    }

    override fun observeById(id: Int): Flow<LocalDbEntry?> {
        return flow {
            emit(_userEntries?.get(id))
        }
    }

    override suspend fun delete(id: Int) {
        _userEntries?.remove(id)
    }

    override suspend fun clearDatabase() {
        _userEntries?.clear()
    }
}