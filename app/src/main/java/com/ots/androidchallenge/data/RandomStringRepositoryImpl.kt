package com.ots.androidchallenge.data

import android.content.ContentResolver
import android.os.Bundle
import com.ots.androidchallenge.common.Constants.CONTENT_URI
import com.ots.androidchallenge.common.Constants.RETRY_ATTEMPT
import com.ots.androidchallenge.data.model.ProviderResponse
import com.ots.androidchallenge.domain.RandomStringRepository
import kotlinx.serialization.json.Json

class RandomStringRepositoryImpl(
    private val contentResolver: ContentResolver,
    private val dao: RandomTextDao
): RandomStringRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun generateRandomString(length: Int) {
        var attempt = 0
        var exception: Exception? = null
        while(attempt < RETRY_ATTEMPT) {
            attempt++
            try {
                val jsonString = queryProvider(length) ?: ""

                val result = json.decodeFromString(ProviderResponse.serializer(), jsonString)
                val entity = RandomTextEntity(
                    value = result.randomText.value,
                    length = result.randomText.length,
                    created = result.randomText.created
                )
                dao.insert(entity)
                return
            } catch (e: SecurityException) {
                e.printStackTrace()
                throw e
            } catch (e: Exception) {
                e.printStackTrace()
                exception = e
            }
        }

        // Throw an exception if all retries failed
        throw exception ?: Exception("Failed to generate random string")
    }

    // get a random string from content provder
    private fun queryProvider(length: Int): String? {
        val args = Bundle().apply {
            putInt(ContentResolver.QUERY_ARG_LIMIT, length)
        }

        val cursor = contentResolver.query(
            CONTENT_URI,
            null,
            args,
            null
        )

        cursor?.use {
            if(it.moveToFirst()) {
                val index = it.getColumnIndex("data")
                if (index >= 0) {
                    return it.getString(index)
                }
            }
        }

        return null
    }

    override fun observeAll() = dao.getAll()

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}
