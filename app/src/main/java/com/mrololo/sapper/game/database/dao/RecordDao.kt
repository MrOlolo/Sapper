package com.mrololo.sapper.game.database.dao

import android.arch.persistence.room.*
import com.mrololo.sapper.game.database.entity.Record

@Dao
interface RecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecord(record: Record)

    @Update
    fun updateRecord(record: Record)

    @Delete
    fun deleteRecord(record: Record)

    @Query("SELECT * FROM Record")
    fun getRecords(): List<Record>

    @Query("SELECT * FROM Record ORDER BY score ASC, size DESC, bombs DESC")
    fun getSortedRecords(): List<Record>
}

