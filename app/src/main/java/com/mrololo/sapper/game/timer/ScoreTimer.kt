package com.mrololo.sapper.game.timer

import android.os.Handler

class ScoreTimer {
    private var handler = Handler()
    private var delay = 1000L
    var score = 0
        private set
    var updater: ScoreUpdater? = null

    private val timer= object: Runnable {
         override fun run() {
             score++
             updater?.updateScore(score)
             handler.postDelayed(this, delay)
         }
    }

    fun start() {
        updater?.updateScore(score)
        handler.postDelayed(timer, delay)
    }

    fun pause() {
        handler.removeCallbacks(timer)
    }

    fun stop() {
        score = 0
        handler.removeCallbacks(timer)
    }
}