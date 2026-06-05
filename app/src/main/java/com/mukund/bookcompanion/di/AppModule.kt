package com.mukund.bookcompanion.di

import android.app.Application
import com.mukund.bookcompanion.data.network.BookDbProvider
import com.mukund.bookcompanion.data.repository.BooksRepositoryImpl
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
    fun provideBooksDatabaseProvider(application: Application): BookDbProvider =
        BookDbProvider(application)

    @Provides
    @Singleton
    fun provideBooksRepository(bookDbProvider: BookDbProvider): BooksRepositoryImpl =
        BooksRepositoryImpl(bookDbProvider)

}