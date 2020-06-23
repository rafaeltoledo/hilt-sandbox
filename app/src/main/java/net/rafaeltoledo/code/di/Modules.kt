package net.rafaeltoledo.code.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import net.rafaeltoledo.code.BuildConfig
import net.rafaeltoledo.code.api.StackOverflowApi
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Reusable
    fun provideClient() = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .setLevel(
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                )
        )
        .build()

    @Provides
    @Reusable
    fun provideMoshi() = Moshi.Builder().build()

    @Provides
    @Reusable
    fun provideRetrofit(client: OkHttpClient, host: HttpUrl, moshi: Moshi) = Retrofit.Builder()
        .client(client)
        .baseUrl(host)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Reusable
    fun provideApi(retrofit: Retrofit) = retrofit.create<StackOverflowApi>()
}

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    @Provides
    @Reusable
    fun provideApiHost() = "https://api.stackexchange.com".toHttpUrlOrNull()!!
}