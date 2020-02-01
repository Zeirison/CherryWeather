package com.zeiris.cherryweather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zeiris.cherryweather.data.db.converter.CoordinatesConverter
import com.zeiris.cherryweather.data.db.converter.DateConverter
import com.zeiris.cherryweather.data.db.converter.ForecastConverter
import com.zeiris.cherryweather.data.db.dao.WeatherDao
import com.zeiris.cherryweather.data.model.Weather
import org.koin.dsl.module

val databaseModule = module {
    single(createdAtStart = true) { AppDatabase.getInstance(get()) }
    single { get<AppDatabase>().weatherDao }
}

@Database(entities = [Weather::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, CoordinatesConverter::class, ForecastConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val weatherDao: WeatherDao

    companion object {
        private var instance: AppDatabase? = null
        private const val DATABASE_NAME = "cherry-weather-db"

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }

    }

}