package com.ad.brainshopchatbot.di

import android.content.Context
import android.util.Log
import com.ad.brainshopchatbot.BuildConfig
import com.ad.brainshopchatbot.network.ApiService
import com.ad.brainshopchatbot.util.InternetConnection
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {
    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    fun provideCache(@ApplicationContext context: Context) =
        Cache(context.cacheDir, cacheSize)

    @Provides
    fun provideOkHttp(@ApplicationContext context: Context, cache: Cache): OkHttpClient =
        OkHttpClient.Builder().cache(cache).connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor { message ->
                Log.d("OkHttp", message)
            }.also {
                it.setLevel(HttpLoggingInterceptor.Level.BODY)
            }).addInterceptor { chain ->
                var request = chain.request()
                request =
                    if (InternetConnection.hasInternetConnection(context)) request.newBuilder()
                        .header("Cache-Control", "public, max-age=" + 5).build()
                    else request.newBuilder().header(
                        "Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                    ).build()
                chain.proceed(request)
            }.build()

    @Provides
    @Singleton
    fun provideApiService(baseUrl: String, okhttp: OkHttpClient): ApiService =
        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(okhttp).build().create(ApiService::class.java)

    companion object {
        private const val cacheSize = (5 * 1024 * 1024).toLong()
    }
}