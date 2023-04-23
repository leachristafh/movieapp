package com.example.movieapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieapp.models.Movie
import com.example.movieapp.utils.CustomConverters
/*
Room

> Room is a database library from Jetpack which provides a
wrapper over SQLite
> Instead of using SQLite directly, Room simplifies db setup,
configurations and interactions
> Data classes in Kotlin provide an easy way to work with inmemory data
> When persisting data, we need to convert this data into a format
compatible with database storage ->
> Tables to store data
> Queries to access and modify data
 */
@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CustomConverters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun MovieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                )
                    .addCallback(MovieDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}