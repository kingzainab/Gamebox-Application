package com.zsinnovations.gamebox.ui.tetris.activity;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zsinnovations.gamebox.R
import com.zsinnovations.gamebox.ui.tetris.database.Score

class HighScoreRecyclerAdapter (private var scores: List<Score>) : RecyclerView.Adapter<HighScoreRecyclerAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemRank: TextView = itemView.findViewById(R.id.rank)
        val itemScore: TextView = itemView.findViewById(R.id.score_rank)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.tetris_highscore_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemScore.text = scores[position].score.toString()
        holder.itemRank.text = (position + 1).toString()
    }

    override fun getItemCount(): Int {
        return scores.size
    }
}