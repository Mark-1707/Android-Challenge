package com.ots.androidchallenge.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "random_strings")
data class RandomTextEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val value: String,
    val length: Int,
    val created: String
)
