package com.pss.augustooth.di

import com.pss.data.api.KakaoAddressApi
import com.pss.presentation.widget.utils.ApiUrl.HOSPITAL_LOCATION_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jaxb.JaxbConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(HOSPITAL_LOCATION_BASE_URL)
            .client(okHttpClient)
            //json 변화기 Factory
            .client(provideHttpClient())
            .addConverterFactory(JaxbConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideKakaoAddressApiService(retrofit: Retrofit): KakaoAddressApi {
        return retrofit.create(KakaoAddressApi::class.java)
    }
/*
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): AdminApi {
        return retrofit.create(AdminApi::class.java)
    }
*/

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }


}