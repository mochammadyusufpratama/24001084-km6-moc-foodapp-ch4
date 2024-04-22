package com.app.makanku.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.makanku.data.source.local.database.dao.CartDao
import com.app.makanku.data.source.local.database.entity.CartEntity


@Database(
    entities = [CartEntity::class],
    version = 2,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
    companion object {
        private const val DB_NAME = "Makanku.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )   .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}