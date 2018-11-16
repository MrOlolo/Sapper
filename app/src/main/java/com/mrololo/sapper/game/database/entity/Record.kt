package com.mrololo.sapper.game.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Record (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val score: Int,
    val size: Int,
    val bombs: Int)