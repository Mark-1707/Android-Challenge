package com.ots.androidchallenge.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ots.androidchallenge.data.RandomTextDao
import com.ots.androidchallenge.data.RandomTextEntity

@Database(entities = [RandomTextEntity::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun randomTextDao(): RandomTextDao
}
