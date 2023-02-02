package com.mukund.bookcompanion.di

import android.content.Context
import androidx.room.Room
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_TABLE
import com.mukund.bookcompanion.data.network.BooksDao
import com.mukund.bookcompanion.data.network.BooksDatabase
import com.mukund.bookcompanion.data.repository.BooksRepositoryImpl
import com.mukund.bookcompanion.domain.repository.BooksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context = context,
        BooksDatabase::class.java,
        name = BOOK_TABLE
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideDao(
        booksDatabase: BooksDatabase
    ) = booksDatabase.booksDao()

    @Provides
    fun provideRepo(
        booksDao: BooksDao
    ) : BooksRepository = BooksRepositoryImpl(booksDao = booksDao)
}