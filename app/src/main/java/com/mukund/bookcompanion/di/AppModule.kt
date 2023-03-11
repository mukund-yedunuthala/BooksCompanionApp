package com.mukund.bookcompanion.di

import android.app.Application
import com.mukund.bookcompanion.data.network.BookDbProvider
import com.mukund.bookcompanion.data.repository.BooksRepositoryImpl
import com.mukund.bookcompanion.domain.repository.BooksBackupRepo
import com.mukund.bookcompanion.domain.repository.BooksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideBooksDatabaseProvider(application: Application) : BookDbProvider = BookDbProvider(application)

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Provides
    fun provideMutex(): Mutex = Mutex()

    @Provides
    @Singleton
    fun provideExecutorCoroutineDispatcher(): ExecutorCoroutineDispatcher
        = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Provides
    @Singleton
    fun provideBooksRepository(bookDbProvider: BookDbProvider) : BooksRepositoryImpl
        = BooksRepositoryImpl(bookDbProvider)

    @Provides
    @Singleton
    fun provideBooksBackupRepo(
        bookDbProvider: BookDbProvider,
        application: Application,
        mutex: Mutex,
        coroutineScope: CoroutineScope,
        executorCoroutineDispatcher: ExecutorCoroutineDispatcher,
    ): BooksBackupRepo = BooksBackupRepo(
        provider = bookDbProvider,
        context = application,
        mutex = mutex,
        scope = coroutineScope,
        dispatcher = executorCoroutineDispatcher
    )
}