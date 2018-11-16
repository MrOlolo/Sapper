package com.mrololo.sapper.game.database.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrololo.sapper.R
import com.mrololo.sapper.game.database.entity.Record
import kotlinx.android.synthetic.main.record_item.view.*

class RecordAdapter(context: Context, private val records: List<Record>):
    RecyclerView.Adapter<RecordAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.record_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return records.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rec = records[position]
        holder.recordBombsAmount?.text = rec.bombs.toString()
        holder.recordFieldSize?.text = rec.size.toString()
        holder.recordScore?.text = rec.score.toString()
    }

    class  ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val recordFieldSize = itemView?.recordFieldSize
        val recordScore = itemView?.recordScore
        val recordBombsAmount = itemView?.recordBombsAmount
    }

}