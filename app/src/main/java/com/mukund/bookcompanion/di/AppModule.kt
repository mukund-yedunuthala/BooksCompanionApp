package com.mukund.bookcompanion.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_TABLE
import com.mukund.bookcompanion.data.network.BooksDao
import com.mukund.bookcompanion.data.network.BooksDatabase
import com.mukund.bookcompanion.ui.settings.dataStore
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
    fun provideBooksDatabase(application: Application): BooksDatabase =
        Room.databaseBuilder(application, BooksDatabase::class.java, BOOK_TABLE)
            .addMigrations(BooksDatabase.MIGRATION_2_3, BooksDatabase.MIGRATION_3_4, BooksDatabase.MIGRATION_4_5)
            .build()

    @Provides
    @Singleton
    fun provideBooksDao(database: BooksDatabase): BooksDao =
        database.booksDao()

    @Provides
    @Singleton
    fun provideDataStore(application: Application): DataStore<Preferences> =
        application.dataStore
}
