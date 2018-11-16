package com.mrololo.sapper.game.presenter

import android.content.Context
import com.mrololo.sapper.game.core.Cell

interface GameView {
    fun startGame(size: Int)
    fun updateField(field: Array <Cell>)
    fun getAppContext(): Context
    fun win()
    fun lose()
}