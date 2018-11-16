package com.mrololo.sapper.game.presenter

import com.mrololo.sapper.game.core.GameState
import com.mrololo.sapper.game.core.Sapper
import com.mrololo.sapper.game.database.RecordDatabase
import com.mrololo.sapper.game.database.entity.Record
import com.mrololo.sapper.game.timer.ScoreTimer
import com.mrololo.sapper.game.timer.ScoreUpdater

class Presenter {
    private var sapper: Sapper? = null
    var gameView: GameView? = null
    private var fieldSize = 1
    private var bombsAmount = 0
    private var timer = ScoreTimer()

    fun startGame(fieldSize: Int, bombsAmount: Int) {
        timer.stop()
        this.fieldSize = fieldSize
        this.bombsAmount = bombsAmount
        sapper = Sapper(bombsAmount, fieldSize)
        sapper?.start()
        timer.start()
        gameView?.startGame(fieldSize)
        gameView?.updateField(sapper!!.gameField)
    }

    fun setScoreUpdate(updater: ScoreUpdater) {
        timer.updater = updater
    }

    fun tryOpenCell(x: Int, y: Int) {
        sapper?.let {
            if (it.openCell(x, y)) {
                gameView?.updateField(it.gameField)
            }

            when (it.state) {
                GameState.WIN -> {
                    updateRecords()
                    timer.stop()
                    gameView?.win()
                }
                GameState.LOSE -> {
                    timer.stop()
                    gameView?.lose()
                }
                else -> { }
            }
        }
    }

    fun tryPutFlag(x: Int, y: Int) {
        sapper?.let {
            if (it.putFlag(x, y)) {
                gameView?.updateField(it.gameField)
                //  fieldView?.invalidate()
            }

            when (it.state) {
                GameState.WIN -> {
                    updateRecords()
                    timer.stop()
                    gameView?.win()
                }
                else -> { }
            }
        }
    }

    fun pause() {
        sapper?.let {
            if(it.state == GameState.PLAYING) {
                timer.pause()
            }
        }
    }

    fun resume() {
        sapper?.let {
            if(it.state == GameState.PLAYING) {
                timer.start()
            }
        }
    }

    private fun updateRecords() {
        val db = RecordDatabase.getRecordDatabase(gameView!!.getAppContext())
        val recordDao = db?.getRecordDao()
        recordDao?.insertRecord(Record(null, timer.score, fieldSize, bombsAmount))
    }
}
