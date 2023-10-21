package com.interview.dragonflights.data.di

import android.content.Context
import androidx.room.Room
import com.interview.dragonflights.data.dao.AppDataBase
import com.interview.dragonflights.data.repository.CurrencyRepository
import com.interview.dragonflights.data.repository.DefaultCurrencyRepository
import com.interview.dragonflights.data.repository.DefaultDragonRidesRepository
import com.interview.dragonflights.data.repository.DragonRideRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectionModule {

    private const val baseURL = "https://mockbin.org/"

    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, AppDataBase::class.java, "APP_DATABASE")
        .allowMainThreadQueries()
        .build()

    @Singleton
    @Provides
    fun provideCurrencyDAO(appDataBase: AppDataBase) = appDataBase.currencyDao()
}

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModules {
    @Binds
    fun provideDragonRideRepositoryImpl(repository: DefaultDragonRidesRepository): DragonRideRepository

    @Binds
    fun provideCurrencyRepositoryImpl(repository: DefaultCurrencyRepository): CurrencyRepository
}
