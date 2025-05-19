package com.example.smartstopwatch.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstopwatch.R
import com.example.smartstopwatch.data.models.SessionWithLaps
import com.example.smartstopwatch.utils.TimeFormatter

class SessionAdapter(private val onExportClick: (SessionWithLaps) -> Unit) : 
    ListAdapter<SessionWithLaps, SessionAdapter.SessionViewHolder>(SessionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_session, parent, false)
        return SessionViewHolder(view, onExportClick)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val sessionWithLaps = getItem(position)
        holder.bind(sessionWithLaps)
    }

    class SessionViewHolder(
        itemView: View,
        private val onExportClick: (SessionWithLaps) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvSessionDate: TextView = itemView.findViewById(R.id.tvSessionDate)
        private val tvSessionDuration: TextView = itemView.findViewById(R.id.tvSessionDuration)
        private val tvLapCount: TextView = itemView.findViewById(R.id.tvLapCount)
        private val btnExportSession: ImageButton = itemView.findViewById(R.id.btnExportSession)

        fun bind(sessionWithLaps: SessionWithLaps) {
            val session = sessionWithLaps.session
            val laps = sessionWithLaps.laps

            tvSessionDate.text = TimeFormatter.formatDate(session.startTime)
            tvSessionDuration.text = "Duration: ${TimeFormatter.formatElapsedTime(session.duration)}"
            tvLapCount.text = "Laps: ${laps.size}"

            btnExportSession.setOnClickListener {
                onExportClick(sessionWithLaps)
            }
        }
    }

    class SessionDiffCallback : DiffUtil.ItemCallback<SessionWithLaps>() {
        override fun areItemsTheSame(oldItem: SessionWithLaps, newItem: SessionWithLaps): Boolean {
            return oldItem.session.id == newItem.session.id
        }

        override fun areContentsTheSame(oldItem: SessionWithLaps, newItem: SessionWithLaps): Boolean {
            return oldItem.session == newItem.session && 
                   oldItem.laps.size == newItem.laps.size
        }
    }
}
