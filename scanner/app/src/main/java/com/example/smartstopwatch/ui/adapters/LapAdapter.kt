package com.example.smartstopwatch.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstopwatch.R
import com.example.smartstopwatch.data.models.Lap
import com.example.smartstopwatch.utils.TimeFormatter

class LapAdapter : ListAdapter<Lap, LapAdapter.LapViewHolder>(LapDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lap, parent, false)
        return LapViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        val lap = getItem(position)
        holder.bind(lap)
    }

    class LapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLapNumber: TextView = itemView.findViewById(R.id.tvLapNumber)
        private val tvLapTime: TextView = itemView.findViewById(R.id.tvLapTime)
        private val tvTotalTime: TextView = itemView.findViewById(R.id.tvTotalTime)

        fun bind(lap: Lap) {
            tvLapNumber.text = "Lap ${lap.lapNumber}"
            tvLapTime.text = TimeFormatter.formatElapsedTime(lap.lapTime)
            tvTotalTime.text = "Total: ${TimeFormatter.formatElapsedTime(lap.totalTime)}"
        }
    }

    class LapDiffCallback : DiffUtil.ItemCallback<Lap>() {
        override fun areItemsTheSame(oldItem: Lap, newItem: Lap): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lap, newItem: Lap): Boolean {
            return oldItem == newItem
        }
    }
}
