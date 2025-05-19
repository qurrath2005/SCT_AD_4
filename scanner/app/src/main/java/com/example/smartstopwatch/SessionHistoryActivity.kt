package com.example.smartstopwatch

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartstopwatch.databinding.ActivitySessionHistoryBinding
import com.example.smartstopwatch.ui.adapters.SessionAdapter
import com.example.smartstopwatch.ui.viewmodels.SessionHistoryViewModel
import com.example.smartstopwatch.utils.CsvExporter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySessionHistoryBinding
    private lateinit var viewModel: SessionHistoryViewModel
    private lateinit var sessionAdapter: SessionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SessionHistoryViewModel::class.java]
        
        setupUI()
        observeSessions()
    }

    private fun setupUI() {
        // Setup RecyclerView
        sessionAdapter = SessionAdapter { sessionWithLaps ->
            exportSession(sessionWithLaps.session.id)
        }
        
        binding.rvSessions.apply {
            adapter = sessionAdapter
            layoutManager = LinearLayoutManager(this@SessionHistoryActivity)
        }
        
        // Setup back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun observeSessions() {
        viewModel.allSessionsWithLaps.observe(this) { sessions ->
            if (sessions.isEmpty()) {
                binding.tvNoSessions.visibility = View.VISIBLE
                binding.rvSessions.visibility = View.GONE
            } else {
                binding.tvNoSessions.visibility = View.GONE
                binding.rvSessions.visibility = View.VISIBLE
                sessionAdapter.submitList(sessions)
            }
        }
    }

    private fun exportSession(sessionId: Long) {
        // Check storage permission for Android < 10
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    this,
                    "Storage permission required for export",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }
        
        CoroutineScope(Dispatchers.Main).launch {
            val sessionWithLaps = withContext(Dispatchers.IO) {
                viewModel.getSessionWithLaps(sessionId)
            }
            
            val file = withContext(Dispatchers.IO) {
                CsvExporter.exportSessionToCsv(this@SessionHistoryActivity, sessionWithLaps)
            }
            
            if (file != null) {
                Toast.makeText(
                    this@SessionHistoryActivity,
                    getString(R.string.export_success),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@SessionHistoryActivity,
                    getString(R.string.export_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
