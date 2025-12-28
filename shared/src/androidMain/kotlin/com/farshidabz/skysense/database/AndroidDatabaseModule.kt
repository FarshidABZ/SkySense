package com.farshidabz.skysense.database

import android.content.Context
import androidx.room.Room
import com.farshidabz.skysense.database.dao.WeatherDao
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

private const val DB_NAME = "skysense.db"

val databaseAndroidModule: Module = module {
    single {
        val context: Context = androidContext()
        Room.databaseBuilder(
            context,
            CityWeatherDatabase::class.java,
            DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    single<WeatherDao> { get<CityWeatherDatabase>().cityWeatherDao() }

    single<WeatherLocalDataSource> { WeatherLocalDataSourceImpl(get()) }
}
