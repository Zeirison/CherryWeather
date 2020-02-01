package com.zeiris.cherryweather.data.remote.network

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.zeiris.cherryweather.data.remote.api.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

val networkModule = module {
    single { provideRetrofit(get(), get(), getProperty("api_url")) }
    single { provideOkHttpClient(get(), get()) }
    single { provideObjectMapper() }
    single { provideAuthInterceptor(getProperty("api_key")) }
    single { provideLoggingInterceptor() }
    single { provideWeatherApi(get()) }
}

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    objectMapper: ObjectMapper,
    url: String
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .build()
}

private fun provideOkHttpClient(
    authInterceptor: AuthInterceptor,
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor).build()
}

fun provideObjectMapper(): ObjectMapper {
    val mapper = ObjectMapper()
    mapper.registerKotlinModule()
    mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
    val module = SimpleModule()
    mapper.registerModule(module)
    return mapper
}

private fun provideAuthInterceptor(api_key: String): AuthInterceptor {
    return AuthInterceptor(api_key)
}

private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BASIC
    return logger
}

private fun provideWeatherApi(retrofit: Retrofit): WeatherApi = retrofit.create(
    WeatherApi::class.java
)

