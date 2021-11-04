package com.pss.augustooth.di

import com.pss.data.api.KakaoAddressApi
import com.pss.data.repository.remote.datasource.KakaoAddressDataSource
import com.pss.data.repository.remote.datasourcelmpl.KakaoAddressDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceImplModule {
    @Provides
    @Singleton
    fun provideKakaoAddressDataSource(
        kakaoAddressApi: KakaoAddressApi
    ) : KakaoAddressDataSource {
        return KakaoAddressDataSourceImpl(
            kakaoAddressApi
        )
    }
}