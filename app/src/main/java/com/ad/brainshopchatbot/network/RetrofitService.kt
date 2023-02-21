package com.ad.brainshopchatbot.network


import android.util.Log
import com.ad.brainshopchatbot.BaseApp
import com.ad.brainshopchatbot.util.InternetConnection
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitService {

    private const val BASE_URL = "http://api.brainshop.ai"

    private const val cacheSize = (5 * 1024 * 1024).toLong()
    private val myCache = Cache(BaseApp.appContext.cacheDir, cacheSize)

    private val logging = HttpLoggingInterceptor { message ->
        Log.d("OkHttp", message)
    }.also {
        it.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val okHttpClient: OkHttpClient =
        OkHttpClient.Builder().cache(myCache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor { chain ->
                var request = chain.request()
                request =
                    if (InternetConnection.hasInternetConnection(BaseApp.appContext)) request.newBuilder()
                        .header("Cache-Control", "public, max-age=" + 5).build()
                    else request.newBuilder().header(
                        "Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                    ).build()
                chain.proceed(request)
            }.build()

    private var gson = GsonBuilder().setLenient().create()

    private var retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build()

    fun <T> createService(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}