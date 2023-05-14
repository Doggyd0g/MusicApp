package com.example.musicplayerapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MusicListAdapter(var songsList: ArrayList<AudioModel>, var context: Context) :
    RecyclerView.Adapter<MusicListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val songData = songsList[position]
        holder.titleTextView.text = songData.title
        if (MyPlayer.currentIndex === position) {
            holder.titleTextView.setTextColor(Color.parseColor("#FF0000"))
        } else {
            holder.titleTextView.setTextColor(Color.parseColor("#000000"))
        }
        holder.itemView.setOnClickListener {
            MyPlayer.getInstance().reset()
            MyPlayer.currentIndex = position
            val intent = Intent(context, MusicPlayerActivity::class.java)
            intent.putExtra("LIST", songsList)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        var iconImageView: ImageView

        init {
            titleTextView = itemView.findViewById(R.id.music_title_text)
            iconImageView = itemView.findViewById(R.id.icon_view)
        }
    }
}