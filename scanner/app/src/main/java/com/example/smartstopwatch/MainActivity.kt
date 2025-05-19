package com.example.smartstopwatch

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartstopwatch.databinding.ActivityMainBinding
import com.example.smartstopwatch.ui.adapters.LapAdapter
import com.example.smartstopwatch.ui.viewmodels.StopwatchViewModel
import com.example.smartstopwatch.utils.CsvExporter
import com.example.smartstopwatch.utils.TimeFormatter
import com.example.smartstopwatch.utils.VoiceRecognitionHandler
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: StopwatchViewModel
    private lateinit var lapAdapter: LapAdapter
    private var voiceRecognitionHandler: VoiceRecognitionHandler? = null
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startVoiceRecognition()
        } else {
            Snackbar.make(
                binding.root,
                "Voice control requires microphone permission",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[StopwatchViewModel::class.java]
        
        setupUI()
        setupObservers()
        setupVoiceRecognition()
    }

    private fun setupUI() {
        // Setup RecyclerView
        lapAdapter = LapAdapter()
        binding.rvLaps.apply {
            adapter = lapAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        // Setup buttons
        binding.btnStartPause.setOnClickListener {
            if (binding.btnStartPause.text == getString(R.string.start)) {
                viewModel.startStopwatch()
                binding.btnStartPause.text = getString(R.string.pause)
                binding.btnLap.isEnabled = true
                binding.btnReset.isEnabled = true
            } else {
                viewModel.pauseStopwatch()
                binding.btnStartPause.text = getString(R.string.start)
            }
        }

        binding.btnLap.setOnClickListener {
            viewModel.recordLap()
        }

        binding.btnReset.setOnClickListener {
            viewModel.resetStopwatch()
            binding.btnStartPause.text = getString(R.string.start)
            binding.btnLap.isEnabled = false
            binding.btnReset.isEnabled = false
            binding.btnExport.isEnabled = false
        }

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, SessionHistoryActivity::class.java))
        }

        binding.btnExport.setOnClickListener {
            exportCurrentSession()
        }

        binding.fabVoiceControl.setOnClickListener {
            checkMicrophonePermission()
        }
    }

    private fun setupObservers() {
        viewModel.currentTimeMillis.observe(this) { timeMillis ->
            binding.tvTimer.text = TimeFormatter.formatElapsedTime(timeMillis)
        }

        viewModel.laps.observe(this) { laps ->
            lapAdapter.submitList(laps)
            binding.btnExport.isEnabled = laps.isNotEmpty()
        }

        viewModel.progress.observe(this) { progress ->
            binding.circularProgress.setProgress(progress)
        }
    }

    private fun setupVoiceRecognition() {
        voiceRecognitionHandler = VoiceRecognitionHandler(
            context = this,
            onStartCommand = {
                if (binding.btnStartPause.text == getString(R.string.start)) {
                    viewModel.startStopwatch()
                    binding.btnStartPause.text = getString(R.string.pause)
                    binding.btnLap.isEnabled = true
                    binding.btnReset.isEnabled = true
                }
            },
            onPauseCommand = {
                if (binding.btnStartPause.text == getString(R.string.pause)) {
                    viewModel.pauseStopwatch()
                    binding.btnStartPause.text = getString(R.string.start)
                }
            },
            onResetCommand = {
                viewModel.resetStopwatch()
                binding.btnStartPause.text = getString(R.string.start)
                binding.btnLap.isEnabled = false
                binding.btnReset.isEnabled = false
                binding.btnExport.isEnabled = false
            },
            onLapCommand = {
                if (binding.btnLap.isEnabled) {
                    viewModel.recordLap()
                }
            },
            onListening = {
                Snackbar.make(binding.root, R.string.listening, Snackbar.LENGTH_SHORT).show()
            },
            onError = { errorMessage ->
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).show()
            }
        )
    }

    private fun checkMicrophonePermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startVoiceRecognition()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun startVoiceRecognition() {
        voiceRecognitionHandler?.startListening()
    }

    private fun exportCurrentSession() {
        val sessionId = viewModel.currentSessionId.value ?: return
        
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
                CsvExporter.exportSessionToCsv(this@MainActivity, sessionWithLaps)
            }
            
            if (file != null) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.export_success),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.export_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        voiceRecognitionHandler?.stopListening()
        viewModel.saveSession()
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceRecognitionHandler?.destroy()
    }
}
