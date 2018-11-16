package com.mrololo.sapper.game.core

import android.util.Log
import java.util.*


class Sapper(private val bombsAmount: Int, private val fieldSize: Int) {
    //For LOG
    //private val TAG = this.javaClass.simpleName
    var gameField = Array(fieldSize * fieldSize) { i ->
        Cell(i % fieldSize, i / fieldSize, 0, false, false)
    }

    var state = GameState.LOSE
        private set

    private var openedCells = 0
    private var foundedBombs = 0
    private var flags = 0

    private fun createField() {
        var bombsOnField = 0
        val random = Random()
        var randomIndex: Int
        while (bombsOnField < bombsAmount) {
            randomIndex = random.nextInt(fieldSize * fieldSize)
            if (gameField[randomIndex].status != 9) {
                gameField[randomIndex].status = 9
                points(randomIndex)
                bombsOnField++
            }
        }
    }

    private fun getIndex(x: Int, y: Int): Int {
        return (x + y * fieldSize)
    }

    private fun getCoord(index: Int): Pair<Int, Int> {
        return Pair(index % fieldSize, index / fieldSize)
    }

    private fun outOfBound(x: Int, y: Int): Boolean {
        return (x < 0) || (x > (fieldSize - 1)) || (y < 0) || (y > (fieldSize - 1))
    }

    private fun points(index: Int) {
        val (x, y) = getCoord(index)
        var indexForCheck: Int
        for (i in (y - 1..y + 1)) {
            for (j in (x - 1..x + 1)) {
                indexForCheck = getIndex(j, i)
                if (!outOfBound(j, i) && gameField[indexForCheck].status != 9) {
                    gameField[indexForCheck].status++
                }
            }
        }
    }

    private fun searchNeighbours(index: Int) {
        if (gameField[index].flagged) {
            flags--
        }
        gameField[index].opened = true
        openedCells++

        if (gameField[index].status != 0) {
            return
        }

        val (x, y) = getCoord(index)
        var indexForCheck: Int

        for (i in (y - 1..y + 1)) {
            for (j in (x - 1..x + 1)) {
                indexForCheck = getIndex(j, i)
                if (!outOfBound(j, i) && !gameField[indexForCheck].opened) {
                    if (gameField[indexForCheck].status in (0 until 9)) {
                        searchNeighbours(indexForCheck)
                    }
                }
            }
        }
    }

    private fun win() {
        if (foundedBombs == bombsAmount && flags == bombsAmount) {
            state = GameState.WIN
        }
        else if(openedCells == (fieldSize*fieldSize - bombsAmount)) {
            state = GameState.WIN
        }
    }

    fun start() {
        createField()
        state = GameState.PLAYING
        openedCells = 0
        foundedBombs = 0
    }

    fun putFlag(x: Int, y: Int): Boolean {
        val index = getIndex(x, y)
        if (outOfBound(x, y)) {
            return false
        }

        if (state != GameState.PLAYING) {
            return false
        }

        if (gameField[index].opened) {
            return false
        }

        if (gameField[index].flagged) {
            return false
        }

        if (gameField[index].status == 9) {
            foundedBombs++
        }

        gameField[index].flagged = true
        flags++

        //For LOG
       /* Log.i(TAG,"Opened = $openedCells")
        Log.i(TAG,"Bombs = $bombsAmount")
        Log.i(TAG,"Flags = $flags")
        Log.i(TAG,"FoundedBombs = $foundedBombs")*/

        win()

        return true

    }

    fun openCell(x: Int, y: Int): Boolean {
        if (state != GameState.PLAYING) {
            return false
        }

        val index = getIndex(x, y)
        if (outOfBound(x, y)) {
            return false
        }

        if (gameField[index].opened) {
            return false
        }

        when {
            (gameField[index].flagged) -> {
                if (gameField[index].status == 9) {
                    foundedBombs--
                }
                gameField[index].flagged = false
                flags--
            }

            (gameField[index].status == 9) -> {
                state = GameState.LOSE
                loseOpen()
                //gameField[index].opened = true
                return true
            }

            else -> {
                searchNeighbours(index)
            }
        }

        win()

        //For LOG
        /*Log.i(TAG,"Opened = $openedCells")
        Log.i(TAG,"Bombs = $bombsAmount")
        Log.i(TAG,"Flags = $flags")
        Log.i(TAG,"FoundedBombs = $foundedBombs")

        for (i in 0 until fieldSize)
            for(j in 0 until fieldSize)
                Log.i(TAG, "x=$j y=$i cell=${gameField[i*fieldSize + j].status}")*/

        return true
    }

    private fun loseOpen() {
        for ((index) in gameField.withIndex()) {
            if (gameField[index].status == 9) {
                gameField[index].opened = true
            }
        }
    }

}
