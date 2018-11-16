package com.mrololo.sapper.game

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import com.mrololo.sapper.R
import com.mrololo.sapper.game.database.RecordDatabase
import com.mrololo.sapper.game.database.adapter.RecordAdapter
import com.mrololo.sapper.game.database.dao.RecordDao
import kotlinx.android.synthetic.main.activity_record.*

class RecordActivity : AppCompatActivity() {

    private var db: RecordDatabase? = null
    private var recordDao: RecordDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        db = RecordDatabase.getRecordDatabase(this)
        recordDao = db?.getRecordDao()
        val rec = recordDao!!.getSortedRecords()
        val recyclerView = list
        val adapter = RecordAdapter(this, rec)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL))
    }
}
