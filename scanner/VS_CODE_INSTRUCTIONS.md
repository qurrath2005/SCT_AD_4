# Running Smart Stopwatch App from VS Code

This guide explains how to run the Smart Stopwatch Android app directly from VS Code without using Android Studio.

## Prerequisites

1. **Visual Studio Code**
   - Download and install from [code.visualstudio.com](https://code.visualstudio.com/)

2. **Java Development Kit (JDK)**
   - Install JDK 11 or newer
   - Set JAVA_HOME environment variable

3. **Android SDK**
   - Download Android Command Line Tools
   - Set ANDROID_HOME environment variable
   - Install required SDK components

4. **VS Code Extensions**
   - Extension Pack for Java
   - Gradle for Java
   - Kotlin Language
   - Android iOS Emulator

## Setup Instructions

### 1. Install Android SDK Components

```powershell
# Accept licenses
sdkmanager --licenses

# Install required components
sdkmanager "platform-tools" "platforms;android-33" "build-tools;33.0.0" "extras;google;m2repository" "extras;android;m2repository"
```

### 2. Open the Project in VS Code

1. Launch VS Code
2. File > Open Workspace from File...
3. Select the `smartstopwatch.code-workspace` file

### 3. Connect an Android Device

1. Enable Developer Options on your Android device
   - Go to Settings > About phone
   - Tap "Build number" 7 times
   - Go back to Settings > System > Developer options
   - Enable "USB debugging"

2. Connect your device via USB
3. Accept the debugging prompt on your device
4. Verify connection by running the "List Connected Devices" task

## Running the App

### Using VS Code Tasks

1. Press `Ctrl+Shift+P` to open the Command Palette
2. Type "Tasks: Run Task" and select it
3. Choose one of the following tasks:
   - "Build Debug APK" - Builds the app
   - "Install APK on Device" - Installs the app on your device
   - "Run App" - Launches the app on your device
   - "View Logs" - Shows app logs

### Using the Run Button

1. Go to the Run and Debug view (Ctrl+Shift+D)
2. Select "Launch App on Android" from the dropdown
3. Click the green play button or press F5

## Troubleshooting

### Common Issues

1. **"adb not found" error**
   - Make sure Android SDK platform-tools are in your PATH
   - Restart VS Code after updating environment variables

2. **Gradle build fails**
   - Check that JDK is properly installed and JAVA_HOME is set
   - Run `./gradlew --stop` and try again

3. **Device not detected**
   - Ensure USB debugging is enabled
   - Try a different USB cable or port
   - Run `adb kill-server` followed by `adb start-server`

4. **App crashes on launch**
   - Check logs using the "View Logs" task
   - Ensure your device meets the minimum API level (23)

## VS Code Shortcuts

- `Ctrl+Shift+B` - Run the default build task
- `Ctrl+Shift+P` - Open Command Palette
- `Ctrl+Shift+D` - Open Run and Debug view
- `Ctrl+Shift+E` - Open Explorer view
- `Ctrl+Shift+G` - Open Source Control view

## Additional Tasks

You can add more tasks to `.vscode/tasks.json` as needed, such as:
- Running specific Gradle tasks
- Generating release builds
- Running tests

## Customizing the Setup

Feel free to modify the VS Code configuration files:
- `.vscode/settings.json` - Editor and project settings
- `.vscode/tasks.json` - Custom tasks
- `.vscode/launch.json` - Debug configurations
