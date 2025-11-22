package com.ots.androidchallenge.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RandomTextDao {
    @Query("SELECT * FROM random_strings ORDER BY id DESC")
    fun getAll(): Flow<List<RandomTextEntity>>

    @Insert
    suspend fun insert(entity: RandomTextEntity)

    @Query("DELETE FROM random_strings WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM random_strings")
    suspend fun deleteAll()
}
