package com.mukund.bookcompanion.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.mukund.bookcompanion.data.network.BookDbProvider
import com.mukund.bookcompanion.data.repository.BooksRepositoryImpl
import com.mukund.bookcompanion.domain.repository.BooksRepository
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
    fun provideBooksDatabaseProvider(application: Application): BookDbProvider =
        BookDbProvider(application)

    @Provides
    @Singleton
    fun provideBooksRepository(bookDbProvider: BookDbProvider): BooksRepository =
        BooksRepositoryImpl(bookDbProvider)

    @Provides
    @Singleton
    fun provideDataStore(application: Application): DataStore<Preferences> =
        application.dataStore
}
