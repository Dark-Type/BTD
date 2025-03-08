package com.example.btd.data.networking

import com.example.btd.data.remote.api_services.ApiServiceStudent
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class NetworkModule(private val config: NetworkConfig = NetworkConfig(baseUrl = "http://103.249.133.18:8080/")) {

    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor? = null,
        loginInterceptor: LoginInterceptor = LoginInterceptor(),
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(loginInterceptor)
            .readTimeout(config.readTimeout, TimeUnit.SECONDS)
            .connectTimeout(config.connectTimeout, TimeUnit.SECONDS)

        authInterceptor?.let {
            builder.addInterceptor(it)
        }

        return builder.build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(config.baseUrl)
        .addConverterFactory(Json {
            ignoreUnknownKeys = true
        }.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()

    inline fun <reified T> provideService(retrofit: Retrofit): T {
        return retrofit.create(T::class.java)
    }

}