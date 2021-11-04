package com.pss.augustooth.di

import com.pss.data.repository.remote.KakaoAddressRepositoryImpl
import com.pss.data.repository.remote.datasourcelmpl.KakaoAddressDataSourceImpl
import com.pss.domain.repository.KakaoAddressRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideKakaoAddressRepository(
        kakaoAddressDataSourceImpl: KakaoAddressDataSourceImpl
    ): KakaoAddressRepository {
        return KakaoAddressRepositoryImpl(
            kakaoAddressDataSourceImpl
        )
    }
}