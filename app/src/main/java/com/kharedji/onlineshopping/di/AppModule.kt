package com.kharedji.onlineshopping.di

import com.kharedji.onlineshopping.data.repository.UserRepositoryImpl
import com.kharedji.onlineshopping.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideSignUpRepository(): UserRepository {
        return UserRepositoryImpl()
    }
}