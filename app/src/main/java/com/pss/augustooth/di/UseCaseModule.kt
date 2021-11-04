package com.pss.augustooth.di

import com.pss.domain.repository.KakaoAddressRepository
import com.pss.domain.usecase.SearchAddressUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideSearchAddressUseCase(repository: KakaoAddressRepository) = SearchAddressUseCase(repository)

}