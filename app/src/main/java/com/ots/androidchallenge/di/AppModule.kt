package com.ots.androidchallenge.di

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.ots.androidchallenge.common.Database
import com.ots.androidchallenge.data.RandomStringRepositoryImpl
import com.ots.androidchallenge.data.RandomTextDao
import com.ots.androidchallenge.domain.RandomStringRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDb(app: Application): Database {
        return Room.databaseBuilder(
            app,
            Database::class.java,
            "RandomText"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRandomTextDao(db: Database) = db.randomTextDao()

    @Provides
    @Singleton
    fun provideContentResolver(app: Application): ContentResolver =
        app.contentResolver

    @Provides
    @Singleton
    fun provideRepository(contentResolver: ContentResolver, dao: RandomTextDao): RandomStringRepository {
        return RandomStringRepositoryImpl(contentResolver, dao)
    }
}