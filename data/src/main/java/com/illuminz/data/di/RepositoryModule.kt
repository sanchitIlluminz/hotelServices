package com.illuminz.data.di

import com.illuminz.data.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class RepositoryModule {
    @Provides
    @Singleton
    fun providesUserRepository(userRepository: UserRepositoryImpl): UserRepository {
        return userRepository
    }

    @Provides
    @Singleton
    fun providesAuthorizationRepository(authorizationRepository: AuthorizationRepositoryImpl): AuthorizationRepository {
        return authorizationRepository
    }

    @Provides
    @Singleton
    fun providesCommonRepository(commonRepository: CommonRepositoryImpl): CommonRepository {
        return commonRepository
    }
}