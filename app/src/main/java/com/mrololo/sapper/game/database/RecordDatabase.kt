package com.mrololo.sapper.game.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.mrololo.sapper.R
import com.mrololo.sapper.game.database.dao.RecordDao
import com.mrololo.sapper.game.database.entity.Record

@Database(entities = [Record::class], version = 1)
abstract class RecordDatabase: RoomDatabase() {
    abstract fun getRecordDao(): RecordDao

    companion object {
        var INSTANCE: RecordDatabase? = null
        fun getRecordDatabase(context: Context): RecordDatabase? {
            if (INSTANCE == null) {
                synchronized(RecordDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RecordDatabase::class.java, context.getString(R.string.database_name))
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}