package com.mrololo.sapper.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.mrololo.sapper.R
import com.mrololo.sapper.game.core.Cell
import com.mrololo.sapper.game.presenter.GameView
import com.mrololo.sapper.game.presenter.Presenter
import com.mrololo.sapper.game.timer.ScoreUpdater
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity(), GameView,
    SeekBar.OnSeekBarChangeListener, ScoreUpdater {
    private val maxFieldSize = 16
    private val minFieldSize = 6
    private val minBombsAmount = 6
    private val ratio = 3
    //For LOG
    //private val TAG = this.javaClass.simpleName
    private var presenter = Presenter()
    private var longTap = false
    private var stateOfEnd: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        presenter.gameView = this
        presenter.setScoreUpdate(this)
        fieldView.visibility = View.GONE
        setLongClickListenerToField()
        setListenerToField()
        fieldSizeBar.progress = 0
        bombsAmountBar.progress = 0
        fieldSizeInfo.text = (minFieldSize).toString()
        bombsAmountInfo.text = (minBombsAmount).toString()
        fieldSizeBar.max = maxFieldSize - minFieldSize
        fieldSizeBar.setOnSeekBarChangeListener(this)
        bombsAmountBar.setOnSeekBarChangeListener(this)
        gameScore.setOnClickListener {
            startActivity(Intent(this, RecordActivity::class.java))
        }
        startButton.setOnClickListener {
            presenter.startGame(fieldSizeBar.progress + minFieldSize,
                bombsAmountBar.progress + minBombsAmount)
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.pause()
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun startGame(size: Int) {
        //For LOG
        //Log.i(TAG, "Start")
        stateOfEnd?.cancel()
        fieldView.visibility = View.VISIBLE
        fieldView.fieldSize = size
    }

    override fun getAppContext(): Context {
        return this
    }

    override fun win() {
        stateOfEnd?.cancel()
        stateOfEnd = Toast.makeText(this, resources.getString(R.string.win_toast), Toast.LENGTH_SHORT)
        stateOfEnd?.setGravity(Gravity.CENTER, 0, 0)
        stateOfEnd?.show()
    }

    override fun lose() {
        stateOfEnd?.cancel()
        stateOfEnd = Toast.makeText(this, resources.getString(R.string.lose_toast), Toast.LENGTH_SHORT)
        stateOfEnd?.setGravity(Gravity.CENTER, 0, 0)
        stateOfEnd?.show()
    }

    override fun updateField(field: Array<Cell>) {
        fieldView.field = field
        fieldView.invalidate()
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {    }

    override fun onStopTrackingTouch(p0: SeekBar?) {    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        when (p0){
            fieldSizeBar -> {
                val fSqr = (fieldSizeBar.progress + minFieldSize)
                fieldSizeInfo.text = (fSqr.toString())
                bombsAmountBar.max = fSqr*fSqr/ratio - minBombsAmount
            }
            bombsAmountBar -> {
                bombsAmountInfo.text = (bombsAmountBar.progress + minBombsAmount).toString()
            }
        }
    }

    override fun updateScore(score: Int) {
        gameScore.text = score.toString()
    }

    private fun setLongClickListenerToField() {
        fieldView.setOnLongClickListener {
            longTap = true
            //For LOG
            //Log.i(TAG, "LongTap")
            true
        }
    }

    private fun setListenerToField() {
        //For LOG
        //Log.i(TAG, "Touch")
        fieldView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if(longTap) {
                    //For LOG
                    //Log.i(TAG, "LongTap")
                    presenter.tryPutFlag(
                        (event.x/fieldView.cellSize).toInt(),
                        (event.y/fieldView.cellSize).toInt())
                    longTap = false
                } else {
                    //For LOG
                    //Log.i(TAG, "Tap")
                    presenter.tryOpenCell(
                        (event.x/fieldView.cellSize).toInt(),
                        (event.y/fieldView.cellSize).toInt())
                }
            }
            false
        }
    }
}
