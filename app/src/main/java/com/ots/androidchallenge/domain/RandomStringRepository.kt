package com.ots.androidchallenge.domain

import com.ots.androidchallenge.data.RandomTextEntity
import kotlinx.coroutines.flow.Flow

interface RandomStringRepository {
    suspend fun generateRandomString(length: Int)

    fun observeAll(): Flow<List<RandomTextEntity>>

    suspend fun deleteAll()

    suspend fun deleteById(id: Long)
}