package com.example.to_do.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [categoryname::class, todo::class], version = 8)
abstract class tododatabase : RoomDatabase() {
    abstract fun todoDAO(): todoDAo
    abstract fun categorydao(): categorydao

    companion object {
        @Volatile
        private var INSTANCE: tododatabase? = null

        fun getDatabase(context: Context):  tododatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    tododatabase::class.java,
                    "todo_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}