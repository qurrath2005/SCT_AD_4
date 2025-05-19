# Smart Stopwatch App

A feature-rich stopwatch Android application with lap recording, session logging, circular timer visualization, and voice control.

## Features

- Start/Pause/Reset functionality
- Lap recording with detailed timing
- Session history with persistent storage
- Circular animated timer visualization
- Export sessions to CSV
- Voice control for hands-free operation

## Requirements

- Android Studio Arctic Fox (2021.3.1) or newer
- Android SDK 33 or higher
- Minimum SDK: 23 (Android 6.0)

## How to Run the App

### Using Android Studio (Recommended)

1. Open Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to the project directory and click "Open"
4. Wait for the Gradle sync to complete
5. Connect an Android device or start an emulator
6. Click the "Run" button (green triangle) in the toolbar
7. Select your device/emulator and click "OK"

### Using Command Line

If you have Android Studio installed but prefer using the command line:

```bash
# Navigate to the project directory
cd path/to/SmartStopwatch

# Build the app
./gradlew assembleDebug

# Install on a connected device
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Launch the app
adb shell am start -n com.example.smartstopwatch/.MainActivity
```

## Using the App

1. **Main Screen**:
   - Press "Start" to begin timing
   - Press "Lap" to record lap times
   - Press "Reset" to clear the timer and laps
   - Press "History" to view past sessions
   - Press "Export" to save the current session as CSV

2. **Voice Control**:
   - Tap the microphone icon
   - Say "start" to start the timer
   - Say "pause" or "stop" to pause the timer
   - Say "reset" to reset the timer
   - Say "lap" to record a lap

3. **Session History**:
   - View all your saved sessions
   - See session date, duration, and lap count
   - Export individual sessions to CSV

## Project Structure

- **UI Layer**: Activities, adapters, and custom views
- **ViewModel Layer**: StopwatchViewModel and SessionHistoryViewModel
- **Data Layer**: Repository, DAOs, and database
- **Model Layer**: Session and Lap entities
- **Utility Layer**: Formatters, exporters, and voice recognition

## License

This project is licensed under the MIT License - see the LICENSE file for details.
